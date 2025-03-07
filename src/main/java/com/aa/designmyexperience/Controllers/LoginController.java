package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginController {

    /* FXML variables */
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text signUpLink;
    @FXML
    private Label loginMessageLabel;


    @FXML
    private void initialize() {
        configureSignUpLink();

    }

    /* The login button will follow this action */
    public void loginButtonOnAction (ActionEvent e) {

        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty() ) {
            loginMessageLabel.setText("Please fill all the fields");
        } else {
            validateLogin();
        }
    }

    public void validateLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            User user = DBconnect.getUser(email);

            if (user == null) {
                loginMessageLabel.setText("User not found.");
                return;
            }

            if(!user.getPassword().equals(password)){
                loginMessageLabel.setText("Invalid credentials.");
                return;
            }

            Session.createSession(user); // We create a session with the user
            NavigationManager.navigate("home.fxml");

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Get to the "Sign Up" View
    private void configureSignUpLink() {
        signUpLink.setOnMouseClicked(event -> {
            try {
                NavigationManager.navigate("signup.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
