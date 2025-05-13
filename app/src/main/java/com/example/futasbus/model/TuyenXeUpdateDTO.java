package com.example.futasbus.model;

public class TuyenXeUpdateDTO {
    private int idTuyenXe;
    private String tenTuyen;
    private int idBenXeDi;
    private int idBenXeDen;
    private int idTinhThanhDi;
    private int idTinhThanhDen;
    private float quangDuong;
    private float thoiGianDiChuyenTB;
    private int soChuyenTrongNgay;
    private int soNgayChayTrongTuan;

    public int getIdTuyenXe() {
        return idTuyenXe;
    }

    public void setIdTuyenXe(int idTuyenXe) {
        this.idTuyenXe = idTuyenXe;
    }

    public String getTenTuyen() {
        return tenTuyen;
    }

    public void setTenTuyen(String tenTuyen) {
        this.tenTuyen = tenTuyen;
    }

    public int getIdBenXeDi() {
        return idBenXeDi;
    }

    public void setIdBenXeDi(int idBenXeDi) {
        this.idBenXeDi = idBenXeDi;
    }

    public int getIdBenXeDen() {
        return idBenXeDen;
    }

    public void setIdBenXeDen(int idBenXeDen) {
        this.idBenXeDen = idBenXeDen;
    }

    public int getIdTinhThanhDi() {
        return idTinhThanhDi;
    }

    public void setIdTinhThanhDi(int idTinhThanhDi) {
        this.idTinhThanhDi = idTinhThanhDi;
    }

    public int getIdTinhThanhDen() {
        return idTinhThanhDen;
    }

    public void setIdTinhThanhDen(int idTinhThanhDen) {
        this.idTinhThanhDen = idTinhThanhDen;
    }

    public float getQuangDuong() {
        return quangDuong;
    }

    public void setQuangDuong(float quangDuong) {
        this.quangDuong = quangDuong;
    }

    public float getThoiGianDiChuyenTB() {
        return thoiGianDiChuyenTB;
    }

    public void setThoiGianDiChuyenTB(float thoiGianDiChuyenTB) {
        this.thoiGianDiChuyenTB = thoiGianDiChuyenTB;
    }

    public int getSoChuyenTrongNgay() {
        return soChuyenTrongNgay;
    }

    public void setSoChuyenTrongNgay(int soChuyenTrongNgay) {
        this.soChuyenTrongNgay = soChuyenTrongNgay;
    }

    public int getSoNgayChayTrongTuan() {
        return soNgayChayTrongTuan;
    }

    public void setSoNgayChayTrongTuan(int soNgayChayTrongTuan) {
        this.soNgayChayTrongTuan = soNgayChayTrongTuan;
    }

    @Override
    public String toString() {
        return "TuyenXeUpdateDTO [idTuyenXe=" + idTuyenXe + ", tenTuyen=" + tenTuyen + ", idBenXeDi=" + idBenXeDi
                + ", idBenXeDen=" + idBenXeDen + ", idTinhThanhDi=" + idTinhThanhDi + ", idTinhThanhDen="
                + idTinhThanhDen + ", quangDuong=" + quangDuong + ", thoiGianDiChuyenTB=" + thoiGianDiChuyenTB
                + ", soChuyenTrongNgay=" + soChuyenTrongNgay + ", soNgayChayTrongTuan=" + soNgayChayTrongTuan + "]";
    }

    public TuyenXeUpdateDTO(int idTuyenXe, String tenTuyen, int idBenXeDi, int idBenXeDen, int idTinhThanhDi,
                            int idTinhThanhDen, float quangDuong, float thoiGianDiChuyenTB, int soChuyenTrongNgay,
                            int soNgayChayTrongTuan) {
        super();
        this.idTuyenXe = idTuyenXe;
        this.tenTuyen = tenTuyen;
        this.idBenXeDi = idBenXeDi;
        this.idBenXeDen = idBenXeDen;
        this.idTinhThanhDi = idTinhThanhDi;
        this.idTinhThanhDen = idTinhThanhDen;
        this.quangDuong = quangDuong;
        this.thoiGianDiChuyenTB = thoiGianDiChuyenTB;
        this.soChuyenTrongNgay = soChuyenTrongNgay;
        this.soNgayChayTrongTuan = soNgayChayTrongTuan;
    }

    public TuyenXeUpdateDTO() {}

}

