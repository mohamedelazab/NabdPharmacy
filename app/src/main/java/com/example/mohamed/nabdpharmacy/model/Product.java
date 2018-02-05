package com.example.mohamed.nabdpharmacy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mohamed on 01/10/17.
 */

@SuppressWarnings("serial")
public class Product implements Serializable{

    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("expiration_date")
    String expirationDate;

    @SerializedName("packages_amount")
    float packagesAmount;

    @SerializedName("stripes_amount")
    float stripesAmount;

    @SerializedName("notes")
    String notes;

    @SerializedName("image_path")
    String imagePath;

    @SerializedName("response")
    String response;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Product(int id, String name, String expirationDate, float packagesAmount, float stripesAmount, String notes, String imagePath){
        this.id =id;
        this.name =name;
        this.expirationDate =expirationDate;
        this.packagesAmount =packagesAmount;
        this.stripesAmount =stripesAmount;
        this.notes =notes;
        this.imagePath =imagePath;

    }

    public String getId() {
        return ""+id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public float getPackagesAmount() {
        return packagesAmount;
    }

    public void setPackagesAmount(int packagesAmount) {
        this.packagesAmount = packagesAmount;
    }

    public float getStripesAmount() {
        return stripesAmount;
    }

    public void setStripesAmount(int stripesAmount) {
        this.stripesAmount = stripesAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getResponse() {
        return response;
    }
}
