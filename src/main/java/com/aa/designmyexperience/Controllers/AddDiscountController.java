package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Discount;
import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;

import static com.aa.designmyexperience.Util.DBconnect.showAlert;

public class AddDiscountController {

    @FXML
    private TextField discountCodeField, discountPercentageField;
    @FXML
    private DatePicker discountEndDateField;

    private int userId; // ID of connected user (business_id)

    @FXML
    private void initialize() {
        /* Get the session with the current user */
        Session session = Session.getInstance();

        if (session != null && session.getCurrentUser() != null) {
            User user = session.getCurrentUser();
            userId = user.getId();
        }

        TextFormatter<String> percentageFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            try {
                int value = Integer.parseInt(newText);
                if (value >= 0 && value <= 100) {
                    return change;
                } else {
                    return null;
                }
            } catch (NumberFormatException e) {
                return null;
            }
        });
        discountPercentageField.setTextFormatter(percentageFormatter);

        /* We will format the date */
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        discountEndDateField.setConverter(new StringConverter<LocalDate>() {
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
    private void handleSaveEvent() throws SQLException, IOException {
        String code = discountCodeField.getText();
        double percentage = Double.parseDouble(discountPercentageField.getText());
        LocalDate endDate = discountEndDateField.getValue();

        // Verify if date is in the future
        if (endDate.isBefore(ChronoLocalDate.from(LocalDateTime.now()))) {
            showAlert("Error", "The date has to be in the future.");
            return;
        }

        // Create the instance
        Discount discount = new Discount(code, this.userId, percentage, endDate);

        // Add in the database
        DBconnect.addDiscount(discount);

        // Go back to the profile
        NavigationManager.navigate("profileOwner.fxml");



    }

    @FXML
    private void handleBack() throws IOException {
        NavigationManager.navigate("profileOwner.fxml");
    }

    @FXML
    private void homeButtonOnAction(ActionEvent event) throws IOException {
        NavigationManager.navigate("home.fxml");
    }
}
