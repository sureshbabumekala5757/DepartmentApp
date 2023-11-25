package com.apcpdcl.departmentapp.models;

/**
 * Created by Admin on 17-02-2018.
 */

public class ReportsList {
    int id;
    String strcmuscno,strStatus,strDate;

    public ReportsList() {

    }

    public ReportsList(int id, String strcmuscno, String strStatus, String strDate) {
        this.id = id;
        this.strcmuscno = strcmuscno;
        this.strStatus = strStatus;
        this.strDate = strDate;

    }

    public ReportsList(String strcmuscno, String strStatus, String strDate) {
        this.strcmuscno = strcmuscno;
        this.strStatus = strStatus;
        this.strDate = strDate;

    }
    public int getID() {
        return this.id;
    }

    // setting id
    public void setID(int id) {
        this.id = id;
    }

    public String getCmuscno() {
        return this.strcmuscno;
    }

    public void setCmuscno(String strcmuscno) {
        this.strcmuscno = strcmuscno;
    }

    public String getStatus() {
        return this.strStatus;
    }

    public void setStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public String getDate() {
        return this.strDate;
    }

    public void setDate(String strDate) {
        this.strDate = strDate;
    }
}
