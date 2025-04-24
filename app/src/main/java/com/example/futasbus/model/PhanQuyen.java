package com.example.futasbus.model;
public class PhanQuyen {

    private int idPhanQuyen;
    private String tenQuyen;

    public PhanQuyen() {
    }

    public PhanQuyen(int idPhanQuyen, String tenQuyen) {
        this.idPhanQuyen = idPhanQuyen;
        this.tenQuyen = tenQuyen;
    }

    public int getIdPhanQuyen() {
        return idPhanQuyen;
    }

    public void setIdPhanQuyen(int idPhanQuyen) {
        this.idPhanQuyen = idPhanQuyen;
    }

    public String getTenQuyen() {
        return tenQuyen;
    }

    public void setTenQuyen(String tenQuyen) {
        this.tenQuyen = tenQuyen;
    }

    @Override
    public String toString() {
        return "PhanQuyen{" +
                "idPhanQuyen=" + idPhanQuyen +
                ", tenQuyen='" + tenQuyen + '\'' +
                '}';
    }
}

