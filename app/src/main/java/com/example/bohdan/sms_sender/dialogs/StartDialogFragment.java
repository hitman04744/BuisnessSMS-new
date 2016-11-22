package com.example.bohdan.sms_sender.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bohdan.sms_sender.R;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKWallPostResult;
import com.vk.sdk.util.VKUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 24-Jun-16.
 */
public class StartDialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    private static String[] sMyScope = new String[]{VKScope.STATUS, VKScope.FRIENDS, VKScope.WALL, VKScope.PHOTOS, VKScope.NOHTTPS};
    String id;
    private String smsTitle;
    private String smsText;
    private SharedPreferences sPrefs;
    private String url_text;

    public StartDialogFragment() {
    }

    public String getUrl_text() {
        return url_text;
    }

    public void setUrl_text(String url_text) {
        this.url_text = url_text;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_launch_dialog_fragment, container);
        String[] fingerprints = VKUtil.getCertificateFingerprint(this.getContext(), this.getActivity().getPackageName());
        getDialog().setTitle("Start");
        view.findViewById(R.id.VK).setOnClickListener(this);
        view.findViewById(R.id.VK_Share).setOnClickListener(this);
        setCancelable(false);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.VK):
                VKSdk.login(getActivity(), sMyScope);
//                EditNameDialogListener activity = (EditNameDialogListener) getActivity();
//                activity.onFinishEditDialog(mEditTextName.getText().toString(),mEditText.getText().toString());

//                this.dismiss();
                break;
            case (R.id.VK_Share):
                VKRequest request = new VKRequest("users.get");
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        //код обработки объекта

                        JSONObject json = response.json;
                        try {
                            JSONArray arr = json.getJSONArray("response");
                            JSONObject idJson = (JSONObject) arr.get(0);
                            id = idJson.getString("id");
//                            System.out.println(id.toString());
                            System.out.println(id);
                            makePost("я установил себе крутое приложение для отправки СМС", Integer.parseInt(id));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


//                            String id =  json.get("id").toString();
                        System.out.println(response.json.toString());


                    }

                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                    }
                });
                sPrefs = getActivity().getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sPrefs.edit();
//                System.out.println(request.requestListener);
                ed.putBoolean("lol", true);
                ed.commit();
                this.dismiss();
                break;


        }
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

    public String getSmsTitle() {
        return smsTitle;
    }

    public void setSmsTitle(String smsTitle) {
        this.smsTitle = smsTitle;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public interface EditNameDialogListener {
        void onFinishEditDialog(String smsTitle, String smsText);
    }


}
