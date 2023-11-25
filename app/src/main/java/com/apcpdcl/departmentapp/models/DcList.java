package com.apcpdcl.departmentapp.models;

/**
 * Created by Admin on 27-12-2017.
 */

public class DcList{
    int id;
    String strscno,strkwh,strkvah,strremarks,strstatus;

        // Empty constructor
        public DcList(){

        }

        // constructor
        public DcList(int id, String strscno, String strkwh, String strkvah, String strremarks, String strstatus){
            this.id=id;
            this.strscno = strscno;
            this.strkwh = strkwh;
            this.strkvah = strkvah;
            this.strremarks = strremarks;
            this.strstatus = strstatus;
        }

        // constructor
        public DcList(String strscno, String strkwh, String strkvah, String strremarks, String strstatus){
            this.strscno = strscno;
            this.strkwh = strkwh;
            this.strkvah = strkvah;
            this.strremarks = strremarks;
            this.strstatus = strstatus;
        }

        public int getID(){
            return this.id;
        }

        // setting id
        public void setID(int id){
            this.id = id;
        }

        public String getScno(){
            return this.strscno;
        }

        public void setScno(String strscno){
            this.strscno = strscno;
        }


        public String getKwh(){
            return this.strkwh;
        }

        public void setKwh(String strkwh){
            this.strkwh = strkwh;
        }

        public String getKvah(){
            return this.strkvah;
        }

        public void setKvah(String strkvah){
            this.strkvah = strkvah;
        }

        public String getRemarks(){
            return this.strremarks;
        }

        public void setRemarks(String strremarks){
            this.strremarks = strremarks;
        }

        public String getStatus(){
            return this.strstatus;
        }

        public void setStatus(String strstatus){
            this.strstatus = strstatus;
        }

}