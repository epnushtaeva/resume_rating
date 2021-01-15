package com.object_mappers;

import com.data_base.entities.File;
import com.dto.FileDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileToFileDtoMapper {
    @Mappings({
            @Mapping(target="fileName", source="file.name"),
            @Mapping(target="filePath", source="file.path"),
            @Mapping(target="hired", source="file.hired"),
            @Mapping(target="userFullName", source="file.hrManager.fullName"),
            @Mapping(target="userId", source="file.hrManager.id")
    })
    FileDto fileToFileDto(File file);

    List<FileDto> filesToFileDtos(Page<File> files);
}
