package com.aa.designmyexperience.Util;

/* We use this class to connect with the database */


import com.aa.designmyexperience.Models.Event;
import com.aa.designmyexperience.Models.User;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DBconnect {

    /* We initialize our connection to the database */
    private static String HOST = "designmyexperience.mysql.database.azure.com"; // We use a remote server
    private static int PORT = 3306;
    private static String URL = "jdbc:mysql://designmyexperience.mysql.database.azure.com:3306/designmyexperience";
    private static String DB_NAME = "designmyexperience";
    private static String USERNAME = "java";
    private static String PASSWORD = "Designmyexperience2025";

    private static Connection conn;

    public static Connection getConnection() {

        /* We establish a connection to the database */
        try {
            conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return conn;
    }

    /* We will get the User Details */
    public static User getUser(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email = '" + email + "'";

        try( Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultQuery = stmt.executeQuery(query) ) {

            if (resultQuery.next()) {
                int id = resultQuery.getInt("idusers");
                String firstName = resultQuery.getString("fName");
                String lastName = resultQuery.getString("lName");
                String userType = resultQuery.getString("users_type");
                String emailD = resultQuery.getString("email");
                String password = resultQuery.getString("password");
                String photo = resultQuery.getString("photo");

                Timestamp tsCreated = resultQuery.getTimestamp("created_at");
                Timestamp tsUpdated = resultQuery.getTimestamp("updated_at");
                LocalDateTime createdAt = (tsCreated != null) ? tsCreated.toLocalDateTime() : null;
                LocalDateTime updatedAt = (tsUpdated != null) ? tsUpdated.toLocalDateTime() : null;


                return new User(id, firstName, lastName, userType, emailD, password, photo, createdAt, updatedAt);
            }
            return null;

        }
    }

    /* We will get the owner by the event */
    public static User getUserByID(int idusers) throws SQLException {
        String query = "SELECT * FROM users WHERE idusers = '" + idusers + "'";

        try( Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet resultQuery = stmt.executeQuery(query) ) {

            if (resultQuery.next()) {
                int id = resultQuery.getInt("idusers");
                String firstName = resultQuery.getString("fName");
                String lastName = resultQuery.getString("lName");
                String userType = resultQuery.getString("users_type");
                String emailD = resultQuery.getString("email");
                String password = resultQuery.getString("password");
                String photo = resultQuery.getString("photo");

                Timestamp tsCreated = resultQuery.getTimestamp("created_at");
                Timestamp tsUpdated = resultQuery.getTimestamp("updated_at");
                LocalDateTime createdAt = (tsCreated != null) ? tsCreated.toLocalDateTime() : null;
                LocalDateTime updatedAt = (tsUpdated != null) ? tsUpdated.toLocalDateTime() : null;


                return new User(id, firstName, lastName, userType, emailD, password, photo, createdAt, updatedAt);
            }
            return null;

        }
    }


    /* We will create a User in the Database */
    public static void createUser(User user) throws SQLException {
        String query = "INSERT INTO users (fName, lName, users_type, email, password, photo, created_at, updated_at) " +
                "VALUES (" +
                "'" + user.getFirstName() + "', " +
                "'" + user.getLastName() + "', " +
                "'" + user.getUserType() + "', " +
                "'" + user.getEmail() + "', " +
                "'" + user.getPassword() + "', " +
                "'" + user.getPhoto() + "', " +
                "CURRENT_TIMESTAMP, CURRENT_TIMESTAMP" + ")";

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {

            int affectedRows = stmt.executeUpdate(query);
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed");
            }

        }
    }



    /* We will get the Event Details */
    public static ArrayList<Event> getEvents() throws SQLException {
        ArrayList<Event> events = new ArrayList<Event>();
        String query = "SELECT * FROM events";
        try( Connection conn = getConnection(); Statement stmt = conn.createStatement();
             ResultSet resultQuery = stmt.executeQuery(query) ) {

            while (resultQuery.next()) {
                Event event = new Event();
                event.setEventId(resultQuery.getInt("id_event"));
                event.setEventOwner(resultQuery.getInt("business_id"));
                event.setEventTitle(resultQuery.getString("title"));
                event.setEventDescription(resultQuery.getString("description"));
                event.setEventDate(resultQuery.getDate("event_date").toLocalDate());
                event.setEventLocation(resultQuery.getString("location"));
                event.setEventPrice(resultQuery.getDouble("price"));
                event.setEventMaxParticipants(resultQuery.getInt("max_participants"));
                event.setEventRegisteredParticipants(resultQuery.getInt("registered_participants"));
                event.setEventDiscount(resultQuery.getDouble("discount"));
                event.setEventImage(resultQuery.getString("image"));
                event.setEventCategory(resultQuery.getString("category"));

                events.add(event);

            }
        }

        return events;
    }

    /* We will get the Event by Date */
    public static ArrayList<Event> getEventsByDate(LocalDate date) throws SQLException {
        ArrayList<Event> events = new ArrayList<Event>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(formatter);

        String query = "SELECT * FROM events WHERE date(event_date) = '" + formattedDate + "'";
        try( Connection conn = getConnection(); Statement stmt = conn.createStatement();
        ResultSet resultQuery = stmt.executeQuery(query) ) {
            while (resultQuery.next()) {
                Event event = new Event();
                event.setEventId(resultQuery.getInt("id_event"));
                event.setEventOwner(resultQuery.getInt("business_id"));
                event.setEventTitle(resultQuery.getString("title"));
                event.setEventDescription(resultQuery.getString("description"));
                event.setEventDate(resultQuery.getDate("event_date").toLocalDate());
                event.setEventLocation(resultQuery.getString("location"));
                event.setEventPrice(resultQuery.getDouble("price"));
                event.setEventMaxParticipants(resultQuery.getInt("max_participants"));
                event.setEventRegisteredParticipants(resultQuery.getInt("registered_participants"));
                event.setEventDiscount(resultQuery.getDouble("discount"));
                event.setEventImage(resultQuery.getString("image"));
                event.setEventCategory(resultQuery.getString("category"));

                events.add(event);
            }
        }

        return events;
    }

    /* We will get the Events by Name */
    public static ArrayList<Event> getEventsByName(String name) throws SQLException {
        ArrayList<Event> events = new ArrayList<Event>();
        String query = "SELECT * FROM events WHERE LOWER(title) LIKE '%" + name.toLowerCase() + "%'";

        try( Connection conn = getConnection(); Statement stmt = conn.createStatement();
        ResultSet resultQuery = stmt.executeQuery(query) ) {
            while (resultQuery.next()) {
                Event event = new Event();
                event.setEventId(resultQuery.getInt("id_event"));
                event.setEventOwner(resultQuery.getInt("business_id"));
                event.setEventTitle(resultQuery.getString("title"));
                event.setEventDescription(resultQuery.getString("description"));
                event.setEventDate(resultQuery.getDate("event_date").toLocalDate());
                event.setEventLocation(resultQuery.getString("location"));
                event.setEventPrice(resultQuery.getDouble("price"));
                event.setEventMaxParticipants(resultQuery.getInt("max_participants"));
                event.setEventRegisteredParticipants(resultQuery.getInt("registered_participants"));
                event.setEventDiscount(resultQuery.getDouble("discount"));
                event.setEventImage(resultQuery.getString("image"));
                event.setEventCategory(resultQuery.getString("category"));
                events.add(event);

            }
        }
        return events;
    }

    /* We will get events by Filters */
    public static ArrayList<Event> getEventsByFilters(String name, String category, String location, double price) throws SQLException {
        ArrayList<Event> events = new ArrayList<>();

        StringBuilder query = new StringBuilder("SELECT * FROM events WHERE 1=1");

        if (name != null && !name.trim().isEmpty()) {
            query.append(" AND LOWER(title) LIKE '%").append(name.trim().toLowerCase()).append("%'");
        }

        if (category != null && !category.trim().isEmpty()) {
            query.append(" AND LOWER(category) = '").append(category.trim().toLowerCase()).append("'");
        }

        if (location != null && !location.trim().isEmpty()) {
            query.append(" AND LOWER(location) LIKE '%").append(location.trim().toLowerCase()).append("%'");
        }

        if (price > 0) {
            query.append(" AND price <= ").append(price);
        }

        try( Connection conn = getConnection(); Statement stmt = conn.createStatement();
             ResultSet resultQuery = stmt.executeQuery(String.valueOf(query)) ) {
            while (resultQuery.next()) {
                Event event = new Event();
                event.setEventId(resultQuery.getInt("id_event"));
                event.setEventOwner(resultQuery.getInt("business_id"));
                event.setEventTitle(resultQuery.getString("title"));
                event.setEventDescription(resultQuery.getString("description"));
                event.setEventDate(resultQuery.getDate("event_date").toLocalDate());
                event.setEventLocation(resultQuery.getString("location"));
                event.setEventPrice(resultQuery.getDouble("price"));
                event.setEventMaxParticipants(resultQuery.getInt("max_participants"));
                event.setEventRegisteredParticipants(resultQuery.getInt("registered_participants"));
                event.setEventDiscount(resultQuery.getDouble("discount"));
                event.setEventImage(resultQuery.getString("image"));
                event.setEventCategory(resultQuery.getString("category"));
                events.add(event);

            }
        }

        return events;
    }

    /* We will get an event by its ID */
    public static Event getEventById(int eventId) throws SQLException {
        Event event = null;
        String query = "SELECT * FROM events WHERE id_event = " + eventId;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                event = new Event();
                event.setEventId(rs.getInt("id_event"));
                event.setEventOwner(rs.getInt("business_id"));
                event.setEventTitle(rs.getString("title"));
                event.setEventDescription(rs.getString("description"));
                event.setEventDate(rs.getDate("event_date").toLocalDate()); // Si besoin, adaptez pour un datetime
                event.setEventLocation(rs.getString("location"));
                event.setEventPrice(rs.getDouble("price"));
                event.setEventMaxParticipants(rs.getInt("max_participants"));
                event.setEventRegisteredParticipants(rs.getInt("registered_participants"));
                event.setEventDiscount(rs.getDouble("discount"));
                event.setEventImage(rs.getString("image"));
                event.setEventCategory(rs.getString("category"));
            }
        }
        return event;
    }

    /* Add an event by the user */
    public static void addEvent(Event event) throws SQLException {

        String query = "INSERT INTO events (business_id, title, description, event_date, location, price, max_participants, registered_participants, discount, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query) ) {
            stmt.setInt(1, event.getEventOwner()); // business_id
            stmt.setString(2, event.getEventTitle());
            stmt.setString(3, event.getEventDescription());
            stmt.setDate(4, java.sql.Date.valueOf(event.getEventDate()));
            stmt.setString(5, event.getEventLocation());
            stmt.setDouble(6, event.getEventPrice());
            stmt.setInt(7, event.getEventMaxParticipants());
            stmt.setInt(8, event.getEventRegisteredParticipants()); // registered_participants initialize at 0
            stmt.setDouble(9, event.getEventDiscount());

            stmt.executeUpdate();
            showAlert("Success", "Event added successfully !");

        } catch (SQLException e) {
            showAlert("Error", "Error when adding event : " + e.getMessage());
        }
    }



    /* We will get the categories names */
    public static ArrayList<String> getCategories() throws SQLException {
        ArrayList<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT category FROM events";
        try( Connection conn = getConnection(); Statement stmt = conn.createStatement();
        ResultSet resultQuery = stmt.executeQuery(query) ) {
            while (resultQuery.next()) {
                categories.add(resultQuery.getString("category"));
            }
        }
        return categories;
    }

    public static ArrayList<String> getLocations() throws SQLException {
        ArrayList<String> locations = new ArrayList<>();
        String query = "SELECT DISTINCT location FROM events";
        try( Connection conn = getConnection(); Statement stmt = conn.createStatement();
        ResultSet resultQuery = stmt.executeQuery(query) ) {
            while (resultQuery.next()) {
                locations.add(resultQuery.getString("location"));
            }
        }
        return locations;
    }


    /* Show alert method */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




}
