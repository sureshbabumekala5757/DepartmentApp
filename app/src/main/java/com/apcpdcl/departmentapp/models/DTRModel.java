package com.apcpdcl.departmentapp.models;

public class DTRModel {
    private String DTRName;
    private String DTRCode;
    private String DTRLat;
    private String DTRLong;

    public DTRModel() {

    }

    public DTRModel(String DTRName, String DTRCode) {
        this.DTRName = DTRName;
        this.DTRCode = DTRCode;
    }

    public DTRModel(String DTRName, String DTRCode, String DTRLat, String DTRLong) {
        this.DTRName = DTRName;
        this.DTRCode = DTRCode;
        this.DTRLat = DTRLat;
        this.DTRLong = DTRLong;
    }

    public String getDTRName() {
        return DTRName;
    }

    public void setDTRName(String DTRName) {
        this.DTRName = DTRName;
    }

    public String getDTRCode() {
        return DTRCode;
    }

    public void setDTRCode(String DTRCode) {
        this.DTRCode = DTRCode;
    }

    public String getDTRLat() {
        return DTRLat;
    }

    public void setDTRLat(String DTRLat) {
        this.DTRLat = DTRLat;
    }

    public String getDTRLong() {
        return DTRLong;
    }

    public void setDTRLong(String DTRLong) {
        this.DTRLong = DTRLong;
    }
}
