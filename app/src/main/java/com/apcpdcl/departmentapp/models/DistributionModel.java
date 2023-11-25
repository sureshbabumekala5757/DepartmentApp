package com.apcpdcl.departmentapp.models;

public class DistributionModel {
    private String DistributionName;
    private String DistributionCode;

    public DistributionModel() {

    }

    public DistributionModel(String distributionName, String distributionCode) {
        DistributionName = distributionName;
        DistributionCode = distributionCode;
    }

    public String getDistributionName() {
        return DistributionName;
    }

    public void setDistributionName(String distributionName) {
        DistributionName = distributionName;
    }

    public String getDistributionCode() {
        return DistributionCode;
    }

    public void setDistributionCode(String distributionCode) {
        DistributionCode = distributionCode;
    }
}
