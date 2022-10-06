package com.my.iplumber.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.my.iplumber.R;
import com.my.iplumber.model.SuccessResGetFaqs;
import com.my.iplumber.model.SuccessResGetLanguages;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ravindra Birla on 05,August,2021
 */
public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.SelectTimeViewHolder> {

    ArrayAdapter ad;

    private Context context;

    private  ArrayList<SuccessResGetLanguages.Result> faqsList;
    List<String> monthsList = new LinkedList<>();

    private boolean from ;

    private boolean showAns = false;

    public LanguagesAdapter(Context context, ArrayList<SuccessResGetLanguages.Result> faqsList)
    {
        this.context = context;
        this.faqsList = faqsList;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.language_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {

        ImageView imageView = holder.itemView.findViewById(R.id.ivFalf);

        CheckBox checkBox = holder.itemView.findViewById(R.id.my_checkbox);

        String image = faqsList.get(position).getImage();

        Glide.with(context)
                .load(image)
                .into(imageView);

        checkBox.setText(faqsList.get(position).getName());

        if(faqsList.get(position).getLanguageSelected().equalsIgnoreCase("1"))
        {
            checkBox.setChecked(true);
        }
        else
        {
            checkBox.setChecked(false);
        }

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
