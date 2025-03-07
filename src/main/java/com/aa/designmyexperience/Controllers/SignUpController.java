package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpController {

    @FXML
    private Text loginPageLink;

    @FXML
    private ChoiceBox<String> accountTypeChoice;

    @FXML
    private TextField emailField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpButton;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private void initialize() {
        configureLoginUpLink();
        accountTypeChoice.getItems().addAll("Customer", "Owner");

    }

    /* The Sign Up button will follow create a user in the database */
    public void signUpOnAction(ActionEvent e) {

        if (emailField.getText().isEmpty() || firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()
                || passwordField.getText().isEmpty() || accountTypeChoice.getValue().isEmpty()) {
            errorMessageLabel.setText("Please fill all the fields");
        } else {
            validateSignUp();
        }

    }

    public void validateSignUp() {
        String email = emailField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String password = passwordField.getText();
        String accountType = accountTypeChoice.getValue();

        try {
            User existingUser = DBconnect.getUser(email);

            if (existingUser != null) {
                errorMessageLabel.setText("This email already exists");
                return;
            }

            /* We create a new instance of User */
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setPassword(password);
            newUser.setUserType(accountType);
            newUser.setPhoto("");

            /* We insert the user in the database and create the session */
            DBconnect.createUser(newUser);
            Session.createSession(newUser);

            NavigationManager.navigate("home.fxml");


        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Get to the "Log In" View
    private void configureLoginUpLink() {
        loginPageLink.setOnMouseClicked(event -> {
            try {
                NavigationManager.navigate("login.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
