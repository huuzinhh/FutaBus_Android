package com.example.futasbus.respone;



import com.example.futasbus.model.NguoiDung;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("nguoiDung")
    private NguoiDungResponse user;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NguoiDungResponse getUser() {
        return user;
    }

    public void setUser(NguoiDungResponse user) {
        this.user = user;
    }
}