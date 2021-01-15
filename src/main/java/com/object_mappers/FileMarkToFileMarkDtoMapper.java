package com.object_mappers;

import com.data_base.entities.FileMarkForSpeciality;
import com.dto.FileMarkDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FileMarkToFileMarkDtoMapper {

    List<FileMarkDto> toFileMarkDtos(List<FileMarkForSpeciality> fileMarks);

    List<FileMarkDto> toFileMarkDtos(Page<FileMarkForSpeciality> fileMarks);
}
