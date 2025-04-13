package com.example.futasbus.request;



import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("soDienThoai")
    private String soDienThoai;

    @SerializedName("matKhau")
    private String matKhau;

    public LoginRequest(String soDienThoai, String matKhau) {
        this.soDienThoai = soDienThoai;
        this.matKhau = matKhau;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
}