package com.example.bohdan.sms_sender.activities;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.bohdan.sms_sender.R;
import com.example.bohdan.sms_sender.adapters.SmsListAdapter;
import com.example.bohdan.sms_sender.data.SMS;
import com.example.bohdan.sms_sender.dialogs.DialogFragment;
import com.example.bohdan.sms_sender.dialogs.StartDialogFragment;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.vk.sdk.VKScope;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKWallPostResult;
import com.vk.sdk.util.VKUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SYSTEM_ALERT_WINDOW;

public class EditableSMSListActivity extends AppCompatActivity implements DialogFragment.EditNameDialogListener {
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    public static boolean editState = false;
    public static String editedNAme;
    public static int editPosition;
    private static String[] sMyScope = new String[]{VKScope.STATUS, VKScope.FRIENDS, VKScope.WALL, VKScope.PHOTOS, VKScope.NOHTTPS};
    private static String vkTokenKey = "VK_ACCESS_TOKEN";
    VKApiUser user;
    String id;
    private RecyclerView rv;
    private Button mAdd;
    private Button mVKLogin;
    private Button mVKShare;
    private LoginButton mNext;
    private SmsListAdapter smsAdapter;
    private List<SMS> list;
    private List<SMS> startlist;
    private boolean firstLaunch = false;
    private SharedPreferences sPrefs;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS, SYSTEM_ALERT_WINDOW,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.SEND_SMS,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_SMS,
                Manifest.permission.CAMERA};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        getSupportActionBar().setTitle("Создание, редактироние, активация шаблонов");
        FacebookSdk.sdkInitialize(getApplicationContext());
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        System.out.println("LOL" + fingerprints[0]);
        setContentView(R.layout.activity_smslist);


        AppEventsLogger.activateApp(this);
        mAdd = (Button) findViewById(R.id.AddNew);
//        mVKLogin = (Button) findViewById(R.id.VK);
//        mVKShare = (Button) findViewById(R.id.VK_Share);
//        mNext = (LoginButton) findViewById(R.id.Next);
        sPrefs = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        SharedPreferences.Editor ed = sPrefs.edit();
        startlist = new ArrayList<>();
        int j = sPrefs.getInt("lolo", 0);
        firstLaunch = sPrefs.getBoolean("lol", false);
        System.out.println(firstLaunch + "");
        if (j == 1 && firstLaunch == true) {
//            Toast.makeText(this, "second", Toast.LENGTH_SHORT).show();

        } else {
//            StartDialogFragment mDialog = new StartDialogFragment();
//            mDialog.show(getSupportFragmentManager(), "dialog");
            startlist.add(new SMS("Визитка", "Визитка", false));
            startlist.add(new SMS("Акция", "Акция", false));
            startlist.add(new SMS("Номер карточки", "Номер карточки", false));
            startlist.add(new SMS("Время работы", "Время работы", false));
            startlist.add(new SMS("Ссылки на информацию", "Ссылки на информацию", false));
            for (int i = 0; i < startlist.size(); i++) {
                startlist.get(i).save();
            }
            ed.putInt("lolo", 1);
//            ed.putBoolean("lol", true);
            ed.commit();
        }
//
        list = SMS.listAll(SMS.class);

        rv = (RecyclerView) findViewById(R.id.SmsList_RV);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(llm);
        smsAdapter = new SmsListAdapter(list, this);
        rv.setAdapter(smsAdapter);

//        rv.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(getApplicationContext(),"Position" +position,Toast.LENGTH_SHORT).show();
//            }
//        }));
//        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                DialogFragment mDialog = new DialogFragment();
//                mDialog.show(getSupportFragmentManager(), "dialog");
//            }
//        }));

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment mDialog = new DialogFragment();
                mDialog.show(getSupportFragmentManager(), "dialog");
            }
        });
//        CallbackManager callbackManager = CallbackManager.Factory.create();
//        mNext.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });
//        mVKLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                VKSdk.login(EditableSMSListActivity.this, sMyScope);
//
//
//            }
//        });
//        mVKShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                VKRequest request = new VKRequest("users.get");
//                request.executeWithListener(new VKRequest.VKRequestListener() {
//                    @Override
//                    public void onComplete(VKResponse response) {
//                        super.onComplete(response);
//                        //код обработки объекта
//
//                        JSONObject json = response.json;
//                        try {
//                            JSONArray arr = json.getJSONArray("response");
//                            JSONObject idJson = (JSONObject) arr.get(0);
//                             id =  idJson.getString("id");
////                            System.out.println(id.toString());
//                            System.out.println(id);
//                            makePost("я установил себе крутое приложение для отправки СМС", Integer.parseInt(id));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
////                            String id =  json.get("id").toString();
//                            System.out.println(response.json.toString());
//
//
//                    }
//
//                    @Override
//                    public void onError(VKError error) {
//                        super.onError(error);
//                    }
//                });
////                System.out.println(request.requestListener);
//
//            }
//        });
//
////


    }

    void makePost(String msg, final int ownerId) {
        VKParameters parameters = new VKParameters();
        parameters.put(VKApiConst.OWNER_ID, String.valueOf(ownerId));
//        parameters.put(VKApiConst.ATTACHMENTS, att);
        parameters.put(VKApiConst.MESSAGE, msg);
        VKRequest post = VKApi.wall().post(parameters);
        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                // post was added
            }

            @Override
            public void onError(VKError error) {
                // error
            }
        });
    }

    @Override
    public void onFinishEditDialog(String smsTitle, String smsText) {
        if (editState) {
            List<SMS> sms = SMS.find(SMS.class, "name = ? ", editedNAme);
            sms.get(0).setName(smsTitle);
            sms.get(0).setText(smsText);
            sms.get(0).save();
            editState = false;
            list.set(editPosition, sms.get(0));
            smsAdapter.notifyDataSetChanged();
        } else {
            SMS sms = new SMS(smsTitle, smsText, false);
            sms.save();
            list.add(sms);
            smsAdapter.notifyDataSetChanged();
            smsAdapter.notifyItemInserted(list.size() - 1);
            List<SMS> logSMS = SMS.listAll(SMS.class);
            for (int i = 0; i < logSMS.size(); i++) {
                System.out.println(i + ": " + logSMS.get(i).getName() + "||" + logSMS.get(i).isActive());
            }
        }
    }

    public boolean isEditState() {
        return editState;
    }

    public void setEditState(boolean editState) {
        this.editState = editState;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}

