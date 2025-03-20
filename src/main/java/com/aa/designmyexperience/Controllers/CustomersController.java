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

public class CustomersController {
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
    private int userId; // ID de l'utilisateur connecte
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
            // Ajout d'une verification pour eviter NullPointerException
            String backgroundPath = "/com/aa/designmyexperience/Images/background.jpg";
            String profilePath = "/com/aa/designmyexperience/Images/profile-photo.jpg";

            if (getClass().getResource(backgroundPath) == null) {
                showAlert("Erreur", "Image de fond introuvable : " + backgroundPath);
            } else {
                backgroundImage.setImage(new Image(getClass().getResource(backgroundPath).toExternalForm()));
            }

            if (getClass().getResource(profilePath) == null) {
                showAlert("Erreur", "Image de profil introuvable : " + profilePath);
            } else {
                profileImage.setImage(new Image(getClass().getResource(profilePath).toExternalForm()));
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/home.fxml"));
            Parent root = loader.load();
            // TODO: Creer HomeController avec les methodes setConnection et setUserId
            // HomeController controller = loader.getController();
            // controller.setConnection(connection);
            // controller.setUserId(userId);

            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la page d'accueil : " + e.getMessage());
        }
    }

    @FXML
    private void logoutOnAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de la deconnexion : " + e.getMessage());
        }
    }

    @FXML
    private void handlePayment() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/bank.fxml"));
            Parent root = loader.load();
            // TODO: Creer BankController avec les methodes setConnection et setUserId
            // BankController controller = loader.getController();
            // controller.setConnection(connection);
            // controller.setUserId(userId);

            Stage stage = (Stage) paymentButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la page bancaire : " + e.getMessage());
        }
    }

    @FXML
    private void handleHistory() {
        if (!hasPaymentMethod) {
            showAlert("Information", "Veuillez d'abord ajouter un moyen de paiement.");
            handlePayment(); // Rediriger vers la page d'ajout de paiement
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/payment.fxml"));
            Parent root = loader.load();
            // TODO: Creer PaymentManagementController avec les methodes setConnection et setUserId
            // PaymentManagementController controller = loader.getController();
            // controller.setConnection(connection);
            // controller.setUserId(userId);

            Stage stage = (Stage) historyButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la page d'historique : " + e.getMessage());
        }
    }

    @FXML
    private void handleEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/editProfile.fxml"));
            Parent root = loader.load();
            // TODO: Creer EditProfileController avec les methodes setConnection et setUserId
            // EditProfileController controller = loader.getController();
            // controller.setConnection(connection);
            // controller.setUserId(userId);

            Stage stage = (Stage) editButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la page de modification : " + e.getMessage());
        }
    }

    // Affiche une alerte a l'utilisateur
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
