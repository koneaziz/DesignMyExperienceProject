package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Discount;
import com.aa.designmyexperience.Controllers.DiscountCellController;
import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DiscountController {

    @FXML
    private ListView<Discount> discountsListView;


    @FXML
    private void initialize() {
        // Retrieve the current user from the session.
        User currentUser = Session.getInstance() != null ? Session.getInstance().getCurrentUser() : null;
        if (currentUser != null) {
            try {
                // Get discounts for the current user from the database
                List<Discount> discounts = DBconnect.getDiscountByUserId(currentUser.getId());
                ObservableList<Discount> observableDiscounts = FXCollections.observableArrayList(discounts);
                discountsListView.setItems(observableDiscounts);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Set the cell factory to display a custom cell for each order.
        discountsListView.setCellFactory(listView -> new ListCell<Discount>() {
            private FXMLLoader loader;

            @Override
            protected void updateItem(Discount discount, boolean empty) {
                super.updateItem(discount, empty);
                if (empty || discount == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (loader == null) {
                        loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/discount-cell.fxml"));
                        try {
                            loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    DiscountCellController cellController = loader.getController();
                    cellController.setDiscount(discount);
                    setGraphic(loader.getRoot());
                }
            }
        });
    }

    /* Go to the add Discount */
    @FXML
    private void handleAddAction(ActionEvent event) throws IOException {
        NavigationManager.navigate("addDiscount.fxml");
    }


    /* Go back to the details page */
    @FXML
    private void backButtonOnAction(ActionEvent event) throws IOException {
        NavigationManager.navigate("profileOwner.fxml");
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
