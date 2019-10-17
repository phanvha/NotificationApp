package com.map4d.utils;

import com.map4d.model.Data;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {
    @FormUrlEncoded
    @POST("/save.php")
    Call<Data> savetoken(
            @Field("email") String email,
            @Field("token") String token
    );
}
