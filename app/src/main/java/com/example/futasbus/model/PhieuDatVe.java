package com.example.futasbus.model;
import java.math.BigDecimal;
import java.util.Date;

public class PhieuDatVe {

    private int idPhieuDatVe;
    private Date thoiGianDatVe;
    private int soLuongVe;
    private BigDecimal tongTien;
    private int trangThai;
    private Date thoiGianCapNhat;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private int idNguoiDung;
    private int idChuyenXe;

    public PhieuDatVe() {
    }

    public PhieuDatVe(int idPhieuDatVe, Date thoiGianDatVe, int soLuongVe, BigDecimal tongTien, int trangThai,
                      Date thoiGianCapNhat, String hoTen, String soDienThoai, String email, int idNguoiDung, int idChuyenXe) {
        this.idPhieuDatVe = idPhieuDatVe;
        this.thoiGianDatVe = thoiGianDatVe;
        this.soLuongVe = soLuongVe;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.thoiGianCapNhat = thoiGianCapNhat;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.idNguoiDung = idNguoiDung;
        this.idChuyenXe = idChuyenXe;
    }

    public int getIdPhieuDatVe() {
        return idPhieuDatVe;
    }

    public void setIdPhieuDatVe(int idPhieuDatVe) {
        this.idPhieuDatVe = idPhieuDatVe;
    }

    public Date getThoiGianDatVe() {
        return thoiGianDatVe;
    }

    public void setThoiGianDatVe(Date thoiGianDatVe) {
        this.thoiGianDatVe = thoiGianDatVe;
    }

    public int getSoLuongVe() {
        return soLuongVe;
    }

    public void setSoLuongVe(int soLuongVe) {
        this.soLuongVe = soLuongVe;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public Date getThoiGianCapNhat() {
        return thoiGianCapNhat;
    }

    public void setThoiGianCapNhat(Date thoiGianCapNhat) {
        this.thoiGianCapNhat = thoiGianCapNhat;
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

    public int getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(int idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public int getIdChuyenXe() {
        return idChuyenXe;
    }

    public void setIdChuyenXe(int idChuyenXe) {
        this.idChuyenXe = idChuyenXe;
    }

    @Override
    public String toString() {
        return "PhieuDatVe{" +
                "idPhieuDatVe=" + idPhieuDatVe +
                ", thoiGianDatVe=" + thoiGianDatVe +
                ", soLuongVe=" + soLuongVe +
                ", tongTien=" + tongTien +
                ", trangThai=" + trangThai +
                ", thoiGianCapNhat=" + thoiGianCapNhat +
                ", hoTen='" + hoTen + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", email='" + email + '\'' +
                ", idNguoiDung=" + idNguoiDung +
                ", idChuyenXe=" + idChuyenXe +
                '}';
    }
}
