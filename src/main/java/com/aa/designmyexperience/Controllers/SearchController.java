package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.awt.*;

public class SearchController {

    @FXML
    private Label fnameLabel;
    @FXML
    private Label lnameLabel;
    @FXML
    private Circle profilPicture;
    @FXML
    private HBox searchContainer;

    @FXML
    private void initialize() {
        /* Get the session with the current user */
        Session session = Session.getInstance();

        if (session != null && session.getCurrentUser() != null) {
            User user = session.getCurrentUser();

            /* We get the current User information in the Session */
            String photo = user.getPhoto();
            Image profilePicture = new Image(photo);
            profilPicture.setFill(new ImagePattern(profilePicture, 0, 0, 1, 1, true));

            fnameLabel.setText(user.getFirstName());
            lnameLabel.setText(user.getLastName());
        } else {
            fnameLabel.setText("Welcome, guest!");
        }




    }



    private void addSearchEvent(HBox hBox) {


    }
}
