package com.appname.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.appname.R;

import java.io.File;
import java.util.Random;

/**
 * Created by avinash.kahar on 3/20/2018.
 */

public class App extends Application {

    public static String TAG = "APP";
    static Context context;
    private static App mInstance;
    public static String PREF_NAME = "Permission_PREFERENCE";
    public static SharePrefrences sharePrefrences;
    public static String App_mode = "1";  //1 for Production and 2 for Development
    public static String App_plateform = "2";  //1 for IOS and 2 for Android

    public static String strFolderName = Environment.getExternalStorageDirectory().toString();
    public static String strFolderFullPath = Environment.getExternalStorageDirectory() + File.separator + App.strFolderName;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);
        context = getApplicationContext();
        mInstance = this;
        sharePrefrences = new SharePrefrences(App.this);

        /*------ Facebook image loading ---------*/
        Fresco.initialize(this);


        //Google AnalyticsTrackers
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
    }


    /*---------------  Google Tracker --------------------*/

    public static synchronized App getInstance() {
        return mInstance;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    //@param screenName screen name to be displayed on GA dashboard
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder()
                .build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    //@param e exception to be tracked
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    //@param category event category //@param action   action of the event //@param label    label
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }

    /*---------------  Finish Google Tracker --------------------*/



    /*------ Log -------*/
    public static void showLog(String ActivityName, String strMessage) {
        Log.d("From: ", ActivityName + " -- " + strMessage);
    }

    /*public static void showLogApiRespose(String op, Response response) {
        String strResponse = new Gson().toJson(response.body());
        Log.d("From: =op==>" + op, "response==>" + strResponse);
    }*/


    /*------ Check Internet -------*/
    public static boolean isInternetAvail(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    /*---------- IMEI Number ----------*/
    public static String getIMEInumber(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String strIMEI = telephonyManager.getDeviceId();

        if (strIMEI != null && strIMEI.length() > 0) {
            return strIMEI;
        } else {
            Random ran = new Random();
            int x = ran.nextInt(5) + 1050;

            return x + "1050";
        }

    }

    /*---------- Snackbar ----------*/
    public static void showSnackBar(View view, String strMessage) {
        // Toast.makeText(context, ""+strMessage, Toast.LENGTH_SHORT).show();

        try {
            Snackbar snackbar = Snackbar.make(view, strMessage, Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.BLACK);
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
   * Start and End Activity
   * */
    public static void myStartActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void myStartActivityRefersh(Activity activity, Intent intent) {
        activity.finish();
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void myStartActivityClearTop(Activity activity, Intent intent) {
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        activity.finish();
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void myFinishActivityRefresh(Activity activity, Intent intent) {
        activity.finish();
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static void myFinishActivity(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
