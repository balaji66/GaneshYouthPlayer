package com.durga.balaji66.ganeshyouthplayer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.durga.balaji66.ganeshyouthplayer.Apis.APIUrl;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArrangingAnxietyActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mAnxiety;

    public String mMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arranging_anxiety);
        gettingMail();
        initializeViews();
        initializeListeners();

    }

    public void gettingMail() {
        mMobile = new UserPerfManager(this).getMobile();
    }

    public void initializeViews() {
        mAnxiety = findViewById(R.id.buttonAnxiety);
    }

    public void initializeListeners() {
        mAnxiety.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAnxiety:
                alertDialog();
                break;
        }
    }

    public void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    public void register() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String game_name = "Arranging Anxiety";
        Call<ResponseBody> call = APIUrl.getmInstance().getApi().registerAnxiety(mMobile, game_name);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.successfully_registered_for_this_game, Toast.LENGTH_LONG).show();

                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.not_registered, Toast.LENGTH_LONG).show();
                } else if (response.code() == 301) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.already_registered, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(ArrangingAnxietyActivity.this).create();
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
            }
        });
    }
}
