module com.mycompany.cinemamanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    opens com.mycompany.cinemamanagementsystem to javafx.fxml;
    exports com.mycompany.cinemamanagementsystem;
}
