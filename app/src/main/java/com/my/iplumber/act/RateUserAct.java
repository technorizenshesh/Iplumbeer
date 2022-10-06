package com.my.iplumber.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.databinding.ActivityRateUserBinding;
import com.my.iplumber.model.SuccessResAddReview;
import com.my.iplumber.model.SuccessResAddReview;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.PlumberInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.iplumber.retrofit.Constant.USER_ID;

public class RateUserAct extends AppCompatActivity {

    ActivityRateUserBinding binding;

    private String plumberId = "";

    private PlumberInterface apiInterface;

    private String myRating = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_rate_user);

        apiInterface = ApiClient.getClient().create(PlumberInterface.class);

        plumberId = getIntent().getStringExtra("id");

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = binding.ratingBar.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;
                    binding.ratingBar.setRating(stars);

                    myRating = stars+"";

                    v.setPressed(false);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }

                return true;
            }});


        binding.RVideo.setOnClickListener(view ->
                {
                    addReview();
                }
                );

    }

  private void addReview()
  {
      String userId = SharedPreferenceUtility.getInstance(RateUserAct.this).getString(USER_ID);
      DataManager.getInstance().showProgressMessage(RateUserAct.this,getString(R.string.please_wait));
      Map<String,String> map=new HashMap<>();
      map.put("user_id",userId);
      map.put("plumber_id",plumberId);
      map.put("review",binding.etReview.getText().toString());
      map.put("rating",myRating);

      Call<SuccessResAddReview> call = apiInterface.addRating(map);
      call.enqueue(new Callback<SuccessResAddReview>() {
          @Override
          public void onResponse(Call<SuccessResAddReview> call, Response<SuccessResAddReview> response) {

              DataManager.getInstance().hideProgressMessage();

              try {

                  SuccessResAddReview data= response.body();
                  if(data.status.equalsIgnoreCase("1"))
                  {
                      Intent i = new Intent(RateUserAct.this, HomeUserAct.class);
// set the new task and clear flags
                      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                      startActivity(i);
                  }
              }catch (Exception e)
              {
                  e.printStackTrace();
              }
          }

          @Override
          public void onFailure(Call<SuccessResAddReview> call, Throwable t) {

              call.cancel();
              DataManager.getInstance().hideProgressMessage();

          }
      });
  }

}