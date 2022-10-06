package com.my.iplumber.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.my.iplumber.R;
import com.my.iplumber.act.UpdatePlumberAct;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.GPSTracker;
import com.my.iplumber.act.utility.PlumberNotifyStatus;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.adapter.HomeUserRecyclerViewAdapter;
import com.my.iplumber.adapter.PlumberPlanAdapter;
import com.my.iplumber.databinding.FragmentHomeUserBinding;
import com.my.iplumber.model.HomeModel;
import com.my.iplumber.model.SuccessResNotifyMe;
import com.my.iplumber.model.SuccessResGetPlumber;
import com.my.iplumber.model.SuccessResGetPlumbers;
import com.my.iplumber.model.SuccessResGetPlumbers;
import com.my.iplumber.model.SuccessResNotifyMe;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.Constant;
import com.my.iplumber.retrofit.PlumberInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.my.iplumber.retrofit.Constant.USER_ID;
import static com.my.iplumber.retrofit.Constant.showToast;

public class HomeUserFragment extends Fragment implements PlumberNotifyStatus {
    
    FragmentHomeUserBinding binding;
    private GPSTracker gpsTracker;
    private String strLat="";
    private String strLng="";

    private ArrayList<SuccessResGetPlumbers.Result> plumberList = new ArrayList<>();

    private PlumberInterface apiInterface;

    private ArrayList<HomeModel> modelList = new ArrayList<>();
    HomeUserRecyclerViewAdapter mAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_user, container, false);
        gpsTracker = new GPSTracker(getActivity());
        apiInterface = ApiClient.getClient().create(PlumberInterface.class);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    private void getPlumbers()
    {
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        //22.7028646    75.8716508

        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));
        Map<String,String> map=new HashMap<>();
        map.put("lat",strLat);
        map.put("lon",strLng);
        map.put("user_id",userId);

        Call<SuccessResGetPlumbers> call = apiInterface.getPlumbers(map);
        call.enqueue(new Callback<SuccessResGetPlumbers>() {
            @Override
            public void onResponse(Call<SuccessResGetPlumbers> call, Response<SuccessResGetPlumbers> response) {

                DataManager.getInstance().hideProgressMessage();

                Log.e("Latittude====",gpsTracker.getLatitude()+""+response);

                try {

                    SuccessResGetPlumbers data= response.body();
                    if(data.status.equalsIgnoreCase("1"))
                    {
                        plumberList.clear();
                        plumberList.addAll(data.getResult());
                        mAdapter = new HomeUserRecyclerViewAdapter(getActivity(),plumberList,HomeUserFragment.this);
                        binding.recyclerall.setHasFixedSize(true);
                        binding.recyclerall.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.recyclerall.setAdapter(mAdapter);

                        mAdapter.SetOnItemClickListener(new HomeUserRecyclerViewAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, HomeModel model) {
                            }
                        });
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetPlumbers> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_REQUEST);
        } else {
            Log.e("Latittude====",gpsTracker.getLatitude()+"");
             strLat = Double.toString(gpsTracker.getLatitude());
             strLng = Double.toString(gpsTracker.getLongitude());

            getPlumbers();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            if (requestCode == Constant.GPS_REQUEST) {
//                isGPS = true; // flag maintain before get location
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.e("Latittude====", gpsTracker.getLatitude() + "");

                    strLat = Double.toString(gpsTracker.getLatitude()) ;
                    strLng = Double.toString(gpsTracker.getLongitude()) ;
                    getPlumbers();

//                    if (isContinue) {
//                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                    } else {
//                        Log.e("Latittude====", gpsTracker.getLatitude() + "");
//
//                        strLat = Double.toString(gpsTracker.getLatitude()) ;
//                        strLng = Double.toString(gpsTracker.getLongitude()) ;
//
//                    }
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }

    @Override
    public void plumberNotification(int position,String status) {
        SuccessResGetPlumbers.Result plumber = plumberList.get(position);
        updateNotifyMe(plumber.getId(),status);
    }

    private void updateNotifyMe(String plumberId,String status)
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));
        Map<String,String> map=new HashMap<>();
        map.put("user_id",userId);
        map.put("plumber_id",plumberId);
        map.put("notify",status);

        Call<SuccessResNotifyMe> call = apiInterface.updateNotify(map);
        call.enqueue(new Callback<SuccessResNotifyMe>() {
            @Override
            public void onResponse(Call<SuccessResNotifyMe> call, Response<SuccessResNotifyMe> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResNotifyMe data= response.body();
                    if(data.status.equalsIgnoreCase("1"))
                    {
                        showToast(getActivity(),data.result);
                    } else if(data.status.equalsIgnoreCase("0"))
                    {
                        showToast(getActivity(),data.result);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResNotifyMe> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }



}