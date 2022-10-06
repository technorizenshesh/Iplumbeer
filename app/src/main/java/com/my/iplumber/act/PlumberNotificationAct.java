package com.my.iplumber.act;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.adapter.NotoficationRecyclerViewAdapter;
import com.my.iplumber.adapter.PlumberNotificationRecyclerViewAdapter;
import com.my.iplumber.databinding.ActivityNotificationBinding;
import com.my.iplumber.databinding.ActivityPlumberNotificationBinding;
import com.my.iplumber.model.HomeModel;
import com.my.iplumber.model.SuccessResGetNotification;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.NetworkAvailablity;
import com.my.iplumber.retrofit.PlumberInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.iplumber.retrofit.Constant.USER_ID;

public class PlumberNotificationAct extends AppCompatActivity {

    ActivityPlumberNotificationBinding binding;
    private ArrayList<SuccessResGetNotification.Result> notificationList = new ArrayList<>();
    private PlumberInterface apiInterface;
    private ArrayList<HomeModel> modelList = new ArrayList<>();
    private PlumberNotificationRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_plumber_notification);
        apiInterface = ApiClient.getClient().create(PlumberInterface.class);

        binding.imgBack.setOnClickListener(view -> finish());

        setAdapter();

        if (NetworkAvailablity.checkNetworkStatus(this)) {
            getProfile();
        } else {
            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
    }
    private void setAdapter() {
        mAdapter = new PlumberNotificationRecyclerViewAdapter(PlumberNotificationAct.this,notificationList);
        binding.recyclerall.setHasFixedSize(true);
        binding.recyclerall.setLayoutManager(new LinearLayoutManager(PlumberNotificationAct.this));
        binding.recyclerall.setAdapter(mAdapter);
    }

    private void getProfile()
    {
        String userId = SharedPreferenceUtility.getInstance(PlumberNotificationAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(PlumberNotificationAct.this,getString(R.string.please_wait));
        Map<String,String> map=new HashMap<>();
        map.put("plumber_id",userId);
        Call<SuccessResGetNotification> call = apiInterface.getPlumberNotification(map);
        call.enqueue(new Callback<SuccessResGetNotification>() {
            @Override
            public void onResponse(Call<SuccessResGetNotification> call, Response<SuccessResGetNotification> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetNotification data= response.body();
                    if(data.status.equalsIgnoreCase("1"))
                    {
                        notificationList.clear();
                        notificationList.addAll(data.getResult());
                        mAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetNotification> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}