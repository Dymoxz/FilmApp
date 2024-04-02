package com.example.filmapp.api.response;

import com.example.filmapp.model.CastMember;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastResponse {
    @SerializedName("cast")
    private List<CastMember> cast;

    public List<CastMember> getCast() {
        return cast;
    }
}
