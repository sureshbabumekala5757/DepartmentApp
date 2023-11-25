package com.apcpdcl.departmentapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class AchieversModel implements Serializable {
    private String IMAGES;
    private String CONTENT;
    private String CATEGORY;
    private ArrayList<AchieversModel> achieversModels;


    public String getIMAGES() {
        return IMAGES;
    }

    public void setIMAGES(String IMAGES) {
        this.IMAGES = IMAGES;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }

    public ArrayList<AchieversModel> getAchieversModels() {
        return achieversModels;
    }

    public void setAchieversModels(ArrayList<AchieversModel> achieversModels) {
        this.achieversModels = achieversModels;
    }
}
