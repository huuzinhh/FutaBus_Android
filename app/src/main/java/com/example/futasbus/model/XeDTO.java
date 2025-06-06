package com.example.futasbus.model;

public class XeDTO {
    private int idXe;
    private String bienSo;
    private String tenXe;
    private int idLoaiXe;

    public XeDTO() {
    }

    public XeDTO(int idXe, int loaiXe, String tenXe, String bienSo) {
        this.idXe = idXe;
        this.idLoaiXe = loaiXe;
        this.tenXe = tenXe;
        this.bienSo = bienSo;
    }

    public int getIdXe() {
        return idXe;
    }

    public void setIdXe(int idXe) {
        this.idXe = idXe;
    }

    public int getLoaiXe() {
        return idLoaiXe;
    }

    public void setLoaiXe(int loaiXe) {
        this.idLoaiXe = loaiXe;
    }

    public String getTenXe() {
        return tenXe;
    }

    public void setTenXe(String tenXe) {
        this.tenXe = tenXe;
    }

    public String getBienSo() {
        return bienSo;
    }

    public void setBienSo(String bienSo) {
        this.bienSo = bienSo;
    }
}
