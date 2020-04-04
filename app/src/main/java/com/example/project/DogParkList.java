package com.example.project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

class DogParkList {
    /**
     *  Arraylist of dog parks.
     */

    @SerializedName("dogParkList")
    @Expose
    private ArrayList<DogPark> dogParkList = new ArrayList<>();

    ArrayList<DogPark> getParks() {
        return dogParkList;
    }
}