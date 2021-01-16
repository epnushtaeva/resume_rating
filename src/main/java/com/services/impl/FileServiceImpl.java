package com.services.impl;

import com.data_base.entities.FileDictionaryForSpeciality;
import com.data_base.entities.FileMarkForSpeciality;
import com.data_base.entities.Speciality;
import com.data_base.entities.User;
import com.data_base.repositories.FileDictionaryForSpecialityRepository;
import com.data_base.repositories.FileMarkRepository;
import com.data_base.repositories.FileRepository;
import com.dto.FileDto;
import com.dto.FileHiredDto;
import com.dto.HrDto;
import com.object_mappers.FileMarkToFileMarkDtoMapper;
import com.object_mappers.FileToFileDtoMapper;
import com.services.FileService;
import com.services.NGramService;
import com.services.SpecialityService;
import com.services.classes.FileFilters;
import com.services.classes.PageSettings;
import com.utils.FileReadingUtils;
import liquibase.util.file.FilenameUtils;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.util.Charsets;
import org.apache.tika.parser.txt.CharsetDetector;
import org.docx4j.Docx4J;
import org.docx4j.Docx4jProperties;
import org.docx4j.XmlUtils;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.CTDocProtect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private FileMarkRepository fileMarkRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileDictionaryForSpecialityRepository fileDictionaryForSpecialityRepository;

    @Autowired
    private FileMarkToFileMarkDtoMapper fileMarkToFileMarkDtoMapper;

    @Autowired
    private FileToFileDtoMapper fileToFileDtoMapper;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Value("${file_storage}")
    private String fileStorageDir;

    private NGramService nGramService = new UnigrammService();

    @Override
    @Transactional
    public List<com.data_base.entities.File> getFilesForDictionary(long specialityId) {
        List<FileMarkForSpeciality> fileMarks = this.fileMarkRepository
                .findBySpecialityIdOrderByFileIdAsc(specialityId)
                .stream()
                .filter(fileMark -> fileMark.getMark() > 0 && fileMark.getFile().isLearnExample())
                .collect(Collectors.toList());
        return this.getFiles(fileMarks);
    }

    @Override
    @Transactional
    public List<com.data_base.entities.File> getFilesForDictionary(List<Long> fileIds) {
        return this.fileRepository.findByIdInOrderByIdAsc(fileIds);
    }

    @Override
    @Transactional
    public List<com.data_base.entities.File> getFilesForTrainingSets(long specialityId) {
        List<com.data_base.entities.File> result = new ArrayList<>();
        List<Long> fileIds = this.fileDictionaryForSpecialityRepository.findAllFileIdsBySpecialityIdOrderByFileIdAsc(specialityId);

        for (long fileId : fileIds) {
            com.data_base.entities.File file = this.fileRepository.getOne(fileId);

            if (!this.isListContainsFile(result, file)) {
                result.add(file);
            }
        }

        return result;
    }

    @Override
    @Transactional
    public List<FileMarkForSpeciality> getFilesMarks(List<Long> fileIds, long specialityId) {
        return this.fileMarkRepository.findByFileIdInAndSpecialityIdOrderByFileIdAsc(fileIds, specialityId);
    }

    @Override
    @Transactional
    public List<FileDto> getFiles(PageSettings pageSettings, FileFilters filterValues) {
        Pageable dbQuerySettings = PageRequest.of(
                pageSettings.getPageNumber() - 1,
                pageSettings.getCountOfObjectsInOnePage(),
                Sort.Direction.DESC,
                "id"
        );
        Page<com.data_base.entities.File> files = this.getFilesPage(dbQuerySettings, filterValues);

        return this.fileToFileDtoMapper.filesToFileDtos(files);
    }

    @Override
    public long getFilesCount(FileFilters filterValues) {
        if(filterValues.getSpecialityId() > 0){
            List<Long> fileIdsWithHighMarksForSpeciality = this.fileMarkRepository.findAllRealResumeBySpeacialityIdAndMaxMark(new PageRequest(0, 1500), filterValues.getSpecialityId());

            if (filterValues.isLearnExample()) {
               fileIdsWithHighMarksForSpeciality = this.fileMarkRepository.findAllLearnExampleBySpeacialityIdAndMaxMark(new PageRequest(0, 1500), filterValues.getSpecialityId());
            }

            if (!StringUtils.isEmpty(filterValues.getFileName())) {
                return this.fileRepository.countByNameContainingIgnoreCaseAndIdIn(filterValues.getFileName(), fileIdsWithHighMarksForSpeciality);
            } else {
                return this.fileRepository.countByIdIn(fileIdsWithHighMarksForSpeciality);
            }
        }

        if (filterValues.isLearnExample()) {
            if (!StringUtils.isEmpty(filterValues.getFileName())) {
                return this.fileRepository.countByNameContainingIgnoreCaseAndLearnExampleTrue(filterValues.getFileName());
            }

            return this.fileRepository.countByLearnExampleTrue();
        } else {
            if (!StringUtils.isEmpty(filterValues.getFileName())) {
                return this.fileRepository.countByNameContainingIgnoreCaseAndLearnExampleFalse(filterValues.getFileName());
            }

            return this.fileRepository.countByLearnExampleFalse();
        }
    }

    @Override
    public String readTextFromFile(String filePath) {
        String fileExtension = FileNameUtils.getExtension(filePath);

        if(fileExtension.equals("docx")){
            return FileReadingUtils.getTextFromWord(filePath);
        } else {
            return FileReadingUtils.getTextFromPdf(filePath);
        }
    }

    @Override
    public int getFilesCount(String directoryPath) {
        return new File(directoryPath).listFiles().length;
    }

    @Override
    public long saveFileToDataBase(String fileName, String filePath, double[] fileMarks, boolean isLearnExample, User hrManager) {
        List<Speciality> specialities = specialityService.getAllSpecialitiesOrdered();

        com.data_base.entities.File file = new com.data_base.entities.File();
        file.setName(fileName);
        file.setPath(filePath);
        file.setLearnExample(isLearnExample);
        file.setHrManager(hrManager);

        this.fileRepository.saveAndFlush(file);

        int fileMarkIndex = 0;

        for (Speciality speciality : specialities) {
            FileMarkForSpeciality fileMarkForSpeciality = new FileMarkForSpeciality();
            fileMarkForSpeciality.setFile(file);
            fileMarkForSpeciality.setSpeciality(speciality);
            fileMarkForSpeciality.setMark(fileMarks[fileMarkIndex]);

            this.fileMarkRepository.saveAndFlush(fileMarkForSpeciality);

            fileMarkIndex++;
        }

        return file.getId();
    }

    @Override
    @Transactional
    public void moveAndSaveLearnExample(MultipartFile file, String fileName) {
        String fileExtension = FilenameUtils.getExtension(fileName);
        String filePath = this.moveMultipartToStorage(file, fileName);
        List<Speciality> specialities = this.specialityService.getAllSpecialitiesOrdered();
        double[] emptyMarks = new double[specialities.size()];
        int specialityIndex = 0;

        for (Speciality speciality : specialities) {
            emptyMarks[specialityIndex] = 0;
            specialityIndex++;
        }

        if (fileExtension.equals("zip")) {
            String unzippedDirectoryPath = this.unzipFile(filePath);

            try {
                Path unzippedDirectory = Paths.get(unzippedDirectoryPath);
                List<Path> filesInUnzippedDirectory = Files
                        .walk(unzippedDirectory)
                        .filter(Files::isRegularFile)
                        .collect(Collectors.toList());

                this.moveAllFilesToStorageAndSaveInDatabase(emptyMarks, filesInUnzippedDirectory);
               this.removeUnzippedDirectory(unzippedDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.saveFileToDataBase(fileName, filePath, emptyMarks, true, null);
        }
    }

    @Override
    public String moveFileToStorage(String filePath, String fileName) {
        String[] fileNameArray = fileName.replace(".", "@").split("@");
        String newFilePath = this.fileStorageDir + UUID.randomUUID() + "." + fileNameArray[fileNameArray.length - 1];

        try {
            Files.move(Paths.get(filePath), Paths.get(newFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newFilePath;
    }

    @Override
    public String moveMultipartToStorage(MultipartFile file, String fileName) {
        String[] fileNameArray = fileName.replace(".", "@").split("@");
        String newFilePath = this.fileStorageDir + UUID.randomUUID() + "." + fileNameArray[fileNameArray.length - 1];

        try {
            file.transferTo(Paths.get(newFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newFilePath;
    }

    @Override
    @Transactional
    public void removeFile(long fileId) {
        com.data_base.entities.File file = this.fileRepository.getOne(fileId);

        if(file.isLearnExample()){
            List<com.data_base.entities.File> realFiles = this.fileRepository.findAllByLearnExampleId(file.getId());

            for(com.data_base.entities.File realFile: realFiles){
                realFile.setLearnExampleId(null);
                this.fileRepository.saveAndFlush(realFile);
            }
        }

        Path filePath = Paths.get(file.getPath());

        try {
            Files.delete(filePath);
        } catch (IOException ioException) {

        }

        this.fileMarkRepository.deleteAllByFileId(fileId);
        this.fileMarkRepository.flush();
        this.fileRepository.deleteById(fileId);
        this.fileRepository.flush();
    }

    @Override
    public HrDto updateFileHired(FileHiredDto fileHiredDto, Principal principal){
        com.data_base.entities.File file = this.fileRepository.getOne(fileHiredDto.getFileId());
        file.setHired(fileHiredDto.isHired());
        this.fileRepository.saveAndFlush(file);
        return this.updateFileHrManager(fileHiredDto.getFileId(), principal);
    }

    public String unzipFile(String filePath) {
        try {
            ZipFile zipFile = new ZipFile(filePath);

            String unzipDirectoryPath = filePath.substring(0, filePath.length() - 4);

            if (!Files.exists(Paths.get(unzipDirectoryPath))) {
                Files.createDirectory(Paths.get(unzipDirectoryPath));
            }

            zipFile.setFileNameCharset("ISO8859-1");
            List list = zipFile.getFileHeaders();
            UnzipParameters param = new UnzipParameters();

            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                FileHeader fh = (FileHeader) iterator.next();
                byte[] b = fh.getFileName().getBytes("ISO8859-1");
                String fname = null;
                try {
                    CharsetDetector charDetect = new CharsetDetector();
                    charDetect.setText(b);
                    String charSet = charDetect.detect().getName();
                    fname = new String(b, charSet);
                } catch (Throwable e) {
                    fname = fh.getFileName();
                }
                zipFile.extractFile(fh, unzipDirectoryPath, param, fname);
            }

            Files.delete(Paths.get(filePath));

            return unzipDirectoryPath;
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public void removeUnzippedDirectory(Path unzippedDirectory) throws IOException {
        Files.delete(unzippedDirectory);
    }

    @Override
    public HrDto updateFileHrManager(long fileId, Principal principal){
        com.data_base.entities.File file = this.fileRepository.getOne(fileId);
        HrDto result = new HrDto();

        if(!file.isLearnExample() && ObjectUtils.isEmpty(file.getHrManager())){
            User hrManager = (User) this.userDetailsService.loadUserByUsername(principal.getName());

            if(hrManager.getRole().getId() == 2) {
                file.setHrManager(hrManager);
            }


        }
        User hrManager = (User) this.userDetailsService.loadUserByUsername(principal.getName());
        result.setFullName(hrManager.getFullName());
        result.setId(hrManager.getId());

        this.fileRepository.saveAndFlush(file);

        return result;
    }

    @Override
    public void clearFileHrManager(long hrManagerId){
        List<com.data_base.entities.File> files = this.fileRepository.findAllByHrManagerId(hrManagerId);

        for(com.data_base.entities.File file: files){
            file.setHrManager(null);
            this.fileRepository.saveAndFlush(file);
        }
    }

    @Override
    @Transactional
    public long makeLearnExampleFromRealFile(long fileId){
        com.data_base.entities.File realFile = this.fileRepository.getOne(fileId);
        com.data_base.entities.File learnExample = new com.data_base.entities.File();

        learnExample.setName(realFile.getName());
        learnExample.setHired(realFile.isHired());
        learnExample.setHrManager(realFile.getHrManager());
        learnExample.setLearnExample(true);
        learnExample.setPath(this.copyFileToStorage(realFile.getPath(), realFile.getName()));
        this.fileRepository.saveAndFlush(learnExample);
        this.copyFileMarksToLearnExample(learnExample, realFile.getMarks());

        realFile.setLearnExampleId(learnExample.getId());
        this.fileRepository.saveAndFlush(realFile);

        return learnExample.getId();
    }

    @Override
    public void unlinkFileFromStorage(String filePath){
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String saveJsonToStorage(String json){
        String newFilePath = this.fileStorageDir + UUID.randomUUID() + ".json";

        try {
            Files.createFile(Paths.get(newFilePath));
            Files.write(Paths.get(newFilePath), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newFilePath;
    }

    @Override
    public void saveResume(String html, String specialityName){
        WordprocessingMLPackage wordMLPackage = null;
        String newFilePath = this.fileStorageDir + UUID.randomUUID() + ".docx";
html = html.replace("&nbsp", "").replace("nbsp", "");
        try
        {
            wordMLPackage = WordprocessingMLPackage.createPackage();
        }
        catch (InvalidFormatException e)
        {
            e.printStackTrace();
        }

        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);

        try {
            wordMLPackage.getMainDocumentPart().getContent().addAll(
                    XHTMLImporter.convert( html, null) );
        } catch (Docx4JException e) {
            e.printStackTrace();
        }

        try{
            Files.write(Paths.get(newFilePath), XmlUtils.marshaltoString(wordMLPackage
                    .getMainDocumentPart().getJaxbElement(), true, true).getBytes());

            com.data_base.entities.File file = new com.data_base.entities.File();
            file.setLearnExample(false);
            file.setPath(newFilePath);
            file.setName("Резюме (" + specialityName + ").docx");
            this.fileRepository.saveAndFlush(file);

            List<Speciality> specialities = this.specialityService.getAllSpecialitiesOrdered();

            for (Speciality speciality : specialities) {
                FileMarkForSpeciality fileMarkForSpeciality = new FileMarkForSpeciality();
                fileMarkForSpeciality.setFile(file);
                fileMarkForSpeciality.setSpeciality(speciality);
                fileMarkForSpeciality.setMark(0);
                this.fileMarkRepository.saveAndFlush(fileMarkForSpeciality);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String copyFileToStorage(String filePath, String fileName) {
        String[] fileNameArray = fileName.replace(".", "@").split("@");
        String newFilePath = this.fileStorageDir + UUID.randomUUID() + "." + fileNameArray[fileNameArray.length - 1];

        try {
            Files.copy(Paths.get(filePath), Paths.get(newFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newFilePath;
    }

    private List<com.data_base.entities.File> getFiles(List<FileMarkForSpeciality> fileMarks) {
        return fileMarks.stream().map(FileMarkForSpeciality::getFile).collect(Collectors.toList());
    }

    private Page<com.data_base.entities.File> getFilesPage(Pageable dbSettings, FileFilters filterValues) {
        if (filterValues.isLearnExample()) {
            return this.getFilesLearnExamplePage(dbSettings, filterValues);
        } else {
            return this.getFilesNotLearnExamplePage(dbSettings, filterValues);
        }
    }

    private Page<com.data_base.entities.File> getFilesNotLearnExamplePage(Pageable dbSettings, FileFilters filterValues) {
        if(filterValues.getSpecialityId() > 0){
            List<Long> fileIdsWithHighMarksForSpeciality = this.fileMarkRepository.findAllRealResumeBySpeacialityIdAndMaxMark(new PageRequest(0, 1500), filterValues.getSpecialityId());

            if (!StringUtils.isEmpty(filterValues.getFileName())) {
                return this.fileRepository.findAllByNameContainingIgnoreCaseAndIdIn(filterValues.getFileName(), fileIdsWithHighMarksForSpeciality, dbSettings);
            } else {
                return this.fileRepository.findAllByIdIn(fileIdsWithHighMarksForSpeciality, dbSettings);
            }
        }

        if (!StringUtils.isEmpty(filterValues.getFileName())) {
            return this.fileRepository.findAllByNameContainingIgnoreCaseAndLearnExampleFalse(filterValues.getFileName(), dbSettings);
        } else {
            return this.fileRepository.findAllByLearnExampleFalse(dbSettings);
        }
    }

    private Page<com.data_base.entities.File> getFilesLearnExamplePage(Pageable dbSettings, FileFilters filterValues) {
        if(filterValues.getSpecialityId() > 0){
            List<Long> fileIdsWithHighMarksForSpeciality = this.fileMarkRepository.findAllLearnExampleBySpeacialityIdAndMaxMark(new PageRequest(0, 1500), filterValues.getSpecialityId());

            if (!StringUtils.isEmpty(filterValues.getFileName())) {
                return this.fileRepository.findAllByNameContainingIgnoreCaseAndIdIn(filterValues.getFileName(), fileIdsWithHighMarksForSpeciality, dbSettings);
            } else {
                return this.fileRepository.findAllByIdIn(fileIdsWithHighMarksForSpeciality, dbSettings);
            }
        }

        if (!StringUtils.isEmpty(filterValues.getFileName())) {
            return this.fileRepository.findAllByNameContainingIgnoreCaseAndLearnExampleTrue(filterValues.getFileName(), dbSettings);
        } else {
            return this.fileRepository.findAllByLearnExampleTrue(dbSettings);
        }
    }

    private boolean isListContainsFile(List<com.data_base.entities.File> filesList, com.data_base.entities.File file) {
        for (com.data_base.entities.File listFile : filesList) {
            if (file.getPath().equals(listFile.getPath())) {
                return true;
            }
        }

        return false;
    }

    private void moveAllFilesToStorageAndSaveInDatabase(double[] emptyMarks, List<Path> filesInUnzippedDirectory) {
        for (Path fileInUnzippedDirectory : filesInUnzippedDirectory) {
            String currentFileName = fileInUnzippedDirectory.getFileName().toFile().getName().toString();
            String currentFilePath = this.moveFileToStorage(fileInUnzippedDirectory.toString(), currentFileName);
            this.saveFileToDataBase(currentFileName, currentFilePath, emptyMarks, true, null);
        }
    }

    private void copyFileMarksToLearnExample(com.data_base.entities.File learnExample, List<FileMarkForSpeciality> realFileMarks){
        for(FileMarkForSpeciality fileMark: realFileMarks){
            FileMarkForSpeciality learnExampleFileMark = new FileMarkForSpeciality();
            learnExampleFileMark.setFile(learnExample);
            learnExampleFileMark.setSpeciality(fileMark.getSpeciality());
            learnExampleFileMark.setMark(fileMark.getMark());
            this.fileMarkRepository.saveAndFlush(learnExampleFileMark);
        }
    }
}
