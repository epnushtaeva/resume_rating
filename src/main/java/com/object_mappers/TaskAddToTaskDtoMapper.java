package com.object_mappers;

import com.data_base.entities.Task;
import com.dto.TaskAddDto;

public interface TaskAddToTaskDtoMapper {
    Task taskAddDtoToTask(TaskAddDto taskAddDto);
}
