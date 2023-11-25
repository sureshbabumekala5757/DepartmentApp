package com.apcpdcl.departmentapp.models;

public  class LoadForecastModel {


    private String enteredIp;
    private String entryDate;
    private int prevConsumption;
    private int consumption;
    private String loadForeCastDate;
    private String natureofLoad;
    private String requestedDesg;
    private String requestedName;
    private String requestedId;
    private String feederId;
    private String feederName;
    private String substationName;
    private String sectionName;
    private String sectionId;

    public String getEnteredIp() {
        return enteredIp;
    }

    public void setEnteredIp(String enteredIp) {
        this.enteredIp = enteredIp;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public int getPrevConsumption() {
        return prevConsumption;
    }

    public void setPrevConsumption(int prevConsumption) {
        this.prevConsumption = prevConsumption;
    }

    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }

    public String getLoadForeCastDate() {
        return loadForeCastDate;
    }

    public void setLoadForeCastDate(String loadForeCastDate) {
        this.loadForeCastDate = loadForeCastDate;
    }

    public String getNatureofLoad() {
        return natureofLoad;
    }

    public void setNatureofLoad(String natureofLoad) {
        this.natureofLoad = natureofLoad;
    }

    public String getRequestedDesg() {
        return requestedDesg;
    }

    public void setRequestedDesg(String requestedDesg) {
        this.requestedDesg = requestedDesg;
    }

    public String getRequestedName() {
        return requestedName;
    }

    public void setRequestedName(String requestedName) {
        this.requestedName = requestedName;
    }

    public String getRequestedId() {
        return requestedId;
    }

    public void setRequestedId(String requestedId) {
        this.requestedId = requestedId;
    }

    public String getFeederId() {
        return feederId;
    }

    public void setFeederId(String feederId) {
        this.feederId = feederId;
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

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
