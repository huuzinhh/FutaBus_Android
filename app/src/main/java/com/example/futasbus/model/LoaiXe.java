package com.example.futasbus.model;

public class LoaiXe {

    private int idLoaiXe;
    private String tenLoai;
    private int soGhe;

    public LoaiXe() {
    }

    public LoaiXe(int idLoaiXe, String tenLoai, int soGhe) {
        this.idLoaiXe = idLoaiXe;
        this.tenLoai = tenLoai;
        this.soGhe = soGhe;
    }

    public int getIdLoaiXe() {
        return idLoaiXe;
    }

    public void setIdLoaiXe(int idLoaiXe) {
        this.idLoaiXe = idLoaiXe;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    @Override
    public String toString() {
        return "LoaiXe{" +
                "idLoaiXe=" + idLoaiXe +
                ", tenLoai='" + tenLoai + '\'' +
                ", soGhe=" + soGhe +
                '}';
    }
}
