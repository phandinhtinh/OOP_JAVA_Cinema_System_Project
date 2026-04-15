package com.mycompany.cinemamanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        // Đường dẫn kết nối SQLite. 
        // File CinemaManagement.db sẽ tự động được đẻ ra ngay trong thư mục Project nếu nó chưa có.
        String url = "jdbc:sqlite:CinemaManagement.db";
        
        // Trả về kết nối. SQLite bản cơ bản không cần username và password rườm rà.
        return DriverManager.getConnection(url);
        // Nếu bạn đã đặt tài khoản 'sa' thì điền vào đây, nếu chưa thì dùng:
        // String user = "sa"; 
        // String pass = "123456"; // Thay bằng mật khẩu bạn đặt lúc cài SQL
    }
}