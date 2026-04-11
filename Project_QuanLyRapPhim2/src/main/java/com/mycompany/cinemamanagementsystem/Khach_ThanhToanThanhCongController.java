package com.mycompany.cinemamanagementsystem;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Khach_ThanhToanThanhCongController implements Initializable {

    @FXML private VBox khungChinh;
    @FXML private Button btnVeTrangChu;        // Nút bấm về trang chủ
    @FXML private ProgressIndicator vongLoad;  // Vòng tròn loading

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // --- 🎨 THÊM HÌNH NỀN ĐỒNG BỘ ---
        if (khungChinh != null) {
            String bgImage = "https://images.unsplash.com/photo-1595769816263-9b910be24d5f?w=1200&h=700&fit=crop";
            khungChinh.setStyle("-fx-background-image: url('" + bgImage + "'); " +
                                "-fx-background-size: cover; " +
                                "-fx-background-position: center center;");
        }
    }

    // Sự kiện khi bấm nút VỀ TRANG CHÍNH
    @FXML
    private void veTrangChu() {
        System.out.println("Dang xu ly quay ve trang chon phim...");

        // 1. Vô hiệu hóa nút bấm lập tức, tránh việc khách click đúp 2, 3 lần gây kẹt app
        if (btnVeTrangChu != null) {
            btnVeTrangChu.setDisable(true);
        }

        // 2. Hiện vòng tròn Loading lên màn hình
        if (vongLoad != null) {
            vongLoad.setVisible(true);
        }

        // 3. Dùng PauseTransition để nhường luồng (Thread) cho UI vẽ cái vòng tròn quay quay
        // Mình set delay 1.2 giây giả lập quá trình tải, nhìn sẽ rất xịn và chuyên nghiệp.
        PauseTransition pause = new PauseTransition(Duration.seconds(1.2));
        pause.setOnFinished(e -> {
            try {
                // Xóa giỏ hàng và chuyển trang sau khi hết thời gian loading
                DatVeSession.xoaSession();
                App.quayLaiChonPhim();
            } catch (Exception ex) {
                System.out.println("Lỗi khi quay về trang chủ:");
                ex.printStackTrace();
            }
        });
        
        // Bắt đầu chạy hiệu ứng
        pause.play();
    }
}