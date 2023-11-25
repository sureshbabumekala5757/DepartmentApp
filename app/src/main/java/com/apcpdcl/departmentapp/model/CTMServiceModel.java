package com.apcpdcl.departmentapp.model;

public class CTMServiceModel {
    private String CUSCNO;

    public CTMServiceModel(String CUSCNO) {
        this.CUSCNO = CUSCNO;
    }

    public String getCUSCNO() {
        return CUSCNO;
    }

    public void setCUSCNO(String CUSCNO) {
        this.CUSCNO = CUSCNO;
    }
}
