package com.example.project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

class DogParkList {
    @SerializedName("dogParkList")
    @Expose
    private ArrayList<DogPark> dogParkList = new ArrayList<>();

    public ArrayList<DogPark> getParks() {
        return dogParkList;
    }
}