package com.durga.balaji66.ganeshyouthplayer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mSignUp;
    private EditText mFirstName, mLastName, mFatherName, mMobileNumber, mPassword, mConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        alertDialog();

    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void alertDialog()
    {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Alert Dialog");
        builder.setCancelable(false);
        builder.setMessage("Are you Sure your age under 20 years");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initializeViews();
                initializeListeners();
                  }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentLoginActivity =new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intentLoginActivity);
                        finish();
                    }
                });
        builder.show();
    }

    public void initializeViews()
    {
        mSignUp =(Button)findViewById(R.id.buttonSignUp);
        mFirstName =(EditText)findViewById(R.id.editTextFirstName);
        mLastName =(EditText)findViewById(R.id.editTextLastName);
        mFatherName =(EditText)findViewById(R.id.editTextFatherName);
        mMobileNumber =(EditText)findViewById(R.id.editTextMobileNumber);
        mPassword =(EditText)findViewById(R.id.editTextPassword);
        mConfirmPassword=(EditText)findViewById(R.id.editTextConfirmPassword);
    }
    public void initializeListeners()
    {
        mSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonSignUp:
                if(inputValidation())
                {
                    //Toast.makeText(getApplicationContext(),"hai",Toast.LENGTH_LONG).show();
                    retrofitSignUpCode();
                }
                break;
        }
    }

    public void retrofitSignUpCode() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //getting editText values and storing in variables
        String fName = mFirstName.getText().toString().trim();
        String name =fName +" " + mLastName.getText().toString().trim();
        String fatherName = mFatherName.getText().toString().trim();
        String phone = mMobileNumber.getText().toString().trim();
        String password = mPassword.getText().toString().trim();


        Call<ResponseBody> call = APIUrl.getmInstance().getApi().newPlayerRegistration(name, fatherName, phone, password);
        //calling the api
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200)
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_LONG).show();
                    attemptLoginActivity();
                }
                else if(response.code() == 401)
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Mobile Number Already Exist, Please Login",Toast.LENGTH_LONG).show();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Please Check After Some Time",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                final android.app.AlertDialog alertDialog =new android.app.AlertDialog.Builder(RegisterActivity.this).create();
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
            }
        });
    }

    public void attemptLoginActivity() {
        String mobile = mMobileNumber.getText().toString();
        String password = mPassword.getText().toString();

            saveLoginDetails(mobile, password);
            startHomeActivity();

    }
    public void startHomeActivity() {
        Intent accountsIntent = new Intent(RegisterActivity.this, GamesListActivity.class);
        accountsIntent.putExtra("Mobile", mMobileNumber.getText().toString().trim());
        startActivity(accountsIntent);
        finish();
    }
    public void saveLoginDetails(String mobile, String password) {
        new UserPerfManager(this).saveLoginDetails(mobile, password);
    }

    public boolean inputValidation()
    {
        if(mFirstName.getText().toString().equals(""))
        {
            mFirstName.setError("First Name Must not be Empty");
        }
        else if(mLastName.getText().toString().equals(""))
        {
            mLastName.setError("Last Name Must not be Empty");
        }
        else if(mFatherName.getText().toString().equals(""))
        {
            mFatherName.setError("Father Name Must not be Empty");
        }
        else if(mMobileNumber.getText().toString().equals(""))
        {
            mMobileNumber.setError("Mobile Number Must not be Empty");
        }
        else if(mMobileNumber.getText().toString().length() >10 || mMobileNumber.getText().toString().length() <10)
        {
         mMobileNumber.setError("Enter Valid 10 digit Mobile Number");
        }
        else if(mPassword.getText().toString().equals(""))
        {
            mPassword.setError("Password field Must not be Empty");
        }
        else if(!mConfirmPassword.getText().toString().equals(mPassword.getText().toString()))
        {
            mConfirmPassword.setError("Password and Confirm Password Should match");
        }
        else
        {
            return true;
        }
        return false;



    }

}
