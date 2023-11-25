package com.apcpdcl.departmentapp.models;

/**
 * Created by Haseen
 * on 24-03-2018.
 */

public class MeterChangeReqModel {
   // private String Regno;
    private String uscNo,serviceRequest;
    public MeterChangeReqModel(String uscNum, String serviceReq) {
        uscNo = uscNum;
        serviceRequest=serviceReq;
    }

    public String getServiceRequest() {
        return serviceRequest;
    }

    public void setServiceRequest(String serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    public String getUscNo() {
        return uscNo;
    }

    public void setUscNo(String uscNum) {
        uscNo = uscNum;
    }
}
