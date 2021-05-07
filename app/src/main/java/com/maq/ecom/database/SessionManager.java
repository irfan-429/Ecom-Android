package com.maq.ecom.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.maq.ecom.R;


/**
 * Created by irfan A. on 17/07/2020.
 */

public class SessionManager {
    final String LOGIN_SATE = "LOGIN_SATE";
    final String USER_NAME = "USER_NAME";
    final String USER_MOBILE = "USER_MOBILE";
    final String USER_PWD = "USER_PWD";
    final String USER_CITY = "USER_CITY";
    final String PROFILE_IMG = "PROFILE_IMG";
    final String OPENING_BALANCE = "OPENING_BALANCE";
    final String OPENING_CRT = "OPENING_CRT";
    final String BALANCE = "BALANCE";
    final String CRATE = "CRATE";
    final String DEMAND = "DEMAND";
    final String PAYMENT = "PAYMENT";
    final String FIRM_ID = "FIRM_ID";
    final String ADMIN = "ADMIN";
    final String OUTSTANDING = "OUTSTANDING";
    final String DUES = "DUES";
    final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    final String BANK_TABLE = "BANK_TABLE";
    final String ORDER_HIST_TABLE = "ORDER_HIST_TABLE";
    final String ORDER_HIST_ADMIN_TABLE = "ORDER_HIST_ADMIN_TABLE";
    final String ROUTE = "ROUTE";
    final String STATEMENT = "STATEMENT";
    final String ITEM = "ITEM";
    final String ITEM_ADMIN = "ITEM_ADMIN";
    final String TRANS = "TRANS";
    final String TRANS_ADMIN = "TRANS_ADMIN";
    final String CRATE_HIST = "CRATE_HIST";
    final String CRATE_HIST_ADMIN = "CRATE_HIST_ADMIN";
    final String ORDER_ITEM = "ORDER_ITEM";
    final String LEDGER = "LEDGER";
    final String LEDGER_CRT = "LEDGER_CRT";
    final String FIRM_NAME = "FIRM_NAME";
    final String FIRM_ADDRESS = "FIRM_ADDRESS";
    final String USER_ADDRESS = "USER_ADDRESS";
    final String USER_ID = "USER_ID";
    final String EMAIL_ID = "EMAIL_ID";
//    final String LEDGER_CRT= "LEDGER_CRT";


    private Context context;

    public SessionManager(Context context) {
        this.context = context;
    }

