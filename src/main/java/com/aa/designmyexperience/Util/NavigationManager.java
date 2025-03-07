package com.aa.designmyexperience.Util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;

/* This Class is created to navigate between the views of the application */

public class NavigationManager {

    private static Stage primaryStage;

    // We initialise the primary stage
    public static void init(Stage stage) {
        primaryStage = stage;
    }

    // A Navigation fonction to navigate between the views
    public static void navigate(String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(NavigationManager.class.getResource("/com/aa/designmyexperience/" + fxmlFile));
        primaryStage.getScene().setRoot(root);
    }
}
