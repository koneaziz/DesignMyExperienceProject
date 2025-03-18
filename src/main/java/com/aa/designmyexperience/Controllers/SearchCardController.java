package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

    String pattern = "dd/MM/yyyy HH:mm"; // We create the way the date will be visualize
    DateFormat df = new SimpleDateFormat(pattern);

    /* We will set the Event informations */
    public void setSearchEvent(Event event) {

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


}
