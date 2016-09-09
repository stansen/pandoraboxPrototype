package me.onionpie.pandorabox.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import me.onionpie.jellybutton.JellyExampleActivity;
import me.onionpie.pandorabox.ConstansParam.Constans;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.UI.ValidateScanCodeService;
import me.onionpie.pandorabox.Utils.CommonPreference;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first);
        startService();
        Log.d("pd_service  first",Thread.currentThread().getId()+"");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean hasAppOpened = CommonPreference.getBoolean(FirstActivity.this, Constans.KEY_IS_APP_FIRST_TIME_OPEN);

                if (hasAppOpened) {   Log.d("has_app_opened","true");
                    Intent intent = new Intent(FirstActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Log.d("has_app_opened","false");
                    Intent intent = new Intent(FirstActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }
            }
        }, 500);
    }

    private void startService(){
        Intent intent = new Intent(this, ValidateScanCodeService.class);
        startService(intent);
    }
}

