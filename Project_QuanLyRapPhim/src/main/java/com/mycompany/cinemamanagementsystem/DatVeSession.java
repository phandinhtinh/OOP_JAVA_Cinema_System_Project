package com.mycompany.cinemamanagementsystem;

import java.util.ArrayList;
import java.util.List;

public class DatVeSession {
    // 1. Thông tin phim và giờ chiếu
    public static int maPhim = 0;
    public static String tenPhim = "";
    public static String gioChieu = "";
    
    // 2. Thông tin ghế
    public static List<String> gheDaChon = new ArrayList<>();
    public static int tongTienVe = 0;
    
    // 3. Thông tin bắp nước
    public static int soLuongCombo1 = 0;
    public static int soLuongCombo2 = 0;
    public static int tongTienDoAn = 0;
    
    // Hàm tính tổng tiền cuối cùng (Vé + Bắp nước)
    public static int getTongTien() {
        return tongTienVe + tongTienDoAn;
    }

    // Hàm reset (xóa sạch dữ liệu) khi khách đã thanh toán xong hoặc bấm hủy
    public static void xoaSession() {
        maPhim = 0;
        tenPhim = "";
        gioChieu = "";
        gheDaChon.clear();
        tongTienVe = 0;
        
        soLuongCombo1 = 0;
        soLuongCombo2 = 0;
        tongTienDoAn = 0;
    }
}