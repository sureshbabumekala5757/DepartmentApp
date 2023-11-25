package com.apcpdcl.departmentapp.models;

/**
 * Created by Admin on 19-12-2017.
 */

public class FeederSaidiSaifi {

    int id;
    String feederName,saifiVal, saidi;

    // Empty constructor
    public FeederSaidiSaifi() {

    }

    // constructor
    public FeederSaidiSaifi(int id, String feederName,String saifiVal, String saidi) {
        this.id = id;
        this.feederName=feederName;
        this.saidi = saidi;
        this.saifiVal = saifiVal;
    }

    // constructor
    public FeederSaidiSaifi(String feederName,String saifiVal, String saidi) {
        this.feederName = feederName;
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

    public String getFeederName() {
        return this.feederName;
    }

    public void setFeederName(String feederName) {
        this.feederName = feederName;
    }

    public String getFeederSaidi() {
        return this.saidi;
    }

    public void setFeederSaidi(String saidi) {
        this.saidi = saidi;
    }

    public String getFeederSaifi() {
        return this.saifiVal;
    }

    public void setFeederSaifi(String saifiVal) {
        this.saifiVal = saifiVal;
    }

}
