package com.dto;

public class TaskDataTableDto {
    private Long taskId;
    private String taskType;
    private String info = "";
    private String specialityName;
    private String taskDate;
    private String startTime;
    private String endTime = "";
    private String status;
    private String deleteButton = "";
    private int pageFrom;
    private int pagesCount;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(String deleteButton) {
        this.deleteButton = deleteButton;
    }

    public int getPageFrom() {
        return pageFrom;
    }

    public void setPageFrom(int pageFrom) {
        this.pageFrom = pageFrom;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }
}
