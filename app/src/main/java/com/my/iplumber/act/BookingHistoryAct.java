package com.my.iplumber.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.adapter.BookingRecyclerViewAdapter;
import com.my.iplumber.databinding.ActivityBookingHistoryBinding;
import com.my.iplumber.model.HomeModel;
import com.my.iplumber.model.SuccessResPurchasedCallsHistory;
import com.my.iplumber.model.SuccessResPurchasedCallsHistory;
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

public class BookingHistoryAct extends AppCompatActivity {

    BookingRecyclerViewAdapter mAdapter;

    private ArrayList<SuccessResPurchasedCallsHistory.Result> historyList = new ArrayList<>();

    PlumberInterface apiInterface;

    ActivityBookingHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_booking_history);

        binding.ivBack.setOnClickListener(view -> finish());

        apiInterface = ApiClient.getClient().create(PlumberInterface.class);
        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        setAdapter();

        if (NetworkAvailablity.checkNetworkStatus(BookingHistoryAct.this)) {
            getHistory();
        } else {
            Toast.makeText(BookingHistoryAct.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

    }

    private void getHistory()
    {

        String userId = SharedPreferenceUtility.getInstance(BookingHistoryAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(BookingHistoryAct.this,getString(R.string.please_wait));
        Map<String,String> map=new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResPurchasedCallsHistory> call = apiInterface.getHistory(map);
        call.enqueue(new Callback<SuccessResPurchasedCallsHistory>() {
            @Override
            public void onResponse(Call<SuccessResPurchasedCallsHistory> call, Response<SuccessResPurchasedCallsHistory> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResPurchasedCallsHistory data= response.body();
                    if(data.status.equalsIgnoreCase("1"))
                    {
                        historyList.clear();
                        historyList.addAll(data.getResult());
                        mAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResPurchasedCallsHistory> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

    private void setAdapter() {

        mAdapter = new BookingRecyclerViewAdapter(BookingHistoryAct.this,historyList);
        binding.recyclerall.setHasFixedSize(true);
        binding.recyclerall.setLayoutManager(new LinearLayoutManager(BookingHistoryAct.this));
        binding.recyclerall.setAdapter(mAdapter);

    }
}