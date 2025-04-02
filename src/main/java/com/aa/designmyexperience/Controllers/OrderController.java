package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Event;
import com.aa.designmyexperience.Models.Order;
import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.EmailSender;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.aa.designmyexperience.Util.DBconnect.showAlert;

public class OrderController {
    @FXML
    private Label fnameLabel, ActivityTitleLabel, LocationLabel, PriceLabel, NbTicketLabel, BookingDateLabel, SubTotalLabel, TotalLabel, DiscountLabel;
    @FXML
    private Label lnameLabel;
    @FXML
    private Circle profilPicture;
    @FXML
    private TextField searchField, emailField, cardNameField, cardNumberField, monthYearField, CvvField;

    private int eventId;
    private int userId;
    private User user;
    private Event event;
    private double eventPrice;
    private double discount;
    private int ticketCount;
    private LocalDate bookingDate;
    private double totalprice;

    @FXML
    private void initialize() {
        /* Get the session with the current user */
        Session session = Session.getInstance();

        if (session != null && session.getCurrentUser() != null) {
            this.user = session.getCurrentUser();
            this.userId = user.getId();

            /* We get the current User information in the Session */
            String photo = user.getPhoto();
            Image profilePicture = new Image(photo);
            profilPicture.setFill(new ImagePattern(profilePicture, 0, 0, 1, 1, true));

            fnameLabel.setText(user.getFirstName());
            lnameLabel.setText(user.getLastName());
        }

        TextFormatter<String> cardNumberFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,16}")) {
                return change;
            } else {
                return null;
            }
        });
        cardNumberField.setTextFormatter(cardNumberFormatter);

        TextFormatter<String> cvvFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,4}")) {
                return change;
            } else {
                return null;
            }
        });
        CvvField.setTextFormatter(cvvFormatter);

        TextFormatter<String> mmyyFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,4}")) {
                return change;
            } else {
                return null;
            }
        });
        monthYearField.setTextFormatter(mmyyFormatter);

        monthYearField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String text = monthYearField.getText();
                if (text != null && text.matches("\\d{4}")) {
                    String formatted = text.substring(0,2) + "/" + text.substring(2,4);
                    monthYearField.setText(formatted);
                }
            }
        });

        SubTotalLabel.setText((this.eventPrice * this.ticketCount) + " £");
        TotalLabel.setText( (this.eventPrice * this.ticketCount) - ((this.eventPrice * this.ticketCount) * discount/100) + " £" );


    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
        loadEventDetails();

    }

    // Extract all the data of the event
    private void loadEventDetails() {
        try {
            this.event = DBconnect.getEventById(eventId);

            if(event != null) {
                ActivityTitleLabel.setText(event.getEventTitle());
                PriceLabel.setText((event.getEventPrice()) + " £");
                LocationLabel.setText(event.getEventLocation());

                this.eventPrice = event.getEventPrice();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;

        NbTicketLabel.setText(String.valueOf(ticketCount));
    }

    public void setBookingEventDate(LocalDate eventDate) {
        this.bookingDate = eventDate;
        if(BookingDateLabel != null && eventDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            BookingDateLabel.setText(eventDate.format(formatter));
        }
    }

    public void setPrices(double discount) {
        this.discount = discount;
        DiscountLabel.setText(this.discount + " %");
        SubTotalLabel.setText((this.eventPrice * this.ticketCount) + " £");
        TotalLabel.setText(((this.eventPrice * this.ticketCount) - ((this.eventPrice * this.ticketCount)*this.discount/100)) + " £");
    }


    /* Create an order */
    @FXML
    private void handleOrder(ActionEvent event) throws SQLException, IOException {

        if (emailField.getText().isEmpty() || cardNameField.getText().isEmpty() || cardNumberField.getText().isEmpty() || monthYearField.getText().isEmpty() || CvvField.getText().isEmpty()) {
            showAlert("Error", "Enter all fields !");
            return;
        }

        totalprice = (this.eventPrice * this.ticketCount) - ((this.eventPrice * this.ticketCount) * this.discount/100);
        String orderStatus = "Confirmed";

        // Create the order instance
        Order order = new Order(this.userId, this.eventId, this.bookingDate, this.ticketCount, totalprice, orderStatus);

        // Insert in the database
        DBconnect.createOrder(order);

        String subject = "Order Confirmation ";
        StringBuffer body = new StringBuffer("<html>Dear Customer,<br>Your order for <b>" + order.getTicketQuantity() + "</b> tickets has been successfully placed.<br>");
                body.append("Total Price: <b>£").append(order.getTotalPrice()).append("</b><br>");
                body.append("You can see your QR Code down this message. <br><br>");
                body.append("<img src=\"cid:image\" width=\"30%\" height=\"30%\" /><br>");
                body.append("Thank you for your purchase!</html>");

        Map<String, String> Images = new HashMap<String, String>();
        Images.put("image", "src/main/resources/Images/QRCODE.png");

        EmailSender.sendEmailImages(emailField.getText(), subject, body.toString(), Images);

        // Go back to the profile
        NavigationManager.navigate("home.fxml");


    }

    /* Go back to the details page */
    @FXML
    private void backButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/event-details.fxml"));
            Parent detailRoot = loader.load();
            EventDetailsController detailController = loader.getController();

            detailController.setEventId(this.event.getEventId());

            NavigationManager.setRoot(detailRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    /* Go to the home page */
    @FXML
    private void homeButtonOnAction(ActionEvent event) throws IOException {
        NavigationManager.navigate("home.fxml");
    }

    /* Go to the profile page */
    @FXML
    private void profilePageButtonOnAction(ActionEvent actionEvent) throws IOException {
        NavigationManager.profilePageButtonOnAction();
    }
}
