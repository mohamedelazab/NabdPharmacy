package com.example.mohamed.nabdpharmacy.retrofit;

import com.example.mohamed.nabdpharmacy.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by mohamed on 01/10/17.
 */

public interface ApiInterface {

    @POST("products.php")
    Call<List<Product>> getProductsInfo();

    @FormUrlEncoded
    @POST("update.php")
    Call<Product> updateData(@Field("name") String name,
                             @Field("expiration_date") String expirationDate, @Field("packages_amount")  float packagesAmount,
                             @Field("stripes_amount") float stripesAmount, @Field("notes") String notes,
                             @Field("image_path") String imagePath);

    @FormUrlEncoded
    @POST("insert.php")
    Call<Product> insertData(@Field("image_path") String imagePath, @Field("name") String name,
                             @Field("expiration_date") String expirationDate, @Field("packages_amount")  float packagesAmount,
                             @Field("stripes_amount") float stripesAmount, @Field("notes") String notes);

    @FormUrlEncoded
    @POST("delete.php")
    Call<Product> deleteData(@Field("name") String name);
}
