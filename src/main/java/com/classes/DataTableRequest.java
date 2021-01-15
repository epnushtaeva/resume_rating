package com.classes;

import com.services.enums.FileMarkFilterType;

public class DataTableRequest {
    private int page;
    private int rowsPerPage;
    private String searchVal;
    private double mark;
    private int fileMarkFilterType;
    private long fileId;
    private String taskDateFrom;
    private String taskDateTo;
    private String username;
    private String fullName;
    private long roleId;
    private long postId;
    private long specialityId;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public String getSearchVal() {
        return searchVal;
    }

    public void setSearchVal(String searchVal) {
        this.searchVal = searchVal;
    }

    public int getFileMarkFilterType() {
        return fileMarkFilterType;
    }

    public void setFileMarkFilterType(int  fileMarkFilterType) {
        this.fileMarkFilterType = fileMarkFilterType;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }


    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public String getTaskDateFrom() {
        return taskDateFrom;
    }

    public void setTaskDateFrom(String taskDateFrom) {
        this.taskDateFrom = taskDateFrom;
    }

    public String getTaskDateTo() {
        return taskDateTo;
    }

    public void setTaskDateTo(String taskDateTo) {
        this.taskDateTo = taskDateTo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public long getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(long specialityId) {
        this.specialityId = specialityId;
    }
}
