package com.appname.utils;


import android.annotation.SuppressLint;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "===MyFirebaseMsgService====";

    @SuppressLint("WrongThread")
    public void onMessageReceived(RemoteMessage remoteMessage) {

        /*--------  Google Analytic ----------*/
        App.getInstance().trackScreenView(AppFlag.GAI_NotificationListing);


        App.showLog(TAG, "====Arrive notification======onMessageReceived=====");

        /*-------------NOTIFICATION TYPE 1  FOR NEWS AND 2 FOR CATEGORY VIDEO-------------*/
        String msg = "";


        //------General---------
        if (remoteMessage.getData().get("msg") != null) {
            msg = remoteMessage.getData().get("msg");
        }




        App.showLog(TAG, "--msg--" + msg);

        sendNotification(msg);


    }

    private void sendNotification(String msg) {
        try {


        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}