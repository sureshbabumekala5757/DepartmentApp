package com.apcpdcl.departmentapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class MeterExceptionsFullModel implements Serializable{
    private ArrayList<String> keys;
    private ArrayList<MeterExceptionModel> meterExceptionModels;
    private ArrayList<MeterExceptionListModel> meterExceptionListModels;

    public ArrayList<String> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<String> keys) {
        this.keys = keys;
    }

    public ArrayList<MeterExceptionModel> getMeterExceptionModels() {
        return meterExceptionModels;
    }

    public void setMeterExceptionModels(ArrayList<MeterExceptionModel> meterExceptionModels) {
        this.meterExceptionModels = meterExceptionModels;
    }

    public ArrayList<MeterExceptionListModel> getMeterExceptionListModels() {
        return meterExceptionListModels;
    }

    public void setMeterExceptionListModels(ArrayList<MeterExceptionListModel> meterExceptionListModels) {
        this.meterExceptionListModels = meterExceptionListModels;
    }
}
