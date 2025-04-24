package com.example.futasbus.model;

public class QuanHuyen {
    private int idQuanHuyen;
    private String tenQuanHuyen;
    private TinhThanh tinhThanh;

    public QuanHuyen() {
    }

    public QuanHuyen(int idQuanHuyen, String tenQuanHuyen, TinhThanh tinhThanh) {
        this.idQuanHuyen = idQuanHuyen;
        this.tenQuanHuyen = tenQuanHuyen;
        this.tinhThanh = tinhThanh;
    }

    public int getIdQuanHuyen() {
        return idQuanHuyen;
    }

    public void setIdQuanHuyen(int idQuanHuyen) {
        this.idQuanHuyen = idQuanHuyen;
    }

    public String getTenQuanHuyen() {
        return tenQuanHuyen;
    }

    public void setTenQuanHuyen(String tenQuanHuyen) {
        this.tenQuanHuyen = tenQuanHuyen;
    }

    public TinhThanh getTinhThanh() {
        return tinhThanh;
    }

    public void setTinhThanh(TinhThanh tinhThanh) {
        this.tinhThanh = tinhThanh;
    }

    @Override
    public String toString() {
        return tenQuanHuyen;
    }
}

