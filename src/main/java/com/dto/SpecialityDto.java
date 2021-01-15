package com.dto;

public class SpecialityDto {
    private long id;
    private long rowNumber = 1;
    private String specialityName;
    private double mark;
    private boolean isEmployees;
    private String action;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public long getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(long rowNumber) {
        this.rowNumber = rowNumber;
    }

    public boolean isEmployees() {
        return isEmployees;
    }

    public void setEmployees(boolean employeesNeeded) {
        isEmployees = employeesNeeded;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
