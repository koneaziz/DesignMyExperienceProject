package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Event;
import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class SearchController {

    @FXML
    private Label fnameLabel;
    @FXML
    private Label lnameLabel;
    @FXML
    private Circle profilPicture;
    @FXML
    private TilePane searchContainer;
    @FXML
    private Button homeButton, searchButton;
    @FXML
    private TextField searchField;
    @FXML
    private ChoiceBox<String> categoryChoiceBox, locationChoiceBox;
    @FXML
    private Slider priceSlider;

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


        categoryChoiceBox.getItems().addAll(DBconnect.getCategories());
        locationChoiceBox.getItems().addAll(DBconnect.getLocations());

    }

    /* Trigger the search field */
    @FXML
    private void searchButtonOnAction(ActionEvent actionEvent) {
        searchContainer.getChildren().clear();
        addSearchEvent(searchContainer);

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

    /* Add the event from the search */
    public void addSearchEvent(TilePane hBox) {
        try {
            ArrayList<Event> events;
            events = DBconnect.getEventsByName(searchField.getText());

            for (Event event : events) {
                if (event.getEventDate() != null &&
                        (event.getEventDate().isEqual(LocalDate.now()) || event.getEventDate().isAfter(LocalDate.now()))) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/search-card.fxml"));
                    Parent card = loader.load();
                    SearchCardController searchCardController = loader.getController();
                    searchCardController.setSearchEvent(event);
                    hBox.getChildren().add(card);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* Add the event from the search and filters */
    public void addSearchFilterEvent(TilePane hBox) {
        try {
            ArrayList<Event> events;
            events = DBconnect.getEventsByFilters(searchField.getText(), categoryChoiceBox.getValue(),locationChoiceBox.getValue(), priceSlider.getValue());

            for (Event event : events) {
                if (event.getEventDate() != null &&
                        (event.getEventDate().isEqual(LocalDate.now()) || event.getEventDate().isAfter(LocalDate.now()))) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/search-card.fxml"));
                    Parent card = loader.load();
                    SearchCardController searchCardController = loader.getController();
                    searchCardController.setSearchEvent(event);
                    hBox.getChildren().add(card);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* Add the event from the filters */
    public void addSearchFilterCategory(TilePane hBox, String category) {
        try {
            ArrayList<Event> events;
            events = DBconnect.getEventsByFilters(searchField.getText(), category,locationChoiceBox.getValue(), priceSlider.getValue());

            for (Event event : events) {
                if (event.getEventDate() != null &&
                        (event.getEventDate().isEqual(LocalDate.now()) || event.getEventDate().isAfter(LocalDate.now()))) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/search-card.fxml"));
                    Parent card = loader.load();
                    SearchCardController searchCardController = loader.getController();
                    searchCardController.setSearchEvent(event);
                    hBox.getChildren().add(card);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* Get the information from the precedent scene for the search text */
    public void getSearchText(String search) {
        searchField.setText(search);
        addSearchEvent(searchContainer);
    }

    @FXML
    /* Action to search the filters */
    public void filterSearch(ActionEvent actionEvent) {
        searchContainer.getChildren().clear();
       addSearchFilterEvent(searchContainer);
    }
}
