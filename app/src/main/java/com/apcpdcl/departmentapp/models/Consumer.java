package com.apcpdcl.departmentapp.models;

/**
 * Created by Admin on 19-12-2017.
 */

public class Consumer {

    int id;
    float strtotal,strLastPayAmnt;
    String strcmcname, strcmuscno, strlastpaydt, strcmcat,strBldiscdt,
            strStype, strMeterNum, strSocialCat, strGovtCat,strCmdtrCode,
            strGrp,strAddr,strLoc,strPhone,strLat,strLong,strLmcode,strPoleNo;

    // Empty constructor
    public Consumer() {

    }


    // constructor
    //strBldiscdt,strStype,strCmcNames,strMeterNum,strConsumerNums,strTotals,strLastPayDates,strSocialCat,strCmCats,strGovtCat
    public Consumer(String strBldiscdt, String strStype, String strcmcname,
                    String strMeterNum, String strcmuscno, float strtotal,
                    String strlastpaydt, String strSocialCat,
                    String strcmcat, String strGovtCat, float strLastPayAmnt,
                    String strCmdtrCode, String strGrp, String strAddr, String strLoc,
                    String strPhone,String strLat, String strLong, String strPoleNo,String strLmcode ) {
        this.strBldiscdt = strBldiscdt;
        this.strStype = strStype;
        this.strcmcname = strcmcname;
        this.strMeterNum = strMeterNum;
        this.strcmuscno = strcmuscno;
        this.strtotal = strtotal;
        this.strlastpaydt = strlastpaydt;
        this.strSocialCat = strSocialCat;
        this.strcmcat = strcmcat;
        this.strGovtCat = strGovtCat;
        this.strLastPayAmnt = strLastPayAmnt;
        this.strCmdtrCode=strCmdtrCode;
        this.strGrp=strGrp;
        this.strAddr = strAddr;
        this.strLoc=strLoc;
        this.strPhone=strPhone;
        this.strLat=strLat;
        this.strLong=strLong;
        this.strPoleNo=strPoleNo;
        this.strLmcode=strLmcode;


    }

    public int getID() {
        return this.id;
    }

    // setting id
    public void setID(int id) {
        this.id = id;
    }

    public String getBldiscdt() {
        return this.strBldiscdt;
    }

    public void setBldiscdt(String strBldiscdt) {
        this.strBldiscdt = strBldiscdt;
    }


    public String getStype() {
        return this.strStype;
    }

    public void setStype(String strStype) {
        this.strStype = strStype;
    }


    public String getCmcName() {
        return this.strcmcname;
    }

    public void setCmcName(String strcmcname) {
        this.strcmcname = strcmcname;
    }


    public String getMeterNum() {
        return this.strMeterNum;
    }

    public void setMeterNum(String strMeterNum) {
        this.strMeterNum = strMeterNum;
    }


    public String getCmuscno() {
        return this.strcmuscno;
    }

    public void setCmuscno(String strcmuscno) {
        this.strcmuscno = strcmuscno;
    }

    public float getTotal() {
        return this.strtotal;
    }

    public void setTotal(float strtotal) {
        this.strtotal = strtotal;
    }


    public String getLastpaydt() {
        return this.strlastpaydt;
    }

    public void setLastpaydt(String strlastpaydt) {
        this.strlastpaydt = strlastpaydt;
    }


    public String getSocialCat() {
        return this.strSocialCat;
    }

    public void setSocialCat(String strSocialCat) {
        this.strSocialCat = strSocialCat;
    }


    public String getCmcat() {
        return this.strcmcat;
    }

    public void setCmcat(String strcmcat) {
        this.strcmcat = strcmcat;
    }

    public String getGovtCat() {
        return this.strGovtCat;
    }

    public void setGovtCat(String strGovtCat) {
        this.strGovtCat = strGovtCat;
    }

    public float getLastPayAmnt() {
        return this.strLastPayAmnt;
    }

    public void setLastPayAmnt(float strLastPayAmnt) {
        this.strLastPayAmnt = strLastPayAmnt;
    }


    public String getCmdtrCode() {
        return this.strCmdtrCode;
    }

    public void setCmdtrCode(String strCmdtrCode) {
        this.strCmdtrCode = strCmdtrCode;
    }

    public String getGrp() {
        return this.strGrp;
    }

    public void setGrp(String strGrp) {
        this.strGrp = strGrp;
    }

    public String getAddr() {
        return this.strAddr;
    }

    public void setAddr(String strAddr) {
        this.strAddr = strAddr;
    }

    public String getLoc() {
        return this.strLoc;
    }

    public void setLoc(String strLoc) {
        this.strLoc = strLoc;
    }

    public String getPhone() {
        return this.strPhone;
    }

    public void setPhone(String strPhone) {
        this.strPhone = strPhone;
    }

    public String getLmcode() {
        return this.strLmcode;
    }

    public void setLmcode(String strLmcode) {
        this.strLmcode = strLmcode;
    }

    public String getLat() {
        return strLat;
    }

    public void setLat(String strLat) {
        this.strLat = strLat;
    }

    public String getLong() {
        return strLong;
    }

    public void setLong(String strLong) {
        this.strLong = strLong;
    }

    public String getPoleNo() {
        return strPoleNo;
    }

    public void setPoleNo(String strPoleNo) {
        this.strPoleNo = strPoleNo;
    }

}
