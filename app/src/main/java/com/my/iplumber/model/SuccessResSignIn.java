package com.my.iplumber.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ravindra Birla on 16,May,2022
 */

public class SuccessResSignIn implements Serializable {

    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

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

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("phone")
        @Expose
        public String phone;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("company_name")
        @Expose
        public String companyName;
        @SerializedName("country_code")
        @Expose
        public String countryCode;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("bio")
        @Expose
        public String bio;
        @SerializedName("state")
        @Expose
        public String state;
        @SerializedName("city")
        @Expose
        public String city;
        @SerializedName("zipcode")
        @Expose
        public String zipcode;
        @SerializedName("license_number")
        @Expose
        public String licenseNumber;
        @SerializedName("user_type")
        @Expose
        public String userType;
        @SerializedName("video_call_price")
        @Expose
        public String videoCallPrice;
        @SerializedName("register_id")
        @Expose
        public String registerId;
        @SerializedName("social_id")
        @Expose
        public String socialId;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("otp")
        @Expose
        public String otp;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("online_status")
        @Expose
        public String onlineStatus;
        @SerializedName("holder_name")
        @Expose
        public String holderName;
        @SerializedName("card_number")
        @Expose
        public String cardNumber;
        @SerializedName("expiry_year")
        @Expose
        public String expiryYear;
        @SerializedName("expiry_month")
        @Expose
        public String expiryMonth;
        @SerializedName("cvc_code")
        @Expose
        public String cvcCode;
        @SerializedName("card_default_status")
        @Expose
        public String cardDefaultStatus;
        @SerializedName("remaining_calls")
        @Expose
        public String remainingCalls;
        @SerializedName("language")
        @Expose
        public String language;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("selected_languages")
        @Expose
        public List<SelectedLanguage> selectedLanguages = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getLicenseNumber() {
            return licenseNumber;
        }

        public void setLicenseNumber(String licenseNumber) {
            this.licenseNumber = licenseNumber;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getVideoCallPrice() {
            return videoCallPrice;
        }

        public void setVideoCallPrice(String videoCallPrice) {
            this.videoCallPrice = videoCallPrice;
        }

        public String getRegisterId() {
            return registerId;
        }

        public void setRegisterId(String registerId) {
            this.registerId = registerId;
        }

        public String getSocialId() {
            return socialId;
        }

        public void setSocialId(String socialId) {
            this.socialId = socialId;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOnlineStatus() {
            return onlineStatus;
        }

        public void setOnlineStatus(String onlineStatus) {
            this.onlineStatus = onlineStatus;
        }

        public String getHolderName() {
            return holderName;
        }

        public void setHolderName(String holderName) {
            this.holderName = holderName;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getExpiryYear() {
            return expiryYear;
        }

        public void setExpiryYear(String expiryYear) {
            this.expiryYear = expiryYear;
        }

        public String getExpiryMonth() {
            return expiryMonth;
        }

        public void setExpiryMonth(String expiryMonth) {
            this.expiryMonth = expiryMonth;
        }

        public String getCvcCode() {
            return cvcCode;
        }

        public void setCvcCode(String cvcCode) {
            this.cvcCode = cvcCode;
        }

        public String getCardDefaultStatus() {
            return cardDefaultStatus;
        }

        public void setCardDefaultStatus(String cardDefaultStatus) {
            this.cardDefaultStatus = cardDefaultStatus;
        }

        public String getRemainingCalls() {
            return remainingCalls;
        }

        public void setRemainingCalls(String remainingCalls) {
            this.remainingCalls = remainingCalls;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public List<SelectedLanguage> getSelectedLanguages() {
            return selectedLanguages;
        }

        public void setSelectedLanguages(List<SelectedLanguage> selectedLanguages) {
            this.selectedLanguages = selectedLanguages;
        }

    }

    public class SelectedLanguage {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("language_image")
        @Expose
        public String languageImage;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getLanguageImage() {
            return languageImage;
        }

        public void setLanguageImage(String languageImage) {
            this.languageImage = languageImage;
        }

    }

}
