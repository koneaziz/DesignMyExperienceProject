package com.aa.designmyexperience.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerController {
    @FXML
    private Button homeButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button paymentButton;
    @FXML
    private Button historyButton;
    @FXML
    private Button editButton;
    @FXML
    private Label nameLabel;
    @FXML
    private Label userTypeLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private ImageView profileImage;

    private Connection connection; // Connexion a la base de donnees
    private int userId; // ID de lutilisateur connecte
    private boolean hasPaymentMethod = false; // Indique si un moyen de paiement est enregistre

    // Methodes pour initialiser la connexion et l'ID utilisateur
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        loadUserData(); // Charger les donnees utilisateur apres avoir defini l'ID
    }

    @FXML
    private void initialize() {
        // Charger les images
        try {
            String backgroundPath = "/com/aa/designmyexperience/Images/background.jpg";
            String profilePath = "/com/aa/designmyexperience/Images/profile-photo.jpg";

            Image background = loadImage(backgroundPath);
            if (background != null) {
                backgroundImage.setImage(background);
            }

            Image profile = loadImage(profilePath);
            if (profile != null) {
                profileImage.setImage(profile);
            }
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement des images : " + e.getMessage());
        }

        // Verifier si un moyen de paiement est enregistre
        checkPaymentMethod();
        if (!hasPaymentMethod) {
            paymentButton.setText("Add Payment Method");
            historyButton.setDisable(true); // Desactiver l'historique si aucun moyen de paiement
        } else {
            paymentButton.setText("Modify Payment Method");
        }
    }

    // Charger les donnees utilisateur depuis la base de donnees
    private void loadUserData() {
        if (connection == null) {
            showAlert("Erreur", "Connexion a la base de donnees non initialisee.");
            return;
        }

        String query = "SELECT name, email, enum FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String role = rs.getString("enum"); // client ou business_owner

                nameLabel.setText(name);
                emailLabel.setText(email);
                userTypeLabel.setText(role);
            } else {
                showAlert("Erreur", "Utilisateur non trouve dans la base de donnees.");
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des donnees utilisateur : " + e.getMessage());
        }
    }

    // Verifier si un moyen de paiement est enregistre
    private void checkPaymentMethod() {
        if (connection == null) {
            showAlert("Erreur", "Connexion a la base de donnees non initialisee.");
            return;
        }

        String query = "SELECT COUNT(*) FROM payment_methods WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                hasPaymentMethod = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la verification du moyen de paiement : " + e.getMessage());
        }
    }

    @FXML
    private void homeButtonOnAction() {
        navigateTo("/com/aa/designmyexperience/home.fxml", homeButton, null);
    }

    @FXML
    private void logoutOnAction() {
        navigateTo("/com/aa/designmyexperience/login.fxml", logoutButton, null);
    }

    @FXML
    private void handlePayment() {
        navigateTo("/com/aa/designmyexperience/bank.fxml", paymentButton, null);
    }

    @FXML
    private void handleHistory() {
        if (!hasPaymentMethod) {
            showAlert("Information", "Veuillez d'abord ajouter un moyen de paiement.");
            handlePayment(); // Rediriger vers la page d'ajout de paiement
            return;
        }

        navigateTo("/com/aa/designmyexperience/payment.fxml", historyButton, null);
    }

    @FXML
    private void handleEdit() {
        navigateTo("/com/aa/designmyexperience/editProfile.fxml", editButton, null);
    }

    // Methode utilitaire pour la navigation
    private void navigateTo(String fxmlPath, Button button, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            if (controller != null) {
                // Si un controleur est fourni, configurer la connexion et l'ID utilisateur
                if (controller instanceof CustomerController) {
                    CustomerController ctrl = (CustomerController) controller;
                    ctrl.setConnection(connection);
                    ctrl.setUserId(userId);
                }
                // Ajouter d'autres types de controleurs si necessaire
            }
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la page : " + e.getMessage());
        }
    }

    // Methode utilitaire pour charger une image
    private Image loadImage(String path) {
        try {
            if (getClass().getResource(path) == null) {
                showAlert("Erreur", "Image introuvable : " + path);
                return null;
            }
            return new Image(getClass().getResource(path).toExternalForm());
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement de l'image : " + e.getMessage());
            return null;
        }
    }

    // Affiche une alerte a lutilisateur
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
