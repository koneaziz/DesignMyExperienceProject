package com.aa.designmyexperience.Controllers;

import com.aa.designmyexperience.Models.Event;
import com.aa.designmyexperience.Models.Session;
import com.aa.designmyexperience.Models.User;
import com.aa.designmyexperience.Util.DBconnect;
import com.aa.designmyexperience.Util.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;

public class HomeController {

    @FXML
    private Label fnameLabel;
    @FXML
    private Label lnameLabel;
    @FXML
    private Circle profilPicture;
    @FXML
    private HBox trendingContainer;
    @FXML
    private HBox dateEventContainer;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField searchField;

    @FXML
    private void initialize() {
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

        addAllEventToContainerSorted(trendingContainer);
        addAllEventToContainer(dateEventContainer);

        /* We will format the date */
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }
            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;
            }
        });

    }

    /* We get all the event and add it on the following container */
    public void addAllEventToContainer(HBox hBox) {
        try {
            ArrayList<Event> events = new ArrayList<Event>();
            events = DBconnect.getEvents();

            for (Event event : events) {
                if (event.getEventDate() != null &&
                        (event.getEventDate().isEqual(LocalDate.now()) || event.getEventDate().isAfter(LocalDate.now()))) {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/card.fxml"));
                    Parent card = loader.load();
                    CardController cardController = loader.getController();
                    cardController.setEvent(event);
                    hBox.getChildren().add(card);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* We get all the event (sorted by Participants) and add it on the following container */
    public void addAllEventToContainerSorted(HBox hBox) {
        try {
            ArrayList<Event> events = new ArrayList<Event>();
            events = DBconnect.getEvents();

            events.sort((e1, e2) -> Integer.compare(e2.getEventRegisteredParticipants(), e1.getEventRegisteredParticipants()));

            for (Event event : events) {
                if (event.getEventDate() != null &&
                        (event.getEventDate().isEqual(LocalDate.now()) || event.getEventDate().isAfter(LocalDate.now()))) {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/card.fxml"));
                    Parent card = loader.load();
                    CardController cardController = loader.getController();
                    cardController.setEvent(event);
                    hBox.getChildren().add(card);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /* We will add the Event to the container following the date */
    public void datePickerOnAction(ActionEvent actionEvent) throws SQLException {
        LocalDate dateSearch = datePicker.getValue();

        if (dateSearch != null) {

            dateEventContainer.getChildren().clear();

            try {
                ArrayList<Event> eventsByDate = new ArrayList<Event>();
                eventsByDate = DBconnect.getEventsByDate(dateSearch);

                for (Event event : eventsByDate) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aa/designmyexperience/card.fxml"));
                    Parent card = loader.load();
                    CardController cardController = loader.getController();
                    cardController.setEvent(event);
                    dateEventContainer.getChildren().add(card);
                }


            } catch (IOException e) {
                e.printStackTrace();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        } else {
            dateEventContainer.getChildren().clear();
            addAllEventToContainer(dateEventContainer);
        }

    }

    /* Go to the profile page */
    public void profilePageButtonOnAction(ActionEvent actionEvent) throws IOException {
        NavigationManager.profilePageButtonOnAction();
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
}
