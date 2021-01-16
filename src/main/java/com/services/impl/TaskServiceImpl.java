package com.services.impl;

import com.classes.LastPageResult;
import com.data_base.entities.Task;
import com.data_base.repositories.TaskRepository;
import com.dto.TaskAddDto;
import com.dto.TaskDataTableDto;
import com.object_mappers.TaskAddToTaskDtoMapper;
import com.object_mappers.TaskToTaskDataTableDtoMapper;
import com.services.TaskService;
import com.services.TaskStatusService;
import com.services.classes.PageSettings;
import com.services.classes.TasksFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskToTaskDataTableDtoMapper taskToTaskDataTableDtoMapper;

    @Autowired
    private TaskAddToTaskDtoMapper taskAddToTaskDtoMapper;

    @Autowired
    private TaskStatusService taskStatusService;

    @Transactional
    @Override
    public List<TaskDataTableDto> getTasks(PageSettings pageSettings, TasksFilters filterValues) {
        Pageable dbQuerySettings = PageRequest.of(
                pageSettings.getPageNumber() - 1,
                pageSettings.getCountOfObjectsInOnePage(),
                Sort.Direction.DESC,
                "id"
        );
        Page<Task> tasks = this.getTasksPage(dbQuerySettings, filterValues);

        return this.taskToTaskDataTableDtoMapper.tasksToTaskDataTableDtos(tasks);
    }

    @Override
    public LastPageResult getLastPage(long specialityId){
        LastPageResult lastPageResult = new LastPageResult();
        Task task = this.taskRepository.findFirstByTypeIdAndSpecialityIdOrderByIdDesc(2, specialityId);

        if(task != null) {
            lastPageResult.setPageFrom(task.getPageFrom());
            lastPageResult.setPageTo(task.getPageFrom() + task.getPagesCount() - 1);
        }

        return lastPageResult;
    }

    @Transactional
    @Override
    public long getTasksCount(TasksFilters filterValues){
        Date taskDateFrom = null;
        Date taskDateTo = null;
        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("dd.MM.yyyy");

        if (!StringUtils.isEmpty(filterValues.getTaskDateFrom())){
            taskDateFrom = java.sql.Date.valueOf(LocalDate.parse(filterValues.getTaskDateFrom(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }

        if (!StringUtils.isEmpty(filterValues.getTaskDateTo())){
            taskDateTo = java.sql.Date.valueOf(LocalDate.parse(filterValues.getTaskDateTo(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        }

        if(taskDateFrom != null && taskDateTo != null){
            return this.taskRepository.countByTaskDateGreaterThanEqualAndTaskDateLessThanEqual(taskDateFrom, taskDateTo);
        }

        if(taskDateFrom != null){
            return this.taskRepository.countByTaskDateGreaterThanEqual(taskDateFrom);
        }

        if(taskDateTo != null){
            return this.taskRepository.countByTaskDateLessThanEqual(taskDateTo);
        }

        return this.taskRepository.count();
    }

    @Transactional
    @Override
    public TaskAddDto addTask(TaskAddDto taskAddDto) {
        this.taskRepository.save(this.taskAddToTaskDtoMapper.taskAddDtoToTask(taskAddDto));
        return taskAddDto;
    }

    @Transactional
    @Override
    public void removeTask(long taskId) {
          this.taskRepository.deleteById(taskId);
    }

    private Page<Task> getTasksPage(Pageable dbQuerySettings, TasksFilters filterValues){
        Date taskDateFrom = null;
        Date taskDateTo = null;
        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("dd.MM.yyyy");

        if (!StringUtils.isEmpty(filterValues.getTaskDateFrom())){
            taskDateFrom = java.sql.Date.valueOf(LocalDate.parse(filterValues.getTaskDateFrom(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }

        if (!StringUtils.isEmpty(filterValues.getTaskDateTo())){
            taskDateTo = java.sql.Date.valueOf(LocalDate.parse(filterValues.getTaskDateTo(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        }

        if(taskDateFrom != null && taskDateTo != null){
            return this.taskRepository.findAllByTaskDateGreaterThanEqualAndTaskDateLessThanEqualOrderByIdDesc(taskDateFrom, taskDateTo, dbQuerySettings);
        }

        if(taskDateFrom != null){
            return this.taskRepository.findAllByTaskDateGreaterThanEqualOrderByIdDesc(taskDateFrom, dbQuerySettings);
        }

        if(taskDateTo != null){
            return this.taskRepository.findAllByTaskDateLessThanEqualOrderByIdDesc(taskDateTo, dbQuerySettings);
        }

        return this.taskRepository.findAllByOrderByIdDesc(dbQuerySettings);
    }

    @Transactional
    @Override
    public void updateTaskStatus(long taskId, long statusId){
        Task task = this.taskRepository.getOne(taskId);
        task.setTaskStatus(this.taskStatusService.getTaskStatus(statusId));

        if(statusId == 2 || statusId == 3) {
            task.setEndTime(new Time(new java.util.Date().getTime()));
        }

        this.taskRepository.save(task);
    }

    @Override
    public List<Task> getUnexecutedTasks(){
             List<Task> tasks = this.taskRepository.findAllByTaskDateAndStartTimeBetweenAndStatusIdOrderById(new Date(System.currentTimeMillis()), Time.valueOf(LocalTime.now().minus(1, ChronoUnit.HOURS)), Time.valueOf(LocalTime.now()), 1);

             for(Task task: tasks){
                 task.setTaskStatus(this.taskStatusService.getTaskStatus(4));
                 this.taskRepository.save(task);
             }

             return tasks;
    }

    @Override
    @Transactional
    public void removeAllTasksForSpeciality(long specialityId){
        this.taskRepository.deleteAllBySpecialityId(specialityId);
    }
}
