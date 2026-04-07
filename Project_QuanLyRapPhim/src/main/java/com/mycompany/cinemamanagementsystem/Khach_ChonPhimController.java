package com.mycompany.cinemamanagementsystem;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Khach_ChonPhimController implements Initializable {

    // Liên kết với cái FlowPane bên SceneBuilder
    @FXML
    private FlowPane phimContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDanhSachPhim();
    }

    private void loadDanhSachPhim() {
        System.out.println("--- BAT DAU TAI DANH SACH PHIM ---");
        
        // Câu lệnh lấy dữ liệu từ bảng Phim
        String sql = "SELECT * FROM Phim"; 

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Xóa sạch các phim cũ trước khi tải lại (tránh bị nhân đôi nếu tải nhiều lần)
            phimContainer.getChildren().clear();
            
            int demPhim = 0;
            while (rs.next()) {
                demPhim++;
                int maPhim = rs.getInt("Ma_Phim");
                String tenPhim = rs.getString("Ten_Phim");
                String theLoai = rs.getString("The_Loai");
                
                // Lấy dữ liệu link ảnh từ cột Anh_Bia
                String linkAnh = rs.getString("Anh_Bia"); 

                System.out.println("=> Da tai len giao dien phim: " + tenPhim);

                // Tạo một tấm thẻ (Card) cho từng bộ phim và truyền thêm đường dẫn ảnh
                VBox card = taoThePhim(maPhim, tenPhim, theLoai, linkAnh);
                
                // Nhét tấm thẻ vào giao diện
                phimContainer.getChildren().add(card);
            }
            
            System.out.println("--- DA TAI XONG " + demPhim + " BO PHIM ---");

        } catch (Exception e) {
            System.out.println("!!! CÓ LỖI XẢY RA KHI LẤY DỮ LIỆU TỪ SQL !!!");
            e.printStackTrace();
        }
    }

    // Hàm dùng code để "vẽ" giao diện cho 1 bộ phim
    private VBox taoThePhim(int maPhim, String tenPhim, String theLoai, String duongDanAnh) {
        VBox vbox = new VBox(12); // Tăng nhẹ khoảng cách giữa các thành phần
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));
        
        // --- THIẾT KẾ KÍNH MỜ (GLASSMORPHISM) CHO THẺ PHIM ---
        vbox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); " +
                      "-fx-background-radius: 15; " +
                      "-fx-border-color: rgba(255, 255, 255, 0.15); " +
                      "-fx-border-radius: 15; " +
                      "-fx-border-width: 1; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 5);");
                      
        vbox.setPrefWidth(220);  
        vbox.setPrefHeight(380); // Tăng chiều cao để nội dung thoải mái hơn

        // --- XỬ LÝ HÌNH ẢNH (CHỐNG LỖI VÀ BO GÓC) ---
        ImageView imageView = new ImageView();
        imageView.setFitWidth(160);  
        imageView.setFitHeight(230); 
        imageView.setPreserveRatio(false); 

        try {
            if (duongDanAnh != null && !duongDanAnh.trim().isEmpty()) {
                Image image = new Image(duongDanAnh.trim(), false); 
                
                if (image.isError()) {
                    imageView.setImage(new Image("https://via.placeholder.com/160x230/333333/FFFFFF?text=Loi+Anh", false));
                } else {
                    imageView.setImage(image);
                }
            } else {
                imageView.setImage(new Image("https://via.placeholder.com/160x230/333333/FFFFFF?text=No+Poster", false));
            }
            
            // Bo góc mượt mà cho ảnh poster
            Rectangle clip = new Rectangle(160, 230);
            clip.setArcWidth(15);
            clip.setArcHeight(15);
            imageView.setClip(clip);
            
            // ========================================================
            // TÍNH NĂNG MỚI: BẤM VÀO ẢNH ĐỂ XEM THÔNG TIN VÀ ĐÁNH GIÁ
            // ========================================================
            imageView.setCursor(Cursor.HAND); // Đổi con trỏ chuột thành hình bàn tay
            imageView.setOnMouseClicked(event -> {
                System.out.println("Ban vua bam vao anh cua phim: " + tenPhim);
                moCuaSoThongTinPhim(maPhim, tenPhim, theLoai, duongDanAnh);
            });
            // ========================================================
            
        } catch (Exception e) {
            System.out.println("Không thể tải ảnh cho phim: " + tenPhim);
            imageView.setImage(new Image("https://via.placeholder.com/160x230/333333/FFFFFF?text=No+Poster", false));
        }

        // 1. Tên phim (Chữ TRẮNG, in đậm)
        Label lblTen = new Label(tenPhim.toUpperCase());
        lblTen.setFont(Font.font("System", FontWeight.BOLD, 15));
        lblTen.setTextFill(Color.WHITE); // Đổi màu trắng để nổi trên nền đen
        lblTen.setWrapText(true); 
        lblTen.setAlignment(Pos.CENTER);
        lblTen.setPrefHeight(45);

        // 2. Thể loại (Chữ XÁM SÁNG)
        Label lblTheLoai = new Label(theLoai);
        lblTheLoai.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 13px;");

        // 3. Nút Đặt Vé (Nền ĐỎ CHUẨN RẠP PHIM, bo tròn)
        Button btnDatVe = new Button("ĐẶT VÉ");
        btnDatVe.setStyle("-fx-background-color: #e50914; " + 
                          "-fx-text-fill: white; " + 
                          "-fx-font-weight: bold; " + 
                          "-fx-background-radius: 20; " + 
                          "-fx-cursor: hand;");
        btnDatVe.setPrefWidth(130);
        btnDatVe.setPrefHeight(32);
        
        // Sự kiện khi bấm nút Đặt Vé
        btnDatVe.setOnAction(e -> {
            System.out.println("Ban vua chon dat ve phim: " + tenPhim + " (Ma: " + maPhim + ")");
            try {
                Khach_ChonGheController.maPhimDangChon = maPhim; 
                
                DatVeSession.maPhim = maPhim;
                DatVeSession.tenPhim = tenPhim;
                
                App.setRoot("Khach_ChonGhe"); 
            } catch (Exception ex) {
                System.out.println("Lỗi khi chuyển trang Chọn Ghế:");
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(imageView, lblTen, lblTheLoai, btnDatVe);
        return vbox;
    }

    // ========================================================
    // HÀM MỚI: HIỂN THỊ CỬA SỔ POP-UP THÔNG TIN PHIM
    // ========================================================
    private void moCuaSoThongTinPhim(int maPhim, String tenPhim, String theLoai, String duongDanAnh) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("Khach_ThongTinPhim.fxml"));
            Parent root = loader.load();

            // CHÚ Ý: Sau khi bạn tạo xong file Khach_ThongTinPhimController.java, 
            // hãy bỏ comment 2 dòng dưới đây để truyền dữ liệu sang màn hình mới.
            Khach_ThongTinPhimController controller = loader.getController();
            controller.nhanDuLieu(maPhim, tenPhim, theLoai, duongDanAnh);

            Stage stage = new Stage();
            stage.setTitle("Thông Tin & Đánh Giá: " + tenPhim);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác ở màn hình dưới khi pop-up đang mở
            stage.show();

        } catch (Exception e) {
            System.out.println("Lỗi: Không tìm thấy file Khach_ThongTinPhim.fxml. Bạn vui lòng tạo file này trước!");
            e.printStackTrace();
        }
    }

    // Sự kiện khi bấm nút Đăng Xuất
    @FXML
    private void handleDangXuat() {
        System.out.println("--- ĐÃ ĐĂNG XUẤT ---");
        try {
            // Xóa sạch giỏ hàng lỡ khách đang chọn dở
            DatVeSession.xoaSession(); 
            
            // Quay về màn hình đăng nhập (primary)
            App.setRoot("primary"); 
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Lỗi khi đăng xuất!");
        }
    }
}