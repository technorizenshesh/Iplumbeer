package com.my.iplumber.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.my.iplumber.R;
import com.my.iplumber.act.ChangeLanguageAct;
import com.my.iplumber.act.SelectPlumberLogin;
import com.my.iplumber.act.SettingAct;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.adapter.HomeUserRecyclerViewAdapter;
import com.my.iplumber.databinding.FragmentHomeProfileBinding;
import com.my.iplumber.databinding.FragmentHomeUserBinding;
import com.my.iplumber.model.HomeModel;
import com.my.iplumber.model.SuccessResGetProfile;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.Constant;
import com.my.iplumber.retrofit.NetworkAvailablity;
import com.my.iplumber.retrofit.PlumberInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.iplumber.retrofit.Constant.USER_ID;

public class ProfileUserFragment extends Fragment {

    FragmentHomeProfileBinding binding;
    private ArrayList<HomeModel> modelList = new ArrayList<>();
    HomeUserRecyclerViewAdapter mAdapter;
    private PlumberInterface apiInterface;
    private SuccessResGetProfile.Result userDetail;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_profile, container, false);
        apiInterface = ApiClient.getClient().create(PlumberInterface.class);
        binding.txtSetting.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SettingAct.class).putExtra("Type","User"));
        });

        binding.rlLang.setOnClickListener(view ->
                {
                    startActivity(new Intent(getActivity(), ChangeLanguageAct.class).putExtra("from","language"));
                }
                );

        binding.RRPay.setOnClickListener(view ->
                {
                    SharedPreferenceUtility.getInstance(getActivity().getApplicationContext()).putBoolean(Constant.IS_USER_LOGGED_IN, false);
                    Intent intent = new Intent(getActivity(),
                            SelectPlumberLogin.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }
                );

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
            getProfile();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

    }

    private void getProfile()
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));
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

        binding.tvUserName.setText(userDetail.getFirstName()+" "+userDetail.getLastName());

        binding.tvAddress.setText(userDetail.getAddress());

        binding.tvCity.setText(userDetail.getCity());

        binding.tvState.setText(userDetail.getState());

        binding.tvZipcode.setText(userDetail.getZipcode());

        binding.tvNumber.setText(userDetail.getCountryCode()+"- "+userDetail.getPhone());

        binding.tvEmail.setText(userDetail.getEmail());

        Glide.with(getActivity())
                .load(userDetail.getImage())
                .into(binding.ivProfile);

    }

}