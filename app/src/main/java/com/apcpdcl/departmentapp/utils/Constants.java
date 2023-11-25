package com.apcpdcl.departmentapp.utils;

/**
 * Created by haseena
 * on 6/12/17.
 */

public class Constants {
    // Log message On or Off
    static final boolean logMessageOnOrOff = true;
    public static final String FROM = "From";
    public static final String FEEDER_READING = "FEEDER_READING";
    public static final String AE_DASHBOARD = "AE";
    public static final String METER_CHANGE_MODEL = "METER_CHANGE_MODEL";
    public static final String SERVICE_DETAILS = "SERVICE_DETAILS";
    public static final String REG_LIST = "getSrcDetails/regno";
    public static final String GEO_REG_LIST = "getSrcDetails/georegno";
    public static final String REG_DETAILS = "getSrcDetails/regdetails";
    public static final String GEO_REG_DETAILS = "getSrcDetails/georegdetails";
    public static final String LATLONGINPUT = "getSrcDetails/longlatInput";
    public static final String GEO_LATLONGINPUT = "getSrcDetails/geolonglatInput";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String MAP_API_KEY = "AIzaSyBEctWnq77IvYbcRpiKsvmK4hKJHVx3M2A";
    public static boolean isDialogOpen = false;
    public static final String APP_PREF = "DC_LIST_Pref";
    public static final String IMEI_NUMBER = "IMEI_NUMBER";
    public static final String FCM_TOKEN = "FCM_TOKEN";


    public static String UPLOAD_POLE_DATA = "upload_service";
    public static final String BROADCAST_ACTION = "com.apcpdcl.departmentapp.utils.ACTION";
    public final static String START_FOREGROUND_ACTION = "android.net.conn.START";
    public final static String STOP_FOREGROUND_ACTION = "android.net.conn.STOP";
    public static final String URL = "http://112.133.252.110:8080/ApcpdclDepartmentApp/mobileAction/";
    //public static final String URL = "http://10.200.201.74:8080/ApcpdclDepartmentApp/mobileAction/"; //CTRLS
    public static final String DOWNLOAD_PATH = "http://112.133.252.110:8080/ApcpdclDepartmentApp/apk/apcpdcl_departmentapp_";

    public static final String GET_COMPLAINT_DETAILS = "http://112.133.252.110:8080/ApcpdclDepartmentApp/mobileAction/CCC/details/";
    //public static final String OMS_URL = "http://59.145.127.110:8088/mobileapp_oms/rest/mobileomsservices/";
    public static final String OMS_URL = "http://112.133.252.105:8999/mobileapp_oms/rest/mobileomsservices/";

    public static final String URL_IMAGES = "http://112.133.252.110:8080/DepartmentApp/images/";
    public static final String URL_GL = "http://112.133.252.110:8080/ApcpdclDepartmentApp/mobileAction/";
    public static final String METER_INSPECTION_URL = "http://112.133.252.110:8080/MeterInspt/rest/main/";

   /* public static final String SAVE = METER_INSPECTION_URL + "saveJsonData";*/
    public static final String CTMETERS_URL = "http://112.133.252.110:8080/CTMeterService/resources/listener/";
    public static final String GET_METERSEAL_DETAILS = URL + "MeterSeal/details";
    public static final String SAVE_METERSEAL_DATA = URL + "MeterSeal/save";
    public static final String GET_DETAILS = METER_INSPECTION_URL + "getServiceNoData";
    public static final String GET_BILL_DETAILS = URL + "bill/billdetails";
    public static final String GET_ABSTRACT_COUNT = URL + "abstract_count/count";
    public static final String GET_CUURENT_MONTH_EXCEPTIONS = URL + "abstract_count/CM_mtrchg";
    public static final String AGEWISE_ASL = URL + "abstract_count/agewise_ASL";
    public static final String GET_ADV_ABSTRACT_COUNT = URL + "abstract_count/advancecount";
    public static final String RC_LIST = URL + "abstract_count/rcList";
    public static final String URL_DC = URL + "dc/";
    public static final String URL_ERO = URL + "EROservices/";
    public static final String LOGIN = URL + "login/validate";
    public static final String GET_METER_MAKE_LIST = URL + "mtr/metermake/";
    public static final String SEND_NOTIFICATION = URL + "notification/register";
    public static final String PWD_CHANGE = URL + "login/pwdchange";
    public static final String APK_CHK = URL_DC + "apkchk";
    public static final String PAY_DETAILS = URL_DC + "payDetails";
    public static final String MATS_PAY_DETAILS = URL + "mats/payDetails";
    public static final String UPDATE = URL_DC + "update";
    public static final String MATS_UPDATE = URL + "mats/update";
    public static final String MATS = URL_DC + "mats";
    public static final String LINEMAN = URL_DC + "lineman";
   /* public static final String AADHAAR_URL = URL + "aadhaarId";*/
    public static final String METER_CHANGE_URL = URL + "mtr/";

