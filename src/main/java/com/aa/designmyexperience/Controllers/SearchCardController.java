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
import java.time.format.DateTimeFormatter;

public class SearchCardController {

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

    private Event event;

    String pattern = "dd/MM/yyyy"; // We create the way the date will be visualize
    DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);

    /* We will set the Event informations */
    public void setSearchEvent(Event event) {
        this.event = event;
        EventNameLabel.setText(event.getEventTitle());
        CategoryLabel.setText(event.getEventCategory());
        DateLabel.setText(event.getEventDate().format(df));
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
