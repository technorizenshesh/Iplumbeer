package com.my.iplumber.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.gson.Gson;
import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.databinding.ActivityHomePlumberBinding;
import com.my.iplumber.model.SuccessResGetUnseenNotification;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.PlumberInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.iplumber.retrofit.Constant.USER_ID;

public class HomePlumberAct extends AppCompatActivity {

    private ActivityHomePlumberBinding binding;
    LocalBroadcastManager lbm;
    private PlumberInterface apiInterface;
    private Bundle intent;
    private String status ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiInterface = ApiClient.getClient().create(PlumberInterface.class);

        binding = ActivityHomePlumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home_plumber);
      //  NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        intent =  getIntent().getExtras();
        if(intent!=null)
        {
            String key = intent.getString("key");
            if (key.equalsIgnoreCase("notification")){
                startActivity(new Intent(HomePlumberAct.this,PlumberNotificationAct.class));
            }
        }

        binding.rrNotification.setOnClickListener(view ->
                {
                    startActivity(new Intent(HomePlumberAct.this,PlumberNotificationAct.class));
                }
                );

        lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(receiver, new IntentFilter("filter_string"));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String str = intent.getStringExtra("key");
                // get all your data from intent and do what you want
                getUnseenNotificationCount();
            }
        }
    };

    private void getUnseenNotificationCount()
    {
        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("plumber_id",userId);
        Call<SuccessResGetUnseenNotification> call = apiInterface.getPlumberUnseenNoti(map);
        call.enqueue(new Callback<SuccessResGetUnseenNotification>() {
            @Override
            public void onResponse(Call<SuccessResGetUnseenNotification> call, Response<SuccessResGetUnseenNotification> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetUnseenNotification data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        int unseenNoti = data.getResult().getTotalUnseenNotification();
                        if(unseenNoti!=0)
                        {
                            binding.tvNotificationCount.setVisibility(View.VISIBLE);
                            binding.tvNotificationCount.setText(unseenNoti+"");
                        }
                        else
                        {
                            binding.tvNotificationCount.setVisibility(View.GONE);
                        }
                    } else if (data.status.equals("0")) {
                        // showToast(this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetUnseenNotification> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

}