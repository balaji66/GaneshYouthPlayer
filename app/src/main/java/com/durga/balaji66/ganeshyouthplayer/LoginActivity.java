package com.durga.balaji66.ganeshyouthplayer;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.durga.balaji66.ganeshyouthplayer.Apis.APIUrl;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText mPhoneNumber, mPassword;
    private Button mSignIn;
    private TextView mNewRegister;
    private TextView mForgotPassword;
    private static final int PERMISSION_REQUEST_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
        initializeListeners();
        if (!checkPermission()) {

            requestPermission();

        }
        if (!new UserPerfManager(this).isUserLogOut()) {
            startHomeActivity();
        }

    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{INTERNET}, PERMISSION_REQUEST_CODE);

    }


    public void startHomeActivity() {
        Intent accountsIntent = new Intent(LoginActivity.this, GamesListActivity.class);
        accountsIntent.putExtra("Mobile", mPhoneNumber.getText().toString().trim());
        startActivity(accountsIntent);
        finish();
    }
    public void initializeViews()
    {
        mPhoneNumber =(TextInputEditText)findViewById(R.id.textInputEditTextPhone);
        mPassword    =(TextInputEditText)findViewById(R.id.textInputEditTextPassword);
        mSignIn      =(Button)findViewById(R.id.buttonLogin);
        mNewRegister =(TextView)findViewById(R.id.textViewNewRegister);
        mForgotPassword =(TextView)findViewById(R.id.textViewForgotPassword);
    }
    public void initializeListeners()
    {
        mSignIn.setOnClickListener(this);
        mNewRegister.setOnClickListener(this);
        mForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.buttonLogin:
                if(inputValidation())
                {
                 signIn();
                }
                break;
            case R.id.textViewNewRegister:
                Intent intentRegisterActivity = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intentRegisterActivity);
                finish();
                break;

            case R.id.textViewForgotPassword:
                Intent intentForgotPasswordActivity = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intentForgotPasswordActivity);
                break;
        }
    }

    public boolean inputValidation()
    {
        if(mPhoneNumber.getText().toString().equals(""))
        {
            mPhoneNumber.setError("Mobile Number Must Not Be Empty");
        }
        else if(mPhoneNumber.getText().toString().length() > 10 || mPhoneNumber.getText().toString().length() <10 )
        {
            mPhoneNumber.setError("Enter Valid 10 digit Mobile Number");
        }
        else if(mPassword.getText().toString().equals(""))
        {
            mPassword.setError("Password Must Not be Empty");
        }
        else
        {
            return true;
        }
        return false;

    }

    public void signIn()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String email = mPhoneNumber.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        Call<ResponseBody> call = APIUrl.getmInstance().getApi().candidateLogin(email, password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200)
                {
                    progressDialog.dismiss();
                    attemptLoginActivity();

                     }
                else if( response.code() == 401)
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Invalid Mobile Number or password",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Check After Some Time",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                final AlertDialog alertDialog =new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("No Internet");
                alertDialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
                alertDialog.setIcon(R.drawable.ic_no_internet);
                alertDialog.setCancelable(false);
                alertDialog.setMessage("check Internet Connection");
                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                Toast.makeText(getApplicationContext(),"Check Your Internet Connection",Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void attemptLoginActivity() {
        String email = mPhoneNumber.getText().toString();
        String password = mPassword.getText().toString();
        saveLoginDetails(email, password);
        startHomeActivity();
    }
    public void saveLoginDetails(String email, String password) {
        new UserPerfManager(this).saveLoginDetails(email, password);
    }

}
