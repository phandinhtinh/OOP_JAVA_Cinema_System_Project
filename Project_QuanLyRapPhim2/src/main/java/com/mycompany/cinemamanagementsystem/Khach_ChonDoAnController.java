package com.mycompany.cinemamanagementsystem;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;

public class Khach_ChonDoAnController implements Initializable {

    // --- Khai báo khungChinh để cài hình nền ---
    @FXML private VBox khungChinh;

    // Khai báo các ID bên file FXML
    @FXML private Spinner<Integer> spnCombo1;
    @FXML private Spinner<Integer> spnCombo2;
    @FXML private Label lblTongTien;

    // Cài đặt giá tiền
    private final int GIA_COMBO_1 = 50000;
    private final int GIA_COMBO_2 = 75000;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // --- 🎨 THÊM HÌNH NỀN RẠP PHIM ĐỒNG BỘ ---
        if (khungChinh != null) {
            String bgImage = "https://images.unsplash.com/photo-1595769816263-9b910be24d5f?w=1200&h=700&fit=crop";
            khungChinh.setStyle("-fx-background-image: url('" + bgImage + "'); " +
                                "-fx-background-size: cover; " +
                                "-fx-background-position: center center;");
        }
        
        // Cài đặt hộp chọn số lượng: Từ 0 đến 10, mặc định là 0
        if (spnCombo1 != null && spnCombo2 != null) {
            spnCombo1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
            spnCombo2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));

            // Lắng nghe sự kiện: Bất cứ khi nào khách bấm nút tăng/giảm số lượng thì tính lại tiền
            spnCombo1.valueProperty().addListener((obs, oldValue, newValue) -> tinhTongTien());
            spnCombo2.valueProperty().addListener((obs, oldValue, newValue) -> tinhTongTien());
        }
    }

    // Hàm tính tổng tiền bắp nước
    private void tinhTongTien() {
        int soLuong1 = spnCombo1.getValue();
        int soLuong2 = spnCombo2.getValue();
        
        int tongTien = (soLuong1 * GIA_COMBO_1) + (soLuong2 * GIA_COMBO_2);
        
        if (lblTongTien != null) {
            // Hiển thị ra màn hình (thêm dấu phẩy cho đẹp: 50,000đ)
            lblTongTien.setText("Tổng tiền bắp nước: " + String.format("%,d", tongTien) + "đ");
        }
    }

    // Sự kiện khi bấm nút TIẾP TỤC ĐẾN THANH TOÁN
    @FXML
    private void xuLyTiepTuc() {
        System.out.println("--- DA CHOT BAP NUOC ---");
        
        if (spnCombo1 != null && spnCombo2 != null) {
            // --- Lưu thông tin vào giỏ hàng Session ---
            DatVeSession.soLuongCombo1 = spnCombo1.getValue();
            DatVeSession.soLuongCombo2 = spnCombo2.getValue();
            DatVeSession.tongTienDoAn = (DatVeSession.soLuongCombo1 * GIA_COMBO_1) + (DatVeSession.soLuongCombo2 * GIA_COMBO_2);
        }
        
        try {
            App.setRoot("Khach_ThanhToan");
        } catch (Exception ex) {
            System.out.println("Lỗi khi chuyển sang trang Thanh Toán:");
            ex.printStackTrace();
        }
    }

    // --- MỚI THÊM LẠI: HÀM QUAY LẠI TRANG TRƯỚC ĐỂ FXML KHÔNG BỊ LỖI ---
    @FXML
    private void quayLaiTrangTruoc() {
        System.out.println("Quay lại trang Chọn Ghế...");
        try {
            App.setRoot("Khach_ChonGhe"); // Lệnh quay về màn hình Chọn Ghế
        } catch (Exception ex) {
            System.out.println("Lỗi khi quay lại trang trước:");
            ex.printStackTrace();
        }
    }
}