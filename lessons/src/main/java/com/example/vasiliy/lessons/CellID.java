package com.example.vasiliy.lessons;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class CellID {
    private String LOG_TAG = "CellID";
    private String CELLID = "";
    Context context;
    private int networkType=0;
    CellID(Context context){
        this.context = context;
        Log.d(LOG_TAG, "CELLID START");
    }
   String nameBTSforWindow(){

       String nameBTSforWindow;
       nameBTSforWindow = cell74();
       return nameBTSforWindow;
   }
   private String cell74(){
       String nameBTSforWindow74="";
       String cellIDSTR =cell();
       Log.d(LOG_TAG,"cellIdFull" + cellIDSTR);
       Integer lenInt = cellIDSTR.length();
       if (networkType == 1 || networkType == 2) {
           if (lenInt == 4) {
           nameBTSforWindow74+= nameBTSforWindow74 + "00" + cellIDSTR.charAt(0) + cellIDSTR.charAt(1) + cellIDSTR.charAt(2);
       } else if (lenInt == 5) {
           nameBTSforWindow74+=nameBTSforWindow74+ "000" + cellIDSTR.charAt(2) + cellIDSTR.charAt(3);
       } else {
           nameBTSforWindow74=nameBTSforWindow74+"99999";
       }
   } else if (networkType == 3 || networkType == 8 ||
    networkType == 9 || networkType == 10 || networkType == 15) {
        if (lenInt == 2) {
            nameBTSforWindow74+= nameBTSforWindow74+"0000" + cellIDSTR.charAt(0);
        } else if (lenInt == 3) {
            nameBTSforWindow74+= nameBTSforWindow74+"000" + cellIDSTR.charAt(0) + cellIDSTR.charAt(1);
        } else if (lenInt == 4) {
            nameBTSforWindow74 += (nameBTSforWindow74+ "00" +cellIDSTR.charAt(0) + cellIDSTR.charAt(1) + cellIDSTR.charAt(2));
        } else if (lenInt == 5) {
            nameBTSforWindow74+= (nameBTSforWindow74+ "00" +cellIDSTR.charAt(1) + cellIDSTR.charAt(2) + cellIDSTR.charAt(3));
        } else {
            nameBTSforWindow74=nameBTSforWindow74+"66666";
        }
       }
       if (networkType == 13) { //if(lenInt==2) {
           nameBTSforWindow74+=nameBTSforWindow74+ "00" + cellIDSTR.charAt(3) + cellIDSTR.charAt(4) + cellIDSTR.charAt(5);
       }
       return nameBTSforWindow74;
   }

    String cell() {

        if ( ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            assert tm != null;
            networkType = tm.getNetworkType();
            if (networkType != 0) {
                GsmCellLocation loc = (GsmCellLocation) tm.getCellLocation();
                int a = loc.getCid() & 0xffff;
                Log.d(LOG_TAG, "TM.MANAGER=" + tm.getNetworkType());
                int bayt = (loc.getCid() - ((loc.getCid() / 256) * 256));
                if (tm.getNetworkType() != 13) {
                    CELLID = Integer.toString(a);
                } else {
                    int hexc = loc.getCid() / 256 & 0xffffff;
                    CELLID = Integer.toString(hexc) + bayt;
                }
            } else {
                CELLID = "Не могу определить";
            }
        }
        return CELLID;
    }
}
