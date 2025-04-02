package com.aa.designmyexperience.Util;

/* We use this class to connect with the database */


import com.aa.designmyexperience.Models.Discount;
import com.aa.designmyexperience.Models.Event;
import com.aa.designmyexperience.Models.Order;
import com.aa.designmyexperience.Models.User;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /* Modify the user (Customer) info */
    public static void modifyUserInfo(User user) throws SQLException {
        String query = "UPDATE users SET fName = ?, lName = ?, email = ?, updated_at = ?, photo = ? WHERE idusers = ?";
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            stmt.setString(5, user.getPhoto());
            stmt.setInt(6, user.getId());

            // Execute the update operation.
            stmt.executeUpdate();
            showAlert("Success", "User modified successfully!");

        } catch (SQLException e) {
            showAlert("Error", "Error when modifying user: " + e.getMessage());
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
                event.setEventEndDate(resultQuery.getDate("event_end_date").toLocalDate());
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
                event.setEventEndDate(resultQuery.getDate("event_end_date").toLocalDate());
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
                event.setEventEndDate(resultQuery.getDate("event_end_date").toLocalDate());
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
                event.setEventEndDate(resultQuery.getDate("event_end_date").toLocalDate());
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
                event.setEventDate(rs.getDate("event_date").toLocalDate());
                event.setEventEndDate(rs.getDate("event_end_date").toLocalDate());
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

    /* We will get the event by the user id (owner) */
    public static List<Event> getEventByUserId(int userId) throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM events WHERE business_id = " + userId;
        try( Connection conn = getConnection(); Statement stmt = conn.createStatement();
             ResultSet resultQuery = stmt.executeQuery(query) ) {

            while (resultQuery.next()) {
                Event event = new Event();
                event.setEventId(resultQuery.getInt("id_event"));
                event.setEventOwner(resultQuery.getInt("business_id"));
                event.setEventTitle(resultQuery.getString("title"));
                event.setEventDescription(resultQuery.getString("description"));
                event.setEventDate(resultQuery.getDate("event_date").toLocalDate());
                event.setEventEndDate(resultQuery.getDate("event_end_date").toLocalDate());
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

    /* Add an event by the user */
    public static void addEvent(Event event) throws SQLException {

        String query = "INSERT INTO events (business_id, title, description, event_date, location, price, max_participants, registered_participants, discount, image, category, event_end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            stmt.setString(10, event.getEventImage());
            stmt.setString(11, event.getEventCategory());
            stmt.setDate(12, java.sql.Date.valueOf(event.getEventEndDate()));

            stmt.executeUpdate();
            showAlert("Success", "Event added successfully !");

        } catch (SQLException e) {
            showAlert("Error", "Error when adding event : " + e.getMessage());
        }
    }

    /* Delete an event */
    public static void deleteEvent(int eventID) throws SQLException {
        String query = "DELETE FROM events WHERE id_event = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, eventID);
            stmt.executeUpdate();
        }
    }

    /* Modify an Event */
    public static void modifyEvent(Event event) throws SQLException {
        String query = "UPDATE events SET business_id = ?, title = ?, description = ?, event_date = ?, location = ?, price = ?, max_participants = ?, registered_participants = ?, discount = ?, image = ?, category = ?, event_end_date = ? WHERE id_event = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameters in the same order as the placeholders in the SQL query.
            stmt.setInt(1, event.getEventOwner()); // business_id
            stmt.setString(2, event.getEventTitle());
            stmt.setString(3, event.getEventDescription());
            stmt.setDate(4, java.sql.Date.valueOf(event.getEventDate()));
            stmt.setString(5, event.getEventLocation());
            stmt.setDouble(6, event.getEventPrice());
            stmt.setInt(7, event.getEventMaxParticipants());
            stmt.setInt(8, event.getEventRegisteredParticipants()); // registered_participants
            stmt.setDouble(9, event.getEventDiscount());
            stmt.setString(10, event.getEventImage());
            stmt.setString(11, event.getEventCategory());
            stmt.setDate(12, java.sql.Date.valueOf(event.getEventEndDate()));
            stmt.setInt(13, event.getEventId()); // WHERE clause

            // Execute the update operation.
            stmt.executeUpdate();
            showAlert("Success", "Event modified successfully!");
        } catch (SQLException e) {
            showAlert("Error", "Error when modifying event: " + e.getMessage());
        }
    }



    /* Add a discount */
    public static void addDiscount(Discount discount) throws SQLException {
        // SQL query to insert a new discount record.
        String query = "INSERT INTO discounts (owner_id, discount_code, discount_percentage, expiration_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameters according to the Discount object.
            stmt.setInt(1, discount.getOwner());
            stmt.setString(2, discount.getCode());
            stmt.setDouble(3, discount.getPercentage());
            stmt.setDate(4, java.sql.Date.valueOf(discount.getEndDate()));

            // Execute the insert statement.
            stmt.executeUpdate();
            showAlert("Success", "Discount added successfully!");
        } catch (SQLException e) {
            showAlert("Error", "Error when adding discount: " + e.getMessage());
        }
    }

    /* Delete a discount */
    public static void deleteDiscount(int discountId) throws SQLException {
        String query = "DELETE FROM discounts WHERE discount_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, discountId);
            stmt.executeUpdate();
        }
    }

    /* Get the discounts of the user */
    public static List<Discount> getDiscountByUserId(int userId) throws SQLException {
        List<Discount> discounts = new ArrayList<>();
        String query = "SELECT * FROM discounts WHERE owner_id = ?";
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Discount discount = new Discount();

                    discount.setId(rs.getInt("discount_id"));
                    discount.setOwner(rs.getInt("owner_id"));
                    discount.setCode(rs.getString("discount_code"));
                    discount.setEndDate(rs.getDate("expiration_date").toLocalDate());
                    discount.setPercentage(rs.getDouble("discount_percentage"));

                    discounts.add(discount);
                }
            }
            return discounts;
        }
    }

    /* Check if the discount is from the right owner */
    public static Discount getDiscountByOwnerAndCode(int ownerId, String discountCode) throws SQLException {
        String query = "SELECT * FROM discounts WHERE owner_id = ? AND discount_code = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, ownerId);
            stmt.setString(2, discountCode);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Discount discount = new Discount();
                    discount.setId(rs.getInt("discount_id"));
                    discount.setOwner(rs.getInt("owner_id"));
                    discount.setCode(rs.getString("discount_code"));
                    discount.setPercentage(rs.getDouble("discount_percentage"));
                    if (rs.getDate("expiration_date") != null) {
                        discount.setEndDate(rs.getDate("expiration_date").toLocalDate());
                    }
                    return discount;
                }
            }
        }
        return null;
    }




    /* Count the number of event of the owner */
    public static int countEventsOwner(int businessId) throws SQLException {
        String query = "SELECT count(*) FROM events WHERE business_id = ?";
        int count = 0;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, businessId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        }
        return count;
    }

    /* Create a reservation */
    public static void createOrder(Order order) throws SQLException {
        String query = "INSERT INTO orders (user_id, event_id, booking_date, ticket_quantity, total_price, order_status) VALUES (?, ?, ?, ?, ?, ?)";
        String query2 = "UPDATE events SET registered_participants = registered_participants + ? WHERE id_event = ?";
        try (Connection conn = getConnection();) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 PreparedStatement stmt2 = conn.prepareStatement(query2)) {


                stmt.setInt(1, order.getUserId());
                stmt.setInt(2, order.getEventId());
                stmt.setDate(3, java.sql.Date.valueOf(order.getBookingDate()));
                stmt.setInt(4, order.getTicketQuantity());
                stmt.setDouble(5, order.getTotalPrice());
                stmt.setString(6, order.getOrderStatus());

                // Execute the insert operation
                stmt.executeUpdate();

                // Update the event registeredParticipants
                stmt2.setInt(1, order.getTicketQuantity());
                stmt2.setInt(2, order.getEventId());

                stmt2.executeUpdate();

                conn.commit();
                showAlert("Success", "Order added successfully!");
            } catch (SQLException e) {
                conn.rollback();
                showAlert("Error", "Error when adding order: " + e.getMessage());
            }
        }


    }

    /* Cancel an order */
    public static void cancelOrder(int orderId) throws SQLException {
        String query = "DELETE FROM orders WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }

    /* Get order history of user */
    public static List<Order> getOrdersByUser(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.*, e.title AS event_name " +
                "FROM orders o " +
                "JOIN events e ON o.event_id = e.id_event " +
                "WHERE o.user_id = ? " +
                "ORDER BY o.booking_date DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setEventId(rs.getInt("event_id"));
                    order.setBookingDate(rs.getDate("booking_date").toLocalDate());
                    order.setTicketQuantity(rs.getInt("ticket_quantity"));
                    order.setTotalPrice(rs.getDouble("total_price"));
                    order.setOrderStatus(rs.getString("order_status"));
                    order.setEventName(rs.getString("event_name"));

                    orders.add(order);
                }
            }
            return orders;
        }
    }

    /* Get order history for the business Owner */
    public static List<Order> getOrdersByOwner(int ownerId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.*, e.title AS event_name " +
                "FROM orders o " +
                "JOIN events e ON o.event_id = e.id_event " +
                "WHERE e.business_id = ? " +
                "ORDER BY o.booking_date DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {

            stmt.setInt(1, ownerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setEventId(rs.getInt("event_id"));
                    order.setBookingDate(rs.getDate("booking_date").toLocalDate());
                    order.setTicketQuantity(rs.getInt("ticket_quantity"));
                    order.setTotalPrice(rs.getDouble("total_price"));
                    order.setOrderStatus(rs.getString("order_status"));
                    order.setEventName(rs.getString("event_name"));

                    orders.add(order);
                }
            }
            return orders;
        }
    }

    /* Get the revenue of the Owner */
    public static double getTotalRevenueOwner(int ownerId) throws SQLException {
        double totalRevenue = 0.0;
        String query = "SELECT SUM(o.total_price) AS total_revenue " +
                "FROM orders o " +
                "JOIN events e ON o.event_id = e.id_event " +
                "WHERE e.business_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, ownerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalRevenue = rs.getDouble("total_revenue");
                    if (rs.wasNull()) {
                        totalRevenue = 0.0;
                    }
                }
            }
        }
        return totalRevenue;
    }

    /* See if the event is already booked by the user */
    public static boolean isEventAlreadyBooked(int userId, int eventId) throws SQLException {
        String query = "SELECT count(*) FROM orders WHERE user_id = ? AND event_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // Return true if the user has an order for the event
                }
            }
        }
        return false;
    }


    /* Get the bar chart of revenue */
    public static Map<String, Double> getRevenuePerEventForOwner(int ownerId) throws SQLException {
        Map<String, Double> revenueMap = new HashMap<>();
        String query = "SELECT e.title AS event_title, SUM(o.total_price) AS revenue " +
                "FROM orders o " +
                "JOIN events e ON o.event_id = e.id_event " +
                "WHERE e.business_id = ? " +
                "GROUP BY e.title";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ownerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("event_title");
                    double revenue = rs.getDouble("revenue");
                    revenueMap.put(title, revenue);
                }
            }
        }
        return revenueMap;
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
