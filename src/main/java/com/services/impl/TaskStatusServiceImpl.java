package com.services.impl;

import com.data_base.entities.TaskStatus;
import com.data_base.repositories.TaskStatusRepository;
import com.services.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusServiceImpl implements TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Override
    public List<TaskStatus> getAllTaskStatuses() {
        return this.taskStatusRepository.findAll();
    }

    @Override
    public TaskStatus getTaskStatus(long taskStatusId) {
        return this.taskStatusRepository.getOne(taskStatusId);
    }
}
