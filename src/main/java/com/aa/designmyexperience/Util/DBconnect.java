package com.aa.designmyexperience.Util;

/* We use this class to connect with the database */


import com.aa.designmyexperience.Models.Event;
import com.aa.designmyexperience.Models.User;
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
                event.setEventDate(resultQuery.getDate("event_date"));
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
                event.setEventDate(resultQuery.getDate("event_date"));
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
}
