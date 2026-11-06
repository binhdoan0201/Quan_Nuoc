package com.example.quannuoc.model;

import java.io.Serializable;

public class DonHang implements Serializable {
    private int id;
    private int idTaiKhoan;
    private String ngayDat;
    private double tongTien;
    private String diaChiGiaoHang;
    private String sdtNguoiNhan;
    private String ghiChu;
    private String trangThai; // "Chờ xác nhận", "Đang pha chế", "Đang giao", "Hoàn thành", "Đã hủy"
    private String phuongThucTT; // "Tiền mặt", "Chuyển khoản QR", "Ví điện tử"

    public DonHang() {
    }

    public DonHang(int id, int idTaiKhoan, String ngayDat, double tongTien, String diaChiGiaoHang, String sdtNguoiNhan, String ghiChu, String trangThai, String phuongThucTT) {
        this.id = id;
        this.idTaiKhoan = idTaiKhoan;
        this.ngayDat = ngayDat;
        this.tongTien = tongTien;
        this.diaChiGiaoHang = diaChiGiaoHang;
        this.sdtNguoiNhan = sdtNguoiNhan;
        this.ghiChu = ghiChu;
        this.trangThai = trangThai;
        this.phuongThucTT = phuongThucTT;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdTaiKhoan() { return idTaiKhoan; }
    public void setIdTaiKhoan(int idTaiKhoan) { this.idTaiKhoan = idTaiKhoan; }

    public String getNgayDat() { return ngayDat; }
    public void setNgayDat(String ngayDat) { this.ngayDat = ngayDat; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getDiaChiGiaoHang() { return diaChiGiaoHang; }
    public void setDiaChiGiaoHang(String diaChiGiaoHang) { this.diaChiGiaoHang = diaChiGiaoHang; }

    public String getSdtNguoiNhan() { return sdtNguoiNhan; }
    public void setSdtNguoiNhan(String sdtNguoiNhan) { this.sdtNguoiNhan = sdtNguoiNhan; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getPhuongThucTT() { return phuongThucTT; }
    public void setPhuongThucTT(String phuongThucTT) { this.phuongThucTT = phuongThucTT; }
}

