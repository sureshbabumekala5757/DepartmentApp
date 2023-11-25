package com.apcpdcl.departmentapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ExceptionalModel implements Serializable {
    private ArrayList<ExceptionReportModel> ERO;
    private ArrayList<ExceptionReportModel> DIVISION;


    public ArrayList<ExceptionReportModel> getERO() {
        return ERO;
    }

    public void setERO(ArrayList<ExceptionReportModel> ERO) {
        this.ERO = ERO;
    }

    public ArrayList<ExceptionReportModel> getDIVISION() {
        return DIVISION;
    }

    public void setDIVISION(ArrayList<ExceptionReportModel> DIVISION) {
        this.DIVISION = DIVISION;
    }
}
