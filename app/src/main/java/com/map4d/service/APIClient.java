package com.map4d.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//public class APIClient {
//
//    private static final  String BaseURL = "https://vbusapp.000webhostapp.com";
//    private static Retrofit retrofit;
//    public static Retrofit getAPIClient(){
//        if (retrofit == null){
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BaseURL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//
//        return retrofit;
//
//    }
//}
public class APIClient {
    private static String baseURL ="https://vbusapp.000webhostapp.com";
    private static Retrofit retrofit = null;
    public static Retrofit getAPIClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

}
