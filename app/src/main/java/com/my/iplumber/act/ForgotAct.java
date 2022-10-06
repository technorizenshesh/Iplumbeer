package com.my.iplumber.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.databinding.ActivityForgotBinding;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.PlumberInterface;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.iplumber.retrofit.Constant.showToast;

public class ForgotAct extends AppCompatActivity {

    ActivityForgotBinding binding;

    private PlumberInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiInterface = ApiClient.getClient().create(PlumberInterface.class);

        binding= DataBindingUtil.setContentView(this,R.layout.activity_forgot);
        binding.RRSend.setOnClickListener(v -> {
            validation();
        });

    }

    private void validation() {

        String email=binding.edtEmail.getText().toString();
        if(email.equalsIgnoreCase(""))
        {
            Toast.makeText(this, ""+getString(R.string.enter_your_email), Toast.LENGTH_SHORT).show();
        }else
        {
            forgot(email);
        }
    }

    private void forgot(String strEmail)
    {

        DataManager.getInstance().showProgressMessage(ForgotAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("email",strEmail);
        Call<ResponseBody> call = apiInterface.forgotPass(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("result");
                    if (data.equals("1")) {
                        showToast(ForgotAct.this, message);
                        String dataResponse = new Gson().toJson(response.body());
                    } else if (data.equals("0")) {
                        showToast(ForgotAct.this, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}