package com.spring.batch;

import javax.validation.constraints.Pattern;

public class DataHolder {
    private long serialNumber;
    private String companyName;
    private String employee;

    @Pattern(regexp = ".*COLLINS")
    private String description;
    private int leave;

    public long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLeave() {
        return leave;
    }

    public void setLeave(int leave) {
        this.leave = leave;
    }


    @Override
    public String toString() {
        return "DataHolder{" +
                "serialNumber=" + serialNumber +
                ", companyName='" + companyName + '\'' +
                ", employee='" + employee + '\'' +
                ", description='" + description + '\'' +
                ", leave=" + leave +
                '}';
    }
}
