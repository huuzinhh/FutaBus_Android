package com.example.futasbus.respone;

public class PurchaseResponse {
    private int soLuongVe;
    private String thoiDiemDi;
    private int trangThai;
    private int idPhieuDatVe;
    private int tongTien;
    private String tenTuyen;

    public PurchaseResponse(int soLuongVe, String thoiDiemDi, int trangThai, int idPhieuDatVe, int tongTien, String tenTuyen) {
        this.soLuongVe = soLuongVe;
        this.thoiDiemDi = thoiDiemDi;
        this.trangThai = trangThai;
        this.idPhieuDatVe = idPhieuDatVe;
        this.tongTien = tongTien;
        this.tenTuyen = tenTuyen;
    }

    // Getter (không cần setter nếu không sửa)
    public int getSoLuongVe() { return soLuongVe; }
    public String getThoiDiemDi() { return thoiDiemDi; }
    public int getTrangThai() { return trangThai; }
    public int getIdPhieuDatVe() { return idPhieuDatVe; }
    public int getTongTien() { return tongTien; }
    public String getTenTuyen() { return tenTuyen; }
}
