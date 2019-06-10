package com.example.docking_milkyway;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/*

*   사용하실때 SaveSharedPreference 객체 생성하고, ex) SaveSharedPreference history = new Save...
*   history.getUserName 이 "NULL" 이 아닐 경우 로그인이 되어있는 상태입니다..
*   getUserName 받아서 계정이름으로 쓰시면 됩니다.
*
* */


public class SaveSharedPreference {

    static final String PREF_USER_NAME= "NULL";

    public SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.putString(PREF_USER_NAME, "NULL");
        editor.commit();
    }
}
