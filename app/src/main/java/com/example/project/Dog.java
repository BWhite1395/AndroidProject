package com.example.project;

public class Dog {
    /**
     * Class for holding dog information.
     */

    private String name;
    private String owner;
    private String breed;
    private String info;
    private String image_url;

    public Dog(String name, String owner, String breed, String info, String image_url) {
        this.name = name;
        this.owner = owner;
        this.breed = breed;

        // Set to null if string is empty.
        if (info.equals("")) {
            this.info = null;
        } else {
            this.info = info;
        }
        if (image_url.equals("")) {
            this.image_url = null;
        } else {
            this.image_url = image_url;
        }
    }

    public Dog(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getOwner() {
        return owner;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    String getInfo() {
        return info;
    }

    String getImage_url() {
        return image_url;
    }
}
