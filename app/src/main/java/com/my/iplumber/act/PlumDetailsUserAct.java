package com.my.iplumber.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.adapter.HomeUserRecyclerViewAdapter;
import com.my.iplumber.adapter.PlumberReviewAdapter;
import com.my.iplumber.databinding.ActivityPlumDetailsUserBinding;
import com.my.iplumber.model.HomeModel;
import com.my.iplumber.model.SuccessResGetPlumber;
import com.my.iplumber.model.SuccessResGetPlumber;
import com.my.iplumber.model.SuccessResGetReview;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.NetworkAvailablity;
import com.my.iplumber.retrofit.PlumberInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlumDetailsUserAct extends AppCompatActivity {

    private String plumberId;

    ActivityPlumDetailsUserBinding binding;

    private SuccessResGetPlumber.Result plumberDetails;

    private PlumberInterface apiInterface;

    private ArrayList<SuccessResGetReview.Result> resultArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_plum_details_user);

        binding.nestedScrolling.setNestedScrollingEnabled(false);

        plumberId  = getIntent().getExtras().getString("id");

        apiInterface = ApiClient.getClient().create(PlumberInterface.class);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.RBook.setOnClickListener(v -> {
           startActivity(new Intent(PlumDetailsUserAct.this,PaymentScreenAct.class).putExtra("id",plumberDetails.getId()).putExtra("price",plumberDetails.getVideoCallPrice()).putExtra("from","user"));
        });

        if (NetworkAvailablity.checkNetworkStatus(PlumDetailsUserAct.this)) {
            getPlumbers();
            getReviewRating();
        } else {
            Toast.makeText(PlumDetailsUserAct.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
    }

    private void getPlumbers()
    {

        DataManager.getInstance().showProgressMessage(PlumDetailsUserAct.this,getString(R.string.please_wait));
        Map<String,String> map=new HashMap<>();
        map.put("plumber_id",plumberId);
        map.put("lat","");
        map.put("lon","");
        Call<SuccessResGetPlumber> call = apiInterface.getPlumberDetail(map);
        call.enqueue(new Callback<SuccessResGetPlumber>() {
            @Override
            public void onResponse(Call<SuccessResGetPlumber> call, Response<SuccessResGetPlumber> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetPlumber data= response.body();
                    if(data.status.equalsIgnoreCase("1"))
                    {
                        plumberDetails = data.getResult();
                        setPlumberDetails();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetPlumber> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getReviewRating()
    {

        DataManager.getInstance().showProgressMessage(PlumDetailsUserAct.this,getString(R.string.please_wait));
        Map<String,String> map=new HashMap<>();
        map.put("plumber_id",plumberId);
        Call<SuccessResGetReview> call = apiInterface.getRating(map);
        call.enqueue(new Callback<SuccessResGetReview>() {
            @Override
            public void onResponse(Call<SuccessResGetReview> call, Response<SuccessResGetReview> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetReview data= response.body();
                    if(data.status.equalsIgnoreCase("1"))
                    {
                        resultArrayList.clear();
                        resultArrayList.addAll(data.getResult());

                        binding.rvReview.setHasFixedSize(true);
                        binding.rvReview.setLayoutManager(new LinearLayoutManager(PlumDetailsUserAct.this));
                        binding.rvReview.setAdapter(new PlumberReviewAdapter(PlumDetailsUserAct.this,resultArrayList));

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetReview> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void setPlumberDetails()
    {

        binding.tvCompanyName.setText(plumberDetails.getCompanyName());
        binding.tvName.setText(plumberDetails.getFirstName()+" "+plumberDetails.getLastName());
        binding.tvBio.setText(plumberDetails.getBio());

        Glide.with(PlumDetailsUserAct.this)
                .load(plumberDetails.getImage())
                .into(binding.imgProfile);

    }

}