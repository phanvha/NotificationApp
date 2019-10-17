package com.map4d.notificationapptest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.map4d.model.Data;
import com.map4d.service.APIClient;
import com.map4d.utils.APIInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String token;
    FloatingActionButton fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fb = findViewById(R.id.add);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });
        getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("notificationapptest");
        token= FirebaseInstanceId.getInstance().getToken();
        String email = "nacac";
        saveToken(email ,token);
        Log.e("ahcajcacwcaca",""+token);
    }
    public void getToken(){
        FirebaseMessaging.getInstance().subscribeToTopic("notificationapptest");
        token= FirebaseInstanceId.getInstance().getToken();
        new FireBaseIDTask().execute(token);
        Log.e("ahcajc",""+token);

    }
    private void saveToken(String email, String Token) {
        APIInterface apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<Data> call = apiInterface.savetoken(email,Token);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.e("aefbehfakaakcanjcbe",response.body().toString());
                if (response.isSuccessful() && response.body()!=null){
                    Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("Error","lỗi");
                }

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.e("aefbehfakaakcanjcbe",t.toString());
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
