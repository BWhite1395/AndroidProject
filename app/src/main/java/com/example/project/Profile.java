package com.example.project;

import java.util.ArrayList;

public class Profile {

    private String email;
    private ArrayList<Dog> dogs = new ArrayList<>();
    private String username = "User";
    private String uuid;

    Profile(String uuid, String email) {
        this.email = email;
        this.uuid = uuid;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(ArrayList<Dog> dogs) {
        this.dogs = dogs;
    }

    public String setUsername(String name) {
        this.username = name;
        return this.username;
    }

    public String getUsername() {
        return this.username;
    }

    public void addDog(Dog d) {
        this.dogs.add(d);
    }

    public Dog getDog(int pos) {
        if (this.dogs.size() == 0) {
            return null;
        } else {
            return this.dogs.get(pos);
        }
    }

    public void removeDog(int pos) {
        if (this.dogs.size() >= pos) {
            this.dogs.remove(pos);
        }
    }
}
