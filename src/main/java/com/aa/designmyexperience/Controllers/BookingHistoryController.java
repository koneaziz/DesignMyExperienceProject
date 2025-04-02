package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Order;
import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingHistoryController {

    @FXML
    private ListView<Order> ordersListView;


    @FXML
    private void initialize() {
        // Retrieve the current user from the session.
        User currentUser = Session.getInstance() != null ? Session.getInstance().getCurrentUser() : null;
        if (currentUser != null) {
            try {
                // Get orders for the current user from the database
                List<Order> orders = DBconnect.getOrdersByUser(currentUser.getId());
                ObservableList<Order> observableOrders = FXCollections.observableArrayList(orders);
                ordersListView.setItems(observableOrders);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Set the cell factory to display a custom cell for each order.
        ordersListView.setCellFactory(listView -> new ListCell<Order>() {
            private FXMLLoader loader;

            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);
                if (empty || order == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (loader == null) {
                        loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/booking-history-cell.fxml"));
                        try {
                            loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    BookingHistoryCellController cellController = loader.getController();
                    cellController.setOrder(order);
                    setGraphic(loader.getRoot());
                }
            }
        });
    }

    /* Go back to the details page */
    @FXML
    private void backButtonOnAction(ActionEvent event) throws IOException {
        NavigationManager.navigate("profileCustomer.fxml");
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

}
