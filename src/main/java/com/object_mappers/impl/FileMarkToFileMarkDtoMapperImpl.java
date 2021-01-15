package com.object_mappers.impl;

import com.data_base.entities.File;
import com.data_base.entities.FileMarkForSpeciality;
import com.dto.FileMarkDto;
import com.object_mappers.FileMarkToFileMarkDtoMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FileMarkToFileMarkDtoMapperImpl implements FileMarkToFileMarkDtoMapper {

    @Override
    public List<FileMarkDto> toFileMarkDtos(List<FileMarkForSpeciality> fileMarks) {
        List<FileMarkDto> result = new ArrayList<>();

        for(FileMarkForSpeciality fileMarkForSpeciality: fileMarks){
            File fileMarkFile = fileMarkForSpeciality.getFile();
            FileMarkDto fileMarkDto = new FileMarkDto();

            fileMarkDto.setFilePath(fileMarkFile.getPath());

            if(!result.contains(fileMarkDto)){
                fileMarkDto.setSpecialityNamesToMarks(new LinkedHashMap<>());
                result.add(fileMarkDto);
            } else {
                fileMarkDto = result.get(result.indexOf(fileMarkDto));
            }

            fileMarkDto.setFileName(fileMarkFile.getName());
            fileMarkDto.setId(fileMarkForSpeciality.getId());
            fileMarkDto.getSpecialityNamesToMarks().put(fileMarkForSpeciality.getSpeciality().getName(), fileMarkForSpeciality.getMark());
        }

        return result;
    }

    @Override
    public List<FileMarkDto> toFileMarkDtos(Page<FileMarkForSpeciality> fileMarks) {
        List<FileMarkDto> result = new ArrayList<>();

        for(FileMarkForSpeciality fileMarkForSpeciality: fileMarks){
            File fileMarkFile = fileMarkForSpeciality.getFile();
            FileMarkDto fileMarkDto = new FileMarkDto();

            fileMarkDto.setFilePath(fileMarkFile.getPath());

            if(!result.contains(fileMarkDto)){
                fileMarkDto.setSpecialityNamesToMarks(new LinkedHashMap<>());
                result.add(fileMarkDto);
            } else {
                fileMarkDto = result.get(result.indexOf(fileMarkDto));
            }

            fileMarkDto.setFileName(fileMarkFile.getName());
            fileMarkDto.setId(fileMarkForSpeciality.getId());
            fileMarkDto.getSpecialityNamesToMarks().put(fileMarkForSpeciality.getSpeciality().getName(), fileMarkForSpeciality.getMark());
        }

        return result;
    }
}
