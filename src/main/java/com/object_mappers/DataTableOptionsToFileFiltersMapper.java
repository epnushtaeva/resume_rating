package com.object_mappers;

import com.classes.DataTableRequest;
import com.services.classes.FileFilters;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface DataTableOptionsToFileFiltersMapper {
    @Mappings({
            @Mapping(target="fileName", source="dataTableRequest.searchVal")
    })
    FileFilters distractFileFilters(DataTableRequest dataTableRequest);
}
