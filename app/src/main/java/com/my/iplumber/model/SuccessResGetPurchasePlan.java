package com.my.iplumber.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetPurchasePlan implements Serializable {

    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("result")
    @Expose
    public Result result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("plumber_id")
        @Expose
        public String plumberId;
        @SerializedName("plan_id")
        @Expose
        public String planId;
        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("request_id")
        @Expose
        public String requestId;
        @SerializedName("payment_type")
        @Expose
        public String paymentType;
        @SerializedName("token")
        @Expose
        public String token;
        @SerializedName("currency")
        @Expose
        public String currency;
        @SerializedName("date_time")
        @Expose
        public String dateTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlumberId() {
            return plumberId;
        }

        public void setPlumberId(String plumberId) {
            this.plumberId = plumberId;
        }

        public String getPlanId() {
            return planId;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

    }

}
