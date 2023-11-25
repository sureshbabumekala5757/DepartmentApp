package com.apcpdcl.departmentapp.models;

public class PoleModel {
    private String PoleName;
    private String PoleCode;
    private String poleLat;
    private String poleLong;

    public PoleModel(String poleName, String poleCode, String poleLat, String poleLong) {
        PoleName = poleName;
        PoleCode = poleCode;
        this.poleLat = poleLat;
        this.poleLong = poleLong;
    }



    public PoleModel(String poleName, String poleCode) {
        PoleName = poleName;
        PoleCode = poleCode;
    }
    public String getPoleLat() {
        return poleLat;
    }

    public void setPoleLat(String poleLat) {
        this.poleLat = poleLat;
    }

    public String getPoleLong() {
        return poleLong;
    }

    public void setPoleLong(String poleLong) {
        this.poleLong = poleLong;
    }

    public String getPoleName() {
        return PoleName;
    }

    public void setPoleName(String poleName) {
        PoleName = poleName;
    }

    public String getPoleCode() {
        return PoleCode;
    }

    public void setPoleCode(String poleCode) {
        PoleCode = poleCode;
    }
}
