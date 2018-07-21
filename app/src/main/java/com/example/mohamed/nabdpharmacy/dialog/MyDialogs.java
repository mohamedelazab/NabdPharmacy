package com.example.mohamed.nabdpharmacy.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.example.mohamed.nabdpharmacy.R;
import com.example.mohamed.nabdpharmacy.model.Product;

/**
 * Created by mohamed on 06/10/17.
 */

public class MyDialogs {

    private Context context;
    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait while loading...");
        progressDialog.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progressDialog.show();
    }

    public static void dismissProgressDialog(Context context){
        progressDialog.dismiss();
    }

    public static void showImageDialog(Context context, Product p){
        Dialog viewImageDialog = new Dialog(context);
        viewImageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewImageDialog.setCancelable(true);
        viewImageDialog.setContentView(R.layout.popup_image_layout);

        ImageView imageFirst= viewImageDialog.findViewById(R.id.popup_image_view);

        Glide.with(context).load(p.getImagePath())
                .thumbnail(0.5f)
                .crossFade()
                .signature(new StringSignature(p.getImagePath()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageFirst);

        viewImageDialog.show();

    }

}
