package com.my.iplumber.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.my.iplumber.R;
import com.my.iplumber.act.PaymentScreenAct;
import com.my.iplumber.act.PlumDetailsUserAct;
import com.my.iplumber.model.HomeModel;
import com.my.iplumber.model.SuccessResGetPlans;

import java.util.ArrayList;

public class PlumberPlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<SuccessResGetPlans.Result> planList;

    public PlumberPlanAdapter(Context context, ArrayList<SuccessResGetPlans.Result> planList) {
        this.mContext = context;
        this.planList = planList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.plan_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        TextView tvPrice = holder.itemView.findViewById(R.id.tv_price0);
        TextView tvTYpe = holder.itemView.findViewById(R.id.tvType0);
        TextView tvDescription = holder.itemView.findViewById(R.id.tvDescription0);
        TextView tvNormalCalls = holder.itemView.findViewById(R.id.tvNormalCalls0);
        TextView tvCount = holder.itemView.findViewById(R.id.tvCount);
        RelativeLayout btnPay = holder.itemView.findViewById(R.id.btnPay0);
        RelativeLayout rlPrice = holder.itemView.findViewById(R.id.rlPrice);
        
        tvPrice.setText(planList.get(position).getAmount());

        tvTYpe.setText(planList.get(position).getTitle());

        tvDescription.setText(planList.get(position).getDescription());

        tvNormalCalls.setText(mContext.getResources().getString(R.string.normal_calls_are)+" "+planList.get(position).getNormalCalls());

        tvCount.setText(mContext.getResources().getString(R.string.additional_calls_are)+" "+planList.get(position).getAdditionalCalls());

        if(position==0)
        {
            rlPrice.setBackground(mContext.getResources().getDrawable(R.drawable.border_subscription));
            btnPay.setBackground(mContext.getResources().getDrawable(R.drawable.border_mix_btn));
        } else if(position==1)
        {
            rlPrice.setBackground(mContext.getResources().getDrawable(R.drawable.border_subscription_one));
            btnPay.setBackground(mContext.getResources().getDrawable(R.drawable.border_mix_btn_one));
        } else if(position == 2)
        {
            rlPrice.setBackground(mContext.getResources().getDrawable(R.drawable.border_subscription_two));
            btnPay.setBackground(mContext.getResources().getDrawable(R.drawable.border_mix_btn_two));
        } else if(position == 3)
        {
            rlPrice.setBackground(mContext.getResources().getDrawable(R.drawable.border_subscription_three));
            btnPay.setBackground(mContext.getResources().getDrawable(R.drawable.border_mix_btn_three));
        }

        btnPay.setOnClickListener(view ->
                {
                    mContext.startActivity(new Intent(mContext, PaymentScreenAct.class).putExtra("id",planList.get(position).getId()).putExtra("price",planList.get(position).getAmount()).putExtra("from","Plumber"));
                }
                );

    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(final View itemView) {
            super(itemView);
        }
    }

}

