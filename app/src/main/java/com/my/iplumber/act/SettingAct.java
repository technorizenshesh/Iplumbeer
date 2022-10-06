package com.my.iplumber.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.my.iplumber.R;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.databinding.ActivitySettingBinding;

import static com.my.iplumber.retrofit.Constant.USER_INFO;

public class SettingAct extends AppCompatActivity {

    ActivitySettingBinding binding;
    String Type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_setting);

       Intent intent=getIntent();
       if(intent!=null)
       {
            Type=intent.getStringExtra("Type").toString();

           if(Type.equalsIgnoreCase("PLumber"))
           {
               binding.cardHistory.setVisibility(View.GONE);
           }else
           {
               binding.cardHistory.setVisibility(View.VISIBLE);
           }
       }

       binding.RRback.setOnClickListener(v -> {
           onBackPressed();
       });

       binding.txtUpdate.setOnClickListener(v -> {

           String userType = SharedPreferenceUtility.getInstance(SettingAct.this).getString(USER_INFO);

           if(userType.equalsIgnoreCase("plumber"))
           {
               startActivity(new Intent(SettingAct.this,UpdatePlumberAct.class));
           }
           else
           {
               startActivity(new Intent(SettingAct.this,UpdateProfileAct.class));
           }

       });

       binding.txtChange.setOnClickListener(v -> {

         startActivity(new Intent(SettingAct.this,ChangePasswordAct.class));

       });

       binding.txtBooking.setOnClickListener(v -> {

         startActivity(new Intent(SettingAct.this,BookingHistoryAct.class));

       });

        binding.cvPrivacyPolicy.setOnClickListener(v -> {

            startActivity(new Intent(SettingAct.this,PrivacyPolicyAct.class));

        });

    }
}