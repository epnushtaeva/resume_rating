package com.services.classes;

import com.services.enums.FileMarkFilterType;

public class FileFilters {
    private String fileName;
    private boolean isLearnExample = false;
    private long specialityId = 0;

    public boolean isLearnExample() {
        return isLearnExample;
    }

    public FileFilters setLearnExample(boolean learnExample) {
        isLearnExample = learnExample;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public FileFilters setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public long getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(long specialityId) {
        this.specialityId = specialityId;
    }
}
