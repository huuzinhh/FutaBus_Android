
package com.example.futasbus.model;

public class BenXeDTO {
    private int idBenXe;
    private String tenBenXe;
    private String diaChi;
    private String soDienThoai;
    private QuanHuyen quanHuyen;
    private int trangThai;

    public BenXeDTO() {
    }

    public BenXeDTO(int idBenXe, String tenBenXe, String diaChi, String soDienThoai, QuanHuyen idQuanHuyen, int trangThai) {
        this.idBenXe = idBenXe;
        this.tenBenXe = tenBenXe;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.quanHuyen = idQuanHuyen;
        this.trangThai = trangThai;
    }

    public int getIdBenXe() {
        return idBenXe;
    }

    public void setIdBenXe(int idBenXe) {
        this.idBenXe = idBenXe;
    }

    public String getTenBenXe() {
        return tenBenXe;
    }

    public void setTenBenXe(String tenBenXe) {
        this.tenBenXe = tenBenXe;
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

    public QuanHuyen getIdQuanHuyen() {
        return quanHuyen;
    }

    public void setIdQuanHuyen(QuanHuyen idQuanHuyen) {
        this.quanHuyen = idQuanHuyen;
    }

    public int getTrangThai() { return trangThai;  }

    public void setTrangThai(int trangThai) {this.trangThai = trangThai; }

    @Override
    public String toString() {
        return tenBenXe;
    }

}
