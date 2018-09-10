package com.durga.balaji66.ganeshyouthplayer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.durga.balaji66.ganeshyouthplayer.Apis.APIUrl;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mMobile, mPassword, mConfirmPassword;
    private Button mReset;
    private Button mButtonSubmit;
    private CardView cardView, cardViewSubmit;
    public static String mStoredMobileNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initializeViews();
        initializeListeners();
        cardView.setVisibility(View.VISIBLE);
        cardViewSubmit.setVisibility(View.GONE);
        mStoredMobileNumber = "";
    }

    public void initializeViews() {
        mButtonSubmit = findViewById(R.id.button_reset_submit);
        mPassword = findViewById(R.id.textinputedittext_reset_password);
        mConfirmPassword = findViewById(R.id.textinputedittext_reset_confirm_password);
        mMobile = findViewById(R.id.edittext_reset_mobile_number);
        mReset = findViewById(R.id.button_reset);
        cardView = findViewById(R.id.card_credentials);
        cardViewSubmit = findViewById(R.id.card_validate);
    }

    public void initializeListeners() {
        mReset.setOnClickListener(this);
        mButtonSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_reset:
                if (validationForMobile()) {
                    checkMobileNumberRegisteredOrNot();
                }
                break;
            case R.id.button_reset_submit:
                if (
                        validationPasswordReset()) {
                    updatePassword();
                }

                break;
        }
    }

    public boolean validationForMobile() {
        if (mMobile.getText().toString().equals("")) {
            mMobile.setError("Mobile Number Must Not Be Empty");
        } else if (mMobile.getText().toString().length() > 10 || mMobile.getText().toString().length() < 10) {
            mMobile.setError("Enter Valid 10 Digit Mobile Number");
        } else {
            return true;
        }
        return false;
    }

    public boolean validationPasswordReset() {
        String pass = mPassword.getText().toString();
        if (mPassword.getText().toString().equals("")) {
            mPassword.setError("Password Must Not Be Empty");
        } else if (!mConfirmPassword.getText().toString().equals(pass)) {
            mConfirmPassword.setError("Password And Confirm Password Should Match");
        } else {
            return true;
        }
        return false;
    }

    public void checkMobileNumberRegisteredOrNot() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String mobile = mMobile.getText().toString().trim();
        mStoredMobileNumber = mobile;
        Call<ResponseBody> call = APIUrl.getmInstance().getApi().checkMobileNumber(mobile);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    cardViewSubmit.setVisibility(View.VISIBLE);
                    cardView.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Enter New Password", Toast.LENGTH_LONG).show();

                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "UnRegistered Mobile Number", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Check After Some Time", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                final AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
                alertDialog.setTitle(getString(R.string.no_internet));
                alertDialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
                alertDialog.setIcon(R.drawable.ic_no_internet);
                alertDialog.setCancelable(false);
                alertDialog.setMessage(getString(R.string.check_internet_connection));
                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                //Toast.makeText(getApplicationContext(),"Check Your Internet Connection",Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updatePassword() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String mobile = mMobile.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        mStoredMobileNumber = mobile;
        Call<ResponseBody> call = APIUrl.getmInstance().getApi().updatePassword(mobile, password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(), "Password Updated Successfully", Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Password Not Updated", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Check After Some Time", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                final AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
                alertDialog.setTitle(getString(R.string.no_internet));
                alertDialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
                alertDialog.setIcon(R.drawable.ic_no_internet);
                alertDialog.setCancelable(false);
                alertDialog.setMessage(getString(R.string.check_internet_connection));
                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                //Toast.makeText(getApplicationContext(),"Check Your Internet Connection",Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
