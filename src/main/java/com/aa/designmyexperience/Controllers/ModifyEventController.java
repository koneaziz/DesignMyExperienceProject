package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Event;
import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;

import static com.aa.designmyexperience.Util.DBconnect.showAlert;

public class ModifyEventController {

    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker eventDateField, eventEndDateField;
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

    private int userId;
    private Event event;
    private File imageFile;

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

    public void setEvent(Event event) {
        this.event = event;
        updateEventInfo();
    }

    public void updateEventInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        titleField.setText(event.getEventTitle());
        descriptionField.setText(event.getEventDescription());
        categoryField.setText(event.getEventCategory());
        eventDateField.setValue(event.getEventDate());
        eventEndDateField.setValue(event.getEventEndDate());
        locationField.setText(event.getEventLocation());
        priceField.setText(String.valueOf(event.getEventPrice()));
        maxParticipantsField.setText(String.valueOf(event.getEventMaxParticipants()));
        discountField.setText(String.valueOf(event.getEventDiscount()));
        Image image = new Image(event.getEventImage());
        imageField.setImage(image);

    }

    @FXML
    private void handleSaveEvent() throws SQLException, IOException {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        LocalDate eventDate = eventDateField.getValue();
        LocalDate eventEndDate = eventEndDateField.getValue();
        String location = locationField.getText().trim();
        String price = priceField.getText().trim();
        String category = categoryField.getText().trim();
        String maxParticipants = maxParticipantsField.getText().trim();
        String discount = discountField.getText().trim();
        String imageUrl = event.getEventImage();
        int eventId = event.getEventId();

        // Verify if the important fields are entered
        if (title.isEmpty() || description.isEmpty() || eventDate == null || eventEndDate == null || location.isEmpty() || price.isEmpty() || maxParticipants.isEmpty() || categoryField.getText().trim().isEmpty()) {
            showAlert("Error", "Enter all the fields !");
            return;
        }


        // Verify if date is in the future
        if (eventDate.isBefore(ChronoLocalDate.from(LocalDateTime.now()))) {
            showAlert("Error", "The date has to be in the future.");
            return;
        }

        // Ensure that the end date is not before the start date
        if (eventEndDate.isBefore(eventDate)) {
            showAlert("Error", "The end date cannot be before the start date.");
            return;
        }

        // Ensure that the end date is not more than 3 months after the start date
        if (eventEndDate.isAfter(eventDate.plusMonths(3))) {
            showAlert("Error", "The end date cannot be more than 3 months after the start date.");
            return;
        }

        // Verify if the price is in the right format
        double eventPrice;
        try {
            eventPrice = Double.parseDouble(price);
            if (eventPrice < 0) {
                showAlert("Error", "The price has to be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "The price has to be a valid number.");
            return;
        }

        // Verify if the max participants is positive
        int maxParticipantsValue;
        try {
            maxParticipantsValue = Integer.parseInt(maxParticipants);
            if (maxParticipantsValue <= 0) {
                showAlert("Error", "The max participants has to be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "The maximum capacity has to be an integer.");
            return;
        }

        int registeredParticipants = event.getEventRegisteredParticipants();

        // Verify the format of the discount
        double discountValue = 0;
        if (!discount.isEmpty()) {
            try {
                discountValue = Double.parseDouble(discount);
                if (discountValue < 0 || discountValue > 100) {
                    showAlert("Error", "The reduction has to be between 0 and 100.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "The reduction has to be a number.");
                return;
            }
        }

        // Get the image url
        if (this.imageFile != null) {
            String containerName = "designmyexperience";
            String sasToken = "sv=2024-11-04&ss=bfqt&srt=sco&sp=rwdlacupiytfx&se=2025-04-26T18:25:34Z&st=2025-03-26T11:25:34Z&sip=0.0.0.0-255.255.255.255&spr=https&sig=ypipIDCLtrTBoGg1%2FeHp9MPD5hjchC0jN9KDPSt%2BpSQ%3D";
            String blobUrlWithSas = "https://designmyexperience.blob.core.windows.net/"
                    + containerName + "/"
                    + imageFile.getName()
                    + "?" + sasToken;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(blobUrlWithSas))

                    .header("x-ms-blob-type", "BlockBlob")
                    .PUT(HttpRequest.BodyPublishers.ofFile(imageFile.toPath()))
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 201 || response.statusCode() == 200) {
                    // We get the url of the blob
                    imageUrl = blobUrlWithSas;
                    System.out.println("Upload successful, image URL: " + imageUrl);
                } else {
                    showAlert("Upload Error", "Failed to upload image. Status code: " + response.statusCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Upload Error", "An error occurred: " + e.getMessage());
            }

        } else {
            imageUrl = event.getEventImage();
        }

        // Save it as an event
        Event event = new Event(eventId,userId, title, description, eventDate, eventEndDate, location, eventPrice, category, maxParticipantsValue, registeredParticipants, discountValue, imageUrl);

        // Insert in the database
        DBconnect.modifyEvent(event);

        // Go back to the profile
        NavigationManager.navigate("profileOwner.fxml");
    }

    @FXML
    private void loadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All images", "*.jpg", "*.png","*.webp"));

        Stage stage = (Stage) imageField.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageField.setImage(image);
        } else {
            showAlert("Not Found", "No image selected.");
        }

        this.imageFile = selectedFile;

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
