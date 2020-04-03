package com.example.project;


import com.google.firebase.database.PropertyName;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

class DogPark {

    @PropertyName("datasetid")
    @SerializedName("datasetid")
    @Expose
    private String datasetid;

    @PropertyName("recordid")
    @SerializedName("recordid")
    @Expose
    private String recordid;

    @PropertyName("url")
    @SerializedName("url")
    @Expose
    private String url;

    @PropertyName("address")
    @SerializedName("address")
    @Expose
    private String address;

    @PropertyName("type")
    @SerializedName("type")
    @Expose
    private String type;

    @PropertyName("coordinates")
    @SerializedName("coordinates")
    @Expose
    private float[][] coordinates;

    @PropertyName("fields")
    @SerializedName("fields")
    @Expose
    private com.google.gson.JsonObject fields;

//    private ArrayList<Dog> dogList = new ArrayList<>();
//
//    public int getDogListSize() {return dogList.size();}
//
//    public ArrayList getDogList() {return dogList;}
//
//    public void addDog(Dog d) {dogList.add(d);}

    @PropertyName("fields")
    public JsonObject getFields() {
        return fields;
    }

    @PropertyName("datasetid")
    public String getDatasetid() {
        return datasetid;
    }

    @PropertyName("recordid")
    public String getRecordid() {
        return recordid;
    }

    @PropertyName("url")
    public String getUrl() {
        return this.getFields().get("url").getAsString();
    }

    @PropertyName("address")
    public String getAddress() {
        return this.getFields().get("address").getAsString();
    }

    @PropertyName("type")
    public String getType() {
        return type;
    }

    @PropertyName("coordinates")
    public float[][] getCoordinates() {
        return coordinates;
    }
}

