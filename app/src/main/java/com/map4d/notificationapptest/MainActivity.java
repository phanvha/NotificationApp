package com.map4d.notificationapptest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.map4d.model.Data;
import com.map4d.service.APIClient;
import com.map4d.utils.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String token, btnre;
    FloatingActionButton fb;
    EditText edEmail;
    Button btnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fb = findViewById(R.id.add);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SendActivity.class);
                startActivity(intent);
            }
        });
        edEmail = (EditText)findViewById(R.id.edEmail);

        getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("notificationapptest");
        token= FirebaseInstanceId.getInstance().getToken();
        String email = "nacac";
//        saveToken(email ,token);
        Log.e("ahcajcacwcaca",""+token);
        btnregister = (Button)findViewById(R.id.btnRegister);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnre = edEmail.getText().toString().trim();
                if(btnre.isEmpty()){
                    edEmail.setError("Please enter a email!");
                }else {
                    DialogUpdateLatlng(v);
                }
            }
        });
    }
    public void getToken(){
        FirebaseMessaging.getInstance().subscribeToTopic("notificationapptest");
        token= FirebaseInstanceId.getInstance().getToken();
        Log.e("ahcajc",""+token);

    }
    private void DialogUpdateLatlng(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Xác nhận!");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.ic_save);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Bạn có muốn nhận thông tin từ máy chủ?\nEmail:"+btnre+"");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Đóng Activity hiện tại
                saveToken(btnre,token);

                //Toast.makeText(getApplicationContext(),longtitude+"",Toast.LENGTH_LONG).show();

            }
        });

        alertDialogBuilder.setNeutralButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("adad", "hủy");
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sendlayout:
                Intent intent = new Intent(this, SendActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveToken(String email, String Token) {
        APIInterface apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<Data> call = apiInterface.savetoken(email,Token);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.e("aefbehfakaakcanjcbe",response.body().toString());
                if (response.isSuccessful() && response.body()!=null){
                    Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();

                }else {
                    Log.d("Error","lỗi");
                }

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.e("aefbehfakaakcanjcbe",t.toString());
            }
        });

    }
}
