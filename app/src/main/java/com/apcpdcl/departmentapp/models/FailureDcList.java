package com.apcpdcl.departmentapp.models;

/**
 * Created by Admin on 05-02-2018.
 */

public class FailureDcList {
    int id;
    String strTotalString;

    // Empty constructor
    public FailureDcList(){

    }

    // constructor
    public FailureDcList(int id,String strTotalString){
        this.id=id;
        this.strTotalString = strTotalString;

    }

    // constructor
    public FailureDcList(String strTotalString){
        this.strTotalString = strTotalString;

    }

    public int getID(){
        return this.id;
    }

    // setting id
    public void setID(int id){
        this.id = id;
    }

    public String getTotalString(){
        return this.strTotalString;
    }

    public void setTotalString(String strTotalString){
        this.strTotalString = strTotalString;
    }

}
