package com.apcpdcl.departmentapp.models;

public class PollModel {

    private String TODATE;
    private String APPUSER;
    private String STATUS;
    private String QUESTION;
    private String FROMDATE;
    private String IMEI;
    private String QID;
    private String YES;
    private String NO;

    public String getTODATE() {
        return TODATE;
    }

    public void setTODATE(String TODATE) {
        this.TODATE = TODATE;
    }

    public String getAPPUSER() {
        return APPUSER;
    }

    public void setAPPUSER(String APPUSER) {
        this.APPUSER = APPUSER;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getQUESTION() {
        return QUESTION;
    }

    public void setQUESTION(String QUESTION) {
        this.QUESTION = QUESTION;
    }

    public String getFROMDATE() {
        return FROMDATE;
    }

    public void setFROMDATE(String FROMDATE) {
        this.FROMDATE = FROMDATE;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getQID() {
        return QID;
    }

    public void setQID(String QID) {
        this.QID = QID;
    }

    public String getYES() {
        return YES;
    }

    public void setYES(String YES) {
        this.YES = YES;
    }

    public String getNO() {
        return NO;
    }

    public void setNO(String NO) {
        this.NO = NO;
    }
}
