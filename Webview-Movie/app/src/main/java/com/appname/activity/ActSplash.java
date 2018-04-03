package com.appname.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.iid.FirebaseInstanceId;
import com.appname.R;
import com.appname.utils.App;
import com.appname.utils.PreferencesKeys;


public class ActSplash extends BaseActivity {

    String TAG = "ActSplash";
    int TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            ViewGroup.inflate(this, R.layout.act_splash, ll_SubMainContainer);

            checkDeviceId();
            checkAnddroidToken();
            initialize();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void checkDeviceId() {
        try {

            String strDeviceId = App.sharePrefrences.getStringPref(PreferencesKeys.strDeviceId);

            if (strDeviceId != null && strDeviceId.length() > 4) {
                App.showLog(TAG, "---------------strDeviceId---------------" + strDeviceId);
                App.sharePrefrences.setPref(PreferencesKeys.strDeviceId, strDeviceId);
            } else {
                App.sharePrefrences.setPref(PreferencesKeys.strDeviceId, App.getIMEInumber(ActSplash.this));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkAnddroidToken() {
        try {
            String strAndroidToken = App.sharePrefrences.getStringPref(PreferencesKeys.strAndroidToken);

            if (strAndroidToken != null && strAndroidToken.length() > 5) {
                App.showLog(TAG, "---------------strAndroidToken---------------" + strAndroidToken);
                App.sharePrefrences.setPref(PreferencesKeys.strAndroidToken, strAndroidToken);
                TIME = 2000;
            } else {
                getDeviceToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get Device token - Function
    private void getDeviceToken() {
        try {

            String strAndroidToken = FirebaseInstanceId.getInstance().getToken();
            App.showLog(TAG, "Refreshed token: " + strAndroidToken);
            App.showLog(TAG, "Subscribed to news topic");
            App.showLog(TAG, "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());

            if (strAndroidToken != null && strAndroidToken.length() > 5) {
                App.sharePrefrences.setPref(PreferencesKeys.strAndroidToken, strAndroidToken);

                TIME = 2000;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initialize() {
        try {

             /*----- BaseActivity -----*/
            setEnableDrawer(false);
            rl_baseToolbar.setVisibility(View.GONE);


            /*----- This Activity -----*/


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent i1 = new Intent(ActSplash.this, ActDashboard.class);
                    App.myStartActivityRefersh(ActSplash.this, i1);


                }
            }, TIME);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
