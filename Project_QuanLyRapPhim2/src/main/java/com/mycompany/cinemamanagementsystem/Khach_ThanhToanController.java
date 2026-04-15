package com.mycompany.cinemamanagementsystem;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Khach_ThanhToanController implements Initializable {

    // --- Khai báo khungChinh để cài hình nền ---
    @FXML private VBox khungChinh;

    // Liên kết với các nhãn hiển thị thông tin hóa đơn
    @FXML private Label lblPhim;
    @FXML private Label lblGioChieu;
    @FXML private Label lblGhe;
    @FXML private Label lblBapNuoc;
    @FXML private Label lblTongTien;

    // Liên kết với các thành phần của khối quét mã QR
    @FXML private ToggleGroup phuongThucThanhToan;
    @FXML private RadioButton rdoChuyenKhoan;
    @FXML private VBox vboxQRCode;
    @FXML private ImageView imgQRCode;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("--- DANG HIEN THI HOA DON THANH TOAN ---");
        
        // --- 🎨 THÊM HÌNH NỀN RẠP PHIM ĐỒNG BỘ ---
        if (khungChinh != null) {
            String bgImage = "https://images.unsplash.com/photo-1595769816263-9b910be24d5f?w=1200&h=700&fit=crop";
            khungChinh.setStyle("-fx-background-image: url('" + bgImage + "'); " +
                                "-fx-background-size: cover; " +
                                "-fx-background-position: center center;");
        }
        
        // 1. Hiển thị Tên phim và Giờ chiếu
        if (lblPhim != null && lblGioChieu != null) {
            lblPhim.setText("- Phim: " + DatVeSession.tenPhim);
            lblGioChieu.setText("- Giờ chiếu: " + DatVeSession.gioChieu);
        }
        
        // 2. Hiển thị danh sách ghế và tiền vé
        if (lblGhe != null) {
            String chuoiGhe = String.join(", ", DatVeSession.gheDaChon);
            lblGhe.setText("- Ghế: " + chuoiGhe + " (" + String.format("%,d", DatVeSession.tongTienVe) + "đ)");
        }
        
        // 3. Hiển thị Bắp nước
        if (lblBapNuoc != null) {
            String chiTietDoAn = "Combo 1 x" + DatVeSession.soLuongCombo1 + ", Combo 2 x" + DatVeSession.soLuongCombo2;
            lblBapNuoc.setText("- Đồ ăn: " + chiTietDoAn + " (" + String.format("%,d", DatVeSession.tongTienDoAn) + "đ)");
        }
        
        // 4. Hiển thị TỔNG CỘNG cuối cùng
        if (lblTongTien != null) {
            int tongCong = DatVeSession.getTongTien();
            lblTongTien.setText("TỔNG CỘNG: " + String.format("%,d", tongCong) + " đ");
        }

        // 5. LẮNG NGHE SỰ KIỆN ẨN/HIỆN MÃ QR
        if (phuongThucThanhToan != null) {
            phuongThucThanhToan.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == rdoChuyenKhoan) {
                    // Nếu khách chọn "Chuyển khoản" -> Hiện khối QR lên
                    vboxQRCode.setVisible(true);
                    vboxQRCode.setManaged(true);
                    
                    // Tải hình ảnh mã QR cá nhân của bạn
                    if (imgQRCode.getImage() == null) {
                        try {
                            // !!! LƯU Ý QUAN TRỌNG !!!
                            // Hãy thay "qr_nganhang.jpg" bằng ĐÚNG tên file ảnh mã QR của bạn.
                            // File ảnh này phải được copy vào cùng thư mục với file Java này.
                            String imagePath = getClass().getResource("qr_nganhang.jpg").toExternalForm();
                            imgQRCode.setImage(new Image(imagePath));
                        } catch (Exception e) {
                            System.out.println("Lỗi: Không tìm thấy file ảnh QR! Vui lòng kiểm tra lại tên file.");
                        }
                    }
                } else {
                    // Nếu khách chọn "Tiền mặt" -> Ẩn khối QR đi
                    vboxQRCode.setVisible(false);
                    vboxQRCode.setManaged(false);
                }
            });
        }
    }

    @FXML
    private void xuLyThanhToan() {
        System.out.println("==================================");
        System.out.println("GIAO DICH THANH CONG!");
        System.out.println("Tong tien da thu: " + DatVeSession.getTongTien() + "dong");
        System.out.println("==================================");
        
        try {
            App.setRoot("Khach_ThanhToanThanhCong"); 
        } catch (Exception ex) {
            System.out.println("Lỗi khi chuyển trang thành công:");
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void quayLaiTrangTruoc() {
        try {
            App.setRoot("Khach_ChonDoAn"); 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}