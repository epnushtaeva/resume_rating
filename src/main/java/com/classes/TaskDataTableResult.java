package com.classes;

import com.dto.TaskDataTableDto;

import java.util.List;

public class TaskDataTableResult {
    private List<TaskDataTableDto> data;
    private long totalRecords;

    public List<TaskDataTableDto> getData() {
        return data;
    }

    public void setData(List<TaskDataTableDto> data) {
        this.data = data;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }
}
