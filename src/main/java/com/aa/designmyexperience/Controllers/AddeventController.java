package com.aa.designmyexperience.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddeventController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField eventDateField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField maxParticipantsField;

    @FXML
    private TextField discountField;

    @FXML
    private TextField imageField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button backButton;

    private Connection connection; // Connexion a la base de donnees
    private int userId; // ID de lutilisateur connecte (business_id)

    // Methodes pour initialiser la connexion et l'ID utilisateur
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @FXML
    private void handleSaveEvent() {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String eventDate = eventDateField.getText().trim();
        String location = locationField.getText().trim();
        String price = priceField.getText().trim();
        String maxParticipants = maxParticipantsField.getText().trim();
        String discount = discountField.getText().trim();
        String image = imageField.getText().trim();

        // Verifier les champs obligatoires
        if (title.isEmpty() || description.isEmpty() || eventDate.isEmpty() || location.isEmpty() || price.isEmpty() || maxParticipants.isEmpty()) {
            showAlert("Erreur", "Tous les champs obligatoires doivent etre remplis !");
            return;
        }

        // Verifier le format de la date et heure (YYYY-MM-DD HH:MM)
        LocalDateTime eventDateTime;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            eventDateTime = LocalDateTime.parse(eventDate, formatter);
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "La date et lheure doivent etre au format YYYY-MM-DD HH:MM (par exemple, 2025-12-31 14:30).");
            return;
        }

        // Verifier que la date est dans le futur
        if (eventDateTime.isBefore(LocalDateTime.now())) {
            showAlert("Erreur", "La date de levenement doit etre dans le futur.");
            return;
        }

        // Verifier le format du prix (doit etre un nombre decimal)
        double eventPrice;
        try {
            eventPrice = Double.parseDouble(price);
            if (eventPrice < 0) {
                showAlert("Erreur", "Le prix ne peut pas etre negatif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit etre un nombre valide (par exemple, 29.99).");
            return;
        }

        // Verifier le format de la capacite maximale (doit etre un entier positif)
        int maxParticipantsValue;
        try {
            maxParticipantsValue = Integer.parseInt(maxParticipants);
            if (maxParticipantsValue <= 0) {
                showAlert("Erreur", "La capacite maximale doit etre un nombre positif superieur a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "La capacite maximale doit etre un nombre entier valide.");
            return;
        }

        // Verifier le format de la reduction (optionnel, doit etre un nombre decimal entre 0 et 100)
        Double discountValue = null;
        if (!discount.isEmpty()) {
            try {
                discountValue = Double.parseDouble(discount);
                if (discountValue < 0 || discountValue > 100) {
                    showAlert("Erreur", "La reduction doit etre un pourcentage entre 0 et 100.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Erreur", "La reduction doit etre un nombre valide (par exemple, 10.50 pour 10,5%).");
                return;
            }
        }

        // Sauvegarder levenement dans la base de donnees
        String query = "INSERT INTO events (business_id, title, description, event_date, location, price, max_participants, registered_participants, discount, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId); // business_id
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setString(4, eventDate);
            stmt.setString(5, location);
            stmt.setDouble(6, eventPrice);
            stmt.setInt(7, maxParticipantsValue);
            stmt.setInt(8, 0); // registered_participants initialise a 0
            if (discountValue != null) {
                stmt.setDouble(9, discountValue);
            } else {
                stmt.setNull(9, java.sql.Types.DECIMAL);
            }
            if (!image.isEmpty()) {
                stmt.setString(10, image);
            } else {
                stmt.setNull(10, java.sql.Types.VARCHAR);
            }
            stmt.executeUpdate();
            showAlert("Succes", "Evenement ajoute avec succes !");

            // Retourner a la page precedente apres sauvegarde
            handleBack();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de lenregistrement de levenement : " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        // Retourner a la page precedente sans sauvegarder
        handleBack();
    }

    @FXML
    private void handleBack() {
        try {
            // TODO: Remplacer par la page appropriee (par exemple, ownerProfile.fxml pour un business_owner)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/profile.fxml"));
            Parent root = loader.load();
            CustomerController controller = loader.getController();
            controller.setConnection(connection);
            controller.setUserId(userId);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du retour a la page precedente : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
