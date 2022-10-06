package com.my.iplumber.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.my.iplumber.R;
import com.my.iplumber.model.HomeModel;
import com.my.iplumber.model.PaymentModel;
import com.my.iplumber.model.SuccessResAddCard;
import com.my.iplumber.model.SuccessResGetCard;

import java.util.ArrayList;

public class PaymentCardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<SuccessResGetCard.Result> modelList;
    private OnItemClickListener mItemClickListener;

    private boolean selected;

    boolean isClick=false;

    private String status="";

    public PaymentCardRecyclerViewAdapter(Context context, ArrayList<SuccessResGetCard.Result> modelList,OnItemClickListener mItemClickListener) {
        this.mContext = context;
        this.modelList = modelList;
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_payment_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            selected = false;
            TextView cardHolderName = holder.itemView.findViewById(R.id.txtmaster);
            TextView tvCardNumber = holder.itemView.findViewById(R.id.tvCardNumber);
            TextView tvDateCvv = holder.itemView.findViewById(R.id.tvDateCvv);
            TextView tvHolderName = holder.itemView.findViewById(R.id.tvHolderName);
            TextView tvBelowCardNumber = holder.itemView.findViewById(R.id.tvBelowCardNumber);
            TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
            ImageView ivDrop = holder.itemView.findViewById(R.id.ivDrop);
            Switch defaultSwitch = holder.itemView.findViewById(R.id.defaultSwitch);
            LinearLayout llAcDetails = holder.itemView.findViewById(R.id.llAcDetails);
            cardHolderName.setText(modelList.get(position).getCardHolderName());
            tvHolderName.setText(modelList.get(position).getCardHolderName());
            tvCardNumber.setText(modelList.get(position).getCardNo());
            tvBelowCardNumber.setText(modelList.get(position).getCardNo());
            tvDate.setText(modelList.get(position).getExpMonth()+"/"+modelList.get(position).getExpYear());

            tvDateCvv.setText("Expiry : "+modelList.get(position).getExpMonth()+"/"+modelList.get(position).getExpYear() );

            ivDrop.setOnClickListener(view ->
                    {
                        if(selected)
                        {
                            llAcDetails.setVisibility(View.GONE);
                            selected =false;
                        } else
                        {
                            llAcDetails.setVisibility(View.VISIBLE);
                            selected =true;
                        }
                    }
                    );

            if(modelList.get(position).getSetDefault().equalsIgnoreCase("0"))
            {
                defaultSwitch.setChecked(false);
            }else
            {
                defaultSwitch.setChecked(true);
                mItemClickListener.setSelected(position);
            }

            defaultSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                     status = "0";
                    if(b)
                    {
                        status="1";
                    } else
                    {
                        status= "0";
                    }

                    View v=new View(mContext);
                    mItemClickListener.onItemClick(v,position,status);

//                    if(status.equalsIgnoreCase("1"))
//                    {
//                        new AlertDialog.Builder(mContext)
//                                .setTitle(mContext.getString(R.string.default_card))
//                                .setMessage(mContext.getString(R.string.update_card_as_default))
//                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // Continue with delete operation
//
//                                        View v=new View(mContext);
//
//                                        mItemClickListener.onItemClick(v,position,status);
//
//                                    }
//                                })
//                                .setNegativeButton(android.R.string.no, null)
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .show();
//
//                    }
//                    else
//                    {
//
//                        new AlertDialog.Builder(mContext)
//                                .setTitle(mContext.getString(R.string.default_card))
//                                .setMessage(mContext.getString(R.string.remove_card_as_default))
//                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // Continue with delete operation
//
//                                        View v=new View(mContext);
//
//                                        mItemClickListener.onItemClick(v,position,status);
//
//                                    }
//                                })
//                                .setNegativeButton(android.R.string.no, null)
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .show();
//
//                    }



                }
            });

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

        void onItemClick(View view, int position,String status);
        void setSelected(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(final View itemView) {
            super(itemView);



        }
    }


}

