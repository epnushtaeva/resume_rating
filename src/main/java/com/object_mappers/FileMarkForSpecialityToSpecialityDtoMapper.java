package com.object_mappers;

import com.data_base.entities.FileMarkForSpeciality;
import com.dto.SpecialityDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMarkForSpecialityToSpecialityDtoMapper {
    @Mappings({
            @Mapping(target="id", source="fileMarkForSpeciality.id"),
            @Mapping(target="specialityName", source="fileMarkForSpeciality.speciality.name"),
            @Mapping(target="mark", source="fileMarkForSpeciality.mark")
    })
    SpecialityDto fileMarkForSpecialityToSpecialityDto(FileMarkForSpeciality fileMarkForSpeciality);

    List<SpecialityDto> filesMarksForSpecialityToSpecialityDtos(Page<FileMarkForSpeciality> filesMarks);
}
