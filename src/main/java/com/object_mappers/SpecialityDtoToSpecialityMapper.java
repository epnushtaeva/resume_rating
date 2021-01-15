package com.object_mappers;

import com.data_base.entities.Speciality;
import com.dto.SpecialityDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface SpecialityDtoToSpecialityMapper {
    @Mappings({
            @Mapping(target = "name", source = "specialityName"),
            @Mapping(target = "employeesNeeded", source = "employees")
    })
    Speciality specialityDtoToSpeciality(SpecialityDto specialityDto);
}
