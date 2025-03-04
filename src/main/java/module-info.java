module com.aa.designmyexperience {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.aa.designmyexperience to javafx.fxml;
    exports com.aa.designmyexperience;
}