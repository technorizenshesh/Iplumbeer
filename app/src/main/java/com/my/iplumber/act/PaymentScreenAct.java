package com.my.iplumber.act;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.gson.Gson;
import com.my.iplumber.R;
import com.my.iplumber.VideoCallingAct;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.adapter.BookingRecyclerViewAdapter;
import com.my.iplumber.adapter.PaymentCardRecyclerViewAdapter;
import com.my.iplumber.databinding.ActivityPaymentScreenBinding;
import com.my.iplumber.model.HomeModel;
import com.my.iplumber.model.PaymentModel;
import com.my.iplumber.model.SuccessResAddCard;
import com.my.iplumber.model.SuccessResGetCard;
import com.my.iplumber.model.SuccessResGetPurchaseCall;
import com.my.iplumber.model.SuccessResGetPurchaseCall;
import com.my.iplumber.model.SuccessResGetPurchasePlan;
import com.my.iplumber.model.SuccessResGetToken;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.PlumberInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.my.iplumber.retrofit.Constant.USER_ID;
import static com.my.iplumber.retrofit.Constant.showToast;

public class PaymentScreenAct extends AppCompatActivity {

    ActivityPaymentScreenBinding binding;
    PaymentCardRecyclerViewAdapter mAdapter;
    private ArrayList<PaymentModel> modelList = new ArrayList<>();
    private PlumberInterface apiInterface;
    String cardNo ="",expirationMonth="",expirationYear="",cvv = "",holderName="";
    private String useCardAsDefault="";
    private ArrayList<SuccessResGetCard.Result> cardList = new ArrayList<>();
    private Dialog dialog;
    private String videoCallPrice="",from="";
    private Context mContext;
    private int selectedPosition = -1;
    private String token="";
    private String cardNum ="",year = "",month = "",cvc = "";

