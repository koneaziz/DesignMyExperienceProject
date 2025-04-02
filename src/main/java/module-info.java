module com.aa.designmyexperience {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires java.net.http;
    requires java.mail;


    opens com.aa.designmyexperience to javafx.fxml;
    exports com.aa.designmyexperience;
    exports com.aa.designmyexperience.Util;
    opens com.aa.designmyexperience.Util to javafx.fxml;
    opens com.aa.designmyexperience.Controllers to javafx.fxml;
}