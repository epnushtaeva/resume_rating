package com.classes;

import com.dto.FileDto;

import java.util.List;

public class FileDataTableResult {
    private List<FileDto> data;
    private long totalRecords;

    public List<FileDto> getData() {
        return data;
    }

    public void setData(List<FileDto> data) {
        this.data = data;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }
}
