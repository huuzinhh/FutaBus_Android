package com.example.futasbus.respone;

import java.math.BigDecimal;
import java.util.List;

public class StatisticsResponse {
    private long totalCustomer;
    private List<String> ngayList;
    private long totalXe;
    private long totalChuyenXe;
    private List<BigDecimal> tongTienList;
    private BigDecimal tongDoanhThuThangHienTai;

    public StatisticsResponse() { }

    public StatisticsResponse(List<BigDecimal> tongTienList, BigDecimal tongDoanhThuThangHienTai, long totalChuyenXe, long totalXe, List<String> ngayList, long totalCustomer) {
        this.tongTienList = tongTienList;
        this.tongDoanhThuThangHienTai = tongDoanhThuThangHienTai;
        this.totalChuyenXe = totalChuyenXe;
        this.totalXe = totalXe;
        this.ngayList = ngayList;
        this.totalCustomer = totalCustomer;
    }

    public long getTotalCustomer() {
        return totalCustomer;
    }

    public void setTotalCustomer(long totalCustomer) {
        this.totalCustomer = totalCustomer;
    }

    public List<String> getNgayList() {
        return ngayList;
    }

    public void setNgayList(List<String> ngayList) {
        this.ngayList = ngayList;
    }

    public long getTotalXe() {
        return totalXe;
    }

    public void setTotalXe(long totalXe) {
        this.totalXe = totalXe;
    }

    public long getTotalChuyenXe() {
        return totalChuyenXe;
    }

    public void setTotalChuyenXe(long totalChuyenXe) {
        this.totalChuyenXe = totalChuyenXe;
    }

    public List<BigDecimal> getTongTienList() {
        return tongTienList;
    }

    public void setTongTienList(List<BigDecimal> tongTienList) {
        this.tongTienList = tongTienList;
    }

    public BigDecimal getTongDoanhThuThangHienTai() {
        return tongDoanhThuThangHienTai;
    }

    public void setTongDoanhThuThangHienTai(BigDecimal tongDoanhThuThangHienTai) {
        this.tongDoanhThuThangHienTai = tongDoanhThuThangHienTai;
    }
}
