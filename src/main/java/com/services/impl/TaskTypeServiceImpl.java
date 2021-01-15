package com.services.impl;

import com.data_base.entities.TaskType;
import com.data_base.repositories.TaskTypeRepository;
import com.services.TaskTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskTypeServiceImpl implements TaskTypeService {
    @Autowired
    private TaskTypeRepository taskTypeRepository;

    @Override
    public List<TaskType> getAllTaskTypes() {
        return this.taskTypeRepository.findAll();
    }

    @Override
    public TaskType getTaskType(long taskTypeId) {
        return this.taskTypeRepository.getOne(taskTypeId);
    }
}
