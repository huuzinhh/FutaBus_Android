package com.example.futasbus.model;

import java.math.BigDecimal;
import java.util.Date;

public class ChuyenXe {
    private int idChuyenXe;
    private Date thoiDiemDi;
    private Date thoiDiemDen;
    private BigDecimal giaVe;
    private int trangThai;
    private TuyenXe tuyenXe;
    private Xe xe;
    private NguoiDung taiXe;

    public ChuyenXe() {
    }

    public int getIdChuyenXe() {
        return idChuyenXe;
    }

    public void setIdChuyenXe(int idChuyenXe) {
        this.idChuyenXe = idChuyenXe;
    }

    public Date getThoiDiemDi() {
        return thoiDiemDi;
    }

    public void setThoiDiemDi(Date thoiDiemDi) {
        this.thoiDiemDi = thoiDiemDi;
    }

    public Date getThoiDiemDen() {
        return thoiDiemDen;
    }

    public void setThoiDiemDen(Date thoiDiemDen) {
        this.thoiDiemDen = thoiDiemDen;
    }

    public BigDecimal getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(BigDecimal giaVe) {
        this.giaVe = giaVe;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public TuyenXe getTuyenXe() {
        return tuyenXe;
    }

    public void setTuyenXe(TuyenXe tuyenXe) {
        this.tuyenXe = tuyenXe;
    }

    public Xe getXe() {
        return xe;
    }

    public void setXe(Xe xe) {
        this.xe = xe;
    }

    public NguoiDung getTaiXe() {
        return taiXe;
    }

    public void setTaiXe(NguoiDung taiXe) {
        this.taiXe = taiXe;
    }
}

