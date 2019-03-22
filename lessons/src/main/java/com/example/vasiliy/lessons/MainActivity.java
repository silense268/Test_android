package com.example.vasiliy.lessons;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.gsm.GsmCellLocation;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class MainActivity extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 0;
    String LogTag = "Main_Avtivity";
    ArrayList<String> spisokbs = new ArrayList<>();
    ArrayList<String> maxInfo = new ArrayList<>();
    ArrayList<String> spisokbsn = new ArrayList<>();
    ArrayList<String> gps = new ArrayList<>();
    ArrayList<String> vivod = new ArrayList<>();
    ArrayList<String> gpslat = new ArrayList<>();
    ArrayList<String> gpslong = new ArrayList<>();
    EditText Poisk;
    SharedPreferences sPref;
    SharedPreferences.Editor ed;
    ImageButton btnOk,btn1;
    ImageButton btnId,btn2,btn3;
    ListView spisok;
    ListView spisok1;
    ListView spisok2;
    CellID cellID;
    SendSMS sendSMS;
    ArrayAdapter<String> adapter;
    Parcelable state;
    String test = "";
    String mikl = "";
    Intent intent;
    Intent intent3;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sPref = getSharedPreferences("preference", MODE_PRIVATE);
        ed = sPref.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spisok = (ListView) findViewById(R.id.spisokSCR);
        spisok2 = (ListView) findViewById(R.id.spisoksecond);
        btnId = findViewById(R.id.imageButton1);
        btnId.setImageResource(R.drawable.cellid);
        btnOk = findViewById(R.id.imageButton2);
        btnOk.setImageResource(R.drawable.map_sml);
        btn1 = findViewById(R.id.imageButton);
        btn1.setImageResource(R.drawable.gear);
        btn2 = findViewById(R.id.imageButton3);
        btn2.setImageResource(R.drawable.alarmred);
        btn3 = findViewById(R.id.imageButton4);
        btn3.setImageResource(R.drawable.mail);
        cellID = new CellID(this);
        sendSMS = new SendSMS(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE},

                    MY_PERMISSIONS_REQUEST_READ_SMS);
        }
        infoClick();
        Click();
        longClick();
        alertdialogSelectEnergyList();

        final String[] info = getResources().getStringArray(R.array.list_bs);


        for (int i = 0; i < info.length; i = i + 1) {

            String[] minibs = info[i].split("@");
            String end = minibs[0] + " " + minibs[1];
            spisokbs.add(0, end);
            spisokbsn.add(0, minibs[2] +"  "+ minibs[5]);
            gps.add(0, minibs[0] + "@" + minibs[1] + "@" + minibs[3] + "@" + minibs[4] + "@");
            gpslong.add(0,minibs[3]);
            gpslat.add(0,minibs[4]);


        }

        adapter = new ArrayAdapter<String>(this, R.layout.adapter, spisokbs);

        Poisk = (EditText) findViewById(R.id.Poisk);
        Poisk.getText().toString();
        state = spisok.onSaveInstanceState();
        spisok.onRestoreInstanceState(state);
        adapter.notifyDataSetChanged();
        spisok.setAdapter(adapter);


        TextWatcher twTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                state = spisok.onSaveInstanceState();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                spisok.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        test = ((TextView) view).getText().toString();
                        Log.d(LogTag, "test=" + test);
                        for (int p = 0; p < spisokbs.size(); p = p + 1) {
                            if (test.contains(spisokbs.get(p))) {

                                mikl += String.valueOf(p) + "!";
                                infoClick();
                            }
                        }

                    }


                    public void onNothingSelected(AdapterView<?> parent) {
                        infoClick();

                    }
                });


                adapter.getFilter().filter(s);

                infoClick();

                Log.d(LogTag, "mikl=" + mikl);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        Poisk.addTextChangedListener(twTextWatcher);
        View.OnClickListener oclBtnId = new View.OnClickListener() {
            String info = "";
            String info1= "";
            @Override
            public void onClick(View p) { //  Cell Id
                String tostNumberBts = "74"+cellID.nameBTSforWindow();
                Log.d(LogTag, "tostNumberBts=" + tostNumberBts);
                for(int i=0; i<spisokbs.size();i=i+1){
                    Log.d(LogTag, "spisokbs=" + spisokbs.get(i));
                    if (spisokbs.get(i).contains(tostNumberBts)){
                        info=spisokbs.get(i);
                        Log.d(LogTag, "info="+ info);
                        info1=spisokbsn.get(i);
                    }
                }
                String tostString = cellID.cell() +"\n" + info + "\n" +
                        info1;
                toast = Toast.makeText(getApplicationContext(),
                        tostString,
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, Gravity.CENTER, -180);
                toast.show();
            }
        };

        View.OnClickListener oclBtn1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {        // Найти
                alertdialogSelectEnergyList();
                }



            };
        View.OnClickListener oclBtn2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {        // Найти
                dialogAll("Комментарий", "Начните сообщение с номера БС");
                 }

            };

        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override

            public void onClick(View v) {        // Найти
                if (vivod.size()>1) {
                    for (int t = 0; t < vivod.size(); t = t + 1) {
                        for (int i = 0; i < gps.size(); i = i + 1) {
                            if (gps.get(i).contains(vivod.get(t))) {


                                maxInfo.add(0, gps.get(i));
                                Log.d(LogTag, "T=" + t + " \n i= " + i);

                            }

                        }
                    }

                intent3 = new Intent(MainActivity.this, Notify.class);
                intent3.putExtra("firstNotify", "На БС:" + "нужен ключ");
                intent3.putExtra("secondNotify", "Ключ от этой базовой станции находится у дежурного");
                startService(intent3);

                intent = new Intent(MainActivity.this, MapsActivity.class);
                    String [] putToIntent = new String[maxInfo.size()];
                    for (int i =0;i<maxInfo.size();i=i+1) {
                        putToIntent[i] = maxInfo.get(i);
                    }
                intent.putExtra("koor", putToIntent);
                startActivity(intent);
                maxInfo.clear();
                }

                if(vivod.size()==0){
                    for (int i = 0; i < gps.size(); i = i + 1) {
                        {

                            maxInfo.add(0, gps.get(i));
                        }

                    }
                    intent3 = new Intent(MainActivity.this, Notify.class);
                    intent3.putExtra("firstNotify", "На БС:" + "нужен ключ");
                    intent3.putExtra("secondNotify", "Ключ от этой базовой станции находится у дежурного");
                    startService(intent3);
                    String [] putToIntent = new String[maxInfo.size()];
                    for (int i =0;i<maxInfo.size();i=i+1) {
                        putToIntent[i] = maxInfo.get(i);
                    }
                    intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("koor",putToIntent);
                    startActivity(intent);
                    maxInfo.clear();
                }

                if(vivod.size()==1){
                    takeAllBs takeAllBs_sending = new takeAllBs();
                    takeAllBs_sending.execute();
                }

            }


        };

        View.OnClickListener oclBtn3 = new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                alertdialogSMS();

            }

            };

        btnOk.setOnClickListener(oclBtnOk);
        btnId.setOnClickListener(oclBtnId);
        btn1.setOnClickListener(oclBtn1);
        btn2.setOnClickListener(oclBtn2);
        btn3.setOnClickListener(oclBtn3);

    }
     
    void longClick(){
        spisok.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override

            public boolean onItemLongClick(AdapterView<?> parent, View itemClicked, int position,
                                           long id) { //нажатие на список с инцидентом долгое
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, R.layout.adapter, vivod);



                String firstSeven=adapter.getItem(position);
                mikl="";
                mikl = mikl + firstSeven.charAt(0) + firstSeven.charAt(1)+  firstSeven.charAt(2)+
                        firstSeven.charAt(3)+ firstSeven.charAt(4) + firstSeven.charAt(5) + firstSeven.charAt(6);

                Log.d(LogTag,"item="+adapter.getItem(position));
                vivod.add(0,mikl);

                spisok2 = (ListView) findViewById(R.id.spisoksecond);
                spisok2.setAdapter(adapter1);
                return true;

            }

        });


    }
    void Click(){
        spisok2.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override

            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) { //нажатие на список с инцидентом
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, R.layout.adapter, vivod);
                spisok2 = (ListView) findViewById(R.id.spisoksecond);
                spisok2.setAdapter(adapter1);
                mikl = "";

                mikl = adapter.getItem(position);
                Log.d(LogTag,"item="+adapter.getItem(position));

                vivod.remove(position);

            }

        });


    }


    void infoClick() {
        spisok.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override

            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) { //нажатие на список с инцидентом


                String test1= ((TextView) itemClicked).getText().toString();
                test1=test1.substring(0,7);
                int numberOfString=0;
                for (int i = 0;i<spisokbs.size();i++){
                    if (spisokbs.get(i).contains(test1)){
                        numberOfString=i;
                        break;
                    }
                }
                Log.d(LogTag," test1=" +test1);
                toast = Toast.makeText(getApplicationContext(),
                        spisokbsn.get((numberOfString)),
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, Gravity.CENTER, -180);
                toast.show();


            }

        });
    }
    private void alertdialogSelectEnergyList() {
        final CharSequence[] items = {"Юра П.","Витя И.","Саша К.","Юра Д.","Слава О.", "Нет дежурного", "Помощь","Назад" };//имена методов Ваших в списке
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Dialog_Alert)
                .setCancelable(false);
        ed = sPref.edit();
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        ed.putString("EnergyPhoneNumber", "+79128910013");
                        break;
                    case 1:
                        ed.putString("EnergyPhoneNumber", "+79128911023");
                        break;
                    case 2:
                        ed.putString("EnergyPhoneNumber", "+79128915053");
                        break;
                    case 3:
                        ed.putString("EnergyPhoneNumber", "+79128917578");
                        break;
                    case 4:
                        ed.putString("EnergyPhoneNumber", "+79128940111");
                        break;
                    case 5:
                        ed.putString("EnergyPhoneNumber", "+79124224347");
                        break;
                    case 6:
                        AlertDialog.Builder alertDialogBld = new AlertDialog.Builder(MainActivity.this);
                        alertDialogBld
                                .setTitle("Помощь")
                                .setMessage("В левой части экрана вы можете увидеть все базовые станции Челябинской области. Для более удобного выбора воспользуйтесь поиском. При коротком нажатии на БС вы получите ее адрес и комментарий, если он есть. При длительном нажатии номер выбранной БС перенесется вправо, там хранятся выбранные вами БС. При коротком нажатии на номер БС справа – БС удалится из списка выбранных. Кнопка с изображением шестерни позволит вам выбрать дежурного энергетика. Кнопка с изображением сотовый вышки выведет информацию о базовой станции, к которой подключен телефон в данный момент. Кнопка с изображением планеты покажет на карте все интересующие вас БС, кнопка работает в 3 режимах. Кнопка с изображением звонка позволит вам написать комментарий, обязательно начните его с номера БС, а после написаний нажмите кнопку “Ок”. Кнопка с изображением сообщения позволяет отправлять наиболее распространённые сообщения дежурному, если у вас возникла другая проблема, нажмите на шаблон “Другая проблема”. ")
                                .setCancelable(true);


                        AlertDialog dialogGame = alertDialogBld.create();
                        dialogGame.show();


                        break;
                }
                ed.apply();

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    private class takeAllBs extends AsyncTask<Object, String, Boolean> {

        ProgressDialog WaitingDialog;
        String[] putToIntent;
        @Override
        protected void onPreExecute() {

            WaitingDialog = ProgressDialog.show(MainActivity.this, "Скоро отоборозятся все БС", "БС очень много, поэтому подождите несколько секунд, после закрытия этого окна", true);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            WaitingDialog.dismiss();
            Log.d(LogTag,"putToIn=" + putToIntent[3]);
            intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra("koor",putToIntent);
            startActivity(intent);
            intent3 = new Intent(MainActivity.this, Notify.class);
            intent3.putExtra("firstNotify", "На БС:" + "нужен ключ");
            intent3.putExtra("secondNotify", "Ключ от этой базовой станции находится у дежурного");
            startService(intent3);
        }


        @Override
        protected Boolean doInBackground(Object... params) {
            String a,b;
            double valuea=0;
            double valueb = 0;
            String c = "";


            ArrayList<String> allBsToMap = new ArrayList();
            Log.d(LogTag,"vivod.get(0)=" + vivod.get(0));
            for(int i=0;i<spisokbs.size();i=i+1){
                Log.d(LogTag,"spisokbs.get(i)=" + spisokbs.get(i));
                if(spisokbs.get(i).contains(vivod.get(0))){
                    a=gpslong.get(i);
                    Log.d(LogTag,"a=" + a);

                    b=gpslat.get(i);
                    c=gps.get(i);
                    Log.d(LogTag,"b=" + b);
                    valuea = Double.parseDouble(a.replaceAll(",","."));
                    valueb = Double.parseDouble(b.replaceAll(",","."));
                    break;
                }
                Log.d(LogTag,"i=" + i);

            }

            Double raznicaLat=0.325;
            Double raznicaLon=0.325;

            for (int i=0;i<gps.size();i=i+1){
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                Log.d(LogTag,"gpslat.get(i)=" + gpslat.get(i));
                Log.d(LogTag,"gpslong.get(i)=" + gpslong.get(i));

                Double allBSLonDouble= Double.valueOf(gpslat.get(i).replaceAll(",","."));
                Double allBSLatDouble= Double.valueOf(gpslong.get(i).replaceAll(",","."));
                Log.d(LogTag,"gpslat.get(i)=" + gpslat.get(i));

                if (valuea-raznicaLat<allBSLatDouble & allBSLatDouble<valuea+raznicaLat){
                    if (valueb-raznicaLon<allBSLonDouble & allBSLonDouble<valueb+raznicaLon){
                        Log.d(LogTag,"puToIntent=");
                        allBsToMap.add(0,gps.get(i));

                    }
                }
                Log.d(LogTag,"End");

            }
            allBsToMap.add(0,c);
            Log.d(LogTag,"StartPutToIntent");
            putToIntent = new String[(allBsToMap.size())];
            Log.d(LogTag,"puToIntent12="+putToIntent.length);
            for (int i =0;i<allBsToMap.size();i=i+1) {
                putToIntent[i] = allBsToMap.get(i);

            }


            return false;
        }


    }
    private void dialogAll(String title, final String body){
        AlertDialog.Builder alertDialogBld = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        alertDialogBld
                .setTitle(title)
                .setMessage(body)
                .setCancelable(true)
                .setView(input);


        alertDialogBld.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phonenumber = sPref.getString("EnergyPhoneNumber","");
                sendSMS.sendMSG ("КоммКР " + input.getText().toString(), "89127787293");


            }
        });
        AlertDialog dialogGame = alertDialogBld.create();
        dialogGame.show();

    }
    private void alertdialogSMS() {
        final CharSequence[] items = {"Захожу на объект","Начинаю работу","Возникли трудности, работа займет больше времени", "Заканчиваю работу", "Покинул объект" ,"Другая проблема","Назад"};//имена методов Ваших в списке
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Dialog_Alert)
                .setCancelable(false);
        ed = sPref.edit();
        final String phonenumber = sPref.getString("EnergyPhoneNumber","");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        sendSMS.sendMSG ("Захожу на объект", phonenumber);;
                        break;
                    case 1:
                        sendSMS.sendMSG ("Начинаю работу", phonenumber);;
                        break;
                    case 2:
                        sendSMS.sendMSG ("Возникли трудности, работа займет больше времени", phonenumber);;
                        break;
                    case 3:
                        sendSMS.sendMSG ("Заканчиваю работу", phonenumber);;
                        break;
                    case 4:
                        sendSMS.sendMSG ("Покинул объект", phonenumber);;
                        break;
                    case 5:
                        AlertDialog.Builder alertDialogBld = new AlertDialog.Builder(MainActivity.this);
                        final EditText input = new EditText(MainActivity.this);
                        alertDialogBld
                                .setTitle("Опишите проблему")
                                .setCancelable(true)
                                .setView(input);


                        alertDialogBld.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendSMS.sendMSG ( input.getText().toString(), phonenumber);


                            }
                        });
                        AlertDialog dialogGame = alertDialogBld.create();
                        dialogGame.show();

                        break;

                }
                ed.apply();

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}