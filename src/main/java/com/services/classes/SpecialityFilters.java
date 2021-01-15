package com.services.classes;

import com.services.enums.FileMarkFilterType;

public class SpecialityFilters {
    private long fileId;
    private String specialityName = "";
    private double mark = 0;
    private FileMarkFilterType markFilterType;

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public FileMarkFilterType getMarkFilterType() {
        return markFilterType;
    }

    public void setMarkFilterType(FileMarkFilterType filterType) {
        this.markFilterType = filterType;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }
}
