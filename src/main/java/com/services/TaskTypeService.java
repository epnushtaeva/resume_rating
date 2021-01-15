package com.services;

import com.data_base.entities.TaskType;

import java.util.List;

public interface TaskTypeService {
    List<TaskType> getAllTaskTypes();

    TaskType getTaskType(long taskTypeId);
}
