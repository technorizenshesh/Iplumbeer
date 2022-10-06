package com.my.iplumber.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.my.iplumber.MainActivity;
import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.adapter.LanguagesAdapter;
import com.my.iplumber.databinding.ActivityChangeLanguageBinding;
import com.my.iplumber.model.SuccessResGetLanguages;
import com.my.iplumber.model.SuccessResRequestStatus;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.Constant;
import com.my.iplumber.retrofit.PlumberInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.iplumber.retrofit.Constant.USER_ID;
import static com.my.iplumber.retrofit.Constant.showToast;
import static com.my.iplumber.retrofit.Constant.updateResources;

public class ChangeLanguageAct extends AppCompatActivity {

    ActivityChangeLanguageBinding binding;

    private PlumberInterface apiInterface;

    boolean callApi = false;

    private ArrayList<SuccessResGetLanguages.Result> results = new ArrayList<>();

    private String languages = "";

    private String from="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding = DataBindingUtil.setContentView(this,R.layout.activity_change_language);

      apiInterface = ApiClient.getClient().create(PlumberInterface.class);

      binding.ivBack.setOnClickListener(view -> finish());

       from = getIntent().getExtras().getString("from");

      getLanguages();

      binding.btnNext.setOnClickListener(v ->
              {

                  languages = "";
                  for (int i = 0; i < results.size(); i++) {
                      View view = binding.rvLanuages.getChildAt(i);
                      CheckBox nameEditText = (CheckBox) view.findViewById(R.id.my_checkbox);

                      if(nameEditText.isChecked())
                      {
                          languages = languages + results.get(i).getId()+",";
                      }

                  }

                  if(!languages.equalsIgnoreCase(""))
                  {
                      languages = languages.substring(0, languages.length() -1);
                      updateLanguages(languages);
                  } else
                  {
                      showToast(ChangeLanguageAct.this,getString(R.string.select_language1));
                  }

              }
              );

        Log.d("TAG", "onCreate: test "+languages);

//      if(from.equalsIgnoreCase("language"))
//      {
//          binding.btnNext.setVisibility(View.GONE);
//          callApi = true;
//      }
//      else
//      {
//          binding.btnNext.setVisibility(View.VISIBLE);
//          callApi = false;
//      }


//      String lang =  SharedPreferenceUtility.getInstance(ChangeLanguageAct.this).getString(Constant.LANG);

//        if(lang.equalsIgnoreCase("es"))
//        {
//            updateResources(ChangeLanguageAct.this,"es");
//            binding.radio3.setChecked(true);
//            binding.radio1.setChecked(false);
//        }
//        else
//        {
//            updateResources(ChangeLanguageAct.this,"en");
//            binding.radio3.setChecked(false);
//            binding.radio1.setChecked(true);
//        }
//
//        binding.radio1.setOnClickListener(v ->
//                {
//                    updateResources(ChangeLanguageAct.this,"en");
//                    binding.radio3.setChecked(false);
//                    if(callApi)
//                    {
//                        changeLanguage("en");
//                    }
//
//                    SharedPreferenceUtility.getInstance(ChangeLanguageAct.this).putString(Constant.LANG, "en");
//                }
//        );
//
//        binding.radio3.setOnClickListener(v ->
//                {
//                    updateResources(ChangeLanguageAct.this,"es");
//                    binding.radio1.setChecked(false);
//                    if(callApi)
//                    {
//                        changeLanguage("es");
//                    }
//                    SharedPreferenceUtility.getInstance(ChangeLanguageAct.this).putString(Constant.LANG, "es");
//                }
//        );
    }

    private void changeLanguage(String lang) {

        String  strUserId = SharedPreferenceUtility.getInstance(ChangeLanguageAct.this).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(ChangeLanguageAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();

        map.put("user_id",strUserId);
        map.put("language",lang);

        Call<SuccessResRequestStatus> call = apiInterface.changeLanguage(map);

        call.enqueue(new Callback<SuccessResRequestStatus>() {
            @Override
            public void onResponse(Call<SuccessResRequestStatus> call, Response<SuccessResRequestStatus> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResRequestStatus data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        showToast(ChangeLanguageAct.this, data.message);
                        String dataResponse = new Gson().toJson(response.body());
//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                    } else if (data.status.equals("0")) {
                        showToast(ChangeLanguageAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResRequestStatus> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    private void getLanguages() {

        String  strUserId = SharedPreferenceUtility.getInstance(ChangeLanguageAct.this).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(ChangeLanguageAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",strUserId);

        Call<SuccessResGetLanguages> call = apiInterface.getLanguage(map);

        call.enqueue(new Callback<SuccessResGetLanguages>() {
            @Override
            public void onResponse(Call<SuccessResGetLanguages> call, Response<SuccessResGetLanguages> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetLanguages data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {

                        String dataResponse = new Gson().toJson(response.body());
                        results.clear();
                        results.addAll(data.getResult());

                        binding.rvLanuages.setHasFixedSize(true);
                        binding.rvLanuages.setLayoutManager(new LinearLayoutManager(ChangeLanguageAct.this));
                        binding.rvLanuages.setAdapter(new LanguagesAdapter(ChangeLanguageAct.this,results));

                    } else if (data.status.equals("0")) {
                        showToast(ChangeLanguageAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetLanguages> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });


    }


    private void updateLanguages(String lang) {

        String  strUserId = SharedPreferenceUtility.getInstance(ChangeLanguageAct.this).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(ChangeLanguageAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",strUserId);
        map.put("language",lang);
        Call<SuccessResRequestStatus> call = apiInterface.updateLanguage(map);

        call.enqueue(new Callback<SuccessResRequestStatus>() {
            @Override
            public void onResponse(Call<SuccessResRequestStatus> call, Response<SuccessResRequestStatus> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResRequestStatus data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        showToast(ChangeLanguageAct.this, data.message);
                        String dataResponse = new Gson().toJson(response.body());
                        if(from.equalsIgnoreCase("login"))
                        {
                            SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                            SharedPreferenceUtility.getInstance(ChangeLanguageAct.this).putString(Constant.USER_INFO,"plumber");
                            Intent i = new Intent(ChangeLanguageAct.this, HomePlumberAct.class);
// set the new task and clear flags
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);

                        }

                    } else if (data.status.equals("0")) {
                        showToast(ChangeLanguageAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResRequestStatus> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });


    }


}