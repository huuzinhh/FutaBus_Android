package com.example.futasbus.model;
public class Xe {
    private int idXe;
    private String bienSo;
    private String tenXe;
    private LoaiXe loaiXe;

    public Xe() {
    }

    public Xe(int idXe, String bienSo, String tenXe, LoaiXe loaiXe) {
        this.idXe = idXe;
        this.bienSo = bienSo;
        this.tenXe = tenXe;
        this.loaiXe = loaiXe;
    }

    public int getIdXe() {
        return idXe;
    }

    public void setIdXe(int idXe) {
        this.idXe = idXe;
    }

    public String getBienSo() {
        return bienSo;
    }

    public void setBienSo(String bienSo) {
        this.bienSo = bienSo;
    }

    public String getTenXe() {
        return tenXe;
    }

    public void setTenXe(String tenXe) {
        this.tenXe = tenXe;
    }

    public LoaiXe getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(LoaiXe loaiXe) {
        this.loaiXe = loaiXe;
    }

    @Override
    public String toString() {
        return "Xe{" +
                "idXe=" + idXe +
                ", bienSo='" + bienSo + '\'' +
                ", tenXe='" + tenXe + '\'' +
                ", loaiXe=" + (loaiXe != null ? loaiXe.getTenLoai() : "null") +
                '}';
    }
}

