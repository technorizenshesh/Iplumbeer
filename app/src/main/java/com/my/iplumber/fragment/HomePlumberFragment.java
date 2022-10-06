package com.my.iplumber.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.my.iplumber.R;
import com.my.iplumber.adapter.HomeUserRecyclerViewAdapter;
import com.my.iplumber.databinding.FragmentHomeUserBinding;
import com.my.iplumber.model.HomeModel;

import java.util.ArrayList;

public class HomePlumberFragment extends Fragment {

    FragmentHomeUserBinding binding;

    private ArrayList<HomeModel> modelList = new ArrayList<>();
    HomeUserRecyclerViewAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_plumber, container, false);

//        setAdapter();

        return binding.getRoot();

    }


//    private void setAdapter() {
//
//        this.modelList.add(new HomeModel("Corn"));
//        this.modelList.add(new HomeModel("Tomotoes"));
//
//        mAdapter = new HomeUserRecyclerViewAdapter(getActivity(),modelList);
//        binding.recyclerall.setHasFixedSize(true);
//        binding.recyclerall.setLayoutManager(new LinearLayoutManager(getActivity()));
//        binding.recyclerall.setAdapter(mAdapter);
//
//        mAdapter.SetOnItemClickListener(new HomeUserRecyclerViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position, HomeModel model) {
//
//            }
//        });
//    }


}