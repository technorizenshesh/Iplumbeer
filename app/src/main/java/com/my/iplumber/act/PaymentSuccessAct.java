package com.my.iplumber.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.my.iplumber.R;
import com.my.iplumber.RtcTokenBuilder;
import com.my.iplumber.VideoCallingAct;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.databinding.ActivityPaymentScreenBinding;
import com.my.iplumber.databinding.ActivityPaymentSuccessBinding;
import com.my.iplumber.model.SuccessResGetProfile;
import com.my.iplumber.model.SuccessResGetPurchaseCall;
import com.my.iplumber.model.SuccessResGetToken;
import com.my.iplumber.model.SuccessResMakeCall;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.NetworkAvailablity;
import com.my.iplumber.retrofit.PlumberInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.my.iplumber.act.utility.RandomString.getAlphaNumericString;
import static com.my.iplumber.retrofit.Constant.USER_ID;
import static com.my.iplumber.retrofit.Constant.showToast;

public class PaymentSuccessAct extends AppCompatActivity {

    ActivityPaymentSuccessBinding binding;

    private SuccessResGetPurchaseCall successResGetPurchaseCall;

    private String userName = "";
    
    static String appId = "33ff465b3c2243f29377b570f0bdd275";
    static String appCertificate = "16e69c7b4b6f4040a0831c5d69baa92f";
    static String channelName = "Grshikha";
    static String userAccount = "2082341273";
    static int uid = 0;
    static int expirationTimeInSeconds = 43200;

    private PlumberInterface apiInterface;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_payment_success);

        apiInterface = ApiClient.getClient().create(PlumberInterface.class);

        if (NetworkAvailablity.checkNetworkStatus(PaymentSuccessAct.this)) {
            getProfile();
        } else {
            Toast.makeText(PaymentSuccessAct.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        
        String result = getIntent().getExtras().getString("result");
        successResGetPurchaseCall = new Gson().fromJson(result,SuccessResGetPurchaseCall.class);

        Log.d("TAG", "onCreate: "+successResGetPurchaseCall.result.plumberDetails.get(0).getFirstName());

        Glide.with(PaymentSuccessAct.this)
                .load(successResGetPurchaseCall.result.plumberDetails.get(0).getImage())
                .into(binding.imgPlumber);

        binding.tvPLumberName.setText(successResGetPurchaseCall.result.plumberDetails.get(0).getFirstName()+" "+successResGetPurchaseCall.result.plumberDetails.get(0).getLastName());

        binding.tvVideoCall.setText("$ "+successResGetPurchaseCall.result.plumberDetails.get(0).getVideoCallPrice()+getString(R.string.per_video_call));

        binding.tvOrder.setText(getString(R.string.order)+" "+successResGetPurchaseCall.result.getOrderNo());

        binding.tvDate.setText(getString(R.string.datecol)+" "+successResGetPurchaseCall.result.getPurchaseDate());

        binding.tvTime.setText(getString(R.string.timecol)+" "+successResGetPurchaseCall.result.getPurchaseTime());

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.RVideo.setOnClickListener(v -> {

            if (NetworkAvailablity.checkNetworkStatus(PaymentSuccessAct.this)) {
                addNotification();
            } else {
                Toast.makeText(PaymentSuccessAct.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
            }
            
        });

        channelName = getAlphaNumericString(10);


        token =   getToken();

        Log.d("TAG", "onCreate: My Token : "+token);

    }

    public String getToken()
    {

        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        String result = token.buildTokenWithUserAccount(appId, appCertificate,
                channelName, userAccount, RtcTokenBuilder.Role.Role_Publisher, timestamp);
        System.out.println(result);

        Log.d("TAG", "onCreate: My Token1"+result);

        result = token.buildTokenWithUid(appId, appCertificate,
                channelName, uid, RtcTokenBuilder.Role.Role_Publisher, timestamp);
        System.out.println(result);

        Log.d("TAG", "onCreate: My Token2"+result);

        return result;
    }

    public void addNotification()
    {
        String userId = SharedPreferenceUtility.getInstance(PaymentSuccessAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(PaymentSuccessAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("plumber_id",successResGetPurchaseCall.result.plumberDetails.get(0).getId());
        map.put("username",userName);
        map.put("token",token);
        map.put("channel",channelName);

        Call<SuccessResMakeCall> call = apiInterface.addNotification(map);
        call.enqueue(new Callback<SuccessResMakeCall>() {
            @Override
            public void onResponse(Call<SuccessResMakeCall> call, Response<SuccessResMakeCall> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResMakeCall data = response.body();
                    if (data.status.equalsIgnoreCase("1")) {

                        startActivity(new Intent(PaymentSuccessAct.this, VideoCallingAct.class).putExtra("id",successResGetPurchaseCall.result.plumberDetails.get(0).getId())
                                .putExtra("channel_name",channelName) .putExtra("token",token)
                                .putExtra("from","user")
                        );
                        finish();

                    } else {
                        showToast(PaymentSuccessAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResMakeCall> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }



    private void getProfile()
    {

        String userId = SharedPreferenceUtility.getInstance(PaymentSuccessAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(PaymentSuccessAct.this,getString(R.string.please_wait));
        Map<String,String> map=new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetProfile> call = apiInterface.getProfile(map);
        call.enqueue(new Callback<SuccessResGetProfile>() {
            @Override
            public void onResponse(Call<SuccessResGetProfile> call, Response<SuccessResGetProfile> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetProfile data= response.body();
                    if(data.status.equalsIgnoreCase("1"))
                    {
                        userName  = data.getResult().getFirstName() + " "+data.getResult().getLastName();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetProfile> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }



}