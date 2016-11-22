package com.example.bohdan.sms_sender.vkAuthorisation;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.orm.SugarContext;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

/**
 * Created by Bohdan on 02.08.2016.
 */

public class VKApplication extends Application {
    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                Toast.makeText(VKApplication.this, "AccessToken invalidated", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(VKApplication.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();


        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);

//        VKSdk.initialize(vkSdkListener, R.integer.com_vk_sdk_AppId, VKAccessToken.tokenFromSharedPreferences(this, String.valueOf(R.string.VK_ACCESS_TOKEN)));
        SugarContext.init(this);
    }
}
