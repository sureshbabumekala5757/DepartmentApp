package com.apcpdcl.departmentapp.models;

import java.io.Serializable;

public class MeterExceptionListModel implements Serializable {
    private String uscno;
    private String servicerequest;

    public String getUscno() {
        return uscno;
    }

    public void setUscno(String uscno) {
        this.uscno = uscno;
    }

    public String getServicerequest() {
        return servicerequest;
    }

    public void setServicerequest(String servicerequest) {
        this.servicerequest = servicerequest;
    }
}
