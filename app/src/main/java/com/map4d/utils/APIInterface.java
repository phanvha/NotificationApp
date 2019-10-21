package com.map4d.utils;

import com.map4d.model.Data;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {
    @FormUrlEncoded
    @POST("/RegisterDevice.php")
    Call<Data> savetoken(
            @Field("email") String email,
            @Field("token") String token
    );
    //Register
    @FormUrlEncoded
    @POST("/sendSinglePush.php")
    Call<Data> sendsinglepush(
            @Field("email") String email,
            @Field("title") String title,
            @Field("message") String message
    );
    @Multipart
    @POST("/sendSinglePush.php")
    Call<Data> sendsinglepushmessage(
            @Part MultipartBody.Part image,
            @Part("email") String email,
            @Part("title") String title,
            @Part("message") String message
    );
}
