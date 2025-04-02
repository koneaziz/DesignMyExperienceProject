package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Event;
import com.aa.designmyexperience.Models.Order;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.EmailSender;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


public class ManageBookingsCellController {

    @FXML
    private Label OrderIdLabel, OrderDateLabel, OrderEventLabel, OrderTicketsLabel, OrderPriceLabel, OrderStatusLabel;
    @FXML
    private Button cancelButton;

    private Order order;


    public void setOrder(Order order) {
        this.order = order;
        updateOrderInfo();
    }

    private void updateOrderInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        OrderIdLabel.setText(String.valueOf(order.getOrderId()));
        OrderDateLabel.setText(order.getBookingDate().format(formatter));
        OrderEventLabel.setText(order.getEventName());
        OrderTicketsLabel.setText(String.valueOf(order.getTicketQuantity()));
        OrderPriceLabel.setText(String.valueOf(order.getTotalPrice()));
        OrderStatusLabel.setText(order.getOrderStatus());

    }

    @FXML
    private void handleCancel() {
        if (order != null) {

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Cancel Order");
            confirmAlert.setHeaderText("Are you sure you want to cancel this order?");
            confirmAlert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {

                    DBconnect.cancelOrder(order.getOrderId());


                    cancelButton.setDisable(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
