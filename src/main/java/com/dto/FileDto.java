package com.dto;

import java.util.ArrayList;
import java.util.List;

public class FileDto {
    private long id;
    private String fileName;
    private String filePath;
    private List<SpecialityDto> specialities = new ArrayList<>();
    private long serverItemsLength = 0;
    private boolean loading = false;
    private boolean isHired;
    private String hr = "";
    private long userId;
    private boolean learnExample;
    private Long learnExampleId;
    private String name;

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public List<SpecialityDto> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(List<SpecialityDto> specialities) {
        this.specialities = specialities;
    }

    public long getServerItemsLength() {
        return serverItemsLength;
    }

    public void setServerItemsLength(long serverItemsLength) {
        this.serverItemsLength = serverItemsLength;
    }

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

    public boolean isHired() {
        return isHired;
    }

    public void setHired(boolean hired) {
        isHired = hired;
    }

    public String getHr() {
        return hr;
    }

    public void setUserFullName(String userFullName) {
        this.hr = userFullName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isLearnExample() {
        return learnExample;
    }

    public void setLearnExample(boolean learnExample) {
        this.learnExample = learnExample;
    }

    public Long getLearnExampleId() {
        return learnExampleId;
    }

    public void setLearnExampleId(Long learnExampleId) {
        this.learnExampleId = learnExampleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
