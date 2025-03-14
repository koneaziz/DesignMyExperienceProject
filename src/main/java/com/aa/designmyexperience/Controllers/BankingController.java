package com.aa.designmyexperience.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.awt.Desktop;
import java.net.URI;

public class BankingController {

    @FXML
    private TextField cardholderName;

    @FXML
    private TextField cardNumber;

    @FXML
    private TextField expiryDate;

    @FXML
    private TextField cvv;

    @FXML
    private Button savePaymentButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button paypalButton;

    /**
     * Méthode appelée lors de l'enregistrement des informations bancaires.
     */
    @FXML
    private void handleSavePayment() {
        String name = cardholderName.getText().trim();
        String cardNum = cardNumber.getText().trim();
        String expiry = expiryDate.getText().trim();
        String cvvCode = cvv.getText().trim();

        // Vérifier si tous les champs sont remplis
        if (name.isEmpty() || cardNum.isEmpty() || expiry.isEmpty() || cvvCode.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        // Vérifier le format du numéro de carte (16 chiffres)
        if (!cardNum.matches("\\d{16}")) {
            showAlert("Erreur", "Le numéro de carte doit contenir 16 chiffres.");
            return;
        }

        // Vérifier le format de la date d'expiration (MM/AA)
        if (!expiry.matches("\\d{2}/\\d{2}")) {
            showAlert("Erreur", "La date d'expiration doit être au format MM/AA.");
            return;
        }

        // Vérifier le format du CVV (3 chiffres)
        if (!cvvCode.matches("\\d{3}")) {
            showAlert("Erreur", "Le CVV doit contenir 3 chiffres.");
            return;
        }

        // Sauvegarde des informations de paiement (Simulation)
        showAlert("Succès", "Informations bancaires enregistrées avec succès !");
    }

    /**
     * Méthode appelée lors de la connexion à PayPal.
     */
    @FXML
    private void handlePaypalLogin() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.paypal.com/signin"));
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir PayPal.");
        }
    }

    /**
     * Affiche une alerte à l'utilisateur.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
