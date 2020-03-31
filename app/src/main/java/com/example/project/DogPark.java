package com.example.project;


import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

class DogPark {

    @SerializedName("datasetid")
    @Expose
    private String datasetid;

    @SerializedName("recordid")
    @Expose
    private String recordid;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("coordinates")
    @Expose
    private float[][] coordinates;

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

    public JsonObject getFields() {
        return fields;
    }

    public String getDatasetid() {
        return datasetid;
    }

    public String getRecordid() {
        return recordid;
    }

    public String getUrl() {
        return this.getFields().get("url").getAsString();
    }

    public String getAddress() {
        return this.getFields().get("address").getAsString();
    }

    public String getType() {
        return type;
    }

    public float[][] getCoordinates() {
        return coordinates;
    }
}

class DogParkList {
    @SerializedName("dogParkList")
    @Expose
    private ArrayList<DogPark> dogParkList = new ArrayList<>();

    public ArrayList<DogPark> getParks() {
        return dogParkList;
    }
}
