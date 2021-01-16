package com.services;

import com.classes.LastPageResult;
import com.data_base.entities.Task;
import com.dto.TaskAddDto;
import com.dto.TaskDataTableDto;
import com.services.classes.PageSettings;
import com.services.classes.TasksFilters;

import javax.transaction.Transactional;
import java.util.List;

public interface TaskService {
    List<TaskDataTableDto> getTasks(PageSettings pageSettings, TasksFilters filterValues);

    LastPageResult getLastPage(long specialityId);

    long getTasksCount(TasksFilters filterValues);

    TaskAddDto addTask(TaskAddDto taskAddDto);

    void removeTask(long taskId);

    void updateTaskStatus(long taskId, long statusId);

    List<Task> getUnexecutedTasks();

    @Transactional
    void removeAllTasksForSpeciality(long specialityId);
}
