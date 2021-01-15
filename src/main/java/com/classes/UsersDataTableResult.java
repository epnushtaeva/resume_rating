package com.classes;

import com.dto.UserDto;

import java.util.List;

public class UsersDataTableResult {
    private List<UserDto> data;
    private long totalRecords;

    public List<UserDto> getData() {
        return data;
    }

    public void setData(List<UserDto> data) {
        this.data = data;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }
}
