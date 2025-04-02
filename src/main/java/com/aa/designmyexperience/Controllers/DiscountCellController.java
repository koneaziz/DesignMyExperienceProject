package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Discount;
import com.aa.designmyexperience.Models.Order;
import com.aa.designmyexperience.Util.DBconnect;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonType;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DiscountCellController {

    @FXML
    private Button deleteButton;
    @FXML
    private Label DiscountIdLabel, DiscountEndDateLabel, DiscountCodeLabel, DiscountPercentageLabel;


    private Discount discount;



    public void setDiscount(Discount discount) {
        this.discount = discount;
        updateDiscount();
    }

    private void updateDiscount() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DiscountIdLabel.setText(String.valueOf(discount.getId()));
        DiscountEndDateLabel.setText(discount.getEndDate().format(formatter));
        DiscountCodeLabel.setText(discount.getCode());
        DiscountPercentageLabel.setText(String.valueOf(discount.getPercentage()));
    }



    @FXML
    private void handleDelete() {
        if (discount != null) {

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Delete Discount");
            confirmAlert.setHeaderText("Are you sure you want to delete this discount?");
            confirmAlert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {

                    DBconnect.deleteDiscount(discount.getId());

                    deleteButton.setDisable(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
