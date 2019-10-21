package com.map4d.notificationapptest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
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
import android.widget.TextView;
import android.widget.Toast;

import com.map4d.model.Data;
import com.map4d.service.APIClient;
import com.map4d.utils.APIInterface;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendActivity extends AppCompatActivity {
    EditText edtitle, edmessage, edemail;
    TextView fileimagepath;
    Button btnsendmessage,btnImage;
    ProgressDialog progressDialog;
    APIInterface apiInterface;
    private String message, title, email;
    int REQUEST_CODE_CAMERA = 667;
    int REQUEST_CODE_FOLDER = 456;
    private String imagePath, imageName;
    private static final String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        edtitle = (EditText) findViewById(R.id.editTitle);
        edemail = (EditText) findViewById(R.id.editEmail);
//        fileimagepath = (TextView) findViewById(R.id.fileimagepath);
        edmessage = (EditText) findViewById(R.id.editMessage);
        btnsendmessage = (Button) findViewById(R.id.btnRegister);
//        btnImage = (Button) findViewById(R.id.btnImage);

        progressDialog = new ProgressDialog(this);
        btnsendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = edtitle.getText().toString().trim();
                email = edemail.getText().toString().trim();
                message = edmessage.getText().toString().trim();

                String token1 = "afnwuhuhvuhvshvis";
                Log.e("aefbehfakaakcanjcbe","email "+email + " title"+title+" Message"+message);
                //save token
                if(email.isEmpty()||message.isEmpty()){
                    edemail.setError("Please enter a email!");
                }else if(title.isEmpty()){
                    edtitle.setError("Please enter a Title!");
                }else if(message.isEmpty()){
                    edmessage.setError("Please enter a Message!");
                }else {
                    DialogUpdate(v);
                }
            }
        });
        progressDialog.setMessage("Please wait......!");
//        btnImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dialog(v);
//            }
//        });

    }
    private void Dialog(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Xác nhận!");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.ic_add);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Lựa chọn? ");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Chọn hình từ máy", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                Intent result = Intent.createChooser(intent, getText(R.string.choosefile));
                startActivityForResult(result, REQUEST_CODE_FOLDER);
            }
        });

        alertDialogBuilder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(SendActivity.this,
                        new String[]{Manifest.permission.CAMERA},REQUEST_CODE_CAMERA);
            }
        });
        alertDialogBuilder.setNeutralButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("exitdialog", "exited");
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                Uri uri = data.getData();
                imagePath = getRealPathFromURI(uri);
                fileimagepath.setText(imagePath);
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            } else if (requestCode == REQUEST_CODE_FOLDER) {
                Uri uri = data.getData();
                imagePath = getRealPathFromURI(uri);
                fileimagepath.setText(imagePath);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
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
            case R.id.savelayout:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    private void DialogUpdate(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Xác nhận!");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.ic_save);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Bạn có muốn gửi một notify tới client có email là "+email+"");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Đóng Activity hiện tại
                if (imagePath!=null){
                    SendSinglePush(email,title,message);
                }else {
                    SendSinglePushMessage(email, title, message);
                }
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
    private void SendSinglePush(String email, String Title, String Message) {
        File file = new File(imagePath);
        RequestBody photoContent = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("image", file.getName(), photoContent);
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<Data> call = apiInterface.sendsinglepushmessage(photo,email,Title,Message);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                progressDialog.dismiss();
                Log.e("aefbehfakaakcanjcbe",response.body().toString());
                if (response.isSuccessful() && response.body()!=null){
                    Log.e("aefbehfakaakcanjcbe",response.body().getSuccess()+"");
                    if (response.body().getSuccess()==1) {
                        Toast.makeText(getApplicationContext(), "Đã gửi thành công!", Toast.LENGTH_SHORT).show();
                    }
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
    private void SendSinglePushMessage(String email, String Title, String Message) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<Data> call = apiInterface.sendsinglepush(email,Title,Message);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                progressDialog.dismiss();
                Log.e("aefbehfakaakcanjcbe",response.body().toString());
                if (response.isSuccessful() && response.body()!=null){
                    Log.e("aefbehfakaakcanjcbe",response.body().getSuccess()+"");
                    if (response.body().getSuccess()==1) {
                        Toast.makeText(getApplicationContext(), "Đã gửi thành công!", Toast.LENGTH_SHORT).show();
                    }
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
