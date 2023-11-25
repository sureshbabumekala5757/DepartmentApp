package com.apcpdcl.departmentapp.models;


/**
 * Created by Haseen on 12-04-2018.
 */
public class SubStationModel {
    private String SSCODE;
    private String SSNAME;
    private String EXISTS;

    public String getSSCODE() {
        return SSCODE;
    }

    public void setSSCODE(String SSCODE) {
        this.SSCODE = SSCODE;
    }

    public String getSSNAME() {
        return SSNAME;
    }

    public void setSSNAME(String SSNAME) {
        this.SSNAME = SSNAME;
    }

    public String getEXISTS() {
        return EXISTS;
    }

    public void setEXISTS(String EXISTS) {
        this.EXISTS = EXISTS;
    }
}
