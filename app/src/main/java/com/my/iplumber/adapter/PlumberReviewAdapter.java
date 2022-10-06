package com.my.iplumber.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.my.iplumber.R;
import com.my.iplumber.model.SuccessResGetFaqs;
import com.my.iplumber.model.SuccessResGetReview;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ravindra Birla on 05,August,2021
 */
public class PlumberReviewAdapter extends RecyclerView.Adapter<PlumberReviewAdapter.SelectTimeViewHolder> {

    ArrayAdapter ad;

    private Context context;

    private  ArrayList<SuccessResGetReview.Result> faqsList ;


    private boolean from ;
    private boolean showAns = false;

    public PlumberReviewAdapter(Context context, ArrayList<SuccessResGetReview.Result> faqsList)
    {
        this.context = context;
        this.faqsList = faqsList;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.review_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {
        

        TextView tvUserName = holder.itemView.findViewById(R.id.tvUserName);
        TextView tvRating = holder.itemView.findViewById(R.id.tvRating);

        TextView tvReview = holder.itemView.findViewById(R.id.tvReview);
        ImageView ivProfile = holder.itemView.findViewById(R.id.imge);

        RatingBar ratingBar = holder.itemView.findViewById(R.id.ratingBar);

        tvUserName.setText(faqsList.get(position).getUsersDetails().get(0).getFirstName()+" "+faqsList.get(position).getUsersDetails().get(0).getLastName());

        tvRating.setText(faqsList.get(position).getRating());

        tvReview.setText(faqsList.get(position).getReview());

        ratingBar.setRating(Float.parseFloat(String.valueOf(faqsList.get(position).getRating())));

        Glide.with(context)
                .load(faqsList.get(position).getUsersDetails().get(0).getImage())
                .into(ivProfile);

    }

    @Override
    public int getItemCount() {
        return faqsList.size();
    }

    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