    private String planId="",amount="",plumberId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_payment_screen);
        apiInterface = ApiClient.getClient().create(PlumberInterface.class);
        mContext = PaymentScreenAct.this;

        binding.RRback.setOnClickListener(view -> finish());

        videoCallPrice = getIntent().getExtras().getString("price");
        from = getIntent().getExtras().getString("from");

        if(from.equalsIgnoreCase("Plumber"))
        {
            amount = getIntent().getExtras().getString("price");
            planId = getIntent().getExtras().getString("id");
        }
        else
        {
            amount = getIntent().getExtras().getString("price");
            plumberId = getIntent().getExtras().getString("id");
        }

        setAdapter();

        getCards();

        binding.RBook.setOnClickListener(v -> {
            if(selectedPosition!=-1)
            {
                cardNum = cardList.get(selectedPosition).getCardNo();
                year = cardList.get(selectedPosition).getExpYear();
                month = cardList.get(selectedPosition).getExpMonth();
                getCvv();
            }else
            {
                showToast(PaymentScreenAct.this,getString(R.string.select_card));
            }
        });

        binding.tvPayNow.setText(getString(R.string.priceStatus)+ " $"+videoCallPrice);
        binding.add.setOnClickListener(view ->
                {
                    fullScreenDialog();
                }
                );
    }

    private void setAdapter() {

        mAdapter = new PaymentCardRecyclerViewAdapter(PaymentScreenAct.this, cardList, new PaymentCardRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,String status) {
                if(status.equalsIgnoreCase("1"))
                {
                    selectedPosition = -1;
                }
                updateDefaultCard(cardList.get(position).getId(),status);
            }

            @Override
            public void setSelected(int position) {
                selectedPosition = position;
            }
        });
        binding.recyclePaymentCart.setHasFixedSize(true);
        binding.recyclePaymentCart.setLayoutManager(new LinearLayoutManager(PaymentScreenAct.this));
        binding.recyclePaymentCart.setAdapter(mAdapter);
    }

    private void getCvv()
    {

        final Dialog dialog = new Dialog(PaymentScreenAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_add_cvv);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        ImageView ivCancel = dialog.findViewById(R.id.cancel);
        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        EditText editTextCvv = dialog.findViewById(R.id.etCvv);

        AppCompatButton appCompatButton = dialog.findViewById(R.id.btnLogin);

        appCompatButton.setOnClickListener(v ->
                {
                    if(editTextCvv.getText().toString().equalsIgnoreCase("") || editTextCvv.getText().toString().length()!=3)
                    {
                        Toast.makeText(PaymentScreenAct.this,"Please Enter a valid cvv",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        cvv = editTextCvv.getText().toString();
                        getToken();
                        dialog.dismiss();
                    }
                }
        );
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void getToken()
    {
        DataManager.getInstance().showProgressMessage(PaymentScreenAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("card_number",cardNum);
        map.put("expiry_year",year);
        map.put("expiry_month",month);
        map.put("cvc_code",cvv);

        Call<SuccessResGetToken> call = apiInterface.getToken(map);
        call.enqueue(new Callback<SuccessResGetToken>() {
            @Override
            public void onResponse(Call<SuccessResGetToken> call, Response<SuccessResGetToken> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetToken data = response.body();
                    if (data.status == 1) {

                        Log.d(TAG, "onResponse: "+token);

                        token = data.getResult().getId();

                        if(token==null)
                        {
                            showToast(PaymentScreenAct.this,"Invalid card details.");
                        }
                        else
                        {

                            if(from.equalsIgnoreCase("Plumber"))
                            {
                                purchasePlan(token);
                            }
                            else
                            {
                                purchaseCall(token);
                            }
//                            callPaymentApi(token);
                        }

                    } else {
                        showToast(PaymentScreenAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetToken> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void fullScreenDialog() {
        dialog = new Dialog(PaymentScreenAct.this, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_add_card);
        AppCompatButton btnAdd =  dialog.findViewById(R.id.btnAdd);
        ImageView ivBack;
        ivBack = dialog.findViewById(R.id.img_header);
        CardForm cardForm = dialog.findViewById(R.id.card_form);

        MaterialCheckBox checkBox = dialog.findViewById(R.id.defaultCheckBox);

        cardForm.cardRequired(true)
                .maskCardNumber(true)
                .maskCvv(true)
                .expirationRequired(true)
                .cvvRequired(false)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .saveCardCheckBoxChecked(false)
                .saveCardCheckBoxVisible(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
                .actionLabel("Purchase")
                .setup((AppCompatActivity)PaymentScreenAct.this);

        cardForm.setOnCardFormSubmitListener(new OnCardFormSubmitListener() {
            @Override
            public void onCardFormSubmit() {
                //cardForm.getAutofillType();
                Toast.makeText(PaymentScreenAct.this, ""+ cardForm.getLayerType(), Toast.LENGTH_SHORT).show();

                cardForm.getLabelFor();

                cardNo = cardForm.getCardNumber();
                expirationMonth = cardForm.getExpirationMonth();
                expirationYear = cardForm.getExpirationYear();
                cvv = cardForm.getCvv();
                holderName = cardForm.getCardholderName();

                if(cardForm.isValid())
                {
                    useCardAsDefault = "";
                    if(checkBox.isChecked())
                    {
                        useCardAsDefault = "1";
                    }
                    else
                    {
                        useCardAsDefault = "0";
                    }
                    addCardDetails();
                }else
                {
                    cardForm.validate();
                }
            }
        });

        btnAdd.setOnClickListener(v ->
                {

                    cardNo = cardForm.getCardNumber();
                    expirationMonth = cardForm.getExpirationMonth();
                    expirationYear = cardForm.getExpirationYear();
                    cvv = cardForm.getCvv();
                    holderName = cardForm.getCardholderName();
                    if(cardForm.isValid())
                    {

                        useCardAsDefault = "";

                        if(checkBox.isChecked())
                        {
                            useCardAsDefault = "1";
                        }
                        else
                        {
                            useCardAsDefault = "0";
                        }

                        addCardDetails();

                    }else
                    {
                        cardForm.validate();
                    }
                }
        );
        ivBack.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );
        dialog.show();
    }

    private void addCardDetails()
    {

        String userId = SharedPreferenceUtility.getInstance(mContext).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(PaymentScreenAct.this,getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("card_no",cardNo);
        map.put("card_holder_name",holderName);
        map.put("exp_date",expirationMonth+"/"+expirationYear);
        map.put("exp_month",expirationMonth);
        map.put("exp_year",expirationYear);
        map.put("set_default",useCardAsDefault);

        Call<SuccessResAddCard> loginCall = apiInterface.addCard(map);
        loginCall.enqueue(new Callback<SuccessResAddCard>() {
            @Override
            public void onResponse(Call<SuccessResAddCard> call, Response<SuccessResAddCard> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddCard data = response.body();

                    String responseString = new Gson().toJson(response.body());

                    showToast(PaymentScreenAct.this,data.message);

                    dialog.dismiss();

                    getCards();

                    Log.e(TAG,"Test Response :"+responseString);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddCard> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void getCards()
    {

        String userId =  SharedPreferenceUtility.getInstance(mContext).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(PaymentScreenAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetCard> call = apiInterface.getCards(map);
        call.enqueue(new Callback<SuccessResGetCard>() {
            @Override
            public void onResponse(Call<SuccessResGetCard> call, Response<SuccessResGetCard> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetCard data = response.body();

                    if (data.status.equals("1")) {

                        binding.RBook.setVisibility(View.VISIBLE);

                        cardList.clear();
                        cardList.addAll(data.getResult());

                        mAdapter.notifyDataSetChanged();

                    } else {
                        binding.RBook.setVisibility(View.GONE);
                        showToast(PaymentScreenAct.this, data.message);
                        cardList.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetCard> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

    private void updateDefaultCard(String id,String status)
    {

        String userId = SharedPreferenceUtility.getInstance(mContext).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(PaymentScreenAct.this,getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("set_default",status+"");
        map.put("id",id);

        Log.e(TAG,"Test Request "+map);

        Call<SuccessResAddCard> loginCall = apiInterface.updateCard(map);
        loginCall.enqueue(new Callback<SuccessResAddCard>() {
            @Override
            public void onResponse(Call<SuccessResAddCard> call, Response<SuccessResAddCard> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddCard data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    showToast(PaymentScreenAct.this,data.message);
                    getCards();
                    dialog.dismiss();
                    Log.e(TAG,"Test Response :"+responseString);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddCard> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
    
private void purchaseCall(String token)
{

    String userId = SharedPreferenceUtility.getInstance(mContext).getString(USER_ID);

    DataManager.getInstance().showProgressMessage(PaymentScreenAct.this,getString(R.string.please_wait));

    Map<String,String> map = new HashMap<>();
    map.put("user_id",userId);
    map.put("plumber_id",plumberId);
    map.put("calls","1");
    map.put("amount",amount);
    map.put("token",token);
    map.put("currency","USD");

    Log.e(TAG,"Test Request "+map);

    Call<SuccessResGetPurchaseCall> loginCall = apiInterface.purchaseCall(map);
    loginCall.enqueue(new Callback<SuccessResGetPurchaseCall>() {
        @Override
        public void onResponse(Call<SuccessResGetPurchaseCall> call, Response<SuccessResGetPurchaseCall> response) {
            DataManager.getInstance().hideProgressMessage();
            try {

                SuccessResGetPurchaseCall data = response.body();

                String responseString = new Gson().toJson(response.body());

                if(data.status.equalsIgnoreCase("1"))
                {

                    startActivity(new Intent(PaymentScreenAct.this, PaymentSuccessAct.class).putExtra("result",new Gson().toJson(data)));

                }else if(data.status.equalsIgnoreCase("0"))
                {
                    showToast(PaymentScreenAct.this,data.message);
                }

                Log.e(TAG,"Test Response :"+responseString);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG,"Test Response :"+response.body());
            }
        }

        @Override
        public void onFailure(Call<SuccessResGetPurchaseCall> call, Throwable t) {
            call.cancel();
            DataManager.getInstance().hideProgressMessage();
        }
    });
}

    private void purchasePlan(String token)
    {

        String userId = SharedPreferenceUtility.getInstance(mContext).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(PaymentScreenAct.this,getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("plumber_id",userId);
        map.put("plan_id",planId);
        map.put("amount",amount);
        map.put("token",token);
        map.put("currency","USD");

        Log.e(TAG,"Test Request "+map);

        Call<SuccessResGetPurchasePlan> loginCall = apiInterface.purchasePlan(map);
        loginCall.enqueue(new Callback<SuccessResGetPurchasePlan>() {
            @Override
            public void onResponse(Call<SuccessResGetPurchasePlan> call, Response<SuccessResGetPurchasePlan> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResGetPurchasePlan data = response.body();

                    String responseString = new Gson().toJson(response.body());

                    if(data.status.equalsIgnoreCase("1"))
                    {

                        startActivity(new Intent(PaymentScreenAct.this,HomePlumberAct.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                    }else if(data.status.equalsIgnoreCase("0"))
                    {
                        showToast(PaymentScreenAct.this,data.message);
                    }

                    Log.e(TAG,"Test Response :"+responseString);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetPurchasePlan> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }



}