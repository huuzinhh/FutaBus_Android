package com.example.futasbus.model;

import java.io.Serializable;

public class ChuyenXeResult implements Serializable {
    private float thoiGianDiChuyenTB;
    private String tenBenXeDi;
    private String tenBenXeDen;
    private Double giaHienHanh;
    private int tripId;
    private String thoiDiemDi;
    private String thoiDiemDen;
    private String tenLoai;
    private Long soGheTrong;
    private int idXe;

    public ChuyenXeResult() {
    }

    public ChuyenXeResult(float thoiGianDiChuyenTB, String tenBenXeDi, String tenBenXeDen, Double giaHienHanh,
                          int tripId, String thoiDiemDi, String thoiDiemDen, String tenLoai,
                          Long soGheTrong, int idXe) {
        this.thoiGianDiChuyenTB = thoiGianDiChuyenTB;
        this.tenBenXeDi = tenBenXeDi;
        this.tenBenXeDen = tenBenXeDen;
        this.giaHienHanh = giaHienHanh;
        this.tripId = tripId;
        this.thoiDiemDi = thoiDiemDi;
        this.thoiDiemDen = thoiDiemDen;
        this.tenLoai = tenLoai;
        this.soGheTrong = soGheTrong;
        this.idXe = idXe;
    }

    public float getThoiGianDiChuyenTB() {
        return thoiGianDiChuyenTB;
    }

    public void setThoiGianDiChuyenTB(float thoiGianDiChuyenTB) {
        this.thoiGianDiChuyenTB = thoiGianDiChuyenTB;
    }

    public String getTenBenXeDi() {
        return tenBenXeDi;
    }

    public void setTenBenXeDi(String tenBenXeDi) {
        this.tenBenXeDi = tenBenXeDi;
    }

    public String getTenBenXeDen() {
        return tenBenXeDen;
    }

    public void setTenBenXeDen(String tenBenXeDen) {
        this.tenBenXeDen = tenBenXeDen;
    }

    public Double getGiaHienHanh() {
        return giaHienHanh;
    }

    public void setGiaHienHanh(Double giaHienHanh) {
        this.giaHienHanh = giaHienHanh;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
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

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public Long getSoGheTrong() {
        return soGheTrong;
    }

    public void setSoGheTrong(Long soGheTrong) {
        this.soGheTrong = soGheTrong;
    }

    public int getIdXe() {
        return idXe;
    }

    public void setIdXe(int idXe) {
        this.idXe = idXe;
    }
}

