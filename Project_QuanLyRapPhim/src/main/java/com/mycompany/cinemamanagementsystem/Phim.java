package com.mycompany.cinemamanagementsystem;

import java.sql.Date; // Bắt buộc dùng java.sql.Date

public class Phim {
    private int maPhim;
    private String tenPhim;
    private String theLoai;
    private int thoiLuong;      
    private Date ngayKhoiChieu; 

    // HÀM KHỞI TẠO (CONSTRUCTOR)
    public Phim(int maPhim, String tenPhim, String theLoai, int thoiLuong, Date ngayKhoiChieu) {
        this.maPhim = maPhim;
        this.tenPhim = tenPhim;
        this.theLoai = theLoai;
        this.thoiLuong = thoiLuong;
        this.ngayKhoiChieu = ngayKhoiChieu;
    }

    // ================= GETTER VÀ SETTER =================
    // JavaFX TableView CẦN có đủ cả Getter và Setter để hiển thị dữ liệu

    public int getMaPhim() { 
        return maPhim; 
    }
    
    public void setMaPhim(int maPhim) {
        this.maPhim = maPhim;
    }

    public String getTenPhim() { 
        return tenPhim; 
    }
    
    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getTheLoai() { 
        return theLoai; 
    }
    
    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public int getThoiLuong() { 
        return thoiLuong; 
    }
    
    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public Date getNgayKhoiChieu() { 
        return ngayKhoiChieu; 
    }
    
    public void setNgayKhoiChieu(Date ngayKhoiChieu) {
        this.ngayKhoiChieu = ngayKhoiChieu;
    }
}