package com.mycompany.cinemamanagementsystem;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Khach_ChonGheController implements Initializable {

    public static int maPhimDangChon;

    @FXML private VBox khungChinh; 
    @FXML private GridPane gridGhe; 
    
    @FXML private ComboBox<String> cbGioChieu;
    @FXML private ComboBox<String> cbPhongChieu; // Đã thêm ComboBox chọn phòng
    @FXML private Button btnXacNhan;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Dang mo trang dat ve cho Phim ma so: " + maPhimDangChon);
        
        // --- 🎨 THÊM HÌNH NỀN VÀ TRANG TRÍ ---
        if (khungChinh != null) {
            String bgImage = "https://images.unsplash.com/photo-1595769816263-9b910be24d5f?w=1200&h=700&fit=crop";
            khungChinh.setStyle("-fx-background-image: url('" + bgImage + "'); " +
                                "-fx-background-size: cover; " +
                                "-fx-background-position: center center;");
        }
        
        if (gridGhe != null) {
            gridGhe.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); " +
                             "-fx-padding: 30; " +
                             "-fx-background-radius: 15; " +
                             "-fx-border-color: rgba(255, 255, 255, 0.2); " +
                             "-fx-border-radius: 15; " +
                             "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 15, 0, 0, 5);");
        }
        
        if (btnXacNhan != null) {
            btnXacNhan.setStyle("-fx-background-color: #e50914; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        }
        // --- 🎨 KẾT THÚC TRANG TRÍ ---

        // 1. Thêm danh sách các khung giờ và phòng chiếu vào ComboBox
        cbGioChieu.getItems().addAll("08:30", "10:00", "13:30", "15:00", "19:00", "21:30");
        cbPhongChieu.getItems().addAll("Phòng 01", "Phòng 02", "Phòng 03", "Phòng VIP"); // Đã thêm dữ liệu phòng
        
        // 2. Vẽ sơ đồ ghế
        taoSoDoGhe();
    }

    private void taoSoDoGhe() {
        int soHang = 5; // A, B, C, D, E
        int soCot = 8;  // 1 đến 8

        if (gridGhe == null) {
            gridGhe = new GridPane();
            gridGhe.setHgap(10);
            gridGhe.setVgap(10);
        }
        
        gridGhe.getChildren().clear(); // Xóa sạch sơ đồ cũ nếu có

        for (int row = 0; row < soHang; row++) {
            char tenHang = (char) ('A' + row); 

            for (int col = 0; col < soCot; col++) {
                String tenGhe = tenHang + String.valueOf(col + 1); 
                
                ToggleButton btnGhe = new ToggleButton(tenGhe);
                btnGhe.setPrefSize(50, 50); 
                
                // Style cho ghế theo phong cách Kính mờ
                btnGhe.setStyle("-fx-background-color: rgba(255, 255, 255, 0.85); -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
                
                btnGhe.setOnAction(e -> {
                    if (btnGhe.isSelected()) {
                        btnGhe.setStyle("-fx-background-color: #4cff4c; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
                    } else {
                        btnGhe.setStyle("-fx-background-color: rgba(255, 255, 255, 0.85); -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
                    }
                });

                gridGhe.add(btnGhe, col, row);
            }
        }
    }
    
    // --- HÀM Xử lý khi khách hàng bấm nút Xác nhận ---
    @FXML
    private void xuLyXacNhan() {
        String gioChieu = cbGioChieu.getValue();
        String phongChieu = cbPhongChieu.getValue(); // Lấy giá trị phòng chiếu
        
        if (gioChieu == null) {
            System.out.println("LỖI: Bạn chưa chọn giờ chiếu!");
            return; 
        }
        
        if (phongChieu == null) {
            System.out.println("LỖI: Bạn chưa chọn phòng chiếu!");
            return; 
        }
        
        // Quét sơ đồ ghế xem ghế nào đang được chọn
        List<String> dsGhe = new ArrayList<>();
        for (Node node : gridGhe.getChildren()) {
            if (node instanceof ToggleButton) {
                ToggleButton btn = (ToggleButton) node;
                if (btn.isSelected()) {
                    dsGhe.add(btn.getText()); // Thêm tên ghế (VD: A1, B2) vào danh sách
                }
            }
        }
        
        // Bắt lỗi nếu khách chưa chọn ghế nào
        if (dsGhe.isEmpty()) {
            System.out.println("LỖI: Bạn chưa chọn ghế nào!");
            return;
        }

        System.out.println("=== THONG TIN DAT VE ===");
        System.out.println("Ma phim: " + maPhimDangChon);
        System.out.println("Gio chieu: " + gioChieu);
        System.out.println("Phong chieu: " + phongChieu);
        System.out.println("Ghe da chon: " + dsGhe);
        
        // Lưu thông tin vào giỏ hàng Session
        DatVeSession.gioChieu = gioChieu;
        DatVeSession.gheDaChon = dsGhe;
        // Ghi chú: Nếu class DatVeSession của bạn chưa có biến 'phongChieu', hãy thêm nó vào class đó nhé.
        // DatVeSession.phongChieu = phongChieu; 
        
        // Tính tiền vé (Giả sử 1 vé là 50.000đ)
        int GIA_VE = 50000;
        DatVeSession.tongTienVe = dsGhe.size() * GIA_VE;
        
        // Chuyển sang trang bắp nước
        try {
            App.setRoot("Khach_ChonDoAn"); 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // --- HÀM Xử lý khi bấm nút Quay lại ---
    @FXML
    private void quayLaiTrangTruoc() {
        try {
            App.quayLaiChonPhim(); 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}