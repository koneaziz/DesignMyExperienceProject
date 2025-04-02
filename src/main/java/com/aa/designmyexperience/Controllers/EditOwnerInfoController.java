package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.UUID;

import static com.aa.designmyexperience.Util.DBconnect.showAlert;

public class EditOwnerInfoController {


    @FXML
    private TextField FirstNameField, LastNameField, EmailField;
    @FXML
    private ImageView imageField;

    private int userId;
    private File imageFile;
    private User user;

    @FXML
    private void initialize() {
        /* Get the session with the current user */
        Session session = Session.getInstance();

        if (session != null && session.getCurrentUser() != null) {
            this.user = session.getCurrentUser();
            this.userId = this.user.getId();
        }

        FirstNameField.setText(user.getFirstName());
        LastNameField.setText(user.getLastName());
        EmailField.setText(user.getEmail());
        Image image = new Image(user.getPhoto());
        imageField.setImage(image);
    }



    @FXML
    private void handleSaveEvent(ActionEvent event) throws IOException, SQLException {
        String FirstName = FirstNameField.getText().trim();
        String LastName = LastNameField.getText().trim();
        String Email = EmailField.getText().trim();
        String imageUrl = user.getPhoto();

        if (FirstName.isEmpty() || LastName.isEmpty()) {
            showAlert("Error", "Enter all the fields !");
            return;
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

        }


        // Save it as an user
        User userMod = new User(this.userId,FirstName,LastName,Email,imageUrl);

        // Insert in the database
        DBconnect.modifyUserInfo(userMod);

        // Go back to the profile
        NavigationManager.navigate("profileCustomer.fxml");

    }




    @FXML
    private void loadFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All images", "*.jpg", "*.png","*.webp"));

        Stage stage = (Stage) imageField.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String originalName = selectedFile.getName();
            int dotIndex = originalName.lastIndexOf('.');
            String extension = (dotIndex > 0) ? originalName.substring(dotIndex) : "";

            String newName = UUID.randomUUID().toString() + extension;

            File newFile = new File(System.getProperty("java.io.tmpdir"), newName);

            Files.copy(selectedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
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
