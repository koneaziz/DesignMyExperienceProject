package com.aa.designmyexperience;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationManager {

    private static Stage primaryStage;

    public static void init(Stage stage) {
        primaryStage = stage;
    }

    public static void navigate(String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(NavigationManager.class.getResource(fxmlFile));
        primaryStage.getScene().setRoot(root);
    }
}
