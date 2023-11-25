package com.apcpdcl.departmentapp.models;

public class LCRequestDropDownModel {
    private String FeederName;
    private String FeederCode;
    private String SSCODE;
    private String SSNAME;
    private String SECCD;
    private String SECNAME;
    private String SSUSER;
    private String SSMOBILE;

    public String getFeederName() {
        return FeederName;
    }

    public void setFeederName(String feederName) {
        FeederName = feederName;
    }

    public String getFeederCode() {
        return FeederCode;
    }

    public void setFeederCode(String feederCode) {
        FeederCode = feederCode;
    }

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

    public String getSECCD() {
        return SECCD;
    }

    public void setSECCD(String SECCD) {
        this.SECCD = SECCD;
    }

    public String getSECNAME() {
        return SECNAME;
    }

    public void setSECNAME(String SECNAME) {
        this.SECNAME = SECNAME;
    }

    public String getSSUSER() {
        return SSUSER;
    }

    public void setSSUSER(String SSUSER) {
        this.SSUSER = SSUSER;
    }

    public String getSSMOBILE() {
        return SSMOBILE;
    }

    public void setSSMOBILE(String SSMOBILE) {
        this.SSMOBILE = SSMOBILE;
    }
}
