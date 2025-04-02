package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Discount;
import com.aa.designmyexperience.Models.Event;
import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OwnerController {

    @FXML
    private Label companyName, numberActivitiesLabel, revenueLabel;
    @FXML
    private ImageView companyLogo;
    @FXML
    private ListView<Event> eventsListView;
    @FXML
    private BarChart<String, Number> revenueBarChart;


    int NumberActivities = 0;
    int userId;

    @FXML
    private void initialize() throws SQLException {
        /* Get the session with the current user */
        Session session = Session.getInstance();

        if (session != null && session.getCurrentUser() != null) {
            User user = session.getCurrentUser();
            userId = user.getId();

            /* We get the current User information in the Session */
            String photo = user.getPhoto();
            Image profilePicture = new Image(photo);
            companyLogo.setImage(profilePicture);

            companyName.setText(user.getFirstName() + " " + user.getLastName());

            try {
                // Get discounts for the current user from the database
                List<Event> events = DBconnect.getEventByUserId(userId);
                ObservableList<Event> observableEvents = FXCollections.observableArrayList(events);
                eventsListView.setItems(observableEvents);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Set the cell factory to display a custom cell for each order.
        eventsListView.setCellFactory(listView -> new ListCell<Event>() {
            private FXMLLoader loader;

            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);
                if (empty || event == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (loader == null) {
                        loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/owner-event-cell.fxml"));
                        try {
                            loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    OwnerEventCellController cellController = loader.getController();
                    cellController.setEvent(event);
                    setGraphic(loader.getRoot());
                }
            }
        });


        NumberActivities = DBconnect.countEventsOwner(userId);
        numberActivitiesLabel.setText(String.valueOf(NumberActivities));

        revenueLabel.setText(DBconnect.getTotalRevenueOwner(userId) + " £");

        try {
            Map<String, Double> revenueData = DBconnect.getRevenuePerEventForOwner(userId);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Revenue per Event");

            for (Map.Entry<String, Double> entry : revenueData.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            revenueBarChart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /* Go to manage discounts */
    @FXML
    private void discountOnAction(ActionEvent event) throws IOException {
        NavigationManager.navigate("discounts.fxml");
    }

    /* Go to manage reservations */
    @FXML
    private void reservationOnAction(ActionEvent event) throws IOException {
        NavigationManager.navigate("manage-bookings.fxml");
    }

    /* Go to edit profile */
    @FXML
    private void editEventOnAction(ActionEvent event) throws IOException {
        NavigationManager.navigate("edit-owner-info.fxml");
    }


    /* Create a logout from the current session */
    @FXML
    private void logoutOnAction(ActionEvent e) throws IOException {
        Session.clearSession();
        NavigationManager.navigate("login.fxml");
    }

    /* Go back to the home page */
    @FXML
    public void homeButtonOnAction(ActionEvent e) throws IOException {
        NavigationManager.navigate("home.fxml");
    }

    /* Add an event */
    @FXML
    public void addEventOnAction(ActionEvent e) throws IOException {
        NavigationManager.navigate("addEvent.fxml");
    }
}
