package com.my.iplumber;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.my.iplumber.act.HomePlumberAct;
import com.my.iplumber.act.HomeUserAct;
import com.my.iplumber.act.SelectPlumberLogin;
import com.my.iplumber.act.utility.HeadsUpNotificationService;
import com.my.iplumber.act.utility.IncomingCallService;
import com.my.iplumber.act.utility.RandomString;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.databinding.ActivityMainBinding;
import com.my.iplumber.retrofit.Constant;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import static com.my.iplumber.act.utility.RandomString.getAlphaNumericString;
import static com.my.iplumber.retrofit.Constant.updateResources;

public class MainActivity extends AppCompatActivity {

    private boolean isUserLoggedIn;

    private String type,lang;

    ActivityMainBinding binding;

    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        isUserLoggedIn = SharedPreferenceUtility.getInstance(MainActivity.this).getBoolean(Constant.IS_USER_LOGGED_IN);

        type =  SharedPreferenceUtility.getInstance(MainActivity.this).getString(Constant.USER_INFO);

        lang =  SharedPreferenceUtility.getInstance(MainActivity.this).getString(Constant.LANG);

        if(lang.equalsIgnoreCase("es"))
        {
            updateResources(MainActivity.this,"es");
        }

        context = MainActivity.this;

        handlerMethod();
    }

    private void handlerMethod() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isUserLoggedIn) {
                    if(type.equalsIgnoreCase("plumber"))
                    {
                        startActivity(new Intent(MainActivity.this, HomePlumberAct.class));
                    }else if(type.equalsIgnoreCase("user"))
                    {
                        startActivity(new Intent(MainActivity.this, HomeUserAct.class));
                    }
                    
                    finish();

                } else {
                    Intent intent=new Intent(MainActivity.this, SelectPlumberLogin.class);
                    startActivity(intent);
                    finish();
                }

                finish();

            }
        }, 3000);
    }

}