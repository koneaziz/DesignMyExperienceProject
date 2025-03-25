package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Event;
import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import com.aa.designmyexperience.Controllers.CustomersController;
import javafx.util.StringConverter;

import static com.aa.designmyexperience.Util.DBconnect.showAlert;

public class AddEventController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker eventDateField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField priceField, categoryField;

    @FXML
    private TextField maxParticipantsField;

    @FXML
    private TextField discountField;

    @FXML
    private ImageView imageField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button backButton;

    private Connection connection; // Connexion a la base de donnees
    private int userId; // ID of connected user (business_id)

    @FXML
    private void initialize() {
        /* Get the session with the current user */
        Session session = Session.getInstance();

        if (session != null && session.getCurrentUser() != null) {
            User user = session.getCurrentUser();
            userId = user.getId();
        }

        /* We will format the date */
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        eventDateField.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }
            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;
            }
        });
    }

    @FXML
    private void handleSaveEvent() {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        LocalDate eventDate = eventDateField.getValue();
        String location = locationField.getText().trim();
        String price = priceField.getText().trim();
        String category = categoryField.getText().trim();
        String maxParticipants = maxParticipantsField.getText().trim();
        String discount = discountField.getText().trim();
        //image = imageField.getText().trim();

        // Verify if the important fields are entered
        if (title.isEmpty() || description.isEmpty() || eventDate == null || location.isEmpty() || price.isEmpty() || maxParticipants.isEmpty()) {
            showAlert("Erreur", "Enter all the fields !");
            return;
        }



        // Verify if date is in the future
        if (eventDate.isBefore(ChronoLocalDate.from(LocalDateTime.now()))) {
            showAlert("Erreur", "The date has to be in the future.");
            return;
        }

        // Verify if the price is in the right format
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

        // Verify if the max participants is positive
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

        // Verify the format of the discount
        double discountValue = 0;
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

        // Save it as an event
        Event event = new Event(userId, title, description, eventDate, location, eventPrice, category, maxParticipantsValue, 0, discountValue);

    }

    @FXML
    private void loadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG image", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG image", "*.png"),
                new FileChooser.ExtensionFilter("All images", "*.jpg", "*.png"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageField.setImage(image);
        } else {
            showAlert("Not Found", "No image selected.");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        NavigationManager.navigate("profileOwner.fxml");
    }

}
