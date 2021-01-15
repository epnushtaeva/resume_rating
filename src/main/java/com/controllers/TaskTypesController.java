package com.controllers;

import com.data_base.entities.TaskType;
import com.services.TaskTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/task_types")
public class TaskTypesController {
    @Autowired
    private TaskTypeService taskTypeService;

    @PostMapping("/all")
    @ResponseBody
    public List<TaskType> getAllTasksTypes(){
        return this.taskTypeService.getAllTaskTypes();
    }
}
