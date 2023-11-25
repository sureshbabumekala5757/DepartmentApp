package com.apcpdcl.departmentapp.models;

/**
 * Created by Admin on 19-12-2017.
 */

public class SecSaidiSaifi {

    int id;
    String sectionName, totalFeeders, saifiVal, saidi;

    // Empty constructor
    public SecSaidiSaifi() {

    }

    // constructor
    public SecSaidiSaifi(int id, String sectionName, String totalFeeders, String saifiVal, String saidi) {
        this.id = id;
        this.sectionName = sectionName;
        this.totalFeeders = totalFeeders;
        this.saidi = saidi;
        this.saifiVal = saifiVal;
    }

    // constructor
    public SecSaidiSaifi(String sectionName, String totalFeeders, String saifiVal, String saidi) {
        this.sectionName = sectionName;
        this.totalFeeders = totalFeeders;
        this.saidi = saidi;
        this.saifiVal = saifiVal;


    }

    public int getID() {
        return this.id;
    }

    // setting id
    public void setID(int id) {
        this.id = id;
    }

    public String getSecName() {
        return this.sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getTotalFeeders() {
        return this.totalFeeders;
    }

    public void setTotalFeeders(String totalFeeders) {this.totalFeeders = totalFeeders;}

    public String getSectionSaidi() {
        return this.saidi;
    }

    public void setSectionSaidi(String saidi) {
        this.saidi = saidi;
    }

    public String getSectionSaifi() {
        return this.saifiVal;
    }

    public void setSectionSaifi(String saifiVal) {
        this.saifiVal = saifiVal;
    }


}
