package com.example.project;

public class Dog {
    private String name;
    private String owner;
    private String breed;
    private String info;
    private String image_url;

    public Dog(String name, String owner, String breed, String info, String image_url) {
        this.name = name;
        this.owner = owner;
        this.breed = breed;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
