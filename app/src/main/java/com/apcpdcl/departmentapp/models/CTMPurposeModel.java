package com.apcpdcl.departmentapp.models;

public class CTMPurposeModel {
    private String CMPURPOSECD;
    private String CMPURPOSENAME;

//    public CTMPurpose(String CMPURPOSECD, String CMPURPOSENAME) {
//        this.CMPURPOSECD = CMPURPOSECD;
//        this.CMPURPOSENAME = CMPURPOSENAME;
//    }

    public String getCMPURPOSECD() {
        return CMPURPOSECD;
    }

    public void setCMPURPOSECD(String CMPURPOSECD) {
        this.CMPURPOSECD = CMPURPOSECD;
    }

    public String getCMPURPOSENAME() {
        return CMPURPOSENAME;
    }

    public void setCMPURPOSENAME(String CMPURPOSENAME) {
        this.CMPURPOSENAME = CMPURPOSENAME;
    }
}
