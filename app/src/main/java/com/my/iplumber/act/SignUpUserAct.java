package com.my.iplumber.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.databinding.ActivityPlumberUserSignUpBinding;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.NetworkAvailablity;
import com.my.iplumber.retrofit.PlumberInterface;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.iplumber.retrofit.Constant.USER_ID;
import static com.my.iplumber.retrofit.Constant.isValidEmail;
import static com.my.iplumber.retrofit.Constant.showToast;

public class SignUpUserAct extends AppCompatActivity {

    ActivityPlumberUserSignUpBinding binding;
    String Type="";

    private String strFirstName = "",strLastName= "",strCity="",strAddress="",
    strState = "",strZipCode="",strCountryCode="",strMobileNum=""
            ,strEmail="",strPass="";
    private String str_image_path="";
    private String str_lincese_path="";
    private PlumberInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_plumber_user_sign_up);

        apiInterface = ApiClient.getClient().create(PlumberInterface.class);

        Intent intent=getIntent();

        if(intent!=null)
        {
            Type=intent.getStringExtra("type").toString();
        }

        binding.llLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpUserAct.this,LoginAct.class).putExtra("type",Type));
        });

        binding.cardLanguage.setOnClickListener(v -> {
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(SignUpUserAct.this, binding.cardLanguage);
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.myfile, popup.getMenu());
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.equals("one"))
                    {
                        binding.txtLanguage.setText("English");
                    }else
                    {
                        binding.txtLanguage.setText("Hindi");
                    }
                    return true;
                }
            });
            popup.show();
        });

        binding.btnSignup.setOnClickListener(view ->
                {
                    strFirstName = binding.etFirstName.getText().toString().trim();
                    strLastName = binding.etLastName.getText().toString().trim();
                    strAddress = binding.edtAddress.getText().toString().trim();
                    strCity = binding.edtCity.getText().toString().trim();
                    strState = binding.edtState.getText().toString().trim();
                    strZipCode = binding.etZipCode.getText().toString().trim();
                    strCountryCode = binding.ccp.getSelectedCountryCode().toString();
                    strMobileNum = binding.etPhone.getText().toString().trim();
                    strEmail = binding.etEmail.getText().toString().trim();
                    strPass = binding.etPassword.getText().toString().trim();

                    if (isValid()) {
                        if (NetworkAvailablity.checkNetworkStatus(SignUpUserAct.this)) {
                            signup();
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                    }
                }
                );

    }

    private boolean isValid() {
        if (strFirstName.equalsIgnoreCase("")) {
            binding.etFirstName.setError(getString(R.string.enter_your_firstname));
            return false;
        } else if (strLastName.equalsIgnoreCase("")) {
            binding.etEmail.setError(getString(R.string.enter_your_last_name));
            return false;
        } else if (strAddress.equalsIgnoreCase("")) {
            binding.edtAddress.setError(getString(R.string.enter_yourAddress));
            return false;
        }else if (strCity.equalsIgnoreCase("")) {
            binding.etEmail.setError(getString(R.string.enter_your_city));
            return false;
        }else if (strState.equalsIgnoreCase("")) {
            binding.edtState.setError(getString(R.string.enter_your_state));
            return false;
        }else if (strZipCode.equalsIgnoreCase("")) {
            binding.etZipCode.setError(getString(R.string.enter_your_zip));
            return false;
        }else if (strMobileNum.equalsIgnoreCase("")) {
            binding.etZipCode.setError(getString(R.string.enter_your_mobile));
            return false;
        }else if (strEmail.equalsIgnoreCase("")) {
            binding.etEmail.setError(getString(R.string.enter_your_email));
            return false;
        }else if (!isValidEmail(strEmail)) {
            binding.etEmail.setError(getString(R.string.enter_valid_email));
            return false;
        }else if (strPass.equalsIgnoreCase("")) {
            binding.etPassword.setError(getString(R.string.enter_your_password));
            return false;
        }
        return true;
    }

    private void signup()
    {

        //sagarpanse007@gmail.com
        DataManager.getInstance().showProgressMessage(SignUpUserAct.this, getString(R.string.please_wait));
        MultipartBody.Part filePart;

        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            if(file!=null)
            {
                filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            }
            else
            {
                filePart = null;
            }
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        RequestBody fName = RequestBody.create(MediaType.parse("text/plain"), strFirstName);
        RequestBody lName = RequestBody.create(MediaType.parse("text/plain"), strLastName);
        RequestBody cName = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), strEmail);
        RequestBody pass = RequestBody.create(MediaType.parse("text/plain"), strPass);
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), strAddress);
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"),strCity);
        RequestBody state = RequestBody.create(MediaType.parse("text/plain"),strState);
        RequestBody zip = RequestBody.create(MediaType.parse("text/plain"),strZipCode);
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"),strMobileNum);
        RequestBody cc = RequestBody.create(MediaType.parse("text/plain"),binding.ccp.getSelectedCountryCode());
        RequestBody license = RequestBody.create(MediaType.parse("text/plain"),"");
        RequestBody user_type = RequestBody.create(MediaType.parse("text/plain"), "user");
        RequestBody vPrice = RequestBody.create(MediaType.parse("text/plain"), "");

        Call<ResponseBody> call = apiInterface.signup(fName,lName,cName,email,pass,cc,phone,address,state,city,zip,license,user_type,vPrice,filePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (data.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        startActivity(new Intent(SignUpUserAct.this,LoginAct.class).putExtra("type",Type));
                    } else if (data.equals("0")) {
                        showToast(SignUpUserAct.this, message);
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