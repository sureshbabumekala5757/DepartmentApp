package com.apcpdcl.departmentapp.models;

import java.io.Serializable;

/**
 * Created by Haseen
 * on 25-04-2018.
 */
public class ComplaintModel implements Serializable {

    private String t_CODE_RESPONSE;
    private String t_CODE;
    private String additional_Information;
    private String complaint_Sub_Type;
    private String complaint_Type;
    private String consumer_Phone_No;
    private String category;
    private String feeder_Name;
    private String dtr_Name;
    private String pole_No;
    private String area_Name;
    private String address;
    private String consumer_Name;
    private String service_No;
    private String complaint_Id;
    private String created_Date_Time;
    private String to_be_completion_Date_Time;
    private String resolution_Flag;

    public ComplaintModel(String t_CODE_RESPONSE, String t_CODE, String additional_Information, String complaint_Sub_Type, String complaint_Type, String consumer_Phone_No, String category, String feeder_Name, String dtr_Name, String pole_No, String area_Name, String address, String consumer_Name, String service_No, String complaint_Id, String created_Date_Time, String to_be_completion_Date_Time, String resolution_Flag) {
        this.t_CODE_RESPONSE = t_CODE_RESPONSE;
        this.t_CODE = t_CODE;
        this.additional_Information = additional_Information;
        this.complaint_Sub_Type = complaint_Sub_Type;
        this.complaint_Type = complaint_Type;
        this.consumer_Phone_No = consumer_Phone_No;
        this.category = category;
        this.feeder_Name = feeder_Name;
        this.dtr_Name = dtr_Name;
        this.pole_No = pole_No;
        this.area_Name = area_Name;
        this.address = address;
        this.consumer_Name = consumer_Name;
        this.service_No = service_No;
        this.complaint_Id = complaint_Id;
        this.created_Date_Time = created_Date_Time;
        this.to_be_completion_Date_Time = to_be_completion_Date_Time;
        this.resolution_Flag = resolution_Flag;
    }

    public String getT_CODE_RESPONSE() {
        return t_CODE_RESPONSE;
    }

    public void setT_CODE_RESPONSE(String t_CODE_RESPONSE) {
        this.t_CODE_RESPONSE = t_CODE_RESPONSE;
    }

    public String getT_CODE() {
        return t_CODE;
    }

    public void setT_CODE(String t_CODE) {
        this.t_CODE = t_CODE;
    }

    public String getAdditional_Information() {
        return additional_Information;
    }

    public void setAdditional_Information(String additional_Information) {
        this.additional_Information = additional_Information;
    }

    public String getComplaint_Sub_Type() {
        return complaint_Sub_Type;
    }

    public void setComplaint_Sub_Type(String complaint_Sub_Type) {
        this.complaint_Sub_Type = complaint_Sub_Type;
    }

    public String getComplaint_Type() {
        return complaint_Type;
    }

    public void setComplaint_Type(String complaint_Type) {
        this.complaint_Type = complaint_Type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFeeder_Name() {
        return feeder_Name;
    }

    public void setFeeder_Name(String feeder_Name) {
        this.feeder_Name = feeder_Name;
    }

    public String getDtr_Name() {
        return dtr_Name;
    }

    public void setDtr_Name(String dtr_Name) {
        this.dtr_Name = dtr_Name;
    }

    public String getPole_No() {
        return pole_No;
    }

    public void setPole_No(String pole_No) {
        this.pole_No = pole_No;
    }

    public String getArea_Name() {
        return area_Name;
    }

    public void setArea_Name(String area_Name) {
        this.area_Name = area_Name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConsumer_Name() {
        return consumer_Name;
    }

    public void setConsumer_Name(String consumer_Name) {
        this.consumer_Name = consumer_Name;
    }

    public String getService_No() {
        return service_No;
    }

    public void setService_No(String service_No) {
        this.service_No = service_No;
    }

    public String getComplaint_Id() {
        return complaint_Id;
    }

    public void setComplaint_Id(String complaint_Id) {
        this.complaint_Id = complaint_Id;
    }

    public String getConsumer_Phone_No() {
        return consumer_Phone_No;
    }

    public void setConsumer_Phone_No(String consumer_Phone_No) {
        this.consumer_Phone_No = consumer_Phone_No;
    }

    public String getCreated_Date_Time() {
        return created_Date_Time;
    }

    public void setCreated_Date_Time(String created_Date_Time) {
        this.created_Date_Time = created_Date_Time;
    }

    public String getTo_be_completion_Date_Time() {
        return to_be_completion_Date_Time;
    }

    public void setTo_be_completion_Date_Time(String to_be_completion_Date_Time) {
        this.to_be_completion_Date_Time = to_be_completion_Date_Time;
    }

    public String getResolution_Flag() {
        return resolution_Flag;
    }

    public void setResolution_Flag(String resolution_Flag) {
        this.resolution_Flag = resolution_Flag;
    }
}
