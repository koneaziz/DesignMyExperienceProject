package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Discount;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.aa.designmyexperience.Util.DBconnect.showAlert;

public class EventDetailsController {

    @FXML
    private Label fnameLabel;
    @FXML
    private Label lnameLabel;
    @FXML
    private Circle profilPicture;
    @FXML
    private Label ActivityTitleLabel, DateLabel, EndDateLabel, LocationLabel, ParticipantsLabel, ActivityDescriptionLabel;
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
    @FXML
    private DatePicker bookDate;

    private int eventId;
    private Event event;
    private double eventPrice;
    private double discountNumber = 0;
    private int number;
    private User user;
    private User owner;

    @FXML
    private void initialize() throws SQLException {
        /* Get the session with the current user */
        Session session = Session.getInstance();

        if (session != null && session.getCurrentUser() != null) {
            this.user = session.getCurrentUser();

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
                totalLabel.setText( ((number * eventPrice) - ((number * eventPrice)* discountNumber /100)) + " £");
            }
        });


    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
        loadEventDetails();
        try {
            if (DBconnect.isEventAlreadyBooked(user.getId(), this.eventId)) {
                bookButton.setDisable(true);
                bookButton.setText("Already booked");
            }
        }catch (SQLException e){
                e.printStackTrace();
            }
        }

    // Extract all the data of the event
    private void loadEventDetails() {
        try {
            this.event = DBconnect.getEventById(eventId);
            this.owner = DBconnect.getUserByID(event.getEventOwner());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            if(event != null) {
                ActivityTitleLabel.setText(event.getEventTitle());
                ActivityDescriptionLabel.setText(event.getEventDescription());
                priceLabel.setText(event.getEventPrice() + " £");
                LocationLabel.setText(event.getEventLocation());
                DateLabel.setText(event.getEventDate().format(formatter));
                EndDateLabel.setText(event.getEventEndDate().format(formatter));
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
    private void applyDiscount(ActionEvent event) throws SQLException {
        String discountCode = discountField.getText().trim();

        if (discountCode.isEmpty()) {
            showAlert("Error", "Please enter a discount code.");
            return;
        }

        Discount discount = DBconnect.getDiscountByOwnerAndCode(owner.getId(), discountCode);

        if (discount == null) {
            showAlert("Error", "Invalid discount code.");
            return;
        }

        this.discountNumber = discount.getPercentage();


        int ticketCount = ticketSpinner.getValue();
        double originalTotal = ticketCount * eventPrice;


        double discountAmount = (originalTotal * discountNumber) / 100.0;
        double newTotal = originalTotal - discountAmount;


        totalLabel.setText(String.format("%.2f", newTotal) + " £");
        discountLabel.setText(this.discountNumber + " %");


        discountField.setDisable(true);
        discountButton.setDisable(true);
    }

    @FXML
    private void bookButtonAction(ActionEvent event) {
        int ticketCount = ticketSpinner.getValue();
        LocalDate selectedDate = bookDate.getValue();

        // Verify if the important fields are entered
        if (ticketCount <= 0 || selectedDate == null) {
            showAlert("Error", "Enter all the fields !");
            return;
        }

        // Check if the selectedDate is in the future
        if (selectedDate.isBefore(LocalDate.now())) {
            showAlert("Error", "Book in the future !");
            return;
        }

        // Check if the selectedDate is after the event Start Date
        if (selectedDate.isBefore(this.event.getEventDate())) {
            showAlert("Error", "Book after the start date !");
            return;
        }

        // Check if the selected date is in the range of the event
        if (selectedDate.isAfter(this.event.getEventEndDate())) {
            showAlert("Error", "End date cannot be after event end date!");
            return;
        }

        try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/order.fxml"));
                Parent orderRoot = loader.load();
                OrderController orderController = loader.getController();

                orderController.setEventId(this.event.getEventId());
                orderController.setTicketCount(ticketCount);
                orderController.setBookingEventDate(selectedDate);
                orderController.setPrices(this.discountNumber);

                NavigationManager.setRoot(orderRoot);

        } catch (IOException e) {
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
        NavigationManager.profilePageButtonOnAction();
    }
}
