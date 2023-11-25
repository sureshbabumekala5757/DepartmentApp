package com.apcpdcl.departmentapp.models;

/**
 * Created by Admin on 19-12-2017.
 */

public class DTrComplaint {

    String ComplId, SecName, Trandt, PhoneNo,Status;

    // Empty constructor
    public DTrComplaint() {

    }

    public DTrComplaint(String complId, String secName, String trandt, String phoneNo, String status) {
        ComplId = complId;
        SecName = secName;
        Trandt = trandt;
        PhoneNo = phoneNo;
        Status = status;
    }

    public String getComplaintID() {
        return this.ComplId;
    }

    public void setComplaintID(String ComplId) {
        this.ComplId = ComplId;
    }

    public String getsecName() {
        return this.SecName;
    }

    public void setsecName(String SecName) {
        this.SecName = SecName;
    }

    public String getCreatedDate() {
        return this.Trandt;
    }

    public void setCreatedDate(String Trandt) {
        this.Trandt = Trandt;
    }

    public String getPhoneNo() {
        return this.PhoneNo;
    }

    public void setPhoneNo(String PhoneNo) {
        this.PhoneNo = PhoneNo;
    }

    public String getStatus() {
        return this.Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }



}
