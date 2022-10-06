package com.my.iplumber.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.databinding.ActivityChangePasswordBinding;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.NetworkAvailablity;
import com.my.iplumber.retrofit.PlumberInterface;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.my.iplumber.retrofit.Constant.USER_ID;
import static com.my.iplumber.retrofit.Constant.showToast;

public class ChangePasswordAct extends AppCompatActivity {

    String oldPass = "",newConfirmPass = "" ,newPass = "";

    ActivityChangePasswordBinding binding;

    private PlumberInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_change_password);

        apiInterface = ApiClient.getClient().create(PlumberInterface.class);

       binding.RRback.setOnClickListener(v -> {
           onBackPressed();
       });

        binding.RRPay.setOnClickListener(v ->
                {

                    oldPass = binding.etOldPass.getText().toString().trim();
                    newPass = binding.etNewPass.getText().toString().trim();
                    newConfirmPass = binding.etConfirmNewPass.getText().toString().trim();

                    if(isValid())
                    {
                        if (NetworkAvailablity.checkNetworkStatus(ChangePasswordAct.this)) {

                            changePassword();

                        } else {
                            Toast.makeText(ChangePasswordAct.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(ChangePasswordAct.this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                    }

                }
        );

    }

    private boolean isValid() {
        if (oldPass.equalsIgnoreCase("")) {
            binding.etOldPass.setError(getString(R.string.please_enter_old_pass));
            return false;
        }else if (newPass.equalsIgnoreCase("")) {
            binding.etNewPass.setError(getString(R.string.enter_new_password));
            return false;
        } else if (newConfirmPass.equalsIgnoreCase("")) {
            binding.etConfirmNewPass.setError(getString(R.string.please_enter_confirm_password));
            return false;
        }else if (!newConfirmPass.equalsIgnoreCase(newPass)) {
            binding.etConfirmNewPass.setError(getString(R.string.password_mismatched));
            return false;
        }
        return true;
    }

    public void changePassword()
    {
        String userId = SharedPreferenceUtility.getInstance(ChangePasswordAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(ChangePasswordAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("old_pass", oldPass);
        map.put("new_pass", newPass);
//        map.put("confirm_password", newConfirmPass);

        Call<ResponseBody> call = apiInterface.changePassword(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Log.d(TAG, "onResponse: "+jsonObject);
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (data.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(ChangePasswordAct.this, message);
                        binding.etConfirmNewPass.setText("");
                        binding.etOldPass.setText("");
                        binding.etNewPass.setText("");
                    } else if (data.equalsIgnoreCase("0")) {
                        showToast(ChangePasswordAct.this, message);
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