package com.my.iplumber.act.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.adapter.PlumberPlanAdapter;
import com.my.iplumber.databinding.FragmentDashboardBinding;
import com.my.iplumber.model.SuccessResGetPlans;
import com.my.iplumber.model.SuccessResGetPlans;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.NetworkAvailablity;
import com.my.iplumber.retrofit.PlumberInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.iplumber.retrofit.Constant.USER_ID;


public class DashboardFragment extends Fragment {

    private PlumberInterface apiInterface;
    private ArrayList<SuccessResGetPlans.Result> planList = new ArrayList<>();

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard,container, false);

        apiInterface = ApiClient.getClient().create(PlumberInterface.class);

        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
            getPlan();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        return binding.getRoot();
    }

    private void getPlan()
    {
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));
        Map<String,String> map=new HashMap<>();
//        map.put("id",userId);

        Call<SuccessResGetPlans> call = apiInterface.getPlan(map);
        call.enqueue(new Callback<SuccessResGetPlans>() {
            @Override
            public void onResponse(Call<SuccessResGetPlans> call, Response<SuccessResGetPlans> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetPlans data= response.body();
                    if(data.status.equalsIgnoreCase("1"))
                    {
                        planList.addAll(data.getResult());
                        binding.rvPlans.setHasFixedSize(true);
                        binding.rvPlans.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvPlans.setAdapter(new PlumberPlanAdapter(getActivity(),planList));

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetPlans> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}