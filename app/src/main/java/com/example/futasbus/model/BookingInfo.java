package com.example.futasbus.model;

public class BookingInfo {
    private int idPhieuDatVe;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private String thoiDiemDi;
    private String thoiDiemDen;
    private double giaVe;
    private int soLuongVe;
    private double tongTien;
    private int trangThai;
    private String thoiGianDatVe;
    private String tenTuyen;
    private String benDi;
    private String benDen;
    private String bienSoXe;
    private String loaiXe;
    private String danhSachGhe;
    private String danhSachIDGhe;

    public BookingInfo() {
    }

    public BookingInfo(int idPhieuDatVe, String hoTen, String soDienThoai, String email,
                       String thoiDiemDi, String thoiDiemDen, double giaVe, int soLuongVe, double tongTien,
                       int trangThai, String thoiGianDatVe, String tenTuyen, String benDi, String benDen,
                       String bienSoXe, String loaiXe, String danhSachGhe, String danhSachIDGhe) {
        this.idPhieuDatVe = idPhieuDatVe;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.thoiDiemDi = thoiDiemDi;
        this.thoiDiemDen = thoiDiemDen;
        this.giaVe = giaVe;
        this.soLuongVe = soLuongVe;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.thoiGianDatVe = thoiGianDatVe;
        this.tenTuyen = tenTuyen;
        this.benDi = benDi;
        this.benDen = benDen;
        this.bienSoXe = bienSoXe;
        this.loaiXe = loaiXe;
        this.danhSachGhe = danhSachGhe;
        this.danhSachIDGhe = danhSachIDGhe;
    }

    public int getIdPhieuDatVe() {
        return idPhieuDatVe;
    }

    public void setIdPhieuDatVe(int idPhieuDatVe) {
        this.idPhieuDatVe = idPhieuDatVe;
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

    public String getThoiDiemDi() {
        return thoiDiemDi;
    }

    public void setThoiDiemDi(String thoiDiemDi) {
        this.thoiDiemDi = thoiDiemDi;
    }

    public String getThoiDiemDen() {
        return thoiDiemDen;
    }

    public void setThoiDiemDen(String thoiDiemDen) {
        this.thoiDiemDen = thoiDiemDen;
    }

    public double getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(double giaVe) {
        this.giaVe = giaVe;
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

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getThoiGianDatVe() {
        return thoiGianDatVe;
    }

    public void setThoiGianDatVe(String thoiGianDatVe) {
        this.thoiGianDatVe = thoiGianDatVe;
    }

    public String getTenTuyen() {
        return tenTuyen;
    }

    public void setTenTuyen(String tenTuyen) {
        this.tenTuyen = tenTuyen;
    }

    public String getBenDi() {
        return benDi;
    }

    public void setBenDi(String benDi) {
        this.benDi = benDi;
    }

    public String getBenDen() {
        return benDen;
    }

    public void setBenDen(String benDen) {
        this.benDen = benDen;
    }

    public String getBienSoXe() {
        return bienSoXe;
    }

    public void setBienSoXe(String bienSoXe) {
        this.bienSoXe = bienSoXe;
    }

    public String getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(String loaiXe) {
        this.loaiXe = loaiXe;
    }

    public String getDanhSachGhe() {
        return danhSachGhe;
    }

    public void setDanhSachGhe(String danhSachGhe) {
        this.danhSachGhe = danhSachGhe;
    }

    public String getDanhSachIDGhe() {
        return danhSachIDGhe;
    }

    public void setDanhSachIDGhe(String danhSachIDGhe) {
        this.danhSachIDGhe = danhSachIDGhe;
    }

    @Override
    public String toString() {
        return "BookingInfo{" +
                "idPhieuDatVe=" + idPhieuDatVe +
                ", hoTen='" + hoTen + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", email='" + email + '\'' +
                ", thoiDiemDi='" + thoiDiemDi + '\'' +
                ", thoiDiemDen='" + thoiDiemDen + '\'' +
                ", giaVe=" + giaVe +
                ", soLuongVe=" + soLuongVe +
                ", tongTien=" + tongTien +
                ", trangThai='" + trangThai + '\'' +
                ", thoiGianDatVe='" + thoiGianDatVe + '\'' +
                ", tenTuyen='" + tenTuyen + '\'' +
                ", benDi='" + benDi + '\'' +
                ", benDen='" + benDen + '\'' +
                ", bienSoXe='" + bienSoXe + '\'' +
                ", loaiXe='" + loaiXe + '\'' +
                ", danhSachGhe='" + danhSachGhe + '\'' +
                ", danhSachIDGhe='" + danhSachIDGhe + '\'' +
                '}';
    }

}
