package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;

import java.io.IOException;
import java.sql.SQLException;

public class OwnerController {

    @FXML
    private Label companyName, numberActivitiesLabel;
    @FXML
    private ImageView companyLogo;


    int NumberActivities = 0;
    int userId;

    @FXML
    private void initialize() throws SQLException {
        /* Get the session with the current user */
        Session session = Session.getInstance();

        if (session != null && session.getCurrentUser() != null) {
            User user = session.getCurrentUser();
            userId = user.getId();

            /* We get the current User information in the Session */
            String photo = user.getPhoto();
            Image profilePicture = new Image(photo);
            companyLogo.setImage(profilePicture);

            companyName.setText(user.getFirstName() + " " + user.getLastName());
        }

        NumberActivities = DBconnect.countEventsOwner(userId);
        numberActivitiesLabel.setText(String.valueOf(NumberActivities));

    }


    /* Create a logout from the current session */
    @FXML
    private void logoutOnAction(ActionEvent e) throws IOException {
        Session.clearSession();
        NavigationManager.navigate("login.fxml");
    }

    /* Go back to the home page */
    @FXML
    public void homeButtonOnAction(ActionEvent e) throws IOException {
        NavigationManager.navigate("home.fxml");
    }

    /* Add an event */
    @FXML
    public void addEventOnAction(ActionEvent e) throws IOException {
        NavigationManager.navigate("addEvent.fxml");
    }
}
