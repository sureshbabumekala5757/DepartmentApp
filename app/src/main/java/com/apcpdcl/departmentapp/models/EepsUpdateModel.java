package com.apcpdcl.departmentapp.models;

import java.io.Serializable;

public class EepsUpdateModel implements Serializable {

    private String REGNO;
    private String consumerName;
    private String load;
    private String status;
    private String str11kvfeeder;
    private String village;
    private String section;
    private String latitude;
    private String longitude;
    private String image;


    public String getREGNO() {
        return REGNO;
    }

    public void setREGNO(String REGNO) {
        this.REGNO = REGNO;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKvFeeder() {
        return str11kvfeeder;
    }

    public void setFeeder(String str11kvfeeder) {
        this.str11kvfeeder = str11kvfeeder;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}

