package com.apcpdcl.departmentapp.models;

import java.io.Serializable;

public class InterruptionModel implements Serializable {

    private String meterNo;
    private String transId;
    private String eventOccur;
    private String feederName;
    private String substationName;
    private int balanceReasons;
    private int updateReasons;
    private int cumilativeFrdInts;
    private String sectionId;
    private String sectionName;

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getEventOccur() {
        return eventOccur;
    }

    public void setEventOccur(String eventOccur) {
        this.eventOccur = eventOccur;
    }

    public String getFeederName() {
        return feederName;
    }

    public void setFeederName(String feederName) {
        this.feederName = feederName;
    }

    public String getSubstationName() {
        return substationName;
    }

    public void setSubstationName(String substationName) {
        this.substationName = substationName;
    }

    public int getBalanceReasons() {
        return balanceReasons;
    }

    public void setBalanceReasons(int balanceReasons) {
        this.balanceReasons = balanceReasons;
    }

    public int getUpdateReasons() {
        return updateReasons;
    }

    public void setUpdateReasons(int updateReasons) {
        this.updateReasons = updateReasons;
    }

    public int getCumilativeFrdInts() {
        return cumilativeFrdInts;
    }

    public void setCumilativeFrdInts(int cumilativeFrdInts) {
        this.cumilativeFrdInts = cumilativeFrdInts;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
