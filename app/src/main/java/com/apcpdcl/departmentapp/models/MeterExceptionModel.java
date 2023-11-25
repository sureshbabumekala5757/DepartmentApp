package com.apcpdcl.departmentapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class MeterExceptionModel implements Serializable{

    private String AGE;
    private String ENDDT;
    private String STARTDT;
    private String STATUS;
    private String BLCLRDT;
    private String CMCELL;
    private String CMPAACD;
    private String CMAGRDETAILS;
    private String CMFDRCD;
    private String CMDTRCD;
    private String CMDRNUM;
    private String CMPOLENUM;
    private String CMCAT;
    private String CMCNAME;
    private String CMUSCNO;
    private ArrayList<MeterExceptionModel> meterExceptionModels;

    public String getAGE() {
        return AGE;
    }

    public void setAGE(String AGE) {
        this.AGE = AGE;
    }

    public String getENDDT() {
        return ENDDT;
    }

    public void setENDDT(String ENDDT) {
        this.ENDDT = ENDDT;
    }

    public String getSTARTDT() {
        return STARTDT;
    }

    public void setSTARTDT(String STARTDT) {
        this.STARTDT = STARTDT;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getBLCLRDT() {
        return BLCLRDT;
    }

    public void setBLCLRDT(String BLCLRDT) {
        this.BLCLRDT = BLCLRDT;
    }

    public String getCMCELL() {
        return CMCELL;
    }

    public void setCMCELL(String CMCELL) {
        this.CMCELL = CMCELL;
    }

    public String getCMPAACD() {
        return CMPAACD;
    }

    public void setCMPAACD(String CMPAACD) {
        this.CMPAACD = CMPAACD;
    }

    public String getCMAGRDETAILS() {
        return CMAGRDETAILS;
    }

    public void setCMAGRDETAILS(String CMAGRDETAILS) {
        this.CMAGRDETAILS = CMAGRDETAILS;
    }

    public String getCMFDRCD() {
        return CMFDRCD;
    }

    public void setCMFDRCD(String CMFDRCD) {
        this.CMFDRCD = CMFDRCD;
    }

    public String getCMDTRCD() {
        return CMDTRCD;
    }

    public void setCMDTRCD(String CMDTRCD) {
        this.CMDTRCD = CMDTRCD;
    }

    public String getCMDRNUM() {
        return CMDRNUM;
    }

    public void setCMDRNUM(String CMDRNUM) {
        this.CMDRNUM = CMDRNUM;
    }

    public String getCMPOLENUM() {
        return CMPOLENUM;
    }

    public void setCMPOLENUM(String CMPOLENUM) {
        this.CMPOLENUM = CMPOLENUM;
    }

    public String getCMCAT() {
        return CMCAT;
    }

    public void setCMCAT(String CMCAT) {
        this.CMCAT = CMCAT;
    }

    public String getCMCNAME() {
        return CMCNAME;
    }

    public void setCMCNAME(String CMCNAME) {
        this.CMCNAME = CMCNAME;
    }

    public String getCMUSCNO() {
        return CMUSCNO;
    }

    public void setCMUSCNO(String CMUSCNO) {
        this.CMUSCNO = CMUSCNO;
    }

    public ArrayList<MeterExceptionModel> getMeterExceptionModels() {
        return meterExceptionModels;
    }

    public void setMeterExceptionModels(ArrayList<MeterExceptionModel> meterExceptionModels) {
        this.meterExceptionModels = meterExceptionModels;
    }
}
