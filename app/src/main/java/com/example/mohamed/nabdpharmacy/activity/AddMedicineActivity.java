package com.example.mohamed.nabdpharmacy.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mohamed.nabdpharmacy.retrofit.ApiClient;
import com.example.mohamed.nabdpharmacy.retrofit.ApiInterface;
import com.example.mohamed.nabdpharmacy.dialog.MyDialogs;
import com.example.mohamed.nabdpharmacy.R;
import com.example.mohamed.nabdpharmacy.model.Product;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddMedicineActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int IMAGE_REQUEST =777;

    Toolbar toolbar;
    ImageView imageView;
    EditText etNewName,etNewExpirationDate,etNewBoxesAmount,etNewStripesAmount,etNewNotes;
    Bitmap bitmap;
    Product p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        imageView = findViewById(R.id.imageview_medicine_new);

        etNewName = findViewById(R.id.et_name_new);
        etNewExpirationDate = findViewById(R.id.et_expiration_date_new);
        etNewBoxesAmount = findViewById(R.id.et_boxes_amount_new);
        etNewStripesAmount = findViewById(R.id.et_stripes_amount_new);
        etNewNotes = findViewById(R.id.et_notes_new);

        toolbar = findViewById(R.id.add_medicine_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item3,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_add:

                boolean result = insertRestrictions();
                if (result){
                        insertData();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageview_medicine_new:
                selectImage();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
            onBackPressed();
            return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==IMAGE_REQUEST && resultCode ==RESULT_OK && data !=null){
            Uri uri =data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imageView.setBackgroundResource(0);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void selectImage() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    public String imageToString(){
        String result ="";
        if (bitmap !=null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imgByte = byteArrayOutputStream.toByteArray();
            result = Base64.encodeToString(imgByte, Base64.DEFAULT);
        }
        return result;
    }


    boolean insertRestrictions(){
        boolean boolResult =true;

        if (etNewName.getText().toString().trim().equals("")){
            etNewName.setError("Required.!");
            boolResult =false;
        }

        if ( etNewBoxesAmount.getText().toString().trim().equals("")){
            etNewBoxesAmount.setError("Required.!");
            boolResult =false;
        }

        if ( etNewExpirationDate.getText().toString().trim().equals("")){
            etNewExpirationDate.setError("Required.!");
            boolResult =false;
        }

        if (boolResult){
            if (etNewStripesAmount.getText().toString().trim().equals("")){
                etNewStripesAmount.setText(String.valueOf(0));
            }
        }

        return boolResult;
    }

    public void insertData(){
        MyDialogs.showProgressDialog(this);
        String imagePath =imageToString();
        ApiInterface apiInterface = ApiClient.getApiClient(this).create(ApiInterface.class);
        apiInterface.insertData(imagePath,
                etNewName.getText().toString().trim(),
                etNewExpirationDate.getText().toString().trim(),
                Float.parseFloat(etNewBoxesAmount.getText().toString().trim()),
                Float.parseFloat(etNewStripesAmount.getText().toString().trim()),
                etNewNotes.getText().toString().trim())
                .enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                p =response.body();
                assert p != null;
                Toast.makeText(AddMedicineActivity.this,p.getResponse(), Toast.LENGTH_SHORT).show();
                MyDialogs.dismissProgressDialog(AddMedicineActivity.this);
                bitmap =null;
                etNewName.setText("");etNewBoxesAmount.setText("");
                etNewStripesAmount.setText("");etNewNotes.setText("");
                imageView.setImageBitmap(null);etNewExpirationDate.setText("");
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                MyDialogs.dismissProgressDialog(AddMedicineActivity.this);
                Toast.makeText(AddMedicineActivity.this, "Check Internet Connection.!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
