package com.mycompany.cinemamanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // --- SỬA KÍCH THƯỚC Ở ĐÂY (Ví dụ: 1200x700) ---
        scene = new Scene(loadFXML("primary"), 1200, 700); 
        stage.setScene(scene);
        
        // Giúp cửa sổ hiện ra ngay giữa màn hình
        stage.centerOnScreen(); 
        
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        
        // Đảm bảo khi chuyển trang, nếu giao diện mới to hơn/nhỏ hơn 
        // thì cửa sổ sẽ tự điều chỉnh và căn giữa lại
        Stage stage = (Stage) scene.getWindow();
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}