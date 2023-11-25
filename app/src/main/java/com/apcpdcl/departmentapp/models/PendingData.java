package com.apcpdcl.departmentapp.models;

/**
 * Created by Admin on 23-02-2018.
 */

public class PendingData {
    int id;
    String strcmuscno, strTotAmnt,strStatus,strPendingAmnt,strType,strCaseNum;

    // Empty constructor
    public PendingData() {

    }

    // constructor
    public PendingData(int id, String strcmuscno, String strTotAmnt, String strStatus, String strPendingAmnt, String strType, String strCaseNum) {
        this.id = id;
        this.strcmuscno = strcmuscno;
        this.strTotAmnt = strTotAmnt;
        this.strStatus = strStatus;
        this.strPendingAmnt = strPendingAmnt;
        this.strType = strType;
        this.strCaseNum = strCaseNum;
    }

    // constructor
    public PendingData(String strcmuscno, String strTotAmnt, String strStatus, String strPendingAmnt, String strType, String strCaseNum) {
        this.strcmuscno = strcmuscno;
        this.strTotAmnt = strTotAmnt;
        this.strStatus = strStatus;
        this.strPendingAmnt = strPendingAmnt;
        this.strType = strType;
        this.strCaseNum = strCaseNum;
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

    public String getTotAmnt() {
        return this.strTotAmnt;
    }

    public void setTotAmnt(String strTotAmnt) {
        this.strTotAmnt = strTotAmnt;
    }

    public String getStatus() {
        return this.strStatus;
    }

    public void setStatus(String strStatus) {
        this.strStatus = strStatus;
    }


    public String getPendingAmnt() {
        return this.strPendingAmnt;
    }

    public void setPendingAmnt(String strPendingAmnt) {
        this.strPendingAmnt = strPendingAmnt;
    }


    public String getType() {
        return this.strType;
    }

    public void setType(String strType) {
        this.strType = strType;
    }

    public String getCaseNum() {
        return this.strCaseNum;
    }

    public void setCaseNum(String strCaseNum) {
        this.strCaseNum = strCaseNum;
    }



}
