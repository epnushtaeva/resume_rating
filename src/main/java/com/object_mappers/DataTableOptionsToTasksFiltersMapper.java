package com.object_mappers;

import com.classes.DataTableRequest;
import com.services.classes.TasksFilters;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface DataTableOptionsToTasksFiltersMapper {
    @Mappings({
            @Mapping(target="taskDateFrom", source="dataTableRequest.taskDateFrom"),
            @Mapping(target="taskDateTo", source="dataTableRequest.taskDateTo"),
    })
    TasksFilters distractTasksFilters(DataTableRequest dataTableRequest);
}
