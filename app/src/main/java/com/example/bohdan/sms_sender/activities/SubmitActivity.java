package com.example.bohdan.sms_sender.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bohdan.sms_sender.R;
import com.example.bohdan.sms_sender.adapters.SubmitAdapter;
import com.example.bohdan.sms_sender.data.SMS;
import com.example.bohdan.sms_sender.utils.RecyclerItemClickListener;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;
import java.util.List;

public class SubmitActivity extends AppCompatActivity {
    private static final int NOTIFY_ID = 101;
    ObjectAnimator animation;
    ProgressBar progressBar;
    private RecyclerView mRecyclerView;
    private SubmitAdapter mSubmitAdapter;
    private Button mButtonSend;
    private List<SMS> sms;
    private List<SMS> sms1;
    //   private List<SMSsend> sms;
    private String number;
    private DonutProgress donutProgress;
    private int counter = 0;
    private String smsTitleNotification;
    private String smsTextNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Какой шаблон отправлять");
        setContentView(R.layout.activity_submit);
        mButtonSend = (Button) findViewById(R.id.buttonSend);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView_Submit);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(llm);
        sms1 = new ArrayList<>();

        sms = SMS.listAll(SMS.class);
        for (int i = 0; i < sms.size(); i++) {
            if (sms.get(i).isActive() == true) {
                sms1.add(sms.get(i));
//            System.out.println(i+": "+sms.get(i).getName()+"||"+sms.get(i).isActive());
//            System.out.println(i+": "+sms1.get(i).getName()+"||"+sms.get(i).isActive());

            }
        }
        for (int j = 0; j < sms1.size(); j++) {
            System.out.println(j + ": " + sms1.get(j).getName() + "||" + sms.get(j).isActive());
        }
//
        mSubmitAdapter = new SubmitAdapter(sms1, this);
//      mSubmitAdapter = new SubmitAdapter(sms,this);
        mRecyclerView.setAdapter(mSubmitAdapter);

        SharedPreferences sPrefs = getApplicationContext().getSharedPreferences("number", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPrefs.edit();
        boolean state = sPrefs.getBoolean("outgoing", false);
//         Проверка состояния: входящий(true)/исходяший (false) вызов
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), sms.get(position).getName() + ": " + sms.get(position).getText(), Toast.LENGTH_SHORT).show();
                smsTitleNotification = sms1.get(position).getName();
                sendSMS(number, sms1.get(position).getText());
                finish();
            }
        }));
//        if (state) {
            number = sPrefs.getString("incoming", "");
            ed.putBoolean("outgoing", false);
            ed.commit();
            mButtonSend.setText("СМС на " + number);
//        } else {
//            number = CallLog.Calls.getLastOutgoingCall(getApplicationContext());
//            mButtonSend.setText("СМС на " + number);
//        }
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//         animation = ObjectAnimator.ofInt (progressBar, "progress", 0, sms.size()) // see this max value coming back here, we animale towards that value

        progressBar.setMax(sms1.size());

//
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri allCalls = Uri.parse("content://call_log/calls");
//                String lastDialed = CallLog.Calls.getLastOutgoingCall(getApplicationContext());
//                SharedPreferences sPrefs = getApplicationContext().getSharedPreferences("number", Context.MODE_PRIVATE);
//                SharedPreferences.Editor ed = sPrefs.edit();
//                String number = sPrefs.getString("incoming","");
//                }
//                List<SMSsend> sms1 = sms;

//            for(int i=0; i<sms.size();i++) {
//                sendSMS(number, sms.get(i).getText());
//
//
//
//            }
//            sms.clear();
            }
        });

    }
//    private void sendSMS(String phoneNumber, String message) {
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(phoneNumber, null, message, null, null);
//    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void sendSMS(String phoneNumber, String message)

    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";


        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        Toast.makeText(getBaseContext(), "SMS sent",
//                                Toast.LENGTH_SHORT).show();
//                        NotificationCompat.Builder mBuilder =
//                                new NotificationCompat.Builder(getBaseContext())
//                                        .setSmallIcon(R.mipmap.ic_launcher)
//                                        .setContentTitle("Sending")
//                                        .setContentText(smsTitleNotification + " to number: " + number);
//
//                        NotificationManager mNotificationManager =
//                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                        mNotificationManager.notify(NOTIFY_ID, mBuilder.build());
////                        for (int i = 0; i<sms.size(); i++) {
////                            animation = ObjectAnimator.ofInt(progressBar, "progress",i);
////
////                            animation.setDuration(1000); //in milliseconds
////                            animation.setInterpolator(new DecelerateInterpolator());
////                            animation.start();
//////                            counter++;
////                        }
//                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        Toast.makeText(getBaseContext(), "Generic failure",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        Toast.makeText(getBaseContext(), "No service",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        Toast.makeText(getBaseContext(), "Null PDU",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        Toast.makeText(getBaseContext(), "Radio off",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }, new IntentFilter(SENT));
//        counter = 0;

        //---when the SMS has been delivered---
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        Toast.makeText(getBaseContext(), "SMS delivered",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(getBaseContext(), "SMS not delivered",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> parts = sms.divideMessage(message);

        if (parts.size() > 1) {
            sms.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
        } else {
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        }
    }

}
