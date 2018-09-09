package com.durga.balaji66.ganeshyouthplayer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.durga.balaji66.ganeshyouthplayer.Apis.APIUrl;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalloonActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mBalloon;
    public String mMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gettingMail();
        setContentView(R.layout.activity_balloon);
        initializeViews();
        initializeListeners();
    }

    public void gettingMail() {
        mMobile = new UserPerfManager(this).getMobile();
    }
    public void initializeViews()
    {
        mBalloon=(Button)findViewById(R.id.buttonBalloon);
    }
    public void initializeListeners()
    {
        mBalloon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonBalloon:
                alertDialog();
                break;
        }
    }

    public void alertDialog()
    {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Alert Dialog");
        builder.setCancelable(false);
        builder.setMessage("Are you Sure Want to Register");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                register();
                     }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    public void register()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String game_name ="Carrying Balloon";
        Call<ResponseBody> call = APIUrl.getmInstance().getApi().registerBalloon(mMobile,game_name);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200)
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Successfully Registered For This Game",Toast.LENGTH_LONG).show();

                }
                else if( response.code() == 401)
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Not registered, Try Again After some Time",Toast.LENGTH_LONG).show();
                }
                else if(response.code() == 301)
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"You Already Registered For This Game",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                final android.app.AlertDialog alertDialog =new android.app.AlertDialog.Builder(BalloonActivity.this).create();
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
            }
        });
    }
}
