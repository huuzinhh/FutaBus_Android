package com.example.futasbus.respone;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.Date;

public class PurchaseItem {
    @SerializedName("idPhieuDatVe")
    private int idPhieuDatVe;

    @SerializedName("tenTuyen")
    private String tenTuyen;

    @SerializedName("thoiDiemDi")
    private long thoiDiemDi;

    @SerializedName("soLuongVe")
    private int soLuongVe;

    @SerializedName("tongTien")
    private long tongTien;

    @SerializedName("trangThai")
    private int trangThai;

    @SerializedName("danhSachIDGhe")
    private String danhSachIDGhe;

    @SerializedName("hoTenNguoiDatVe")
    private String hoTenNguoiDatVe;

    @SerializedName("sdtnguoiDatVe")
    private String sdtNguoiDatVe;

    @SerializedName("emailNguoiDatVe")
    private String emailNguoiDatVe;

    @SerializedName("thoiGianDatVe")
    private long thoiGianDatVe;

    @SerializedName("bienSoXe")
    private String bienSoXe;

    @SerializedName("loaiXe")
    private String loaiXe;

    @SerializedName("diaChiBenXeDi")
    private String diaChiBenXeDi;

    @SerializedName("sdtbenXeDi")
    private String sdtBenXeDi;

    @SerializedName("diaChiBenXeDen")
    private String diaChiBenXeDen;

    public PurchaseItem(String tenTuyen, int idPhieuDatVe, long thoiDiemDi, int soLuongVe, long tongTien, int trangThai, String danhSachIDGhe, String hoTenNguoiDatVe, String sdtNguoiDatVe, String emailNguoiDatVe, long thoiGianDatVe, String bienSoXe, String loaiXe, String diaChiBenXeDi, String sdtBenXeDi, String diaChiBenXeDen) {
        this.tenTuyen = tenTuyen;
        this.idPhieuDatVe = idPhieuDatVe;
        this.thoiDiemDi = thoiDiemDi;
        this.soLuongVe = soLuongVe;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.danhSachIDGhe = danhSachIDGhe;
        this.hoTenNguoiDatVe = hoTenNguoiDatVe;
        this.sdtNguoiDatVe = sdtNguoiDatVe;
        this.emailNguoiDatVe = emailNguoiDatVe;
        this.thoiGianDatVe = thoiGianDatVe;
        this.bienSoXe = bienSoXe;
        this.loaiXe = loaiXe;
        this.diaChiBenXeDi = diaChiBenXeDi;
        this.sdtBenXeDi = sdtBenXeDi;
        this.diaChiBenXeDen = diaChiBenXeDen;
    }

    public PurchaseItem() {
    }

    public int getIdPhieuDatVe() {
        return idPhieuDatVe;
    }

    public void setIdPhieuDatVe(int idPhieuDatVe) {
        this.idPhieuDatVe = idPhieuDatVe;
    }

    public String getTenTuyen() {
        return tenTuyen;
    }

    public void setTenTuyen(String tenTuyen) {
        this.tenTuyen = tenTuyen;
    }

    public long getThoiDiemDi() {
        return thoiDiemDi;
    }

    public void setThoiDiemDi(long thoiDiemDi) {
        this.thoiDiemDi = thoiDiemDi;
    }

    public int getSoLuongVe() {
        return soLuongVe;
    }

    public void setSoLuongVe(int soLuongVe) {
        this.soLuongVe = soLuongVe;
    }

    public long getTongTien() {
        return tongTien;
    }

    public void setTongTien(long tongTien) {
        this.tongTien = tongTien;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getDanhSachIDGhe() {
        return danhSachIDGhe;
    }

    public void setDanhSachIDGhe(String danhSachIDGhe) {
        this.danhSachIDGhe = danhSachIDGhe;
    }

    public String getHoTenNguoiDatVe() {
        return hoTenNguoiDatVe;
    }

    public void setHoTenNguoiDatVe(String hoTenNguoiDatVe) {
        this.hoTenNguoiDatVe = hoTenNguoiDatVe;
    }

    public String getSdtNguoiDatVe() {
        return sdtNguoiDatVe;
    }

    public void setSdtNguoiDatVe(String sdtNguoiDatVe) {
        this.sdtNguoiDatVe = sdtNguoiDatVe;
    }

    public String getEmailNguoiDatVe() {
        return emailNguoiDatVe;
    }

    public void setEmailNguoiDatVe(String emailNguoiDatVe) {
        this.emailNguoiDatVe = emailNguoiDatVe;
    }

    public long getThoiGianDatVe() {
        return thoiGianDatVe;
    }

    public void setThoiGianDatVe(long thoiGianDatVe) {
        this.thoiGianDatVe = thoiGianDatVe;
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

    public String getDiaChiBenXeDi() {
        return diaChiBenXeDi;
    }

    public void setDiaChiBenXeDi(String diaChiBenXeDi) {
        this.diaChiBenXeDi = diaChiBenXeDi;
    }

    public String getSdtBenXeDi() {
        return sdtBenXeDi;
    }

    public void setSdtBenXeDi(String sdtBenXeDi) {
        this.sdtBenXeDi = sdtBenXeDi;
    }

    public String getDiaChiBenXeDen() {
        return diaChiBenXeDen;
    }

    public void setDiaChiBenXeDen(String diaChiBenXeDen) {
        this.diaChiBenXeDen = diaChiBenXeDen;
    }
}

