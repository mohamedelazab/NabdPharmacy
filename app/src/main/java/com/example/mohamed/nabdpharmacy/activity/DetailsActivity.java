package com.example.mohamed.nabdpharmacy.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
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

import static com.example.mohamed.nabdpharmacy.R.id.app_bar_layout;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener{

    Product p;
    Toolbar toolbar;
    ImageView collapseImageView;
    EditText etNameDetails,etExDateDetails,etBoxesNumDetails,etStripesNumDetails,etNotesDetails;
    FloatingActionButton fabEdit;
    AppBarLayout appBarLayout;

    MenuItem uploadMenuItem;
    MenuItem saveMenuItem;

    Bitmap bitmap;
    public static final int IMAGE_REQUEST =777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        handleUI_onCreate();

        p = (Product) getIntent().getSerializableExtra("product_object");
        collapseImageView.setContentDescription(p.getName()+"H");
        assignValues(p);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    public void assignValues(Product p){

        etNameDetails.setText(p.getName());
        etExDateDetails.setText(p.getExpirationDate());
        etBoxesNumDetails.setText(String.valueOf(p.getPackagesAmount()));
        etStripesNumDetails.setText(String.valueOf(p.getStripesAmount()));
        etNotesDetails.setText(p.getNotes());

            //Glide.with(this).load(p.getImagePath()).centerCrop().into(collapseImageView);
            Glide.with(this)
                    .load(p.getImagePath())
                    .signature(new StringSignature(p.getImagePath()))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(collapseImageView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_name_title:
                String data =etNameDetails.getText().toString().trim();
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData =ClipData.newPlainText("Text Copied.!",data);
                assert clipboardManager != null;
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(this, "Text copied.!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.collapse_imageView:

                MyDialogs.showImageDialog(this,p);
                break;

            case R.id.fab_edit:
                handleUI_fabClick(true);
                appBarLayout.setExpanded(false);
                uploadMenuItem.setVisible(true);
                saveMenuItem.setVisible(true);
                break;
        }
    }

    private void handleUI_edit(Boolean state){
        etExDateDetails.setEnabled(state);
        etBoxesNumDetails.setEnabled(state);
        etStripesNumDetails.setEnabled(state);
        etNotesDetails.setEnabled(state);
    }

    private void handleUI_fabClick(Boolean state) {
        handleUI_edit(state);
        if (state) {
            etExDateDetails.setBackgroundResource(android.R.drawable.edit_text);
            etBoxesNumDetails.setBackgroundResource(android.R.drawable.edit_text);
            etStripesNumDetails.setBackgroundResource(android.R.drawable.edit_text);
            etNotesDetails.setBackgroundResource(android.R.drawable.edit_text);
        }
    }

    private void handleUI_onCreate() {

        toolbar = findViewById(R.id.toolbar_details);
        appBarLayout = findViewById(app_bar_layout);
        collapseImageView = findViewById(R.id.collapse_imageView);
        etNameDetails = findViewById(R.id.et_name_details);
        etExDateDetails = findViewById(R.id.et_expiration_date_details);
        etBoxesNumDetails = findViewById(R.id.et_boxes_amount_details);
        etStripesNumDetails = findViewById(R.id.et_stripes_amount_details);
        etNotesDetails = findViewById(R.id.et_notes_details);

        etNameDetails.setTextColor(Color.BLACK);
        etExDateDetails.setTextColor(Color.BLACK);
        etBoxesNumDetails.setTextColor(Color.BLACK);
        etStripesNumDetails.setTextColor(Color.BLACK);
        etNotesDetails.setTextColor(Color.BLACK);

        fabEdit = findViewById(R.id.fab_edit);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item2,menu);

         uploadMenuItem = menu.findItem(R.id.action_upload_img);
         saveMenuItem = menu.findItem(R.id.action_save);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        switch (menuItem.getItemId()){
            case R.id.action_save:

                if (etBoxesNumDetails.getText().toString().equals("")){
                    etBoxesNumDetails.setText(String.valueOf(0));
                }
                    updateData(etNameDetails.getText().toString().trim(),
                            etExDateDetails.getText().toString().trim(),
                            Float.parseFloat(etBoxesNumDetails.getText().toString().trim()),
                            Float.parseFloat(etStripesNumDetails.getText().toString().trim()),
                            etNotesDetails.getText().toString().trim(),
                            imageToString());
                break;

            case R.id.action_upload_img:
                selectImage();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==IMAGE_REQUEST && resultCode ==RESULT_OK && data !=null){
            Uri uri =data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                Glide.clear(collapseImageView);
                collapseImageView.setImageBitmap(bitmap);
                appBarLayout.setExpanded(true);
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
            result =Base64.encodeToString(imgByte, Base64.DEFAULT);
        }

        return result;
    }

    public void updateData(String name, String expirationDate,
                           float packagesAmount, float stripesAmount, String notes, String imagePath){

        MyDialogs.showProgressDialog(this);
        imagePath =imageToString();
        ApiInterface apiInterface = ApiClient.getApiClient(DetailsActivity.this).create(ApiInterface.class);
        apiInterface.updateData(name,expirationDate,packagesAmount,stripesAmount,notes,imagePath).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                p =response.body();
                assert p != null;
                Toast.makeText(DetailsActivity.this, ""+p.getResponse(), Toast.LENGTH_SHORT).show();
                //collapseImageView.setImageBitmap(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Glide.with(DetailsActivity.this)
                        .load(stream.toByteArray())
                        .asBitmap()
                        .into(collapseImageView);
                MyDialogs.dismissProgressDialog(DetailsActivity.this);
                //startActivity(new Intent(DetailsActivity.this,MainActivity.class));
              //  recreate();
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                Toast.makeText(DetailsActivity.this, "Check Internet connection.!", Toast.LENGTH_SHORT).show();
                MyDialogs.dismissProgressDialog(DetailsActivity.this);
            }
        });

    }
}
