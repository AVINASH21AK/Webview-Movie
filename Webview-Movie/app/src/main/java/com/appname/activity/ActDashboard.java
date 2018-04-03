package com.appname.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.amnix.adblockwebview.ui.AdBlocksWebViewActivity;
import com.appname.R;
import com.appname.utils.App;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;


public class ActDashboard extends BaseActivity {

    String TAG = "ActDashboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.inflate(this, R.layout.act_dashboard, ll_SubMainContainer);



        /*if(AppFlag.flagCheckUpdate){
            forceUpdate();    //Check for App new Version Available
        }

        appRater();*/

        initialize();
        clickEvent();

    }

    private void appRater(){
        try{

            AppRate.with(this)
                    .setInstallDays(2)         // days after app is installed.
                    .setLaunchTimes(3)         // app launch interval times.
                    .setRemindInterval(1)      // days interval after last time launched.
                    .setShowLaterButton(true)  // show - Remind me Later button
                    .setDebug(false)           // default false
                    .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                        @Override
                        public void onClickButton(int which) {
                            App.showLog(TAG, Integer.toString(which));  //This will redirect to Playstore
                            App.showLog(TAG, "App Rater working");
                        }
                    })
                    .monitor();


            /*-----show every time-------*/
            // AppRate.with(KDAH_MainActivity.this).showRateDialog(KDAH_MainActivity.this);

            /*-----show as per condition-------*/
            AppRate.showRateDialogIfMeetsConditions(this);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initialize() {
        try {

             /*----- BaseActivity -----*/
            rl_baseToolbar.setVisibility(View.GONE);
            setEnableDrawer(false);


            /*----- This Activity -----*/
            tvTitle.setText(getString(R.string.header_Dashboard));


            //Reference : https://github.com/AmniX/AdBlockedWebView-Android

            /*
            * use edittext and pass url to below code for more flexible usage
            * */

            AdBlocksWebViewActivity.init(this);
            AdBlocksWebViewActivity.startWebView(ActDashboard.this, "http://world4ufree.to", getResources().getColor(R.color.colorPrimary) );


             /*------- Fonts --------*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickEvent() {
        try {


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        App.myFinishActivity(ActDashboard.this);
    }


}
