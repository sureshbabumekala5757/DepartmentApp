package com.apcpdcl.departmentapp.models;

public class ReasonsModel {

    private int l4_ID;
    private int l1_ID;
    private int l3_ID;
    private int l2_ID;
    private String flag;
    private String faultSubCategory;
    private String faultCategory;
    private String type;
    private String interruptionType;
    private String relayInduction;

    public int getL4_ID() {
        return l4_ID;
    }

    public void setL4_ID(int l4_ID) {
        this.l4_ID = l4_ID;
    }

    public int getL1_ID() {
        return l1_ID;
    }

    public void setL1_ID(int l1_ID) {
        this.l1_ID = l1_ID;
    }

    public int getL3_ID() {
        return l3_ID;
    }

    public void setL3_ID(int l3_ID) {
        this.l3_ID = l3_ID;
    }

    public int getL2_ID() {
        return l2_ID;
    }

    public void setL2_ID(int l2_ID) {
        this.l2_ID = l2_ID;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFaultSubCategory() {
        return faultSubCategory;
    }

    public void setFaultSubCategory(String faultSubCategory) {
        this.faultSubCategory = faultSubCategory;
    }

    public String getFaultCategory() {
        return faultCategory;
    }

    public void setFaultCategory(String faultCategory) {
        this.faultCategory = faultCategory;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInterruptionType() {
        return interruptionType;
    }

    public void setInterruptionType(String interruptionType) {
        this.interruptionType = interruptionType;
    }

    public String getRelayInduction() {
        return relayInduction;
    }

    public void setRelayInduction(String relayInduction) {
        this.relayInduction = relayInduction;
    }
}
