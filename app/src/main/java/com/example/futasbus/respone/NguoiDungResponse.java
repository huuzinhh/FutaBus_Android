package com.example.futasbus.respone;


import com.google.gson.annotations.SerializedName;

public class NguoiDungResponse {
    @SerializedName("idNguoiDung")
    private int idNguoiDung;

    @SerializedName("hoTen")
    private String hoTen;

    @SerializedName("idPhanQuyen")
    private int idPhanQuyen;

    public int getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(int idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public int getIdPhanQuyen() {
        return idPhanQuyen;
    }

    public void setIdPhanQuyen(int idPhanQuyen) {
        this.idPhanQuyen = idPhanQuyen;
    }
}