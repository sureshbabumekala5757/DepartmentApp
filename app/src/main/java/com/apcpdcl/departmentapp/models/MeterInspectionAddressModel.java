package com.apcpdcl.departmentapp.models;

public class MeterInspectionAddressModel {

    private String NAME;
    private String SECTION;
    private String ADDRESS;
    private String CATEGORY;
    private String LOAD;
    private String DISTRIBUTION;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getSECTION() {
        return SECTION;
    }

    public void setSECTION(String SECTION) {
        this.SECTION = SECTION;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }

    public String getLOAD() {
        return LOAD;
    }

    public void setLOAD(String LOAD) {
        this.LOAD = LOAD;
    }

    public String getDISTRIBUTION() {
        return DISTRIBUTION;
    }

    public void setDISTRIBUTION(String DISTRIBUTION) {
        this.DISTRIBUTION = DISTRIBUTION;
    }
}
