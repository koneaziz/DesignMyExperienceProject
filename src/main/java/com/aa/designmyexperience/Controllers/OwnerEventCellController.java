package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Event;
import com.aa.designmyexperience.Models.Order;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.aa.designmyexperience.Util.DBconnect.showAlert;

public class OwnerEventCellController {

    @FXML
    private Label EventIdLabel, EventStartDateLabel, EventEndDateLabel, EventPriceLabel, EventLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Circle EventImage;

    private Event event;

    public void setEvent(Event event) {
        this.event = event;
        updateEventInfo();
    }

    private void updateEventInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        EventIdLabel.setText(String.valueOf(event.getEventId()));
        EventStartDateLabel.setText(event.getEventDate().format(formatter));
        EventEndDateLabel.setText(event.getEventEndDate().format(formatter));
        EventLabel.setText(event.getEventTitle());
        EventPriceLabel.setText(String.valueOf(event.getEventPrice()));

        Image eventImage = new Image(event.getEventImage());
        EventImage.setFill(new ImagePattern(eventImage, 0, 0, 1, 1, true));

    }

    @FXML
    private void handleModify() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/modify-event.fxml"));
            Parent modifyRoot = loader.load();

            ModifyEventController modifyController = loader.getController();

            modifyController.setEvent(this.event);

            NavigationManager.setRoot(modifyRoot);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load modify event page: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        if (event != null) {

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Delete Event");
            confirmAlert.setHeaderText("Are you sure you want to delete this event ?");
            confirmAlert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {

                    DBconnect.deleteEvent(event.getEventId());

                    cancelButton.setDisable(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
