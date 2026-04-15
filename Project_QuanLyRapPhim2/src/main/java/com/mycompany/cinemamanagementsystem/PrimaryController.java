package com.mycompany.cinemamanagementsystem;

import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class PrimaryController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMessage;
    
    // Thêm 2 biến để điều khiển giao diện Loading
    @FXML private Button btnXacNhan;
    @FXML private ProgressIndicator vongLoad;

    @FXML
    private void handleLogin() {
        String user = txtUsername.getText();
        String pass = txtPassword.getText();

        // Xóa thông báo lỗi cũ (nếu có) trước khi chạy loading
        lblMessage.setText("");

        // 1. Khóa nút bấm lại và hiện vòng xoay Loading lên
        if (btnXacNhan != null) btnXacNhan.setDisable(true);
        if (vongLoad != null) vongLoad.setVisible(true);

        // 2. Chạy hiệu ứng quay vòng tròn 1.5 giây để giả lập thời gian tải hệ thống
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(e -> {
            try {
                // KIỂM TRA ĐĂNG NHẬP (Code gốc của bạn)
                
                // 1. Kiểm tra tài khoản Admin (Quản lý)
                if ("admin".equals(user) && "123".equals(pass)) {
                    App.setRoot("secondary"); // Chuyển sang màn hình quản lý (Secondary)
                } 
                // 2. Kiểm tra tài khoản Customer (Khách hàng)
                else if ("user".equals(user) && "123".equals(pass)) {
                    App.setRoot("Khach_ChonPhim"); // Chuyển sang màn hình khách hàng mua vé
                } 
                // 3. Nếu nhập sai tài khoản hoặc mật khẩu
                else {
                    lblMessage.setText("Tài khoản hoặc mật khẩu không chính xác!");
                    
                    // Thất bại thì phải mở lại nút và ẩn vòng loading đi để khách thử lại
                    if (btnXacNhan != null) btnXacNhan.setDisable(false);
                    if (vongLoad != null) vongLoad.setVisible(false);
                }
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        // Bắt đầu chạy hiệu ứng
        pause.play();
    }
}