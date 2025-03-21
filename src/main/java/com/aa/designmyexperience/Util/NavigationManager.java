package com.aa.designmyexperience.Util;

import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
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

    // Set the root
    public static void setRoot(Parent root) throws IOException {
        primaryStage.getScene().setRoot(root);
    }

    // Go to profile page
    public static void profilePageButtonOnAction() throws IOException {
        Session session = Session.getInstance();
        User user = null;
        if (session != null && session.getCurrentUser() != null) {
            user = session.getCurrentUser();
        }

        String userType = user.getUserType();

        if(userType.equals("Customer")) {
            NavigationManager.navigate("profileCustomer.fxml");
        } else {
            NavigationManager.navigate("profileOwner.fxml");
        }
    }
}
