package com.example.vasiliy.lessons;

import android.content.Context;
import android.content.res.Resources;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;



public class SendSMS {

    String  result;
    ArrayList<String> NumberArray = new ArrayList<>();
    ArrayList<String> BtsSmsArray = new ArrayList<>();
    ArrayList<String> arraySend = new ArrayList<>();
    ArrayList<String> arrayPhoneNumber = new ArrayList<>();
    boolean startFlow = false;
    Context context;
    public SendSMS(Context context){
        this.context=context;
    }

    void sendThreadPause(){
        Thread SmsRequestThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (BtsSmsArray.size() != 0 && NumberArray.size()!=0) {
                    for (int i=0; i < BtsSmsArray.size();i++) {
                        try {
                            startFlow=true;
                            String sms = BtsSmsArray.get(i);
                            String numberSMS=NumberArray.get(i);
                            SmsManager smsManager = SmsManager.getDefault();
                            ArrayList<String> msgArray = smsManager.divideMessage(sms);
                            smsManager.sendMultipartTextMessage(numberSMS, null, msgArray, null, null);
                            Thread.sleep(5000);

                        } catch (InterruptedException e) {
                            result="Не удалось отправить сообщение";
                            e.printStackTrace();
                        }
                    }
                    startFlow = false;
                    BtsSmsArray.clear();
                    NumberArray.clear();
                }
            }
        });
        SmsRequestThread.start();
    }

    public String sendMSG(final String sendMessage, final String phoneNumber) {

        arraySend.add(0,sendMessage);
        arrayPhoneNumber.add(0,phoneNumber);
        result = " ";
        if (!arraySend.isEmpty() && !arrayPhoneNumber.isEmpty()) {

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm.getNetworkType() != 0) {
                NumberArray.addAll(arrayPhoneNumber);
                BtsSmsArray.addAll(arraySend);
                if (startFlow == false) {
                    sendThreadPause();
                }
            } else {
                result = "Сети нет, не могу отправить сообщение.";
            }
            arrayPhoneNumber.clear();
            arraySend.clear();
        }

        return result;
        }
}
