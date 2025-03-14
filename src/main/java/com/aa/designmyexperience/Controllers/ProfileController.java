package com.aa.designmyexperience.Controllers;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ProfileController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private ImageView profileImage;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private Button logoutButton;
    @FXML
    private Button paymentButton;
    @FXML
    private Button historyButton;
    @FXML
    private Button editButton;

    private HostServices hostServices;
    private boolean hasPaymentMethod = false;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    private void initialize() {
        nameLabel.setText("Name: John Doe");
        emailLabel.setText("Email: john.doe@example.com");
        profileImage.setImage(new Image(getClass().getResource("/com/aa/designmyexperience/Images/profile-photo.jpg").toExternalForm()));
        backgroundImage.setImage(new Image(getClass().getResource("/com/aa/designmyexperience/Images/background.jpg").toExternalForm()));

        if (!hasPaymentMethod) {
            paymentButton.setDisable(true);
            historyButton.setDisable(true);
        }
    }

    @FXML
    private void handleLogout() {
        System.out.println("Logout...");
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handlePayment() {
        if (!hasPaymentMethod) {
            System.out.println("Redirecting to payment addition page...");
            openPaymentPage();
        } else {
            System.out.println("Modify payment method...");
        }
    }

    @FXML
    private void handleHistory() {
        if (hasPaymentMethod) {
            System.out.println("Display booking history...");
        } else {
            System.out.println("No payment method registered. Redirecting to payment addition page...");
            openPaymentPage();
        }
    }

    @FXML
    private void handleEdit() {
        System.out.println("Edit personal information...");
    }

    private void openPaymentPage() {
        System.out.println("Opening payment addition page...");
    }
}