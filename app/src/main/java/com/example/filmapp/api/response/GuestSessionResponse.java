package com.example.filmapp.api.response;

import com.google.gson.annotations.SerializedName;

public class GuestSessionResponse {
    @SerializedName("guest_session_id")
    private String guestsessionId;

    public String getGuestsessionId() {
        return guestsessionId;
    }
}
