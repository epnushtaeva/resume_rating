package com.dto;

import java.util.Map;

public class FileMarkDto {
    private long id;
    private String fileName;
    private String filePath;
    private Map<String, Double> specialityNamesToMarks;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Map<String, Double> getSpecialityNamesToMarks() {
        return specialityNamesToMarks;
    }

    public void setSpecialityNamesToMarks(Map<String, Double> specialityNamesToMarks) {
        this.specialityNamesToMarks = specialityNamesToMarks;
    }

    @Override
    public boolean equals(Object otherFileMarkDto){
        return this.filePath.equals(((FileMarkDto)otherFileMarkDto).getFilePath());
    }
}
