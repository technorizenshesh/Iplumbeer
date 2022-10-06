package com.my.iplumber.retrofit;

import com.my.iplumber.model.SuccessResAddCard;
import com.my.iplumber.model.SuccessResAddReview;
import com.my.iplumber.model.SuccessResGetCard;
import com.my.iplumber.model.SuccessResGetFaqs;
import com.my.iplumber.model.SuccessResGetLanguages;
import com.my.iplumber.model.SuccessResGetNotification;
import com.my.iplumber.model.SuccessResGetPlans;
import com.my.iplumber.model.SuccessResGetPlumber;
import com.my.iplumber.model.SuccessResGetPlumbers;
import com.my.iplumber.model.SuccessResGetProfile;
import com.my.iplumber.model.SuccessResGetPurchaseCall;
import com.my.iplumber.model.SuccessResGetPurchasePlan;
import com.my.iplumber.model.SuccessResGetReview;
import com.my.iplumber.model.SuccessResGetToken;
import com.my.iplumber.model.SuccessResGetUnseenNotification;
import com.my.iplumber.model.SuccessResMakeCall;
import com.my.iplumber.model.SuccessResNotifyMe;
import com.my.iplumber.model.SuccessResPrivacyPolicy;
import com.my.iplumber.model.SuccessResPurchasedCallsHistory;
import com.my.iplumber.model.SuccessResRequestStatus;
import com.my.iplumber.model.SuccessResSignIn;
import com.my.iplumber.model.SuccessResUpdateStatus;

import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PlumberInterface {

    @Multipart
    @POST("signup")
    Call<ResponseBody> signup (
                                              @Part("first_name") RequestBody firstName,
                                              @Part("last_name") RequestBody lastName,
                                              @Part("company_name") RequestBody cName,
                                              @Part("email") RequestBody email,
                                              @Part("password") RequestBody password,
                                              @Part("country_code") RequestBody strcc,
                                              @Part("phone") RequestBody strPhone,
                                              @Part("address") RequestBody address,
                                              @Part("state") RequestBody state,
                                              @Part("city") RequestBody city,
                                              @Part("zipcode") RequestBody zipCode,
                                              @Part("license_number") RequestBody license,
                                              @Part("user_type") RequestBody type,
                                              @Part("video_call_price") RequestBody vPrice,
                                              @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("login")
    Call<SuccessResSignIn> login(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<ResponseBody> forgotPass(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_profile")
    Call<SuccessResGetProfile> getProfile(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("change_password")
    Call<ResponseBody> changePassword(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("update_profile")
    Call<ResponseBody> updateProfile (
            @Part("user_id") RequestBody id,
            @Part("bio") RequestBody bio,
            @Part("first_name") RequestBody firstName,
            @Part("last_name") RequestBody lastName,
            @Part("company_name") RequestBody cName,
            @Part("email") RequestBody email,
            @Part("country_code") RequestBody strcc,
            @Part("phone") RequestBody strPhone,
            @Part("address") RequestBody address,
            @Part("state") RequestBody state,
            @Part("city") RequestBody city,
            @Part("zipcode") RequestBody zipCode,
            @Part("license_number") RequestBody license,
            @Part("user_type") RequestBody type,
            @Part("video_call_price") RequestBody vPrice,
            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("plans")
    Call<SuccessResGetPlans> getPlan(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("userStatus")
    Call<SuccessResUpdateStatus> setOnlineStatus(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("allPlumber")
    Call<SuccessResGetPlumbers> getPlumbers(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("getPlumber")
    Call<SuccessResGetPlumber> getPlumberDetail(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("updateUserLatlon")
    Call<SuccessResGetProfile> updateLatLon(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("add_card")
    Call<SuccessResAddCard> addCard(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("getCard")
    Call<SuccessResGetCard> getCards(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("set_default_card")
    Call<SuccessResAddCard> updateCard(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("update_user_notify")
    Call<SuccessResNotifyMe> updateNotify(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("get_user_notification")
    Call<SuccessResGetNotification> getNotification(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("get_plumber_notification")
    Call<SuccessResGetNotification> getPlumberNotification(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("get_unseen_notification")
    Call<SuccessResGetUnseenNotification> getUnseenNoti(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_unseen_plumber_notification")
    Call<SuccessResGetUnseenNotification> getPlumberUnseenNoti(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_token")
    Call<SuccessResGetToken> getToken(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("purchase_calls")
    Call<SuccessResGetPurchaseCall> purchaseCall(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("purchase_subscription")
    Call<SuccessResGetPurchasePlan> purchasePlan(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("get_user_purchase_calls")
    Call<SuccessResPurchasedCallsHistory> getHistory(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("get_faq")
    Call<SuccessResGetFaqs> getUserFaqs(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_review_rating")
    Call<SuccessResAddReview> addRating(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_plumber_review_rating")
    Call<SuccessResGetReview> getRating(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("privacy_policy")
    Call<SuccessResPrivacyPolicy> getPrivacyPolicy(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("change_language")
    Call<SuccessResRequestStatus> changeLanguage(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_language")
    Call<SuccessResGetLanguages> getLanguage(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("change_language")
    Call<SuccessResRequestStatus> updateLanguage(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST("video_call_invitation")
    Call<SuccessResMakeCall> addNotification(@FieldMap Map<String, String> paramHashMap);



}
