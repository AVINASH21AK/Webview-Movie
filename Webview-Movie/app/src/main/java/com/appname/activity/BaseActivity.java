package com.appname.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.g00fy2.versioncompare.Version;
import com.appname.R;
import com.appname.utils.App;
import com.appname.utils.AppFlag;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BaseActivity extends AppCompatActivity {

    protected String TAG = "BaseActivity";

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.left_drawer) RelativeLayout left_drawer;
    @BindView(R.id.rl_baseToolbar) RelativeLayout rl_baseToolbar;
    @BindView(R.id.ll_SubMainContainer) LinearLayout ll_SubMainContainer;
    @BindView(R.id.ivMenu) ImageView ivMenu;
    @BindView(R.id.tvTitle) TextView tvTitle;


    //App Updater
    Version checkCurrentVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_base);

        ButterKnife.bind(this);

        baseClickEvent();
    }

    public void baseClickEvent(){
        try{
            ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (drawer.isDrawerOpen(left_drawer)) {
                        drawer.closeDrawers();
                    } else {
                        drawer.openDrawer(left_drawer);

                        /*--------  Google Analytic ----------*/
                        App.getInstance().trackScreenView(AppFlag.GAI_SideMenu);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void setEnableDrawer(boolean blnEnable) {
        if (blnEnable == true) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

    }



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(left_drawer)) {
            drawer.closeDrawers();
        } else {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }


    /*
   * Check for App new Version Available
   * */
    public void forceUpdate(){
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo =  packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = packageInfo.versionName;
        new ForceUpdateAsync(currentVersion,BaseActivity.this).execute();
    }

    public class ForceUpdateAsync extends AsyncTask<String, String, JSONObject> {

        private String playSotreVersion;
        private String currentVersion;
        private Context context;
        public ForceUpdateAsync(String currentVersion, Context context){
            this.currentVersion = currentVersion;
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {

                playSotreVersion = Jsoup.connect("https://play.google.com/store/apps/details?id="+context.getPackageName()+"&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(playSotreVersion!=null){
                App.showLog(TAG, "AppName currentVersion: "+currentVersion);
                App.showLog(TAG, "AppName playSotreVersion: "+playSotreVersion);

                checkCurrentVersion = new Version(currentVersion);

                if(checkCurrentVersion.isLowerThan(playSotreVersion)){
                    showForceUpdateDialog();
                }
            }
            super.onPostExecute(jsonObject);
        }


        public void showForceUpdateDialog() {
            {
                final Dialog dialog = new Dialog(BaseActivity.this);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);


                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.row_check_appupdate);
                dialog.setCancelable(false);


                TextView tvTitleDialog = (TextView) dialog.findViewById(R.id.tvTitleDialog);
                TextView tvTxt = (TextView) dialog.findViewById(R.id.tvTxt);
                TextView tvNo = (TextView) dialog.findViewById(R.id.tvNo);
                TextView tvYes = (TextView) dialog.findViewById(R.id.tvYes);

                tvTitleDialog.setText("Update Available");
                tvTxt.setText("A new version of Wolfkeeper is available. Please update to version " + playSotreVersion + " now");
                tvNo.setText("May be Later");
                tvYes.setText("Update");


                dialog.show();

                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppFlag.flagCheckUpdate = false;
                        dialog.dismiss();
                    }
                });

                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppFlag.flagCheckUpdate = false;
                        dialog.dismiss();
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                    }
                });

            }
        }
    }
}
