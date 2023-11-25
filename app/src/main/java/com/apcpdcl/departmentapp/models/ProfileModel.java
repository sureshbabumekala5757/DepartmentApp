package com.apcpdcl.departmentapp.models;

/**
 * Created by Haseena on 06-03-2018.
 */

public class ProfileModel {

    private String UNITCODE;
    private String STATUS;
    private String DESIGNATION;
    private String MOBILE;
    private String NAME;
    private String SECTION;

    public String getUNITCODE() {
        return UNITCODE;
    }

    public void setUNITCODE(String UNITCODE) {
        this.UNITCODE = UNITCODE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getDESIGNATION() {
        return DESIGNATION;
    }

    public void setDESIGNATION(String DESIGNATION) {
        this.DESIGNATION = DESIGNATION;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

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
}
