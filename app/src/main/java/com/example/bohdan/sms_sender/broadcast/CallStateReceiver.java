package com.example.bohdan.sms_sender.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.bohdan.sms_sender.activities.SubmitActivity;

import java.lang.reflect.Method;

public class CallStateReceiver extends BroadcastReceiver {
    private static final String TAG = "PhoneStateBroadcastReceiver1";
    SharedPreferences sPrefs;
    String incoming_nr;
    private boolean incomingCall = false;
    private String phoneState;
    private WindowManager windowManager;
    private ViewGroup windowLayout;
    private Class c;
    private Method m;
    private int prev_state;
    //    private com.android.internal.telephony.ITelephony telephonyService;
    private Intent intent;

    @Override
    public void onReceive(final Context context, Intent intent) {
        sPrefs = context.getSharedPreferences("number", context.MODE_PRIVATE);
        String callingSIM = "";
        Bundle bundle = intent.getExtras();
        callingSIM = String.valueOf(bundle.getInt("simId", -1));
//        if(callingSIM == "0") {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);

                Context context1 = context;
//
                if (incomingNumber != null && incomingNumber.length() > 0) {
                    incoming_nr = incomingNumber;
                    SharedPreferences.Editor ed = sPrefs.edit();
                    ed.putString("incoming", incoming_nr);
                    Log.d(TAG, "Save Phone : " +incomingNumber);
                    ed.commit();
                }
                SharedPreferences.Editor ed = sPrefs.edit();
                switch (state) {

                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d(TAG, "CALL_STATE_RINGING");
                        prev_state = state;
                        ed.putBoolean("outgoing", true);
                        ed.commit();
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.d(TAG, "CALL_STATE_OFFHOOK");
                        prev_state = state;
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d(TAG, "CALL_STATE_IDLE==>" + incoming_nr);

                        if ((prev_state == TelephonyManager.CALL_STATE_OFFHOOK)) {
                            prev_state = state;

                            Intent i = new Intent(context, SubmitActivity.class);
                            i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
//                             Toast.makeText(context,"offhook",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Запуск Активити");
                            incomingCall = false;
                            //Answered Call which is ended
                        }
                        if ((prev_state == TelephonyManager.CALL_STATE_RINGING)) {
                            prev_state = state;
                            //Rejected or Missed call


                        }
                        break;


                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
//
    }

}
