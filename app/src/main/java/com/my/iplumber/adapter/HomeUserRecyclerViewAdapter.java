package com.my.iplumber.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.iplumber.MainActivity;
import com.my.iplumber.R;
import com.my.iplumber.act.PlumDetailsUserAct;
import com.my.iplumber.act.SelectPlumberLogin;
import com.my.iplumber.act.utility.PlumberNotifyStatus;
import com.my.iplumber.model.HomeModel;
import com.my.iplumber.model.SuccessResGetPlumbers;

import java.util.ArrayList;


public class HomeUserRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<SuccessResGetPlumbers.Result> modelList;
    private OnItemClickListener mItemClickListener;

    private PlumberNotifyStatus plumberNotifyStatus;

    public HomeUserRecyclerViewAdapter(Context context, ArrayList<SuccessResGetPlumbers.Result> modelList,PlumberNotifyStatus plumberNotifyStatus) {
        this.mContext = context;
        this.modelList = modelList;
        this.plumberNotifyStatus = plumberNotifyStatus;
    }

    public void updateList(ArrayList<SuccessResGetPlumbers.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_home, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {

            ImageView imgPlumber = holder.itemView.findViewById(R.id.imgPlumber);
            TextView tvPLumberName = holder.itemView.findViewById(R.id.tvPLumberName);
            TextView tvName = holder.itemView.findViewById(R.id.txtName);
            TextView tvVideoCallRate = holder.itemView.findViewById(R.id.tvCallPerRate);
            TextView tvAddress = holder.itemView.findViewById(R.id.tvAddress);
            TextView tvDistance = holder.itemView.findViewById(R.id.tvDistance);
            TextView txtViewDetails = holder.itemView.findViewById(R.id.txtViewDetails);
            ImageView txtAvailable = holder.itemView.findViewById(R.id.ivAvailable);
            Switch switchAlert = holder.itemView.findViewById(R.id.switchAlert);

            tvName.setText(modelList.get(position).getCompanyName());

            tvPLumberName.setText(modelList.get(position).getFirstName()+ " "+ modelList.get(position).getLastName());

            tvVideoCallRate.setText("$ "+ modelList.get(position).getVideoCallPrice()+mContext.getString(R.string.per_video_call));

            tvAddress.setText(modelList.get(position).getAddress()+", "+modelList.get(position).getCity()+", "+modelList.get(position).getState());

            tvDistance.setText(modelList.get(position).getDistance()+" miles");

            if(modelList.get(position).getNotifyMe().equalsIgnoreCase("1"))
            {
                switchAlert.setChecked(true);
            }
            else
            {
                switchAlert.setChecked(false);
            }

            txtViewDetails.setOnClickListener(view ->
                    {
                        mContext.startActivity(new Intent(mContext, PlumDetailsUserAct.class).putExtra("id",modelList.get(position).getId()));
                    }
                    );

            switchAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(b)
                    {
                        plumberNotifyStatus.plumberNotification(position,"1");
                    }
                    else
                    {
                        plumberNotifyStatus.plumberNotification(position,"0");
                    }

                }
            });

            Glide.with(mContext)
                    .load(modelList.get(position).getImage())
                    .into(imgPlumber);

            if(modelList.get(position).getOnlineStatus().equalsIgnoreCase("1"))
            {
//                txtAvailable.setVisibility(View.VISIBLE);
                txtAvailable.setBackgroundResource(R.drawable.gren_circle);

            }else
            {
                txtAvailable.setBackgroundResource(R.drawable.red_circle);
            }

//            final HomeModel model = getItem(position);
//            final ViewHolder genericViewHolder = (ViewHolder) holder;
//
//            if(position==0)
//            {
//                genericViewHolder.txtAvailable.setVisibility(View.VISIBLE);
//            }else
//            {
//                genericViewHolder.txtAvailable.setVisibility(View.GONE);
//            }
//            genericViewHolder.txtViewDetails.setOnClickListener(v -> {
//                mContext.startActivity(new Intent(mContext, PlumDetailsUserAct.class));
//            });
//
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    genericViewHolder.txtAvailable.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.move_it));
//                }
//            }, 1000);

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

            txtViewDetails=itemView.findViewById(R.id.txtViewDetails);

        }
    }




}

