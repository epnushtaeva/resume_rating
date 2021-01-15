package com.classes;

import com.dto.SpecialityDto;

import java.util.List;

public class SpecialityDataTableResult {
    private List<SpecialityDto> data;
    private long totalRecords;

    public List<SpecialityDto> getData() {
        return data;
    }

    public void setData(List<SpecialityDto> data) {
        this.data = data;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }
}
