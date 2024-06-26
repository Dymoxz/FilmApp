package com.example.filmapp.model;

import java.io.Serializable;


public class CastMember implements Serializable {

    private String profile_path;

    private String name;

    private String character;

    public String getProfile_path() {
        return profile_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

}
