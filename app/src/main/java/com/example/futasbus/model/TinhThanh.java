package com.example.futasbus.model;

public class TinhThanh {
    private int idTinhThanh;
    private String tenTinh;

    public TinhThanh() {
    }

    public TinhThanh(int idTinhThanh, String tenTinh) {
        this.idTinhThanh = idTinhThanh;
        this.tenTinh = tenTinh;
    }

    public int getIdTinhThanh() {
        return idTinhThanh;
    }

    public void setIdTinhThanh(int idTinhThanh) {
        this.idTinhThanh = idTinhThanh;
    }

    public String getTenTinh() {
        return tenTinh;
    }

    public void setTenTinh(String tenTinh) {
        this.tenTinh = tenTinh;
    }

    @Override
    public String toString() {
        return tenTinh; // thường để hiển thị trong Spinner
    }
}
