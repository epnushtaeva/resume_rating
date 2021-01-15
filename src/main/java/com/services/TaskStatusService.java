package com.services;

import com.data_base.entities.TaskStatus;

import java.util.List;

public interface TaskStatusService {
    List<TaskStatus> getAllTaskStatuses();

    TaskStatus getTaskStatus(long taskStatusId);
}
