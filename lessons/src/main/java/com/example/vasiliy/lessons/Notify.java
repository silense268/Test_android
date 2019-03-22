package com.example.vasiliy.lessons;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import java.util.Random;


public class Notify extends Service {
    SharedPreferences sPref;
    NotificationManager nm;
    String firstNotify;
    String secondNotify;
    String cellIDIntent;
    String LogTag = "Notify";


    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
      public int onStartCommand(Intent intent, int flags, int startId) {
          Log.d(LogTag, "Start");
          Intent resultIntent = new Intent(this, MainActivity.class);
          PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
                  PendingIntent.FLAG_UPDATE_CURRENT);
          //BitmapFactory.Options options = new BitmapFactory.Options();
          Resources res = this.getResources();
          Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.radiation);


          NotificationCompat.Builder builder =
                  new NotificationCompat.Builder(this)
                          //.setAutoCancel(true)
                          //.setSmallIcon(R.mipmap.ic_launcher)
                          .setSmallIcon(R.drawable.radiation)
                          .setLargeIcon(bitmap)
                          .setContentTitle("Нужен ключ")
                          .setContentText("Не забудте ключ от БС")
                          .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                          //.setSound(Uri.parse("/raw/wtf.ogg"))
                          .setContentIntent(resultPendingIntent);
          Log.d(LogTag, "Start1");
          Notification notification = builder.build();


          NotificationManager notificationManager =
                  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
          notificationManager.notify(1, notification);

          Log.d(LogTag, "Start2");
          return super.onStartCommand(intent, flags, startId);
      }

public IBinder onBind(Intent intent) {
        return null;
        }
        }