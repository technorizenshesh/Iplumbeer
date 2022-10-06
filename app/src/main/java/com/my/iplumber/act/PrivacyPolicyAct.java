package com.my.iplumber.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.databinding.ActivityPrivacyPolicyBinding;
import com.my.iplumber.model.SuccessResPrivacyPolicy;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.PlumberInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.iplumber.retrofit.Constant.showToast;

public class PrivacyPolicyAct extends AppCompatActivity {

    ActivityPrivacyPolicyBinding binding;
    private PlumberInterface apiInterface;

    String description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_privacy_policy);

       apiInterface = ApiClient.getClient().create(PlumberInterface.class);

       binding.RRback.setOnClickListener(view ->
               {
                   finish();
               }
               );

       getPrivacyPolicy();

    }

    private void getPrivacyPolicy() {

        DataManager.getInstance().showProgressMessage(PrivacyPolicyAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();

        Call<SuccessResPrivacyPolicy> call = apiInterface.getPrivacyPolicy(map);

        call.enqueue(new Callback<SuccessResPrivacyPolicy>() {
            @Override
            public void onResponse(Call<SuccessResPrivacyPolicy> call, Response<SuccessResPrivacyPolicy> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResPrivacyPolicy data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        description = data.getResult().getDescription();
                        setWebView();


//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                    } else if (data.status.equals("0")) {
                        showToast(PrivacyPolicyAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResPrivacyPolicy> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    private void setWebView() {
        binding.webView.getSettings().setJavaScriptEnabled(true);

//        String text = description.replace("'","&quot;");

//        Log.d("TAG", "setWebView: "+text);

        binding.webView.loadDataWithBaseURL(null,description, "text/html; charset=utf-8", "UTF-8",null);


    }


}