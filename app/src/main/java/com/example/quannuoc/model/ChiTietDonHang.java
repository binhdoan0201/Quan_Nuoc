package com.example.quannuoc.model;

import java.io.Serializable;

public class ChiTietDonHang implements Serializable {
    private int id;
    private int idDonHang;
    private int idMon;
    private String tenMon;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private String ghiChuMon;

    public ChiTietDonHang() {
    }

    public ChiTietDonHang(int id, int idDonHang, int idMon, String tenMon, int soLuong, double donGia, double thanhTien, String ghiChuMon) {
        this.id = id;
        this.idDonHang = idDonHang;
        this.idMon = idMon;
        this.tenMon = tenMon;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.ghiChuMon = ghiChuMon;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdDonHang() { return idDonHang; }
    public void setIdDonHang(int idDonHang) { this.idDonHang = idDonHang; }

    public int getIdMon() { return idMon; }
    public void setIdMon(int idMon) { this.idMon = idMon; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }

    public String getGhiChuMon() { return ghiChuMon; }
    public void setGhiChuMon(String ghiChuMon) { this.ghiChuMon = ghiChuMon; }
}

