package com.example.quannuoc.model;

import java.io.Serializable;

public class TaiKhoan implements Serializable {
    private int id;
    private String tenDangNhap;
    private String matKhau;
    private String hoTen;
    private String sdt;
    private String email;
    private String diaChi;
    private String quyen; // "admin" hoặc "user"

    public TaiKhoan() {
    }

    public TaiKhoan(int id, String tenDangNhap, String matKhau, String hoTen, String sdt, String email, String diaChi, String quyen) {
        this.id = id;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.email = email;
        this.diaChi = diaChi;
        this.quyen = quyen;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getQuyen() { return quyen; }
    public void setQuyen(String quyen) { this.quyen = quyen; }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(quyen);
    }
}

