package com.map4d.notificationapptest;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.map4d.model.Data;
import com.map4d.service.APIClient;
import com.map4d.utils.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {
    EditText edEmail;
    ProgressDialog progressDialog;
    APIInterface apiInterface;
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edEmail = (EditText) findViewById(R.id.email);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait......!");
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
            case R.id.save:

                String email = edEmail.getText().toString().trim();
                String token1 = "afnwuhuhvuhvshvis";
                Log.e("aefbehfakaakcanjcbe","email "+email + "token"+token1);
                //save token
                if(email.isEmpty()){
                    edEmail.setError("Please enter a email!");
                }else {
                    saveToken(email, token1);
                }return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveToken(String email, String Token) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<Data> call = apiInterface.savetoken(email,Token);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                progressDialog.dismiss();
                Log.e("aefbehfakaakcanjcbe",response.body().toString());
                if (response.isSuccessful() && response.body()!=null){
                    Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("Error","lỗi");
                }

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
            progressDialog.dismiss();
                Log.e("aefbehfakaakcanjcbe",t.toString());
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
