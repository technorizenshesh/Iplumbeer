package com.my.iplumber.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.iplumber.R;
import com.my.iplumber.act.PlumDetailsUserAct;
import com.my.iplumber.model.HomeModel;
import com.my.iplumber.model.SuccessResGetNotification;

import java.util.ArrayList;


public class NotoficationRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<SuccessResGetNotification.Result> modelList;
    private OnItemClickListener mItemClickListener;


    public NotoficationRecyclerViewAdapter(Context context, ArrayList<SuccessResGetNotification.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {

            ImageView ivPlumber = holder.itemView.findViewById(R.id.plumberImage);
            TextView tvMessage = holder.itemView.findViewById(R.id.tvMessage);
            TextView tvTimeAgo = holder.itemView.findViewById(R.id.tvTimeAgo);

            Glide.with(mContext)
                    .load(modelList.get(position).getPlumberImage())
                    .into(ivPlumber);

            tvMessage.setText(modelList.get(position).getMessage());

            tvTimeAgo.setText(modelList.get(position).getTimeAgo());

        }
    }

    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }



    public interface OnItemClickListener {

        void onItemClick(View view, int position, HomeModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       TextView txtViewDetails;

        public ViewHolder(final View itemView) {
            super(itemView);

        }
    }


}

