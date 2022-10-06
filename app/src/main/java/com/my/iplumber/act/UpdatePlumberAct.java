package com.my.iplumber.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.my.iplumber.R;
import com.my.iplumber.act.utility.DataManager;
import com.my.iplumber.act.utility.RealPathUtil;
import com.my.iplumber.act.utility.SharedPreferenceUtility;
import com.my.iplumber.databinding.ActivityUpdatePlumberBinding;
import com.my.iplumber.model.SuccessResGetProfile;
import com.my.iplumber.retrofit.ApiClient;
import com.my.iplumber.retrofit.NetworkAvailablity;
import com.my.iplumber.retrofit.PlumberInterface;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.iplumber.retrofit.Constant.USER_ID;
import static com.my.iplumber.retrofit.Constant.isValidEmail;
import static com.my.iplumber.retrofit.Constant.showToast;

public class UpdatePlumberAct extends AppCompatActivity {

    ActivityUpdatePlumberBinding binding;

    private PlumberInterface apiInterface;

    private SuccessResGetProfile.Result userDetail;
    private static final int MY_PERMISSION_CONSTANT = 5;

    private String firstName;
    private String lastName;
    private String companyName;
    private String linceNumber;
    private String address;
    private String city;
    private String state;
    private String zIpCode;
    private String mobile;
    private String email;
    private String password;
    private String priceVideoCalling;

    private String str_image_path="";

    private static final int REQUEST_CAMERA_PHOTO_ID = 11;
    private static final int REQUEST_CAMERA_PHOTO_ID_WITH = 12;

