package com.my.iplumber.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.my.iplumber.R;
import com.my.iplumber.databinding.ActivitySelectPlumberLoginBinding;

public class SelectPlumberLogin extends AppCompatActivity {

    ActivitySelectPlumberLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_select_plumber_login);

        binding.RRContinew.setOnClickListener(v -> {
            if(binding.RadiobtnPlumber.isChecked())
            {
                startActivity(new Intent(SelectPlumberLogin.this,LoginAct.class).putExtra("type","plumber"));
            }else  if(binding.RadiobtnUser.isChecked())
            {
                startActivity(new Intent(SelectPlumberLogin.this,LoginAct.class).putExtra("type","user"));
            }else
            {
                Toast.makeText(this, "Please Select user and plumber..", Toast.LENGTH_SHORT).show();
            }

        });
        binding.cardLoginPlumber.setOnClickListener(v -> {
            binding.RadiobtnPlumber.setChecked(true);
            binding.RadiobtnUser.setChecked(false);
        });

        binding.cardLoginUser.setOnClickListener(v -> {

            binding.RadiobtnPlumber.setChecked(false);
            binding.RadiobtnUser.setChecked(true);
        });
    }
}