    public static final String GET_INTERRUPTIONS_LIST = OMS_URL + "fdrInterruptionsEntryReport";
    public static final String INTERRUPTIONS_FORM = OMS_URL + "fdrInterruptionsEntryForm";
    public static final String SCHEDULED_POWER_OUTAGE_INSERT = OMS_URL + "scheduledPowerOutageInsert";
    public static final String SCHEDULED_POWER_OUTAGE_LIST_REPORT = OMS_URL + "scheduledPowerOutageListReport";
    public static final String NATURE_OF_LOAD = OMS_URL + "natureofFeeder";
    public static final String GET_LIVE_INTERRUPTIONS__FDR_REPORT = OMS_URL + "liveFdrInterruptionReport";
    public static final String GET_LIVE_INTERRUPTIONS_ABSTRACT = OMS_URL + "sectionWiseLiveIntrAbstractReport";
    public static final String GET_LIVE_INTERRUPTIONS_REPORT = OMS_URL + "sectionWiseLiveIntrViewReport";
    public static final String OUT_AGE_REASONS = OMS_URL + "outagereasons";
    public static final String SECTION_WISE_LIVEINTR_UPDATEREPORT = OMS_URL + "sectionWiseLiveIntrUpdateReport";
    public static final String LC_LM_REQUEST_FORM_INSERT = OMS_URL + "lcLMRequestFormInsert";
    public static final String LC_AE_APPROVAL_REPORT = OMS_URL + "lcAEApprovalReport";
    public static final String lcAEApprovalRejectUpdate = OMS_URL + "lcAEApprovalRejectUpdate";
    public static final String LC_SSO_ISSUE_REPORT = OMS_URL + "lcSSOIssueReport";
    public static final String LC_TRACKING_STATUS = OMS_URL + "lcTrackingStatus";
    public static final String LC_LMTOSSO_RETURNREPORT = OMS_URL + "lcLMtoSSOReturnReport";
    public static final String LC_SSO_ISSUE_UPDATE = OMS_URL + "lcSSOIssueUpdate";
    public static final String LCLMTOSSORETURNUPDATE = OMS_URL + "lcLMtoSSOReturnUpdate";
    public static final String LC_LM_RETURN_REPORT = OMS_URL + "lcLMReturnReport";
    public static final String LC_LM_RETURN_UPDATE = OMS_URL + "lcLMReturnUpdate";
    public static final String GET_COMPLAINT_STATUS_DETAILS = URL + "CCC/status/";
    public static final String COMPLAINT_UPDATION = URL + "CCC/update/";
    public static final String SUB_STATION_DETAILS = URL + "substation/details";
    public static final String DIV_SECTIONS = URL + "substation/div_sections";
    public static final String FEEDER_DETAILS = URL + "substation/feederList";
    public static final String PROFILE = "dc/profile";
    public static final String UPDATE_PROFILE = "dc/profileUpdate";
    public static final String FORGOT_PWD = "forgotpwd";
    public static final String AE_COUNT_SLAB = URL + "ae/lmcountsl";
    public static final String AE_COUNT_NON_SLAB = URL + "ae/lmcountns";
    public static final String AAO_COUNT_SLAB = URL + "aao/aecountsl";
    public static final String AAO_COUNT_NON_SLAB = URL + "aao/adecountns";
    public static final String ADE_COUNT_NON_SLAB = URL + "ade/aecountns";
   /* public static final String FCM_REGISTER = URL + "fcm/register";*/
    //public static final String FCM_DELETE = URL + "fcm/delete";
   /* public static final String FCM_UPDATE = URL + "fcm/update";*/
    public static final String SUBSTATION_LIST = "substation/list";
    public static final String FIELD_METER_LIST = "fieldMtrDetais";
    public static final String DELETE_MTR_INITIATION_DETAIS = "deleteMtrInitiationDetais";
    public static final String REJECTED_MTR_DETAILS = "rejectedMtrDetails";
    public static final String UPDATE_FIELD_MTR_DETAILS = "approveFieldMtrDetails";
    public static final String EMP_SUBSTATION_LIST = "substation/emplist";
    public static final String SUBSTATION_COORDINATES = "substation/savecoord";
    public static final String SAVEEMP = "substation/saveemp";
    public static final String FETCH_DETAILS = "mtr/fetchdetais";
    public static final String MODIFY_MTR_DETAILS = "modifyMtrdetais";
    public static final String DELETE_MTR_DETAILS = "deleteMtrDetails";
    public static final String UPDATE_MTR_DETAILS = "updateMtrdetais";
    public static final String POST_DETAILS = "mtr/postdetais";
    public static final String CHECK_READING = "checkReading/entry";
    public static final String CHECK_READING_DETAILS = "checkReading/details";
    public static final String AADHAAR_FETCH = "aadhaar/fetch";
    public static final String AADHAAR_UPDATE = "aadhaar/update";
    public static final String MOBILE_UPDATE = "mobile/update";
    public static final String TONG_DTRLIST = "tong/dtrlist";
    public static final String TONG_CAPLIST = "tong/dtrCapList";
    public static final String TONG_SAVE = "tong/save";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String DATE_PICKER = "Date Picker";
    public static final String COMPLAINT_DATA = "COMPLAINT_DATA";
    public static final String RE_CONEECTION_DATA = "RE_CONEECTION_DATA";
    public static final String USCNO = "USCNO";
    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String NEWMTRNO = "NEWMTRNO";
    public static final String MTRCHGDT = "MTRCHGDT";
    public static final String SERVICE_TYPE = "SERVICE_TYPE";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 3;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION_SINGLE = 13;
    public static final int MY_PERMISSIONS_REQUEST_IMEI = 33;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
}
