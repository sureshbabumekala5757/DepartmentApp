package com.apcpdcl.departmentapp.models;

/**
 * Created by Admin on 19-12-2017.
 */

public class Eeps {

    int id;
    String uscno, socialcat, cname, ctr_load, distname, feeder, substation, reg_no,remarks;

    // Empty constructor
    public Eeps() {

    }

    // constructor
    public Eeps(int id, String uscno, String socialcat, String cname, String ctr_load, String distname, String feeder, String substation,String reg_no, String remarks) {
        this.id = id;
        this.uscno = uscno;
        this.socialcat = socialcat;
        this.cname = cname;
        this.ctr_load = ctr_load;
        this.distname = distname;
        this.feeder = feeder;
        this.substation = substation;
        this.reg_no=reg_no;
        this.remarks = remarks;
    }

    // constructor
    public Eeps(String uscno, String socialcat, String cname, String ctr_load, String distname, String feeder, String substation,String reg_no, String remarks) {
        this.uscno = uscno;
        this.socialcat = socialcat;
        this.cname = cname;
        this.ctr_load = ctr_load;
        this.distname = distname;
        this.feeder = feeder;
        this.substation = substation;
        this.reg_no=reg_no;
        this.remarks = remarks;
    }

    public int getID() {
        return this.id;
    }

    // setting id
    public void setID(int id) {
        this.id = id;
    }

    public String getUscno() {
        return this.uscno;
    }

    public void setUscno(String uscno) {
        this.uscno = uscno;
    }

    public String getsocialcat() {
        return this.socialcat;
    }

    public void setsocialcat(String socialcat) {
        this.socialcat = socialcat;
    }

    public String getcname() {
        return this.cname;
    }

    public void setcname(String servicename) {
        this.cname = cname;
    }

    public String getctr_load() {
        return this.ctr_load;
    }

    public void setctr_load(String ctr_load) {
        this.ctr_load = ctr_load;
    }

    public String getdistname() {
        return this.distname;
    }

    public void setdistname(String distname) {
        this.distname = distname;
    }


    public String getFeeder() {
        return this.feeder;
    }

    public void setFeeder(String feeder) {
        this.feeder = feeder;
    }

    public String getSubStation() {
        return this.substation;
    }

    public void setSubStation(String substation) {
        this.substation = substation;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;

    }
    public String getreg_no() {
        return this.reg_no;
    }

    public void setreg_no(String reg_no) {
        this.reg_no = reg_no;

    }


}
