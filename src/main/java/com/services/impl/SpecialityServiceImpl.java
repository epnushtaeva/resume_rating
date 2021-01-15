package com.services.impl;

import com.data_base.entities.FileMarkForSpeciality;
import com.data_base.entities.Speciality;
import com.data_base.repositories.FileMarkRepository;
import com.data_base.repositories.SpecialityRepository;
import com.dto.HrDto;
import com.dto.SpecialityDto;
import com.object_mappers.FileMarkForSpecialityToSpecialityDtoMapper;
import com.object_mappers.SpecialityDtoToSpecialityMapper;
import com.object_mappers.SpecialityToSpecialityDtoMapper;
import com.services.*;
import com.services.classes.PageSettings;
import com.services.classes.SpecialityFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialityServiceImpl implements SpecialityService {
    @Autowired
    private SpecialityRepository specialityRepository;

    @Autowired
    private FileMarkRepository fileMarkRepository;

    @Autowired
    private FileMarkForSpecialityToSpecialityDtoMapper fileMarkForSpecialityToSpecialityDtoMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private SpecialityDtoToSpecialityMapper specialityDtoToSpecialityMapper;

    @Autowired
    private NeuralNetworkService neuralNetworkService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private SpecialityToSpecialityDtoMapper specialityToSpecialityDtoMapper;

    @Override
    public List<Speciality> getAllSpecialities() {
        return specialityRepository.findAll();
    }

    @Override
    public List<Speciality> getAllSpecialitiesOrdered() {
        return specialityRepository.findAllByOrderByIdAsc();
    }

    @Override
    public long getAllSpecialitiesCount() {
        return specialityRepository.count();
    }

    @Override
    public Speciality getSpecialityById(long specialityId) {
        return this.specialityRepository.getOne(specialityId);
    }

    @Override
    public Speciality getSpecialityByName(String specialityName) {
        return this.specialityRepository.findOneByName(specialityName);
    }

    @Override
    @Transactional
    public List<SpecialityDto> getSpecialities(PageSettings pageSettings, SpecialityFilters specialityFilters) {
        Pageable dbQuerySettings = PageRequest.of(
                pageSettings.getPageNumber() - 1,
                pageSettings.getCountOfObjectsInOnePage(),
                Sort.Direction.ASC,
                "id"
        );

        Page<Speciality> specialities = null;

        if(StringUtils.isEmpty(specialityFilters.getSpecialityName())){
            specialities =  this.specialityRepository.findAllByOrderByIdAsc(dbQuerySettings);
        } else {
            specialities =  this.specialityRepository.findByNameContainingIgnoreCaseOrderByIdAsc(dbQuerySettings, specialityFilters.getSpecialityName());
        }


        List<SpecialityDto> specialityDtos = this.specialityToSpecialityDtoMapper.specialitiesToSpecialityDtos(specialities);
        long rowNumber = (pageSettings.getCountOfObjectsInOnePage() * (pageSettings.getPageNumber() - 1)) + 1;

        for(SpecialityDto specialityForFile:specialityDtos){
            specialityForFile.setRowNumber(rowNumber);
            rowNumber++;
        }

        return specialityDtos;
    }

    @Override
    @Transactional
    public long getSpecialitiesCountForEditing(SpecialityFilters specialityFilters) {
        if(StringUtils.isEmpty(specialityFilters.getSpecialityName())){
            return this.specialityRepository.count();
        } else {
            return this.specialityRepository.countByNameContainingIgnoreCase(specialityFilters.getSpecialityName());
        }
    }

    @Override
    @Transactional
    public List<SpecialityDto> getSpecialitiesForFile(PageSettings pageSettings, SpecialityFilters specialityFilters) {
        Pageable dbQuerySettings = PageRequest.of(
                pageSettings.getPageNumber() - 1,
                pageSettings.getCountOfObjectsInOnePage(),
                Sort.Direction.ASC,
                "id"
        );
        Page<FileMarkForSpeciality> specialitiesForFilePage = this.getSpecialitiesForFilePage(dbQuerySettings, specialityFilters);

        List<SpecialityDto> specialitiesForFile = this.fileMarkForSpecialityToSpecialityDtoMapper.filesMarksForSpecialityToSpecialityDtos(specialitiesForFilePage);
        long rowNumber = (pageSettings.getCountOfObjectsInOnePage() * (pageSettings.getPageNumber() - 1)) + 1;

        for(SpecialityDto specialityForFile:specialitiesForFile){
            specialityForFile.setRowNumber(rowNumber);
            rowNumber++;
        }

        return specialitiesForFile;
    }

    @Override
    public long getSpecialitiesCount(SpecialityFilters specialityFilters) {
        if (!StringUtils.isEmpty(specialityFilters.getSpecialityName())) {
            List<Speciality> specialitiesWithNeededName = this.specialityRepository.findAllByNameContainingIgnoreCase(specialityFilters.getSpecialityName());
            long specialitiesCount = 0;

            for(Speciality speciality: specialitiesWithNeededName) {
                switch (specialityFilters.getMarkFilterType()) {
                    case DEFAULT:
                        specialitiesCount += this.fileMarkRepository.countByFileIdAndSpecialityId(specialityFilters.getFileId(), speciality.getId());
                    case EQUAL:
                        specialitiesCount += this.fileMarkRepository.countByFileIdAndSpecialityIdAndMark(specialityFilters.getFileId(), speciality.getId(), specialityFilters.getMark());
                    case MORE_THAN:
                        specialitiesCount += this.fileMarkRepository.countByFileIdAndSpecialityIdAndMarkGreaterThan(specialityFilters.getFileId(), speciality.getId(), specialityFilters.getMark());
                    case MORE_THAN_OR_EQUAL:
                        specialitiesCount += this.fileMarkRepository.countByFileIdAndSpecialityIdAndMarkGreaterThanEqual(specialityFilters.getFileId(), speciality.getId(), specialityFilters.getMark());
                    case LESS_THAN:
                        specialitiesCount += this.fileMarkRepository.countByFileIdAndSpecialityIdAndMarkLessThan(specialityFilters.getFileId(), speciality.getId(), specialityFilters.getMark());
                    case LESS_THAN_OR_EQUAL:
                        specialitiesCount += this.fileMarkRepository.countByFileIdAndSpecialityIdAndMarkLessThanEqual(specialityFilters.getFileId(), speciality.getId(), specialityFilters.getMark());
                }
            }

            return specialitiesCount;
        } else {
            switch (specialityFilters.getMarkFilterType()) {
                case DEFAULT:
                    return this.fileMarkRepository.countByFileId(specialityFilters.getFileId());
                case EQUAL:
                    return this.fileMarkRepository.countByFileIdAndMark(specialityFilters.getFileId(), specialityFilters.getMark());
                case MORE_THAN:
                    return this.fileMarkRepository.countByFileIdAndMarkGreaterThan(specialityFilters.getFileId(), specialityFilters.getMark());
                case MORE_THAN_OR_EQUAL:
                    return this.fileMarkRepository.countByFileIdAndMarkGreaterThanEqual(specialityFilters.getFileId(), specialityFilters.getMark());
                case LESS_THAN:
                    return this.fileMarkRepository.countByFileIdAndMarkLessThan(specialityFilters.getFileId(), specialityFilters.getMark());
                case LESS_THAN_OR_EQUAL:
                    return this.fileMarkRepository.countByFileIdAndMarkLessThanEqual(specialityFilters.getFileId(), specialityFilters.getMark());
            }
        }

        return 0;
    }

    @Override
    @Transactional
    public HrDto updateSpecialityMark(long specialityMarkId, double mark, Principal principal){
        FileMarkForSpeciality fileMarkForSpeciality = this.fileMarkRepository.getOne(specialityMarkId);
        fileMarkForSpeciality.setMark(mark);

        this.fileMarkRepository.saveAndFlush(fileMarkForSpeciality);

        return this.fileService.updateFileHrManager(fileMarkForSpeciality.getFile().getId(), principal);
    }

    @Override
    public SpecialityDto addSpeciality(SpecialityDto specialityDto){
        Speciality speciality = this.specialityDtoToSpecialityMapper.specialityDtoToSpeciality(specialityDto);

        this.specialityRepository.saveAndFlush(speciality);

        specialityDto.setId(speciality.getId());
        return specialityDto;
    }

    @Override
    public void updateSpeciality(SpecialityDto specialityDto){
        Speciality speciality = this.specialityRepository.getOne(specialityDto.getId());
        speciality.setName(specialityDto.getSpecialityName());
        speciality.setEmployeesNeeded(specialityDto.isEmployees());
        this.specialityRepository.saveAndFlush(speciality);
    }

    @Override
    @Transactional
    public void removeSpeciality(long specialityId){
        this.fileMarkRepository.deleteAllBySpecialityId(specialityId);
        this.specialityRepository.deleteById(specialityId);
        this.neuralNetworkService.removeNetworkForSpeciality(specialityId);
        this.taskService.removeAllTasksForSpeciality(specialityId);
        this.dictionaryService.clearDictionary(specialityId);
    }

    @Transactional
    private Page<FileMarkForSpeciality> getSpecialitiesForFilePage(Pageable pageSettings, SpecialityFilters specialityFilters) {
        if (!StringUtils.isEmpty(specialityFilters.getSpecialityName())) {
            List<Long> specialitiesIds = this.specialityRepository
                    .findAllByNameContainingIgnoreCase(specialityFilters.getSpecialityName())
                    .stream()
                    .map(speciality -> speciality.getId())
                    .collect(Collectors.toList());

            switch (specialityFilters.getMarkFilterType()) {
                case DEFAULT:
                    return this.fileMarkRepository.findByFileIdAndSpecialityIdInOrderByFileIdAsc(pageSettings, specialityFilters.getFileId(), specialitiesIds);
                case EQUAL:
                    return this.fileMarkRepository.findByFileIdAndSpecialityIdInAndMarkOrderByFileIdAsc(pageSettings, specialityFilters.getFileId(), specialitiesIds, specialityFilters.getMark());
                case MORE_THAN:
                    return this.fileMarkRepository.findByFileIdAndSpecialityIdInAndMarkGreaterThanOrderByFileIdAsc(pageSettings, specialityFilters.getFileId(), specialitiesIds, specialityFilters.getMark());
                case MORE_THAN_OR_EQUAL:
                    return this.fileMarkRepository.findByFileIdAndSpecialityIdInAndMarkGreaterThanEqualOrderByFileIdAsc(pageSettings, specialityFilters.getFileId(), specialitiesIds, specialityFilters.getMark());
                case LESS_THAN:
                    return this.fileMarkRepository.findByFileIdAndSpecialityIdInAndMarkLessThanOrderByFileIdAsc(pageSettings, specialityFilters.getFileId(), specialitiesIds, specialityFilters.getMark());
                case LESS_THAN_OR_EQUAL:
                    return this.fileMarkRepository.findByFileIdAndSpecialityIdInAndMarkLessThanEqualOrderByFileIdAsc(pageSettings, specialityFilters.getFileId(), specialitiesIds, specialityFilters.getMark());
            }
        } else {
            switch (specialityFilters.getMarkFilterType()) {
                case DEFAULT:
                    return this.fileMarkRepository.findByFileIdOrderByFileIdAsc(pageSettings, specialityFilters.getFileId());
                case EQUAL:
                    return this.fileMarkRepository.findByFileIdAndMarkOrderByFileIdAsc(pageSettings, specialityFilters.getFileId(), specialityFilters.getMark());
                case MORE_THAN:
                    return this.fileMarkRepository.findByFileIdAndMarkGreaterThanOrderByFileIdAsc(pageSettings, specialityFilters.getFileId(), specialityFilters.getMark());
                case MORE_THAN_OR_EQUAL:
                    return this.fileMarkRepository.findByFileIdAndMarkGreaterThanEqualOrderByFileIdAsc(pageSettings, specialityFilters.getFileId(), specialityFilters.getMark());
                case LESS_THAN:
                    return this.fileMarkRepository.findByFileIdAndMarkLessThanOrderByFileIdAsc(pageSettings, specialityFilters.getFileId(), specialityFilters.getMark());
                case LESS_THAN_OR_EQUAL:
                    return this.fileMarkRepository.findByFileIdAndMarkLessThanEqualOrderByFileIdAsc(pageSettings, specialityFilters.getFileId(), specialityFilters.getMark());
            }
        }

        return null;
    }
}