    private static final int SELECT_PHOTO_ID = 7;
    private static final int SELECT_PHOTO_ID_WITH = 8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_update_plumber);

        apiInterface = ApiClient.getClient().create(PlumberInterface.class);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        if (NetworkAvailablity.checkNetworkStatus(UpdatePlumberAct.this)) {
            getProfile();
        } else {
            Toast.makeText(UpdatePlumberAct.this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.tvCompanyPhoto.setOnClickListener(v ->
                {
                    if(checkPermisssionForReadStorage())
                        showImageSelection(true);
                }
        );

        binding.RRSignUp.setOnClickListener(v -> {
            validation();
        });

    }

    private void validation() {

        firstName=binding.edtFname.getText().toString();
        lastName=binding.edtLname.getText().toString();
        companyName=binding.edtCompanyName.getText().toString();
        linceNumber=binding.edtLinceNumber.getText().toString();
        address=binding.edtAddress.getText().toString();
        city=binding.edtCity.getText().toString();
        state=binding.edtState.getText().toString();
        zIpCode=binding.edtZIpCode.getText().toString();
        mobile=binding.edtMobile.getText().toString();
        email=binding.edtEmail.getText().toString();
        priceVideoCalling=binding.edtPriceVideoCalling.getText().toString();

        if(firstName.equalsIgnoreCase(""))
        {
            Toast.makeText(this,getString( R.string.enter_firs_name), Toast.LENGTH_SHORT).show();

        }else if(lastName.equalsIgnoreCase(""))
        {
            Toast.makeText(this, getString(R.string.enter_last_name), Toast.LENGTH_SHORT).show();
        }else if(companyName.equalsIgnoreCase(""))
        {
            Toast.makeText(this,getString( R.string.enter_company_name), Toast.LENGTH_SHORT).show();
        }else if(linceNumber.equalsIgnoreCase(""))
        {
            Toast.makeText(this, getString(R.string.enter_license), Toast.LENGTH_SHORT).show();

        }else if(address.equalsIgnoreCase(""))
        {
            Toast.makeText(this,getString( R.string.enter_address), Toast.LENGTH_SHORT).show();

        }else if(city.equalsIgnoreCase(""))
        {
            Toast.makeText(this,getString( R.string.enter_ciy), Toast.LENGTH_SHORT).show();

        }else if(state.equalsIgnoreCase(""))
        {
            Toast.makeText(this, getString(R.string.enter_state), Toast.LENGTH_SHORT).show();

        }else if(zIpCode.equalsIgnoreCase(""))
        {
            Toast.makeText(this, getString(R.string.enter_zip_code), Toast.LENGTH_SHORT).show();

        }else if(mobile.equalsIgnoreCase(""))
        {
            Toast.makeText(this, getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show();

        }else if(email.equalsIgnoreCase(""))
        {
            Toast.makeText(this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
        }else if(!isValidEmail(binding.edtEmail.getText().toString().trim()))
        {
            Toast.makeText(this, getString(R.string.enter_valid), Toast.LENGTH_SHORT).show();
        }else if(priceVideoCalling.equalsIgnoreCase(""))
        {
            Toast.makeText(this, getString(R.string.enter_video_call), Toast.LENGTH_SHORT).show();

        }else
        {
            updateProfile();
        }
    }


    private void getProfile()
    {

        String userId = SharedPreferenceUtility.getInstance(UpdatePlumberAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(UpdatePlumberAct.this,getString(R.string.please_wait));
        Map<String,String> map=new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetProfile> call = apiInterface.getProfile(map);
        call.enqueue(new Callback<SuccessResGetProfile>() {
            @Override
            public void onResponse(Call<SuccessResGetProfile> call, Response<SuccessResGetProfile> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetProfile data= response.body();
                    if(data.status.equalsIgnoreCase("1"))
                    {
                        userDetail  = data.getResult();
                        setUserDetail();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetProfile> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

    private void setUserDetail()
    {

        binding.edtFname.setText(userDetail.getFirstName());
        binding.edtLname.setText(userDetail.getLastName());

        binding.edtCompanyName.setText(userDetail.getCompanyName());

        Glide.with(UpdatePlumberAct.this)
                .load(userDetail.getImage())
                .into(binding.ivCom);

        binding.edtLinceNumber.setText(userDetail.getLicenseNumber()+"");

        binding.edtAddress.setText(userDetail.getAddress());

        binding.edtCity.setText(userDetail.getCity());

        binding.edtState.setText(userDetail.getState());

        binding.edtZIpCode.setText(userDetail.getZipcode());

        binding.ccp.setCountryForPhoneCode(Integer.parseInt(userDetail.getCountryCode()));

        binding.edtMobile.setText(userDetail.getPhone());

        binding.edtEmail.setText(userDetail.getEmail());

        binding.edtBio.setText(userDetail.getBio()+"");

        binding.edtPriceVideoCalling.setText(userDetail.getVideoCallPrice());


    }


    public Bitmap BITMAP_RE_SIZER(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title_" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(UpdatePlumberAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(UpdatePlumberAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(UpdatePlumberAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdatePlumberAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(UpdatePlumberAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(UpdatePlumberAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)

            ) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSION_CONSTANT);
                }

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSION_CONSTANT);
                }
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(UpdatePlumberAct.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        //  showImageSelection();
                    } else {
                        Toast.makeText(UpdatePlumberAct.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UpdatePlumberAct.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public void showImageSelection(boolean from) {

        final Dialog dialog = new Dialog(UpdatePlumberAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialog.findViewById(R.id.layoutGallary);
        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                if(from)
                {
                    openCamera(from);
                }
                else
                {
                    openCamera(from);
                }
            }
        });
        layoutGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                if(from)
                {
                    getPhotoFromGallary(from);
                }
                else
                {
                    getPhotoFromGallary(from);
                }
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getPhotoFromGallary(boolean from) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        if(from)
        {
            startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_PHOTO_ID);
        }
        else
        {
            startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_PHOTO_ID_WITH);
        }
    }

    private void openCamera(boolean from) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(from)
        {
            if (cameraIntent.resolveActivity(UpdatePlumberAct.this.getPackageManager()) != null)
                startActivityForResult(cameraIntent, REQUEST_CAMERA_PHOTO_ID);

        }
        else
        {

            if (cameraIntent.resolveActivity(UpdatePlumberAct.this.getPackageManager()) != null)
                startActivityForResult(cameraIntent, REQUEST_CAMERA_PHOTO_ID_WITH);

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", requestCode + "");
            if (requestCode == SELECT_PHOTO_ID) {
                try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmapNew = MediaStore.Images.Media.getBitmap(UpdatePlumberAct.this.getContentResolver(), selectedImage);
                    Bitmap bitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                    Glide.with(UpdatePlumberAct.this)
                            .load(selectedImage)
                            .centerCrop()
                            .into(binding.ivCom);
                    Uri tempUri = getImageUri(UpdatePlumberAct.this, bitmap);
                    String image = RealPathUtil.getRealPath(UpdatePlumberAct.this, tempUri);
                    str_image_path = image;

                } catch (IOException e) {
                    Log.i("TAG", "Some exception " + e);
                }

            } else if (requestCode == REQUEST_CAMERA_PHOTO_ID) {

                try {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        Bitmap bitmapNew = (Bitmap) extras.get("data");
                        Bitmap imageBitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());

                        Glide.with(UpdatePlumberAct.this)
                                .load(imageBitmap)
                                .centerCrop()
                                .into(binding.ivCom);

                        Uri tempUri = getImageUri(UpdatePlumberAct.this, imageBitmap);
                        String image = RealPathUtil.getRealPath(UpdatePlumberAct.this, tempUri);
                        str_image_path = image;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateProfile()
    {

        String strUserId = SharedPreferenceUtility.getInstance(UpdatePlumberAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(UpdatePlumberAct.this, getString(R.string.please_wait));
        MultipartBody.Part filePart;

        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            if(file!=null)
            {
                filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            }
            else
            {
                filePart = null;
            }
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody fName = RequestBody.create(MediaType.parse("text/plain"), firstName);
        RequestBody lName = RequestBody.create(MediaType.parse("text/plain"), lastName);
        RequestBody cName = RequestBody.create(MediaType.parse("text/plain"), companyName);
        RequestBody email1 = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody address1 = RequestBody.create(MediaType.parse("text/plain"), address);
        RequestBody city1 = RequestBody.create(MediaType.parse("text/plain"),city);
        RequestBody state1 = RequestBody.create(MediaType.parse("text/plain"),state);
        RequestBody zip = RequestBody.create(MediaType.parse("text/plain"),zIpCode);
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"),mobile);
        RequestBody license = RequestBody.create(MediaType.parse("text/plain"),linceNumber);
        RequestBody user_type = RequestBody.create(MediaType.parse("text/plain"), "plumber");
        RequestBody bio = RequestBody.create(MediaType.parse("text/plain"), binding.edtBio.getText().toString());
        RequestBody vPrice = RequestBody.create(MediaType.parse("text/plain"), priceVideoCalling);
        RequestBody cc = RequestBody.create(MediaType.parse("text/plain"),binding.ccp.getSelectedCountryCode());

        Call<ResponseBody> call = apiInterface.updateProfile(id,bio,fName,lName,cName,email1,cc,phone,address1,state1,city1,zip,license,user_type,vPrice,filePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if (data.equals("1")) {

                        String dataResponse = new Gson().toJson(response.body());
                        showToast(UpdatePlumberAct.this, message);
                    } else if (data.equals("0")) {
                        showToast(UpdatePlumberAct.this, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    
}