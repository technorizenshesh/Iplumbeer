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
import com.my.iplumber.act.PaymentScreenAct;
import com.my.iplumber.act.PlumDetailsUserAct;
import com.my.iplumber.model.HomeModel;
import com.my.iplumber.model.SuccessResPurchasedCallsHistory;

import java.util.ArrayList;


public class BookingRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<SuccessResPurchasedCallsHistory.Result> modelList;
    private OnItemClickListener mItemClickListener;

    public BookingRecyclerViewAdapter(Context context, ArrayList<SuccessResPurchasedCallsHistory.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_booking_history, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {


            TextView tvRebook = holder.itemView.findViewById(R.id.txtViewDetails);
            TextView tvPLumberName = holder.itemView.findViewById(R.id.txtName);
            TextView tvVideoPermin = holder.itemView.findViewById(R.id.tvVideoPermin);
            TextView tvTime = holder.itemView.findViewById(R.id.tvTime);
            TextView tvDate = holder.itemView.findViewById(R.id.tvDate);

            ImageView imgProfile = holder.itemView.findViewById(R.id.imgPlumber);

            tvPLumberName.setText(modelList.get(position).getPlumberDetails().get(0).getFirstName()+" "+modelList.get(position).getPlumberDetails().get(0).getLastName());

            tvVideoPermin.setText("$ "+modelList.get(position).getPlumberDetails().get(0).getVideoCallPrice()+mContext.getString(R.string.per_video_call));

            tvDate.setText(mContext.getString(R.string.datecol)+" "+modelList.get(position).getPurchaseDate());

            tvTime.setText(mContext.getString(R.string.timecol)+" "+modelList.get(position).getPurchaseTime());

            tvRebook.setOnClickListener(view ->
                    {
                        mContext.startActivity(new Intent(mContext, PaymentScreenAct.class).putExtra("id",modelList.get(position).getPlumberId()).putExtra("price",modelList.get(position).getPlumberDetails().get(0).getVideoCallPrice()).putExtra("from","user"));
                    }
                    );

            Glide.with(mContext)
                    .load(modelList.get(position).getPlumberDetails().get(0).getImage())
                    .into(imgProfile);

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

        public ViewHolder(final View itemView) {
            super(itemView);
        }
    }

}

