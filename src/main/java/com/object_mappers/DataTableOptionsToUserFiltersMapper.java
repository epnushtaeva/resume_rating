package com.object_mappers;

import com.classes.DataTableRequest;
import com.services.classes.UsersFilters;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface DataTableOptionsToUserFiltersMapper {
    @Mappings({
            @Mapping(target="fullName", source="dataTableRequest.fullName"),
            @Mapping(target="login", source="dataTableRequest.username"),
            @Mapping(target="roleId", source="dataTableRequest.roleId"),
            @Mapping(target="postId", source="dataTableRequest.postId")
    })
    UsersFilters distractUserFilters(DataTableRequest dataTableRequest);
}
