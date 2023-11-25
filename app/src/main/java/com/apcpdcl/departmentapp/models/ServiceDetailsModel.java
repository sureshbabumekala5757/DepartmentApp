package com.apcpdcl.departmentapp.models;

import java.io.Serializable;

/**
 * Created by Haseen
 * on 24-03-2018.
 */

public class ServiceDetailsModel implements Serializable {

    private String category;
    private String load;
    private String address;
    private String consumerName;
    private String reg_date;
    private String REGNO;
    private String longitude;
    private String latitude;
    private String seal;
    private String image;
    private String signature;

    public ServiceDetailsModel(String category, String load, String address, String consumerName, String reg_date, String REGNO, String longitude, String latitude, String seal, String image, String signature) {
        this.category = category;
        this.load = load;
        this.address = address;
        this.consumerName = consumerName;
        this.reg_date = reg_date;
        this.REGNO = REGNO;
        this.longitude = longitude;
        this.latitude = latitude;
        this.seal = seal;
        this.image = image;
        this.signature = signature;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getREGNO() {
        return REGNO;
    }

    public void setREGNO(String REGNO) {
        this.REGNO = REGNO;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getSeal() {
        return seal;
    }

    public void setSeal(String seal) {
        this.seal = seal;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
