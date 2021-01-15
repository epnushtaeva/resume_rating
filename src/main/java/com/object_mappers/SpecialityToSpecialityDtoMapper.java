package com.object_mappers;

import com.data_base.entities.Speciality;
import com.dto.SpecialityDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecialityToSpecialityDtoMapper {
    @Mappings({
            @Mapping(target = "specialityName", source = "name"),
            @Mapping(target = "employees", source = "employeesNeeded")
    })
    SpecialityDto specialityToSpecialityDto(Speciality speciality);

    List<SpecialityDto> specialitiesToSpecialityDtos(Page<Speciality> specialities);
}
