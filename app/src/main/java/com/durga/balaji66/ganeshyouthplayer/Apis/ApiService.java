package com.durga.balaji66.ganeshyouthplayer.Apis;

//import com.durga.balaji66.signupusingretrofitpostrequest.Models.DefaultResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    //The Register call
    @FormUrlEncoded
    @POST("newPlayerRegister")
    Call<ResponseBody> newPlayerRegistration(
            @Field("candidate_name") String name,
            @Field("father_name") String email,
            @Field("mobile_number") String phone,
            @Field("password") String password,
            @Field("register_date") String date

            );
    @FormUrlEncoded
    @POST("candidateLogin")
    Call<ResponseBody> candidateLogin(
            @Field("candidate_mobile") String email,
            @Field("candidate_password") String password
    );

    @FormUrlEncoded
    @POST("registerAnxiety")
    Call<ResponseBody> registerAnxiety(
            @Field("candidate_mobile") String email,
            @Field("game_name") String game_name
    );
    @FormUrlEncoded
    @POST("registerBubble")
    Call<ResponseBody> registerBubble(
            @Field("candidate_mobile") String email,
            @Field("game_name") String game_name
    );
    @FormUrlEncoded
    @POST("registerBalloon")
    Call<ResponseBody> registerBalloon(
            @Field("candidate_mobile") String email,
            @Field("game_name") String game_name
    );
    @FormUrlEncoded
    @POST("check_for_resetpassword")
    Call<ResponseBody> checkMobileNumber(
            @Field("mobile_number") String mobile

    );

    @FormUrlEncoded
    @POST("resetpassword")
    Call<ResponseBody> updatePassword(
            @Field("mobile_number") String mobile,
            @Field("password") String password

    );


}
