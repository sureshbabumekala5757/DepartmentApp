package com.apcpdcl.departmentapp.services;

public class ServiceConstants {

    /****************PRODUCTION URLs********************/
    //public static String URL = "http://103.231.215.237:8080/SBWS/rest/mobilebillingservices/";

    /****************TESTING URLs********************/
    //public static String URL = "http://103.231.215.237:8080/SBWS/rest/mobilebillingservices/";
    public static final String USER_AUTH = "Basic dGVzdF9jbTU6QXBjcGRjbDJAMTIz";
    /****************DEV URLs********************/
    public static String URL = "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/ISU/";
    public static String SAPURLURLCOM = "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/SAPISU/";
    public static String SAPURL = "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DEPTAPP/SAPISU/";
    public static String F4HELPURL = "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/F4HELP/DEV?process=newconnection";
    public static String SERVICE_CHANGE_F4HELPURL = "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/F4HELP/DEV?process=changeconnection";
    public static String DTRF4HELP = "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/Section/DEV";

    /****************Method names or varibles****************/
    public static String COMMUNICATION = "/DEV";
    public static final String USER_FORGOT_PWD_OTPREQUEST =  URL+"OTPRequest"+COMMUNICATION;
    public static final String USER_FORGOT_PWD =  URL+"PasswordChange"+COMMUNICATION;
    //Login
    public static final String USER_LOGIN =  URL+"Login"+COMMUNICATION;
    //AGL Pending Service
    public static final String USER_AGL_PENDING_LIST=  URL+"PendingRegistration"+COMMUNICATION;
    //NewConnection Release Save
    public static final String USER_NEWCONNECTION_RELEASE_SAVE =  URL+"NewConnectionRelease/Save"+COMMUNICATION;
    //DC List Disconnection
    public static final String USER_DC_DISCONNECTION =  SAPURL+"Disconnection"+COMMUNICATION;
    //DC List UNable to Disconnection
    public static final String USER_DC_UNABLETODISCONNECT =  SAPURL+"UnableToDisconnect"+COMMUNICATION;

    //Complaint List
    public static final String USER_DTR_COMPLAINTLIST =  SAPURLURLCOM+"ComplaintList"+COMMUNICATION;
    //Complaint Details
//    public static final String USER_DTR_COMPLAINT_STATUS =  URL+"ComplaintStatus"+COMMUNICATION;
    public static final String USER_DTR_COMPLAINT_STATUS =  "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/ConsumerApp/SAPISU/CustomerCareComplaintStatus/DEV";
    //DTR
    public static final String USER_DTR_CREATE_NOTIFICATION =  "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DEPTAPP/CREATE_NOTIFICATION/DEV";
    //DTR E
    public static final String USER_DTR_CHECK_EQUER =  "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DEPTAPP/CHECK_EQUNR/DEV";
    //Meter chnage request
    public static final String USER_METER_CHANGE_DATA_SAVE = "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/SAPISU/MeterReplacement/SubmitMeterChangeReq/DEV";

}
