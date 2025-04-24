package com.example.futasbus.model;
public class BookingRequest {
    private int soLuongVe;
    private double tongTien;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private int idChuyenXe;
    private String idViTriGhe;

    public BookingRequest() {
    }

    public BookingRequest(int soLuongVe, double tongTien, String hoTen, String soDienThoai, String email,
                          int idChuyenXe, String idViTriGhe) {
        this.soLuongVe = soLuongVe;
        this.tongTien = tongTien;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.idChuyenXe = idChuyenXe;
        this.idViTriGhe = idViTriGhe;
    }

    public int getSoLuongVe() {
        return soLuongVe;
    }

    public void setSoLuongVe(int soLuongVe) {
        this.soLuongVe = soLuongVe;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdChuyenXe() {
        return idChuyenXe;
    }

    public void setIdChuyenXe(int idChuyenXe) {
        this.idChuyenXe = idChuyenXe;
    }

    public String getIdViTriGhe() {
        return idViTriGhe;
    }

    public void setIdViTriGhe(String idViTriGhe) {
        this.idViTriGhe = idViTriGhe;
    }
}
