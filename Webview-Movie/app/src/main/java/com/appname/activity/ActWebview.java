package com.appname.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.amnix.adblockwebview.ui.AdBlocksWebViewActivity;
import com.appname.R;
import com.appname.utils.App;


public class ActWebview extends BaseActivity {

    String TAG = "ActWebview";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            ViewGroup.inflate(this, R.layout.act_webview, ll_SubMainContainer);

            initialize();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }



    public void initialize() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







    @Override
    public void onBackPressed() {
        super.onBackPressed();
        App.myFinishActivity(ActWebview.this);
    }


}
