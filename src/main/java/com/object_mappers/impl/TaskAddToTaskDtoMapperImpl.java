package com.object_mappers.impl;

import com.data_base.entities.Task;
import com.dto.TaskAddDto;
import com.object_mappers.TaskAddToTaskDtoMapper;
import com.services.SpecialityService;
import com.services.TaskStatusService;
import com.services.TaskTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class TaskAddToTaskDtoMapperImpl implements TaskAddToTaskDtoMapper {
    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private TaskTypeService taskTypeService;

    @Autowired
    private SpecialityService specialityService;

    @Override
    public Task taskAddDtoToTask(TaskAddDto taskAddDto) {
        Task task = new Task();
        task.setTaskType(this.taskTypeService.getTaskType(taskAddDto.getTaskTypeId()));

        if(taskAddDto.getSpecialityId() != null) {
            task.setSpeciality(this.specialityService.getSpecialityById(taskAddDto.getSpecialityId()));
        }

        task.setTaskDate(Date.valueOf(LocalDate.parse(taskAddDto.getTaskDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        task.setStartTime(Time.valueOf(taskAddDto.getStartTime()));

        if(!StringUtils.isEmpty(taskAddDto.getEndTime())){
            task.setEndTime(Time.valueOf(taskAddDto.getEndTime()));
        }

        task.setTaskStatus(this.taskStatusService.getTaskStatus(taskAddDto.getTaskStatusId()));
        return task;
    }
}
