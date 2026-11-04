package com.example.quannuoc.model;

import java.io.Serializable;

public class GioHang implements Serializable {
    private int id;
    private int idTaiKhoan;
    private int idMon;
    private int soLuong;
    private String ghiChu;

    // Các trường mở rộng để thuận tiện hiển thị trên giao diện giỏ hàng
    private String tenMon;
    private double giaTien;
    private String hinhAnh;

    public GioHang() {
    }

    public GioHang(int id, int idTaiKhoan, int idMon, int soLuong, String ghiChu) {
        this.id = id;
        this.idTaiKhoan = idTaiKhoan;
        this.idMon = idMon;
        this.soLuong = soLuong;
        this.ghiChu = ghiChu;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdTaiKhoan() { return idTaiKhoan; }
    public void setIdTaiKhoan(int idTaiKhoan) { this.idTaiKhoan = idTaiKhoan; }

    public int getIdMon() { return idMon; }
    public void setIdMon(int idMon) { this.idMon = idMon; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public double getGiaTien() { return giaTien; }
    public void setGiaTien(double giaTien) { this.giaTien = giaTien; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public double getThanhTien() {
        return giaTien * soLuong;
    }
}

