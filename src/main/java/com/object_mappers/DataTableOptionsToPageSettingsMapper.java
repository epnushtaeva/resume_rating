package com.object_mappers;

import com.classes.DataTableRequest;
import com.services.classes.PageSettings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface DataTableOptionsToPageSettingsMapper {
    @Mappings({
            @Mapping(target="pageNumber", source="dataTableRequest.page"),
            @Mapping(target="countOfObjectsInOnePage", source="dataTableRequest.rowsPerPage")
    })
    PageSettings distractPageSettings(DataTableRequest dataTableRequest);
}
