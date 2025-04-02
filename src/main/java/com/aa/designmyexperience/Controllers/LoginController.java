package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.EmailSender;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static com.aa.designmyexperience.Util.DBconnect.showAlert;

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

    /* Validate the login of the user */
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

    /* Prompt the user to get his password */
    public void handleForgotPassword() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Password Recovery");
        dialog.setHeaderText("Forgot Password");
        dialog.setContentText("Please enter your email:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(email -> {
            email = email.trim();
            if (email.isEmpty()) {
                showAlert("Error", "Please enter a valid email address.");
                return;
            }

            try {
                User user = DBconnect.getUser(email);

                if (user != null) {
                    String subject = "Your Password Recovery";
                    String body =
                            "Dear" + user.getFirstName() + ",\n" +
                            "Your password is: " + user.getPassword() + "\n" +
                            "Please change your password after logging in." +
                            "\n\n" +
                            "DesignMyExperience";
                    EmailSender.sendEmail(email, subject, body);
                    showAlert("Success", "Your password has been sent to your email.");
                } else {
                    showAlert("Error", "Email not found in our records.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
