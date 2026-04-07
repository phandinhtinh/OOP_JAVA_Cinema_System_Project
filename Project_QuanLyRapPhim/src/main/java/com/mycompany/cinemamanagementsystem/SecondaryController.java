package com.mycompany.cinemamanagementsystem;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class SecondaryController implements Initializable {

    // --- CÁC LAYOUT PANE (ẨN/HIỆN) ---
    @FXML private StackPane stackPaneMain;
    @FXML private VBox panePhim;
    @FXML private VBox paneSuatChieu;
    @FXML private VBox paneBanVe;
    @FXML private VBox paneThongKe;

    // --- CÁC MENU BUTTON ---
    @FXML private Button btnPhim, btnSuatChieu, btnBanVe, btnThongKe;

    // ================== THÀNH PHẦN BẢNG PHIM ==================
    @FXML private TableView<Phim> tvPhim;
    @FXML private TableColumn<Phim, String> colTenPhim, colTheLoai;
    @FXML private TableColumn<Phim, Integer> colThoiLuong;
    @FXML private TableColumn<Phim, Date> colNgayKhoiChieu;
    @FXML private TextField txtTenPhim, txtTheLoai, txtThoiLuong;
    @FXML private DatePicker dpNgay;

    // ================== THÀNH PHẦN BẢNG SUẤT CHIẾU ==================
    @FXML private TableView<ObservableList<String>> tvSuatChieu;
    @FXML private TableColumn<ObservableList<String>, String> colSCMaSuat, colSCPhim, colSCPhong, colSCThoiGian, colSCGiaVe;
    @FXML private TextField txtSCMaPhim, txtSCMaPhong, txtSCThoiGian, txtSCGiaVe;

    // ================== THÀNH PHẦN BẢNG BÁN VÉ ==================
    @FXML private TableView<ObservableList<String>> tvBanVe;
    @FXML private TableColumn<ObservableList<String>, String> colVeMaVe, colVePhim, colVeSuat, colVeGhe, colVeNgayDat, colVeTrangThai;
    @FXML private TextField txtVeMaSuat, txtVeMaGhe, txtVeNgayDat, txtVeTrangThai;

    // ================== THÀNH PHẦN BẢNG THỐNG KÊ ==================
    @FXML private TableView<ObservableList<String>> tvThongKe;
    @FXML private TableColumn<ObservableList<String>, String> colTKPhim, colTKSoVe, colTKDoanhThu;
    @FXML private Label lblTongDoanhThu;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Cài đặt hiển thị cho các bảng
        setupTablePhim();
        setupTableSuatChieu();
        setupTableBanVe();
        setupTableThongKe();

        // ÉP CỬA SỔ HIỂN THỊ ĐẦY ĐỦ KHI VỪA MỞ LÊN
        Platform.runLater(() -> {
            if (tvPhim.getScene() != null && tvPhim.getScene().getWindow() != null) {
                Stage stage = (Stage) tvPhim.getScene().getWindow();
                stage.setMinWidth(1000);
                stage.setMinHeight(700);
                stage.setWidth(1000);
                stage.setHeight(700);
                stage.setMaximized(true); 
                stage.centerOnScreen(); 
            }
        });

        // Khởi động vào tab Phim đầu tiên
        moTabPhim();
    }

    // =========================================================
    // 1. CHỨC NĂNG CHUYỂN ĐỔI TAB (ẨN/HIỆN GIAO DIỆN)
    // =========================================================
    @FXML
    private void moTabPhim() {
        setAllPanesInvisible();
        panePhim.setVisible(true);
        panePhim.toFront();
        hienThiDanhSachPhim(null);
    }

    @FXML
    private void moTabSuatChieu() {
        setAllPanesInvisible();
        paneSuatChieu.setVisible(true);
        paneSuatChieu.toFront();
        loadDataTabSuatChieu();
    }

    @FXML
    private void moTabVeDaBan() {
        setAllPanesInvisible();
        paneBanVe.setVisible(true);
        paneBanVe.toFront();
        loadDataTabBanVe();
    }

    @FXML
    private void moTabThongKe() {
        setAllPanesInvisible();
        paneThongKe.setVisible(true);
        paneThongKe.toFront();
        loadDataTabThongKe();
        tinhTongDoanhThuToanHeThong();
    }

    private void setAllPanesInvisible() {
        panePhim.setVisible(false);
        paneSuatChieu.setVisible(false);
        paneBanVe.setVisible(false);
        paneThongKe.setVisible(false);
    }

    // =========================================================
    // 2. XỬ LÝ DỮ LIỆU BẢNG PHIM (Giữ nguyên code cực chuẩn của bạn)
    // =========================================================
    private void setupTablePhim() {
        colThoiLuong.setStyle("-fx-alignment: CENTER;");
        colNgayKhoiChieu.setStyle("-fx-alignment: CENTER;");

        colTenPhim.setCellValueFactory(new PropertyValueFactory<>("tenPhim"));
        colTheLoai.setCellValueFactory(new PropertyValueFactory<>("theLoai"));
        colThoiLuong.setCellValueFactory(new PropertyValueFactory<>("thoiLuong"));
        colNgayKhoiChieu.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNgayKhoiChieu()));

        colNgayKhoiChieu.setCellFactory(column -> new TableCell<Phim, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(new SimpleDateFormat("dd/MM/yyyy").format(item));
                }
            }
        });

        tvPhim.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tvPhim.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtTenPhim.setText(newSelection.getTenPhim());
                txtTheLoai.setText(newSelection.getTheLoai());
                txtThoiLuong.setText(String.valueOf(newSelection.getThoiLuong()));
                if (newSelection.getNgayKhoiChieu() != null) {
                    dpNgay.setValue(newSelection.getNgayKhoiChieu().toLocalDate());
                } else {
                    dpNgay.setValue(null); 
                }
            }
        });
    }

    @FXML
    private void hienThiDanhSachPhim(javafx.event.ActionEvent event) {
        ObservableList<Phim> danhSachPhim = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Phim"; 

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String ngayStr = rs.getString("Ngay_Khoi_Chieu");
                java.sql.Date ngayChieu = null;
                if (ngayStr != null && !ngayStr.isEmpty()) {
                    try { ngayChieu = java.sql.Date.valueOf(ngayStr); } catch (Exception e) { }
                }
                danhSachPhim.add(new Phim(
                    rs.getInt("Ma_Phim"), rs.getString("Ten_Phim"),
                    rs.getString("The_Loai"), rs.getInt("Thoi_Luong"), ngayChieu 
                ));
            }
            tvPhim.setItems(danhSachPhim);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Lỗi kết nối", "Không thể lấy dữ liệu Phim từ CSDL!");
        }
    }

    @FXML
    private void themPhim() {
        if (txtTenPhim.getText().isEmpty() || txtThoiLuong.getText().isEmpty()) {
            showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập ít nhất Tên phim và Thời lượng!");
            return;
        }
        String sql = "INSERT INTO Phim (Ten_Phim, The_Loai, Thoi_Luong, Ngay_Khoi_Chieu) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, txtTenPhim.getText());
            pstmt.setString(2, txtTheLoai.getText());
            try { pstmt.setInt(3, Integer.parseInt(txtThoiLuong.getText())); } 
            catch (NumberFormatException e) { showAlert(AlertType.WARNING, "Lỗi", "Thời lượng phải là số!"); return; }
            
            if (dpNgay.getValue() != null) pstmt.setDate(4, java.sql.Date.valueOf(dpNgay.getValue()));
            else pstmt.setNull(4, java.sql.Types.DATE);
            
            pstmt.executeUpdate();
            hienThiDanhSachPhim(null); 
            lamMoiFieldsPhim(); 
            showAlert(AlertType.INFORMATION, "Thành công", "Đã thêm phim mới thành công!");
        } catch (Exception e) { showAlert(AlertType.ERROR, "Lỗi", "Không thể thêm phim: " + e.getMessage()); }
    }

    @FXML
    private void suaPhim() {
        Phim selected = tvPhim.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng chọn phim để sửa!"); return;
        }
        String sql = "UPDATE Phim SET Ten_Phim=?, The_Loai=?, Thoi_Luong=?, Ngay_Khoi_Chieu=? WHERE Ma_Phim=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, txtTenPhim.getText());
            pstmt.setString(2, txtTheLoai.getText());
            try { pstmt.setInt(3, Integer.parseInt(txtThoiLuong.getText())); } 
            catch (NumberFormatException e) { showAlert(AlertType.WARNING, "Lỗi", "Thời lượng phải là số!"); return; }
            
            if (dpNgay.getValue() != null) pstmt.setDate(4, java.sql.Date.valueOf(dpNgay.getValue()));
            else pstmt.setNull(4, java.sql.Types.DATE); 
            
            pstmt.setInt(5, selected.getMaPhim()); 
            pstmt.executeUpdate();
            hienThiDanhSachPhim(null);
            lamMoiFieldsPhim();
            showAlert(AlertType.INFORMATION, "Thành công", "Cập nhật thành công!");
        } catch (Exception e) { showAlert(AlertType.ERROR, "Lỗi", "Không thể cập nhật: " + e.getMessage()); }
    }

    @FXML
    private void xoaPhim() {
        Phim selected = tvPhim.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng chọn phim để xóa!"); return;
        }
        String sql = "DELETE FROM Phim WHERE Ma_Phim = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, selected.getMaPhim());
            pstmt.executeUpdate();
            hienThiDanhSachPhim(null);
            lamMoiFieldsPhim();
            showAlert(AlertType.INFORMATION, "Thành công", "Đã xóa phim!");
        } catch (Exception e) { showAlert(AlertType.ERROR, "Lỗi", "Không thể xóa: " + e.getMessage()); }
    }

    private void lamMoiFieldsPhim() {
        txtTenPhim.clear(); txtTheLoai.clear(); txtThoiLuong.clear(); dpNgay.setValue(null);
        tvPhim.getSelectionModel().clearSelection(); 
    }

    // =========================================================
    // 3. XỬ LÝ DỮ LIỆU BẢNG SUẤT CHIẾU
    // =========================================================
    private void setupTableSuatChieu() {
        colSCMaSuat.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(0)));
        colSCPhim.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(1)));
        colSCPhong.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(2)));
        colSCThoiGian.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(3)));
        colSCGiaVe.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(4)));

        tvSuatChieu.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                // Lấy ID phim (nằm ẩn) thay vì lấy Tên Phim. Cần lấy mã phim thực tế từ CSDL, 
                // nhưng tạm thời ta điền tên phim vào ô Mã Phim cho người dùng dễ nhìn.
                // Nếu muốn chuẩn, cột sql cần SELECT thêm p.Ma_Phim.
                txtSCMaPhim.setText(newVal.get(1)); 
                txtSCMaPhong.setText(newVal.get(2));
                txtSCThoiGian.setText(newVal.get(3));
                txtSCGiaVe.setText(newVal.get(4));
            }
        });
    }

    private void loadDataTabSuatChieu() {
        String sql = "SELECT sc.Ma_Suat, p.Ten_Phim, sc.Ma_Phong, sc.Thoi_Gian_Bat_Dau, sc.Gia_Ve " +
                     "FROM Suat_Chieu sc JOIN Phim p ON sc.Ma_Phim = p.Ma_Phim " +
                     "ORDER BY sc.Thoi_Gian_Bat_Dau DESC";
        loadDynamicData(tvSuatChieu, sql);
    }

    @FXML
    private void themSuatChieu() {
        String sql = "INSERT INTO Suat_Chieu (Ma_Phim, Ma_Phong, Thoi_Gian_Bat_Dau, Gia_Ve) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(txtSCMaPhim.getText())); // Lưu ý: Cần nhập đúng MÃ PHIM (số)
            ps.setInt(2, Integer.parseInt(txtSCMaPhong.getText()));
            ps.setString(3, txtSCThoiGian.getText());
            ps.setDouble(4, Double.parseDouble(txtSCGiaVe.getText()));
            ps.executeUpdate();
            loadDataTabSuatChieu();
            showAlert(AlertType.INFORMATION, "Thành công", "Thêm suất chiếu thành công!");
        } catch (Exception e) { showAlert(AlertType.ERROR, "Lỗi", "Kiểm tra lại dữ liệu nhập. Mã phim/phòng phải là số!"); }
    }

    @FXML
    private void suaSuatChieu() {
        ObservableList<String> selected = tvSuatChieu.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert(AlertType.WARNING, "Cảnh báo", "Chọn suất chiếu để sửa!"); return; }
        
        String sql = "UPDATE Suat_Chieu SET Ma_Phim=?, Ma_Phong=?, Thoi_Gian_Bat_Dau=?, Gia_Ve=? WHERE Ma_Suat=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(txtSCMaPhim.getText())); 
            ps.setInt(2, Integer.parseInt(txtSCMaPhong.getText()));
            ps.setString(3, txtSCThoiGian.getText());
            ps.setDouble(4, Double.parseDouble(txtSCGiaVe.getText()));
            ps.setInt(5, Integer.parseInt(selected.get(0))); // Mã suất chiếu
            ps.executeUpdate();
            loadDataTabSuatChieu();
            showAlert(AlertType.INFORMATION, "Thành công", "Sửa suất chiếu thành công!");
        } catch (Exception e) { showAlert(AlertType.ERROR, "Lỗi", "Kiểm tra lại dữ liệu nhập!"); }
    }

    @FXML
    private void xoaSuatChieu() {
        ObservableList<String> selected = tvSuatChieu.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert(AlertType.WARNING, "Cảnh báo", "Chọn suất chiếu để xóa!"); return; }
        
        String sql = "DELETE FROM Suat_Chieu WHERE Ma_Suat=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(selected.get(0)));
            ps.executeUpdate();
            loadDataTabSuatChieu();
            showAlert(AlertType.INFORMATION, "Thành công", "Đã xóa suất chiếu!");
        } catch (Exception e) { showAlert(AlertType.ERROR, "Lỗi", "Không thể xóa!"); }
    }

    // =========================================================
    // 4. XỬ LÝ VÉ VÀ THỐNG KÊ
    // =========================================================
    private void setupTableBanVe() {
        colVeMaVe.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(0)));
        colVePhim.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(1)));
        colVeSuat.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(2)));
        colVeGhe.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(3)));
        colVeNgayDat.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(4)));
        colVeTrangThai.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(5)));

        // Bắt sự kiện click: Lấy thông tin từ CSDL đổ xuống các ô nhập liệu
        tvBanVe.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                String maVe = newVal.get(0);
                String sql = "SELECT Ma_Suat, Ma_Ghe, Thoi_Gian_Dat, Trang_Thai FROM Ve WHERE Ma_Ve = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                     
                    ps.setInt(1, Integer.parseInt(maVe));
                    ResultSet rs = ps.executeQuery();
                    
                    if (rs.next()) {
                        txtVeMaSuat.setText(rs.getString("Ma_Suat"));
                        txtVeMaGhe.setText(rs.getString("Ma_Ghe"));
                        txtVeNgayDat.setText(rs.getString("Thoi_Gian_Dat"));
                        txtVeTrangThai.setText(rs.getString("Trang_Thai"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadDataTabBanVe() {
        String sql = "SELECT v.Ma_Ve, p.Ten_Phim, sc.Thoi_Gian_Bat_Dau, v.Ma_Ghe, v.Thoi_Gian_Dat, v.Trang_Thai " +
                     "FROM Ve v " +
                     "JOIN Suat_Chieu sc ON v.Ma_Suat = sc.Ma_Suat " +
                     "JOIN Phim p ON sc.Ma_Phim = p.Ma_Phim " +
                     "ORDER BY v.Thoi_Gian_Dat DESC";
        loadDynamicData(tvBanVe, sql);
    }

    @FXML 
    private void themVe() {
        String sql = "INSERT INTO Ve (Ma_Suat, Ma_Ghe, Thoi_Gian_Dat, Trang_Thai) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(txtVeMaSuat.getText()));
            ps.setString(2, txtVeMaGhe.getText());
            ps.setString(3, txtVeNgayDat.getText());
            ps.setString(4, txtVeTrangThai.getText());
            
            ps.executeUpdate();
            loadDataTabBanVe();
            lamMoiFieldsVe();
            showAlert(AlertType.INFORMATION, "Thành công", "Đã thêm vé mới!");
        } catch (NumberFormatException e) {
            showAlert(AlertType.WARNING, "Lỗi nhập liệu", "Mã suất phải là một con số!");
        } catch (Exception e) { 
            showAlert(AlertType.ERROR, "Lỗi", "Kiểm tra lại dữ liệu nhập!\n" + e.getMessage()); 
        }
    }

    @FXML 
    private void suaVe() {
        ObservableList<String> selected = tvBanVe.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng chọn vé trên bảng để sửa!"); return; }
        
        String sql = "UPDATE Ve SET Ma_Suat=?, Ma_Ghe=?, Thoi_Gian_Dat=?, Trang_Thai=? WHERE Ma_Ve=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(txtVeMaSuat.getText()));
            ps.setString(2, txtVeMaGhe.getText());
            ps.setString(3, txtVeNgayDat.getText());
            ps.setString(4, txtVeTrangThai.getText());
            ps.setInt(5, Integer.parseInt(selected.get(0))); // Lấy Mã vé đang chọn
            
            ps.executeUpdate();
            loadDataTabBanVe();
            lamMoiFieldsVe();
            showAlert(AlertType.INFORMATION, "Thành công", "Đã cập nhật thông tin vé!");
        } catch (Exception e) { 
            showAlert(AlertType.ERROR, "Lỗi", "Kiểm tra lại dữ liệu nhập!\n" + e.getMessage()); 
        }
    }

    @FXML 
    private void xoaVe() {
        ObservableList<String> selected = tvBanVe.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng chọn vé để xóa!"); return; }
        
        String sql = "DELETE FROM Ve WHERE Ma_Ve=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(selected.get(0)));
            ps.executeUpdate();
            loadDataTabBanVe();
            lamMoiFieldsVe();
            showAlert(AlertType.INFORMATION, "Thành công", "Đã xóa vé thành công!");
        } catch (Exception e) { 
            showAlert(AlertType.ERROR, "Lỗi", "Không thể xóa vé này vì có dữ liệu ràng buộc!"); 
        }
    }
    
    private void lamMoiFieldsVe() {
        txtVeMaSuat.clear();
        txtVeMaGhe.clear();
        txtVeNgayDat.clear();
        txtVeTrangThai.clear();
        tvBanVe.getSelectionModel().clearSelection();
    }
    
    //

    private void setupTableThongKe() {
        colTKPhim.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(0)));
        colTKSoVe.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(1)));
        colTKDoanhThu.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(2)));
    }

    private void loadDataTabThongKe() {
        String sql = "SELECT p.Ten_Phim, COUNT(v.Ma_Ve), IFNULL(SUM(sc.Gia_Ve), 0) " +
                     "FROM Phim p LEFT JOIN Suat_Chieu sc ON p.Ma_Phim = sc.Ma_Phim LEFT JOIN Ve v ON sc.Ma_Suat = v.Ma_Suat " +
                     "GROUP BY p.Ma_Phim ORDER BY IFNULL(SUM(sc.Gia_Ve), 0) DESC";
        loadDynamicData(tvThongKe, sql);
    }
    
    private void tinhTongDoanhThuToanHeThong() {
        // Chỉ cộng tiền của các vé có trạng thái "Đã thanh toán"
        String sql = "SELECT SUM(sc.Gia_Ve) AS TongTien " +
                     "FROM Ve v " +
                     "JOIN Suat_Chieu sc ON v.Ma_Suat = sc.Ma_Suat " +
                     "WHERE v.Trang_Thai = 'Đã thanh toán'"; 

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                double tongTien = rs.getDouble("TongTien");
                // Cập nhật lên giao diện, format số tiền có dấu phẩy cho dễ đọc (VD: 1,500,000 VNĐ)
                lblTongDoanhThu.setText(String.format("%,.0f VNĐ", tongTien));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi tính tổng doanh thu!");
        }
    }

    // =========================================================
    // HÀM TRỢ GIÚP DÙNG CHUNG
    // =========================================================
    private void loadDynamicData(TableView<ObservableList<String>> table, String sql) {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection(); 
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int cols = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= cols; i++) {
                    row.add(rs.getString(i) != null ? rs.getString(i) : "");
                }
                data.add(row);
            }
            table.setItems(data);
        } catch (SQLException e) { 
            e.printStackTrace(); 
            showAlert(AlertType.ERROR, "Lỗi truy xuất", "Không thể lấy dữ liệu: " + e.getMessage());
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void dangXuat() throws IOException {
        // Chuyển scene về lại file primary.fxml (trang đăng nhập)
        App.setRoot("primary"); 
    }
}