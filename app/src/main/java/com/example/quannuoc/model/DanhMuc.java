package com.example.quannuoc.model;

import java.io.Serializable;

public class DanhMuc implements Serializable {
    private int id;
    private String tenDanhMuc;
    private String moTa;
    private String hinhAnh;

    public DanhMuc() {
    }

    public DanhMuc(int id, String tenDanhMuc, String moTa, String hinhAnh) {
        this.id = id;
        this.tenDanhMuc = tenDanhMuc;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenDanhMuc() { return tenDanhMuc; }
    public void setTenDanhMuc(String tenDanhMuc) { this.tenDanhMuc = tenDanhMuc; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    @Override
    public String toString() {
        return tenDanhMuc;
    }
}

