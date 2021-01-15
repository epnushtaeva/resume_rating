package com.object_mappers;

import com.classes.DataTableRequest;
import com.services.classes.SpecialityFilters;
import com.services.enums.FileMarkFilterType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface DataTableOptionsToSpecialityFiltersMapper {
    @Mappings({
            @Mapping(target="fileId", source="dataTableRequest.fileId"),
            @Mapping(target="markFilterType", source="dataTableRequest.fileMarkFilterType"),
            @Mapping(target="specialityName", source="dataTableRequest.searchVal"),
            @Mapping(target="mark", source="dataTableRequest.mark")
    })
    SpecialityFilters distractSpecialityFilters(DataTableRequest dataTableRequest);

    default FileMarkFilterType toFilterType(int markFilterType){
        switch (markFilterType){
            case 5:
                return FileMarkFilterType.EQUAL;
            case 4:
                return FileMarkFilterType.LESS_THAN_OR_EQUAL;
            case 3:
                return FileMarkFilterType.MORE_THAN_OR_EQUAL;
            case 2:
                return FileMarkFilterType.LESS_THAN;
            case 1:
                return FileMarkFilterType.MORE_THAN;
        }

        return FileMarkFilterType.DEFAULT;
    }
}
