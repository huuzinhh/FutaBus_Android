package com.example.futasbus.model;

public class QuanHuyenDTO {
    private int idQuanHuyen;
    private String tenQuanHuyen;
    private int idTinhThanh;

    public QuanHuyenDTO() {
    }

    public QuanHuyenDTO(int idQuanHuyen, int idTinhThanh, String tenQuanHuyen) {
        this.idQuanHuyen = idQuanHuyen;
        this.idTinhThanh = idTinhThanh;
        this.tenQuanHuyen = tenQuanHuyen;
    }

    public int getIdQuanHuyen() {
        return idQuanHuyen;
    }

    public void setIdQuanHuyen(int idQuanHuyen) {
        this.idQuanHuyen = idQuanHuyen;
    }

    public String getTenQuanHuyen() {
        return tenQuanHuyen;
    }

    public void setTenQuanHuyen(String tenQuanHuyen) {
        this.tenQuanHuyen = tenQuanHuyen;
    }

    public int getIdTinhThanh() {
        return idTinhThanh;
    }

    public void setIdTinhThanh(int idTinhThanh) {
        this.idTinhThanh = idTinhThanh;
    }
}
