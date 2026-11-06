package com.example.quannuoc.model;

import java.io.Serializable;

public class Mon implements Serializable {
    private int id;
    private String tenMon;
    private double giaTien;
    private String moTa;
    private String hinhAnh;
    private int idDanhMuc;
    private int trangThai; // 1: Còn bán, 0: Ngừng bán

    public Mon() {
    }

    public Mon(int id, String tenMon, double giaTien, String moTa, String hinhAnh, int idDanhMuc, int trangThai) {
        this.id = id;
        this.tenMon = tenMon;
        this.giaTien = giaTien;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
        this.idDanhMuc = idDanhMuc;
        this.trangThai = trangThai;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public double getGiaTien() { return giaTien; }
    public void setGiaTien(double giaTien) { this.giaTien = giaTien; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public int getIdDanhMuc() { return idDanhMuc; }
    public void setIdDanhMuc(int idDanhMuc) { this.idDanhMuc = idDanhMuc; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return tenMon;
    }
}

