package com.my.iplumber.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.databinding.ActivityUpdateProfileBinding;
import com.my.iplumber.model.SuccessResGetProfile;
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

public class UpdateProfileAct extends AppCompatActivity {

    ActivityUpdateProfileBinding binding;

    private String firstName;
    private String lastName;
    private String companyName;
    private String linceNumber;
    private String address;
    private String city;
    private String state;
    private String zIpCode;
    private String mobile;
    private String email;
    private String password;
    private String priceVideoCalling;

    private String str_image_path="";


    private PlumberInterface apiInterface;

    private SuccessResGetProfile.Result userDetail;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_update_profile);

       apiInterface = ApiClient.getClient().create(PlumberInterface.class);
       
       binding.RRback.setOnClickListener(v -> {
           onBackPressed();
       });

        if (NetworkAvailablity.checkNetworkStatus(UpdateProfileAct.this)) {
            getProfile();
        } else {
            Toast.makeText(UpdateProfileAct.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.RRSignUp.setOnClickListener(v -> {
            validation();
//            startActivity(new Intent(UpdateProfileAct.this,HomePlumberAct.class));
        });

    }

    private void validation() {

        firstName=binding.edtFname.getText().toString();
        lastName=binding.edtLname.getText().toString();
        companyName="";
        linceNumber="";
        address=binding.edtAddress.getText().toString();
        city=binding.edtCity.getText().toString();
        state=binding.edtState.getText().toString();
        zIpCode=binding.edtZIpCode.getText().toString();
        mobile=binding.edtMobile.getText().toString();
        email=binding.edtEmail.getText().toString();
        priceVideoCalling="";

        if(firstName.equalsIgnoreCase(""))
        {
            Toast.makeText(this,getString( R.string.enter_firs_name), Toast.LENGTH_SHORT).show();

        }else if(lastName.equalsIgnoreCase(""))
        {
            Toast.makeText(this, getString(R.string.enter_last_name), Toast.LENGTH_SHORT).show();
        }else if(address.equalsIgnoreCase(""))
        {
            Toast.makeText(this,getString( R.string.enter_address), Toast.LENGTH_SHORT).show();

        }else if(city.equalsIgnoreCase(""))
        {
            Toast.makeText(this,getString( R.string.enter_ciy), Toast.LENGTH_SHORT).show();

        }else if(state.equalsIgnoreCase(""))
        {
            Toast.makeText(this, getString(R.string.enter_state), Toast.LENGTH_SHORT).show();

        }else if(zIpCode.equalsIgnoreCase(""))
        {
            Toast.makeText(this, getString(R.string.enter_zip_code), Toast.LENGTH_SHORT).show();

        }else if(mobile.equalsIgnoreCase(""))
        {
            Toast.makeText(this, getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show();

        }else if(email.equalsIgnoreCase(""))
        {
            Toast.makeText(this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
        }else if(!isValidEmail(binding.edtEmail.getText().toString().trim()))
        {
            Toast.makeText(this, getString(R.string.enter_valid), Toast.LENGTH_SHORT).show();
        }else
        {
            updateProfile();
        }
    }

    private void updateProfile()
    {

        String strUserId = SharedPreferenceUtility.getInstance(UpdateProfileAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(UpdateProfileAct.this, getString(R.string.please_wait));
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
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody fName = RequestBody.create(MediaType.parse("text/plain"), firstName);
        RequestBody lName = RequestBody.create(MediaType.parse("text/plain"), lastName);
        RequestBody cName = RequestBody.create(MediaType.parse("text/plain"), companyName);
        RequestBody email1 = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody address1 = RequestBody.create(MediaType.parse("text/plain"), address);
        RequestBody city1 = RequestBody.create(MediaType.parse("text/plain"),city);
        RequestBody state1 = RequestBody.create(MediaType.parse("text/plain"),state);
        RequestBody zip = RequestBody.create(MediaType.parse("text/plain"),zIpCode);
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"),mobile);
        RequestBody license = RequestBody.create(MediaType.parse("text/plain"),linceNumber);
        RequestBody user_type = RequestBody.create(MediaType.parse("text/plain"), "user");
        RequestBody bio = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody vPrice = RequestBody.create(MediaType.parse("text/plain"), priceVideoCalling);
        RequestBody cc = RequestBody.create(MediaType.parse("text/plain"),binding.ccp.getSelectedCountryCode());

        Call<ResponseBody> call = apiInterface.updateProfile(id,bio,fName,lName,cName,email1,cc,phone,address1,state1,city1,zip,license,user_type,vPrice,filePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if (data.equals("1")) {

                        String dataResponse = new Gson().toJson(response.body());
                        showToast(UpdateProfileAct.this, message);
                    } else if (data.equals("0")) {
                        showToast(UpdateProfileAct.this, message);
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



    private void getProfile()
    {

        String userId = SharedPreferenceUtility.getInstance(UpdateProfileAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(UpdateProfileAct.this,getString(R.string.please_wait));
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
                        userDetail  = data.getResult();
                        setUserDetail();
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

    private void setUserDetail()
    {

        binding.edtFname.setText(userDetail.getFirstName());
        binding.edtLname.setText(userDetail.getLastName());

        binding.edtAddress.setText(userDetail.getAddress());

        binding.edtCity.setText(userDetail.getCity());

        binding.edtState.setText(userDetail.getState());

        binding.edtZIpCode.setText(userDetail.getZipcode());

        binding.ccp.setCountryForPhoneCode(Integer.parseInt(userDetail.getCountryCode()));

        binding.edtMobile.setText(userDetail.getPhone());

        binding.edtEmail.setText(userDetail.getEmail());



    }
    
}