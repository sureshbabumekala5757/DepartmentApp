package com.apcpdcl.departmentapp.models;

import java.io.Serializable;

public class LmDcListModel implements Serializable {

    private String REJECTED;
    private String PENDING;
    private String ACCEPTED;
    private String USERCODE;
    private boolean NS;

    public String getREJECTED() {
        return REJECTED;
    }

    public void setREJECTED(String REJECTED) {
        this.REJECTED = REJECTED;
    }

    public String getPENDING() {
        return PENDING;
    }

    public void setPENDING(String PENDING) {
        this.PENDING = PENDING;
    }

    public String getACCEPTED() {
        return ACCEPTED;
    }

    public void setACCEPTED(String ACCEPTED) {
        this.ACCEPTED = ACCEPTED;
    }

    public String getUSERCODE() {
        return USERCODE;
    }

    public void setUSERCODE(String USERCODE) {
        this.USERCODE = USERCODE;
    }

    public boolean isNS() {
        return NS;
    }

    public void setNS(boolean NS) {
        this.NS = NS;
    }
}
