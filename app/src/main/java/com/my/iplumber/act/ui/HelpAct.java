package com.my.iplumber.act.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.adapter.FaqsAdapter;
import com.my.iplumber.databinding.ActivityHelpBinding;
import com.my.iplumber.model.SuccessResGetFaqs;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.PlumberInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.iplumber.retrofit.Constant.showToast;

public class HelpAct extends AppCompatActivity {

    ActivityHelpBinding binding;

    private ArrayList<SuccessResGetFaqs.Result> faqsList = new ArrayList<>();

    private PlumberInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_help);
        apiInterface = ApiClient.getClient().create(PlumberInterface.class);

        binding.ivBack.setOnClickListener(view -> finish());

        getFaqs();


    }

    public void getFaqs()

    {

        DataManager.getInstance().showProgressMessage(HelpAct.this, getString(R.string.please_wait));

        Map<String, String> map = new HashMap<>();

        Call<SuccessResGetFaqs> call = apiInterface.getUserFaqs(map);

        call.enqueue(new Callback<SuccessResGetFaqs>() {
            @Override
            public void onResponse(Call<SuccessResGetFaqs> call, Response<SuccessResGetFaqs> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetFaqs data = response.body();
                    if (data.status.equals("1")) {

                        faqsList.clear();
                        faqsList.addAll(data.getResult());

                        binding.rvScheduleTime.setHasFixedSize(true);
                        binding.rvScheduleTime.setLayoutManager(new LinearLayoutManager(HelpAct.this));
                        binding.rvScheduleTime.setAdapter(new FaqsAdapter(HelpAct.this,faqsList));

                    } else {
                        showToast(HelpAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetFaqs> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }

}