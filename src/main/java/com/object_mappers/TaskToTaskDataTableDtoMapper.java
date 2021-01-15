package com.object_mappers;

import com.data_base.entities.FileMarkForSpeciality;
import com.data_base.entities.Task;
import com.dto.SpecialityDto;
import com.dto.TaskDataTableDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskToTaskDataTableDtoMapper {
    @Mappings({
            @Mapping(target="taskId", source="id"),
            @Mapping(target="taskType", source="taskType.name"),
            @Mapping(target="status", source="taskStatus.name"),
            @Mapping(target="specialityName", source="speciality.name"),
            @Mapping(target="taskDate", source="taskDate", dateFormat = "dd.MM.yyyy"),
            @Mapping(target="startTime",expression = "java(String.valueOf(task.getStartTime() == null?\"\":task.getStartTime()))"),
            @Mapping(target="endTime",expression = "java(String.valueOf(task.getEndTime()== null?\"\":task.getEndTime()))")
    })
    TaskDataTableDto taskToTaskDataTableDto(Task task);

    List<TaskDataTableDto> tasksToTaskDataTableDtos(Page<Task> tasks);
}