    private SharedPreferences.Editor getPreferencesEditor() {
        return getSharedPreferences().edit();
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void clearSharedPref() {
        getPreferencesEditor().clear().commit();
    }

    public void setIsLoggedIn(boolean flag) {
        getPreferencesEditor().putBoolean(LOGIN_SATE, flag).commit();
    }

    public boolean getIsLoggedIn() {
        return getSharedPreferences().getBoolean(LOGIN_SATE, false);
    }

    public void setAdmin(boolean flag) {
        getPreferencesEditor().putBoolean(ADMIN, flag).commit();
    }

    public boolean isAdmin() {
        return getSharedPreferences().getBoolean(ADMIN, false);
    }


    public void setUserName(String value) {
        getPreferencesEditor().putString(USER_NAME, value).commit();
    }

    public String getUserName() {
        return getSharedPreferences().getString(USER_NAME, "name not set");
    }

    public void setUserId(String value) {
        getPreferencesEditor().putString(USER_ID, value).commit();
    }

    public String getUserId() {
        return getSharedPreferences().getString(USER_ID, null);
    }

    public void setEmailId(String value) {
        getPreferencesEditor().putString(EMAIL_ID, value).commit();
    }

    public String getEmailId() {
        return getSharedPreferences().getString(EMAIL_ID, null);
    }

    public void setUserMobile(String value) {
        getPreferencesEditor().putString(USER_MOBILE, value).commit();
    }

    public String getUserMobile() {
        return getSharedPreferences().getString(USER_MOBILE, "");
    }

    public void setUserPwd(String value) {
        getPreferencesEditor().putString(USER_PWD, value).commit();
    }

    public String getUserPwd() {
        return getSharedPreferences().getString(USER_PWD, "");
    }

    public void setUserCity(String value) {
        getPreferencesEditor().putString(USER_CITY, value).commit();
    }

    public String getUserCity() {
        return getSharedPreferences().getString(USER_CITY, "");
    }

    public void setProfileImg(String value) {
        getPreferencesEditor().putString(PROFILE_IMG, value).commit();
    }

    public String getProfileImg() {
        return getSharedPreferences().getString(PROFILE_IMG, null);
    }

    public void setOpeningBalance(String value) {
        getPreferencesEditor().putString(OPENING_BALANCE, value).commit();
    }

    public String getOpeningBalance() {
        return getSharedPreferences().getString(OPENING_BALANCE, "0.0");
    }

    public void setOpeningCrt(String value) {
        getPreferencesEditor().putString(OPENING_CRT, value).commit();
    }

    public String getOpeningCrt() {
        return getSharedPreferences().getString(OPENING_CRT, "0.0");
    }

    public void setBalance(String value) {
        getPreferencesEditor().putString(BALANCE, value).commit();
    }

    public String getBalance() {
        return getSharedPreferences().getString(BALANCE, "0.0");
    }

    public void setCrate(String value) {
        getPreferencesEditor().putString(CRATE, value).commit();
    }

    public String getCrate() {
        return getSharedPreferences().getString(CRATE, "0");
    }

    public void setDemand(String value) {
        getPreferencesEditor().putString(DEMAND, value).commit();
    }

    public String getDemand() {
        return getSharedPreferences().getString(DEMAND, "0");
    }

    public void setPayment(String value) {
        getPreferencesEditor().putString(PAYMENT, value).commit();
    }

    public String getPayment() {
        return getSharedPreferences().getString(PAYMENT, "0");
    }

    public void setFirmId(String value) {
        getPreferencesEditor().putString(FIRM_ID, value).commit();
    }

    public String getFirmId() {
        return getSharedPreferences().getString(FIRM_ID, "1");
    }


    public void setFirmName(String value) {
        getPreferencesEditor().putString(FIRM_NAME, value).commit();
    }

    public String getFirmName() {
        return getSharedPreferences().getString(FIRM_NAME, "");
    }


    public void setFirmAddress(String value) {
        getPreferencesEditor().putString(FIRM_ADDRESS, value).commit();
    }

    public String getFirmAddress() {
        return getSharedPreferences().getString(FIRM_ADDRESS, "Firm address not set");
    }

    public void setUserAddress(String value) {
        getPreferencesEditor().putString(USER_ADDRESS, value).commit();
    }

    public String getUserAddress() {
        return getSharedPreferences().getString(USER_ADDRESS, "Address not set");
    }


    public void setOutstanding(String value) {
        getPreferencesEditor().putString(OUTSTANDING, value).commit();
    }

    public String getOutstanding() {
        return getSharedPreferences().getString(OUTSTANDING, null);
    }

    public void setDues(String value) {
        getPreferencesEditor().putString(DUES, value).commit();
    }

    public String getDues() {
        return getSharedPreferences().getString(DUES, null);
    }


    public void setCustomerLastFetched(String date) {
        getPreferencesEditor().putString(CUSTOMER_TABLE, date).commit();
    }

    public String getCustomerLastFetched() {
        return getSharedPreferences().getString(CUSTOMER_TABLE, null);
    }


    public void setBankLastFetched(String date) {
        getPreferencesEditor().putString(BANK_TABLE, date).commit();
    }

    public String getBankLastFetched() {
        return getSharedPreferences().getString(BANK_TABLE, null);
    }


    public void setOrderHistLastFetched(String date) {
        getPreferencesEditor().putString(ORDER_HIST_TABLE, date).commit();
    }

    public String getOrderHistLastFetched() {
        return getSharedPreferences().getString(ORDER_HIST_TABLE, null);
    }

    public void setOrderHistAdminLastFetched(String date) {
        getPreferencesEditor().putString(ORDER_HIST_ADMIN_TABLE, date).commit();
    }

    public String getOrderHistAdminLastFetched() {
        return getSharedPreferences().getString(ORDER_HIST_ADMIN_TABLE, null);
    }

    public void setRouteLastFetched(String date) {
        getPreferencesEditor().putString(ROUTE, date).commit();
    }

    public String getRouteLastFetched() {
        return getSharedPreferences().getString(ROUTE, null);
    }


    public void setStatementLastFetched(String date) {
        getPreferencesEditor().putString(STATEMENT, date).commit();
    }

    public String getStatementLastFetched() {
        return getSharedPreferences().getString(STATEMENT, null);
    }

    public void setItemsLastFetched(String date) {
        getPreferencesEditor().putString(ITEM, date).commit();
    }

    public String getItemsLastFetched() {
        return getSharedPreferences().getString(ITEM, null);
    }

    public void setItemAdminRecordUpdated(boolean flag) {
        getPreferencesEditor().putBoolean(ITEM_ADMIN, flag).commit();
    }

    public boolean getItemAdminRecordUpdated() {
        return getSharedPreferences().getBoolean(ITEM_ADMIN, false);
    }


    public void setTransLastFetched(String date) {
        getPreferencesEditor().putString(TRANS, date).commit();
    }

    public String getTransLastFetched() {
        return getSharedPreferences().getString(TRANS, null);
    }

    public void setTransAdminLastFetched(String date) {
        getPreferencesEditor().putString(TRANS_ADMIN, date).commit();
    }

    public String getTransAdminLastFetched() {
        return getSharedPreferences().getString(TRANS_ADMIN, null);
    }

    public void setCratesLastFetched(String date) {
        getPreferencesEditor().putString(CRATE_HIST, date).commit();
    }

    public String getCratesLastFetched() {
        return getSharedPreferences().getString(CRATE_HIST, null);
    }

    public void setCratesAdminLastFetched(String date) {
        getPreferencesEditor().putString(CRATE_HIST_ADMIN, date).commit();
    }

    public String getCratesAdminLastFetched() {
        return getSharedPreferences().getString(CRATE_HIST_ADMIN, null);
    }

    public void setOrderItemLastFetched(String date) {
        getPreferencesEditor().putString(ORDER_ITEM, date).commit();
    }

    public String getOrderItemLastFetched() {
        return getSharedPreferences().getString(ORDER_ITEM, null);
    }

    public void setLedgerAllLastFetched(String date) {
        getPreferencesEditor().putString(LEDGER, date).commit();
    }

    public String getLedgerAllLastFetched() {
        return getSharedPreferences().getString(LEDGER, null);
    }

    public void setLedgerCrtLastFetched(String date) {
        getPreferencesEditor().putString(LEDGER_CRT, date).commit();
    }

    public String getLedgerCrtLastFetched() {
        return getSharedPreferences().getString(LEDGER_CRT, null);
    }

}
