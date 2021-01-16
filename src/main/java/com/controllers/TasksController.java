package com.controllers;

import com.classes.DataTableRequest;
import com.classes.LastPageResult;
import com.classes.LoadAjaxQueriesResult;
import com.classes.TaskDataTableResult;
import com.data_base.entities.Task;
import com.dto.TaskAddDto;
import com.dto.TaskRemoveDto;
import com.object_mappers.DataTableOptionsToPageSettingsMapper;
import com.object_mappers.DataTableOptionsToTasksFiltersMapper;
import com.services.TaskService;
import com.services.classes.PageSettings;
import com.services.classes.TasksFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TasksController {
    @Autowired
    private DataTableOptionsToPageSettingsMapper dataTableOptionsToPageSettingsMapper;

    @Autowired
    private DataTableOptionsToTasksFiltersMapper dataTableOptionsToTasksFiltersMapper;

    @Autowired
    private TaskService taskService;

    @PostMapping("/all")
    @ResponseBody
    public TaskDataTableResult getAllTasksPost(@RequestBody DataTableRequest dataTableRequest) {
        PageSettings pageSettings = this.dataTableOptionsToPageSettingsMapper.distractPageSettings(dataTableRequest);
        TasksFilters tasksFilters = this.dataTableOptionsToTasksFiltersMapper.distractTasksFilters(dataTableRequest);

        TaskDataTableResult result = new TaskDataTableResult();
        result.setData(this.taskService.getTasks(pageSettings, tasksFilters));
        result.setTotalRecords(this.taskService.getTasksCount(tasksFilters));

        return result;
    }

    @PostMapping("/add")
    @ResponseBody
    public TaskAddDto addTask(@RequestBody TaskAddDto taskDto){
        return this.taskService.addTask(taskDto);
    }

    @PostMapping("/delete")
    @ResponseBody
    public LoadAjaxQueriesResult removeTask(@RequestBody TaskRemoveDto taskDto){
        this.taskService.removeTask(taskDto.getTaskId());

        LoadAjaxQueriesResult result = new LoadAjaxQueriesResult();
        result.setRes("OK");
        return result;
    }

    @GetMapping("/get_last_page_from")
    @ResponseBody
    public LastPageResult getLastPageForTask(@RequestParam("speciality_id") long specialityId){
               return this.taskService.getLastPage(specialityId);
    }
}
