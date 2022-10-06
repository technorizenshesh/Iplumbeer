package com.my.iplumber.act.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.GPSTracker;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.databinding.FragmentHomeBinding;
import com.my.iplumber.model.SuccessResGetProfile;
import com.my.iplumber.model.SuccessResUpdateStatus;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.Constant;
import com.my.iplumber.retrofit.NetworkAvailablity;
import com.my.iplumber.retrofit.PlumberInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.my.iplumber.retrofit.Constant.USER_ID;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private PlumberInterface apiInterface;

    private SuccessResGetProfile.Result userDetail;

    private GPSTracker gpsTracker;
    private String strLat="";
    private String strLng="";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        apiInterface = ApiClient.getClient().create(PlumberInterface.class);

        gpsTracker = new GPSTracker(getActivity());

        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
            getProfile();
            getLocation();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

//        binding.mySwitch.setOnToggledListener(new OnToggledListener() {
//            @Override
//            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
//                if(isOn)
//                {
//                    updateOnlineStatus("1");
//                }
//                else
//                {
//                    updateOnlineStatus("0");
//                }
//            }
//        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

    private void updateLocation()
    {
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));
        Map<String,String> map=new HashMap<>();
        map.put("user_id",userId);
        map.put("lat",strLat);
        map.put("lon",strLng);
        Call<SuccessResGetProfile> call = apiInterface.updateLatLon(map);
        call.enqueue(new Callback<SuccessResGetProfile>() {
            @Override
            public void onResponse(Call<SuccessResGetProfile> call, Response<SuccessResGetProfile> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetProfile data= response.body();
                    if(data.status.equalsIgnoreCase("1"))
                    {
//                        userDetail  = data.getResult();
//                        setUserDetail();
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

        binding.videoCallPrice.setText(getString(R.string.video_call_prices)+ userDetail.getVideoCallPrice());
        binding.profileStatus.setText(getString(R.string.profile_status)+" " +userDetail.getStatus());

        Glide.with(getActivity())
                .load(userDetail.getImage())
                .into(binding.ivProfile);

        binding.tvUserName.setText(userDetail.getFirstName()+" "+userDetail.getLastName());

        binding.numberOfCalls.setText(getString(R.string.number_of_calls_left)+" " +userDetail.getRemainingCalls());

//        if(userDetail.getOnlineStatus().equalsIgnoreCase("0"))
//        {
//            binding.mySwitch.setOn(false);
//        }
//        else
//        {
//            binding.mySwitch.setOn(true);
//        }

        binding.rating.setText(getString(R.string.customer_ratings)+" "+userDetail.getRating());

    }

    private void updateOnlineStatus(String status)
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));
        Map<String,String> map=new HashMap<>();
        map.put("user_id",userId);
        map.put("status",status);
        Call<SuccessResUpdateStatus> call = apiInterface.setOnlineStatus(map);

        call.enqueue(new Callback<SuccessResUpdateStatus>() {
            @Override
            public void onResponse(Call<SuccessResUpdateStatus> call, Response<SuccessResUpdateStatus> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResUpdateStatus data= response.body();
                    if(data.status.equalsIgnoreCase("1"))
                    {

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUpdateStatus> call, Throwable t) {

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
            strLng = Double.toString(gpsTracker.getLongitude()) ;
            updateLocation();
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
                    updateLocation();


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


}