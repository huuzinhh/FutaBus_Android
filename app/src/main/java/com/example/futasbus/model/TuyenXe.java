package com.example.futasbus.model;
public class TuyenXe {
    private int idTuyenXe;
    private String tenTuyen;
    private int soNgayChayTrongTuan;
    private int soChuyenTrongNgay;
    private float thoiGianDiChuyenTB;
    private double giaHienHanh;
    private float quangDuong;

    private TinhThanh tinhThanhDi;
    private TinhThanh tinhThanhDen;
    private BenXe benXeDi;
    private BenXe benXeDen;

    public TuyenXe() {
    }

    public TuyenXe(int idTuyenXe, String tenTuyen, int soNgayChayTrongTuan, int soChuyenTrongNgay,
                   float thoiGianDiChuyenTB, double giaHienHanh, float quangDuong,
                   TinhThanh tinhThanhDi, TinhThanh tinhThanhDen,
                   BenXe benXeDi, BenXe benXeDen) {
        this.idTuyenXe = idTuyenXe;
        this.tenTuyen = tenTuyen;
        this.soNgayChayTrongTuan = soNgayChayTrongTuan;
        this.soChuyenTrongNgay = soChuyenTrongNgay;
        this.thoiGianDiChuyenTB = thoiGianDiChuyenTB;
        this.giaHienHanh = giaHienHanh;
        this.quangDuong = quangDuong;
        this.tinhThanhDi = tinhThanhDi;
        this.tinhThanhDen = tinhThanhDen;
        this.benXeDi = benXeDi;
        this.benXeDen = benXeDen;
    }

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

    public int getSoNgayChayTrongTuan() {
        return soNgayChayTrongTuan;
    }

    public void setSoNgayChayTrongTuan(int soNgayChayTrongTuan) {
        this.soNgayChayTrongTuan = soNgayChayTrongTuan;
    }

    public int getSoChuyenTrongNgay() {
        return soChuyenTrongNgay;
    }

    public void setSoChuyenTrongNgay(int soChuyenTrongNgay) {
        this.soChuyenTrongNgay = soChuyenTrongNgay;
    }

    public float getThoiGianDiChuyenTB() {
        return thoiGianDiChuyenTB;
    }

    public void setThoiGianDiChuyenTB(float thoiGianDiChuyenTB) {
        this.thoiGianDiChuyenTB = thoiGianDiChuyenTB;
    }

    public double getGiaHienHanh() {
        return giaHienHanh;
    }

    public void setGiaHienHanh(double giaHienHanh) {
        this.giaHienHanh = giaHienHanh;
    }

    public float getQuangDuong() {
        return quangDuong;
    }

    public void setQuangDuong(float quangDuong) {
        this.quangDuong = quangDuong;
    }

    public TinhThanh getTinhThanhDi() {
        return tinhThanhDi;
    }

    public void setTinhThanhDi(TinhThanh tinhThanhDi) {
        this.tinhThanhDi = tinhThanhDi;
    }

    public TinhThanh getTinhThanhDen() {
        return tinhThanhDen;
    }

    public void setTinhThanhDen(TinhThanh tinhThanhDen) {
        this.tinhThanhDen = tinhThanhDen;
    }

    public BenXe getBenXeDi() {
        return benXeDi;
    }

    public void setBenXeDi(BenXe benXeDi) {
        this.benXeDi = benXeDi;
    }

    public BenXe getBenXeDen() {
        return benXeDen;
    }

    public void setBenXeDen(BenXe benXeDen) {
        this.benXeDen = benXeDen;
    }

    @Override
    public String toString() {
        return "TuyenXe{" +
                "idTuyenXe=" + idTuyenXe +
                ", tenTuyen='" + tenTuyen + '\'' +
                ", soNgayChayTrongTuan=" + soNgayChayTrongTuan +
                ", soChuyenTrongNgay=" + soChuyenTrongNgay +
                ", thoiGianDiChuyenTB=" + thoiGianDiChuyenTB +
                ", giaHienHanh=" + giaHienHanh +
                ", quangDuong=" + quangDuong +
                ", tinhThanhDi=" + tinhThanhDi +
                ", tinhThanhDen=" + tinhThanhDen +
                ", benXeDi=" + benXeDi +
                ", benXeDen=" + benXeDen +
                '}';
    }
}
