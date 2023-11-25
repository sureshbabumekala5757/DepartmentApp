package com.apcpdcl.departmentapp.models;

/**
 * Created by Admin on 19-12-2017.
 */

public class MatsConsumerModel {

    private String CASENO;
    private String POLENO;
    private float EXECSSLOAD;
    private String LONG;
    private String CMCAT;
    private float TOT;
    private String CMUSCNO;
    private String CMCNAME;
    private String LAT;
    private String CMADDRESS;
    private String LMCODE;
    private int id;
    private float strtotal, strLastPayAmnt;

    public String getCASENO() {
        return CASENO;
    }

    public void setCASENO(String CASENO) {
        this.CASENO = CASENO;
    }

    public String getPOLENO() {
        return POLENO;
    }

    public void setPOLENO(String POLENO) {
        this.POLENO = POLENO;
    }

    public float getEXECSSLOAD() {
        return EXECSSLOAD;
    }

    public void setEXECSSLOAD(float EXECSSLOAD) {
        this.EXECSSLOAD = EXECSSLOAD;
    }

    public String getLONG() {
        return LONG;
    }

    public void setLONG(String LONG) {
        this.LONG = LONG;
    }

    public String getCMCAT() {
        return CMCAT;
    }

    public void setCMCAT(String CMCAT) {
        this.CMCAT = CMCAT;
    }

    public float getTOT() {
        return TOT;
    }

    public void setTOT(float TOT) {
        this.TOT = TOT;
    }

    public String getCMUSCNO() {
        return CMUSCNO;
    }

    public void setCMUSCNO(String CMUSCNO) {
        this.CMUSCNO = CMUSCNO;
    }

    public String getCMCNAME() {
        return CMCNAME;
    }

    public void setCMCNAME(String CMCNAME) {
        this.CMCNAME = CMCNAME;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getCMADDRESS() {
        return CMADDRESS;
    }

    public void setCMADDRESS(String CMADDRESS) {
        this.CMADDRESS = CMADDRESS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getStrtotal() {
        return strtotal;
    }

    public void setStrtotal(float strtotal) {
        this.strtotal = strtotal;
    }

    public float getStrLastPayAmnt() {
        return strLastPayAmnt;
    }

    public void setStrLastPayAmnt(float strLastPayAmnt) {
        this.strLastPayAmnt = strLastPayAmnt;
    }

    public String getLMCODE() {
        return LMCODE;
    }

    public void setLMCODE(String LMCODE) {
        this.LMCODE = LMCODE;
    }
}
