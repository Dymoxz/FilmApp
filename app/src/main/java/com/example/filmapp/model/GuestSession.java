package com.example.filmapp.model;

import com.google.gson.annotations.SerializedName;

public class GuestSession {

    @SerializedName("guest_session_id")
    private String guestSessionId;

    public String getGuestSessionId() {
        return guestSessionId;
    }

}
