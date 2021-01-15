package com.services;

import com.data_base.entities.File;
import com.data_base.entities.FileMarkForSpeciality;
import com.data_base.entities.User;
import com.dto.FileDto;
import com.dto.FileHiredDto;
import com.dto.HrDto;
import com.services.classes.FileFilters;
import com.services.classes.PageSettings;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;

public interface FileService {

    long saveFileToDataBase(String fileName, String filePath, double[] fileMarks, boolean isLearnExample, User user);

    @Transactional
    List<File> getFilesForDictionary(long specialityId);

    @Transactional
    List<File> getFilesForDictionary(List<Long> fileIds);

    @Transactional
    List<File> getFilesForTrainingSets(long specialityId);

    @Transactional
    List<FileMarkForSpeciality> getFilesMarks(List<Long> fileIds, long specialityId);

    int getFilesCount(String directoryPath);

    @Transactional
    List<FileDto> getFiles(PageSettings pageSettings, FileFilters filterValues);

    long getFilesCount(FileFilters filterValues);

    String readTextFromFile(String filePath);

    void moveAndSaveLearnExample(MultipartFile file, String fileName);

    String moveFileToStorage(String filePath, String fileName);

    String moveMultipartToStorage(MultipartFile file, String fileName);

    void removeFile(long fileId);

    HrDto updateFileHired(FileHiredDto fileHiredDto, Principal principal);

    String unzipFile(String filePath);

    void removeUnzippedDirectory(Path unzippedDirectory) throws IOException;

    HrDto updateFileHrManager(long fileId, Principal principal);

    void clearFileHrManager(long hrManagerId);

    @Transactional
    long makeLearnExampleFromRealFile(long fileId);

    void unlinkFileFromStorage(String filePath);

    String saveJsonToStorage(String json);
}
