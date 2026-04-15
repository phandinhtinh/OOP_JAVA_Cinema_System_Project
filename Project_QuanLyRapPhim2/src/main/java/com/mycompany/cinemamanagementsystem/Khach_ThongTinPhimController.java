package com.mycompany.cinemamanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Khach_ThongTinPhimController {

    @FXML private ImageView imgPoster;
    @FXML private Label lblTenPhim;
    @FXML private Label lblTheLoai;
    @FXML private TextArea txtMoTa;
    @FXML private ListView<String> lvDanhGia;

    private int maPhimHienTai;

    // Hàm này nhận dữ liệu từ màn hình Khach_ChonPhimController truyền sang
    public void nhanDuLieu(int maPhim, String tenPhim, String theLoai, String duongDanAnh) {
        this.maPhimHienTai = maPhim;
        
        // Hiển thị thông tin cơ bản
        lblTenPhim.setText(tenPhim.toUpperCase());
        lblTheLoai.setText(theLoai);

        // Xử lý hiển thị ảnh poster
        try {
            if (duongDanAnh != null && !duongDanAnh.trim().isEmpty()) {
                Image image = new Image(duongDanAnh.trim(), false);
                if (!image.isError()) {
                    imgPoster.setImage(image);
                } else {
                    imgPoster.setImage(new Image("https://via.placeholder.com/200x300/333333/FFFFFF?text=Loi+Anh", false));
                }
            } else {
                imgPoster.setImage(new Image("https://via.placeholder.com/200x300/333333/FFFFFF?text=No+Poster", false));
            }
        } catch (Exception e) {
            System.out.println("Không thể tải ảnh cho chi tiết phim.");
        }

        // Gọi hàm truy xuất Database để lấy Mô tả và Đánh giá
        taiThongTinTuDatabase();
    }

    private void taiThongTinTuDatabase() {
        // 1. Tải Mô tả từ bảng Phim
        // (Giả sử bảng Phim của bạn có cột Mo_Ta, nếu không có nó sẽ tự bắt lỗi và bỏ qua)
        String sqlPhim = "SELECT Mo_Ta FROM Phim WHERE Ma_Phim = ?";
        
        // 2. Tải Đánh giá từ bảng DanhGia 
        // (Giả sử bạn có bảng DanhGia gồm Noi_Dung. Nếu chưa tạo bảng này, code vẫn không bị crash)
        String sqlDanhGia = "SELECT Noi_Dung FROM DanhGia WHERE Ma_Phim = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            
            // --- LẤY MÔ TẢ PHIM ---
            try (PreparedStatement pst = conn.prepareStatement(sqlPhim)) {
                pst.setInt(1, this.maPhimHienTai);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    String moTa = rs.getString("Mo_Ta");
                    if (moTa != null && !moTa.isEmpty()) {
                        txtMoTa.setText(moTa);
                    } else {
                        txtMoTa.setText("Bộ phim này hiện chưa có thông tin mô tả chi tiết.");
                    }
                }
            } catch (Exception e) {
                txtMoTa.setText("Bộ phim này hiện chưa có thông tin mô tả chi tiết. (Có thể do CSDL chưa có cột Mo_Ta)");
            }

            // --- LẤY ĐÁNH GIÁ ---
            lvDanhGia.getItems().clear(); // Xóa dữ liệu cũ
            try (PreparedStatement pst = conn.prepareStatement(sqlDanhGia)) {
                pst.setInt(1, this.maPhimHienTai);
                ResultSet rs = pst.executeQuery();
                boolean coDanhGia = false;
                
                while (rs.next()) {
                    coDanhGia = true;
                    String noiDung = rs.getString("Noi_Dung");
                    lvDanhGia.getItems().add("⭐ " + noiDung); // Thêm từng dòng bình luận vào ListView
                }
                
                if (!coDanhGia) {
                    lvDanhGia.getItems().add("Chưa có đánh giá nào. Hãy là người đầu tiên trải nghiệm bộ phim này!");
                }
            } catch (Exception e) {
                lvDanhGia.getItems().add("Hiện tại không có đánh giá nào.");
                System.out.println("Lưu ý: Bảng DanhGia có thể chưa được tạo trong Database.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm gắn với nút ĐÓNG CỬA SỔ
    @FXML
    private void dongCuaSo(ActionEvent event) {
        // Lấy Stage (cửa sổ) hiện tại dựa vào nút bấm và đóng nó lại
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}