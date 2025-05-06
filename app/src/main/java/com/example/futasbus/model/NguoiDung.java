package com.example.futasbus.model;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class NguoiDung implements Serializable {

    private int idNguoiDung;
    private String hoTen;
    private boolean gioiTinh;
    private int namSinh;
    @SerializedName("cccd")
    private String CCCD;
    private String diaChi;
    private String soDienThoai;
    private String email;
    private String matKhau;
    private Date ngayDangKy;
    private int trangThai;
    private int idPhanQuyen;

    public NguoiDung() {
    }

    public NguoiDung(int idNguoiDung, String hoTen, boolean gioiTinh, int namSinh, String CCCD, String diaChi,
                     String soDienThoai, String email, String matKhau, Date ngayDangKy, int trangThai, int idPhanQuyen) {
        this.idNguoiDung = idNguoiDung;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.namSinh = namSinh;
        this.CCCD = CCCD;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.matKhau = matKhau;
        this.ngayDangKy = ngayDangKy;
        this.trangThai = trangThai;
        this.idPhanQuyen = idPhanQuyen;
    }

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

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public int getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(int namSinh) {
        this.namSinh = namSinh;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
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

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public Date getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(Date ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public int getIdPhanQuyen() {
        return idPhanQuyen;
    }

    public void setIdPhanQuyen(int idPhanQuyen) {
        this.idPhanQuyen = idPhanQuyen;
    }

    @Override
    public String toString() {
        return "NguoiDung{" +
                "idNguoiDung=" + idNguoiDung +
                ", hoTen='" + hoTen + '\'' +
                ", gioiTinh=" + gioiTinh +
                ", namSinh=" + namSinh +
                ", CCCD='" + CCCD + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", email='" + email + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", ngayDangKy=" + ngayDangKy +
                ", trangThai=" + trangThai +
                ", idPhanQuyen=" + idPhanQuyen +
                '}';
    }
}
