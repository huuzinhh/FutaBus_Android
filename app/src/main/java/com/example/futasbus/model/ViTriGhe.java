package com.example.futasbus.model;

public class ViTriGhe {
    private int idViTriGhe;
    private String tenViTri;
    private int trangThai;
    private Xe xe;

    public ViTriGhe() {
    }

    public ViTriGhe(int idViTriGhe, String tenViTri, int trangThai, Xe xe) {
        this.idViTriGhe = idViTriGhe;
        this.tenViTri = tenViTri;
        this.trangThai = trangThai;
        this.xe = xe;
    }

    public int getIdViTriGhe() {
        return idViTriGhe;
    }

    public void setIdViTriGhe(int idViTriGhe) {
        this.idViTriGhe = idViTriGhe;
    }

    public String getTenViTri() {
        return tenViTri;
    }

    public void setTenViTri(String tenViTri) {
        this.tenViTri = tenViTri;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public Xe getXe() {
        return xe;
    }

    public void setXe(Xe xe) {
        this.xe = xe;
    }

    @Override
    public String toString() {
        return "ViTriGhe{" +
                "idViTriGhe=" + idViTriGhe +
                ", tenViTri='" + tenViTri + '\'' +
                ", trangThai=" + trangThai +
                ", xe=" + xe +
                '}';
    }
}

