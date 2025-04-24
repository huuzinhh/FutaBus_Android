package com.example.futasbus.model;
public class VeXe {
    private int idVe;
    private int idPhieuDatVe;
    private int trangThai;
    private int idViTriGhe;

    public VeXe() {
    }

    public VeXe(int idVe, int idPhieuDatVe, int trangThai, int idViTriGhe) {
        this.idVe = idVe;
        this.idPhieuDatVe = idPhieuDatVe;
        this.trangThai = trangThai;
        this.idViTriGhe = idViTriGhe;
    }

    public int getIdVe() {
        return idVe;
    }

    public void setIdVe(int idVe) {
        this.idVe = idVe;
    }

    public int getIdPhieuDatVe() {
        return idPhieuDatVe;
    }

    public void setIdPhieuDatVe(int idPhieuDatVe) {
        this.idPhieuDatVe = idPhieuDatVe;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public int getIdViTriGhe() {
        return idViTriGhe;
    }

    public void setIdViTriGhe(int idViTriGhe) {
        this.idViTriGhe = idViTriGhe;
    }

    @Override
    public String toString() {
        return "VeXe{" +
                "idVe=" + idVe +
                ", idPhieuDatVe=" + idPhieuDatVe +
                ", trangThai=" + trangThai +
                ", idViTriGhe=" + idViTriGhe +
                '}';
    }
}
