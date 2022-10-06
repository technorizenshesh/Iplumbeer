package com.my.iplumber.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.GPSTracker;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.databinding.ActivityLoginBinding;
import com.my.iplumber.model.SuccessResSignIn;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.Constant;
import com.my.iplumber.retrofit.NetworkAvailablity;
import com.my.iplumber.retrofit.PlumberInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.iplumber.retrofit.Constant.showToast;

public class LoginAct extends AppCompatActivity {

    private LocationManager manager;


    ActivityLoginBinding binding;
    String Type="";
    PlumberInterface apiInterface;
    public static String TAG = "LoginActivity";
    private String strEmail ="",strPassword= "",deviceToken = "";

    private FirebaseAuth mAuth;

    GPSTracker gpsTracker;
    private String strLat="";
    private String strLng="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        gpsTracker = new GPSTracker(LoginAct.this);

        getToken();

        apiInterface = ApiClient.getClient().create(PlumberInterface.class);

        manager =  (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        Intent intent=getIntent();

        if(intent!=null)
        {
            Type=intent.getStringExtra("type").toString();
        }

        binding.llCreate.setOnClickListener(v -> {

            if(Type.equalsIgnoreCase("plumber"))
            {
                startActivity(new Intent(LoginAct.this, SignUpPlumberAct.class).putExtra("type","plumber"));
            }else if(Type.equalsIgnoreCase("user"))
            {
                startActivity(new Intent(LoginAct.this, SignUpUserAct.class).putExtra("type","user"));
            }
        });

    binding.RLogin.setOnClickListener(v -> {
        validation();
        });
        binding.txtForogot.setOnClickListener(v -> {
            startActivity(new Intent(LoginAct.this,ForgotAct.class));
        });
        getLocation();

    }

    private void validation() {
      strEmail=binding.edtEmail.getText().toString();
      strPassword=binding.edtPassword.getText().toString();
        if(strEmail.equalsIgnoreCase(""))
        {
            Toast.makeText(this, ""+getString(R.string.enter_your_email), Toast.LENGTH_SHORT).show();
        }else  if(strPassword.equalsIgnoreCase(""))
        {
            Toast.makeText(this, ""+getString(R.string.enter_your_password), Toast.LENGTH_SHORT).show();
        }else
        {
            if (NetworkAvailablity.checkNetworkStatus(this)) {
                login();
            } else {
                Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void login() {

        String lang =  SharedPreferenceUtility.getInstance(LoginAct.this).getString(Constant.LANG);

        if(lang.equalsIgnoreCase(""))
        {
            lang = "en";
        }
        DataManager.getInstance().showProgressMessage(LoginAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("email",strEmail);
        map.put("password",strPassword);
        map.put("register_id",deviceToken);
        map.put("lat",strLat);
        map.put("lon",strLng);
//        map.put("language",lang);
        Call<SuccessResSignIn> call = apiInterface.login(map);
        call.enqueue(new Callback<SuccessResSignIn>() {
            @Override
            public void onResponse(Call<SuccessResSignIn> call, Response<SuccessResSignIn> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignIn data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SharedPreferenceUtility.getInstance(LoginAct.this).putString(Constant.USER_ID,data.getResult().getId());
                        Toast.makeText(LoginAct.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                        if(data.getResult().getUserType().equalsIgnoreCase("plumber"))
                        {

                            if(data.getResult().getSelectedLanguages().size()>0)
                            {
                                SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                                SharedPreferenceUtility.getInstance(LoginAct.this).putString(Constant.USER_INFO,"plumber");
                                Intent i = new Intent(LoginAct.this, HomePlumberAct.class);
// set the new task and clear flags
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                            else
                            {
                                startActivity(new Intent(LoginAct.this, ChangeLanguageAct.class).putExtra("from","login"));
                            }

                       }else if(data.getResult().getUserType().equalsIgnoreCase("user"))
                        {
                            SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                            startActivity(new Intent(LoginAct.this, HomeUserAct.class));
                            SharedPreferenceUtility.getInstance(LoginAct.this).putString(Constant.USER_INFO,"user");
                            finish();
                        }

                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(LoginAct.this, data.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResSignIn> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
    
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(LoginAct.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(LoginAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginAct.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_REQUEST);
        } else {
            Log.e("Latittude====",gpsTracker.getLatitude()+"");
            strLat = Double.toString(gpsTracker.getLatitude()) ;
            strLng = Double.toString(gpsTracker.getLongitude()) ;
            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                buildAlertMessageNoGps();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.e("Latittude====", gpsTracker.getLatitude() + "");

                    strLat = Double.toString(gpsTracker.getLatitude());
                    strLng = Double.toString(gpsTracker.getLongitude());

                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        buildAlertMessageNoGps();
                    }

                } else {
                    Toast.makeText(LoginAct.this, LoginAct.this.getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }

    private void getToken() {
        try {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, ""+getString(R.string.fetching_fcm_token_failed), task.getException());
                                return;
                            }
                            // Get new FCM registration token
                            String token = task.getResult();

                            Log.d(TAG, "onComplete: gauravToken:  "+token);


                            deviceToken = token;
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(LoginAct.this, "Error=>" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, please enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}