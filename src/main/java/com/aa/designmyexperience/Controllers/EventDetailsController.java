package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Event;
import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.sql.SQLException;

public class EventDetailsController {

    @FXML
    private Label fnameLabel;
    @FXML
    private Label lnameLabel;
    @FXML
    private Circle profilPicture;
    @FXML
    private Label ActivityTitleLabel, DateLabel, LocationLabel, ParticipantsLabel, ActivityDescriptionLabel;
    @FXML
    private Label priceLabel, numberTickets, totalLabel, discountLabel, ownerLabel ;
    @FXML
    private Button bookButton, discountButton;
    @FXML
    private Spinner<Integer> ticketSpinner;
    @FXML
    private TextField discountField, searchField;;
    @FXML
    private ImageView ActivityImage;

    private int eventId;
    private Event event;
    private double eventPrice;
    private int number;
    private User owner;

    @FXML
    private void initialize() throws SQLException {
        /* Get the session with the current user */
        Session session = Session.getInstance();

        if (session != null && session.getCurrentUser() != null) {
            User user = session.getCurrentUser();

            /* We get the current User information in the Session */
            String photo = user.getPhoto();
            Image profilePicture = new Image(photo);
            profilPicture.setFill(new ImagePattern(profilePicture, 0, 0, 1, 1, true));

            fnameLabel.setText(user.getFirstName());
            lnameLabel.setText(user.getLastName());
        }


        /* Set and get the value of the Spinner (Number of tickets) */
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 1);
        ticketSpinner.setValueFactory(valueFactory);

        number = ticketSpinner.getValue();
        numberTickets.setText(String.valueOf(number));
        totalLabel.setText(String.valueOf((number * eventPrice)));

        ticketSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {

                number = ticketSpinner.getValue();
                numberTickets.setText(String.valueOf(number));
                totalLabel.setText(String.valueOf(number * eventPrice));
            }
        });


    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
        loadEventDetails();
    }

    // Méthode pour récupérer les détails depuis la base de données et mettre à jour l'interface
    private void loadEventDetails() {
        try {
            this.event = DBconnect.getEventById(eventId);
            this.owner = DBconnect.getUserByID(event.getEventOwner());

            if(event != null) {
                ActivityTitleLabel.setText(event.getEventTitle());
                ActivityDescriptionLabel.setText(event.getEventDescription());
                priceLabel.setText(String.valueOf(event.getEventPrice()));
                LocationLabel.setText(event.getEventLocation());
                DateLabel.setText(String.valueOf(event.getEventDate()));
                ParticipantsLabel.setText(String.valueOf(event.getEventRegisteredParticipants()));
                ActivityImage.setImage(new Image(event.getEventImage()));
                ownerLabel.setText(owner.getFirstName() + " " + owner.getLastName());


                this.eventPrice = event.getEventPrice();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void homeButtonOnAction(ActionEvent event) throws IOException {
        NavigationManager.navigate("home.fxml");
    }

    @FXML
    /* Go to search page */
    public void searchButtonOnAction(ActionEvent actionEvent) throws IOException {
        String searchText = searchField.getText();

        if (searchText != null && !searchText.isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/search.fxml"));
            Parent searchRoot = loader.load();
            SearchController searchController = loader.getController();
            searchController.getSearchText(searchText);

            NavigationManager.setRoot(searchRoot);

        }
    }

    @FXML
    /* Go to the profile page */
    public void profilePageButtonOnAction(ActionEvent actionEvent) throws IOException {
        NavigationManager.navigate("profileCustomer.fxml");
    }
}
