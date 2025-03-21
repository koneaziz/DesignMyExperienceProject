package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Event;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CardController {

    @FXML
    private Label EventNameLabel;
    @FXML
    private Label CategoryLabel;
    @FXML
    private Label DateLabel;
    @FXML
    private Label LocationLabel;
    @FXML
    private Label PriceLabel;
    @FXML
    private ImageView EventImage;

    String pattern = "dd/MM/yyyy HH:mm"; // We create the way the date will be visualize
    DateFormat df = new SimpleDateFormat(pattern);
    private Event event;

    /* We will set the Event informations */
    public void setEvent(Event event) {
        this.event = event;
        EventNameLabel.setText(event.getEventTitle());
        CategoryLabel.setText(event.getEventCategory());
        DateLabel.setText(df.format(event.getEventDate()));
        LocationLabel.setText(event.getEventLocation());
        PriceLabel.setText(String.valueOf(event.getEventPrice()));

        if (event.getEventImage() != null && !event.getEventImage().isEmpty()) {
            Image image = new Image(event.getEventImage(), true);
            EventImage.setImage(image);
        }
    }

    @FXML
    private void handleCardClick(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/event-details.fxml"));
            Parent detailRoot = loader.load();
            EventDetailsController detailsController = loader.getController();
            detailsController.setEventId(this.event.getEventId());

            NavigationManager.setRoot(detailRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
