package com.apcpdcl.departmentapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ExceptionReportModel implements Serializable {

    private String Balance;
    private String Meter_Replaced;
    private String LS_Status;
    private String GS_Status;
    private String Status;
    private String Section;
    private String DIV_Name;
    private String ERO_Name;
    private ArrayList<ExceptionReportModel> SECTION;
    public String getBalance() {
        return Balance;
    }

    public void setBalance(String Balance) {
        this.Balance = Balance;
    }

    public String getMeter_Replaced() {
        return Meter_Replaced;
    }

    public void setMeter_Replaced(String Meter_Replaced) {
        this.Meter_Replaced = Meter_Replaced;
    }

    public String getLS_Status() {
        return LS_Status;
    }

    public void setLS_Status(String LS_Status) {
        this.LS_Status = LS_Status;
    }

    public String getGS_Status() {
        return GS_Status;
    }

    public void setGS_Status(String GS_Status) {
        this.GS_Status = GS_Status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

    public String getDIV_Name() {
        return DIV_Name;
    }

    public void setDIV_Name(String DIV_Name) {
        this.DIV_Name = DIV_Name;
    }

    public String getERO_Name() {
        return ERO_Name;
    }

    public void setERO_Name(String ERO_Name) {
        this.ERO_Name = ERO_Name;
    }


    public ArrayList<ExceptionReportModel> getSECTION() {
        return SECTION;
    }

    public void setSECTION(ArrayList<ExceptionReportModel> SECTION) {
        this.SECTION = SECTION;
    }
}
