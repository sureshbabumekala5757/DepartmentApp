package com.apcpdcl.departmentapp.models;

public class ModuleModel {
    private String tabName;
    private int imageName;

    public ModuleModel(String tabName, int imageName) {
        this.tabName = tabName;
        this.imageName = imageName;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public int getImageName() {
        return imageName;
    }

    public void setImageName(int imageName) {
        this.imageName = imageName;
    }
}
