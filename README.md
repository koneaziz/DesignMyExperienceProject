DesignMyExperience - Java Project

**DesignMyExperience** is a JavaFX application that allows users to discover, book, and manage events. Users can be either customers (who explore and book events) or business owners (who create and manage events). The application uses a relational database to store information about users, events, payments, and more.

## Prerequisites

To run this project, ensure you have the following installed and configured:

### 1. **Development Environment**
- **Java Development Kit (JDK)**: Version 17 or higher (the project uses JavaFX 23, which is compatible with Java 17+).
- **IntelliJ IDEA** (recommended) or another IDE compatible with JavaFX (e.g., Eclipse).
- **Maven**: The project uses Maven to manage dependencies. Ensure Maven is installed and configured in your IDE.

### 2. **JavaFX Dependencies**
The project uses JavaFX 23. Add the following dependencies to your `pom.xml` file (if using Maven):

```xml
<dependencies>
    <!-- JavaFX -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>23</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>23</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-web</artifactId>
        <version>23</version>
    </dependency>
    <!-- FontAwesomeFX for icons -->
    <dependency>
        <groupId>de.jensd</groupId>
        <artifactId>fontawesomefx-fontawesome</artifactId>
        <version>4.7.0-9.1.2</version>
    </dependency>
    <!-- JDBC for database connection (example for MySQL) -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
</dependencies>
```

If you are not using Maven, download the corresponding JARs and add them manually to your project.

### 3. **Database**
- **Database Management System (DBMS)**: MySQL (or another DBMS compatible with JDBC, such as PostgreSQL).


### 4. **Scene Builder (Optional)**
- **Scene Builder**: To edit FXML files (graphical interface). Download Scene Builder from [Gluon](https://gluonhq.com/products/scene-builder/).
- Add the FontAwesomeFX library to Scene Builder to load FXML files that use FontAwesome icons:
    - In Scene Builder, go to `File > Preferences > JAR/FXML Manager` and add the `fontawesomefx-fontawesome` JAR.

### 5. **Execution Configuration**
- If you are using a modular project (with a `module-info.java` file), ensure it includes the necessary modules:
  ```java
  module com.aa.designmyexperience {
      requires javafx.controls;
      requires javafx.fxml;
      requires javafx.web;
      requires java.sql;
      requires java.desktop;

      opens com.aa.designmyexperience to javafx.fxml;
      opens com.aa.designmyexperience.Controllers to javafx.fxml;

      exports com.aa.designmyexperience;
      exports com.aa.designmyexperience.Controllers;
  }
  ```
- Configure the JVM arguments in IntelliJ (or your IDE) to include JavaFX:
  ```
  --module-path /path/to/javafx-sdk-23/lib --add-modules javafx.controls,javafx.fxml,javafx.web
  ```

## Installation

1. **Clone the Project:**
   ```bash
   git clone <repository-URL>
   cd DesignMyExperienceProject
   ```

2. **Set Up the Database:**
    - Create the database and tables as described above.
    - Update the connection details in your code (e.g., in a `DatabaseConnection.java` class).

3. **Open the Project in IntelliJ:**
    - Import the project as a Maven project.
    - Let IntelliJ download the dependencies.

4. **Run the Application:**
    - Set up a run configuration in IntelliJ to execute the main class (e.g., `Main.java`).
    - Add the JVM arguments mentioned above.
    - Launch the application.

## Main Features

### 1. **Registration and Login**
- Users can register as a `customer` or `business_owner`.
- Users can log in using their email and password.
- A session is maintained to track the logged-in user.

### 2. **Home Page (`home.fxml`)**
- Displays a list of events as cards (`card.fxml`) in a `ScrollPane` with a `HBox`.
- Each card shows the event's name, category, date, location, price, and image.
- Clicking on a card redirects the user to a details page (`event-details.fxml`).

### 3. **Customer Profile (`profile.fxml`)**
- Displays the customer's information (name, photo).
- Allows the customer to manage payment information (`payment.fxml`):
    - Add a credit card (number, holder, expiration date, CVV).
    - Option to log in to PayPal (opens a URL in the browser).
- Allows booking events (not implemented in the provided files).

### 4. **Business Owner Profile (`ownerProfile.fxml`)**
- Displays the business information (name, logo, description).
- Lists the events created by the owner as `RadioButton`s in a `ScrollPane` with a `VBox`.
- Allows the owner to:
    - Add a new event (`addEvent.fxml`).
    - Edit an existing event (pre-fills the `addEvent.fxml` form).
    - Delete an event (with confirmation).
- Buttons to manage bookings and promotions (not implemented in the provided files).

### 5. **Add and Edit Events (`addEvent.fxml`)**
- Form to create or edit an event:
    - Title, description, date, location, price, maximum capacity, discount (optional), image (optional).
- Field validation (e.g., date in `YYYY-MM-DD HH:MM` format, positive price, etc.).
- Saves the data to the `events` table.

### 6. **Navigation**
- Uses a `NavigationManager` class to handle navigation between pages (e.g., `login.fxml`, `home.fxml`, `ownerProfile.fxml`, etc.).
- Buttons to return to the home page or log out.

## Project Structure

- **`src/main/java/com/aa/designmyexperience/Controllers/`**: Contains JavaFX controllers (e.g., `CardController.java`, `OwnerController.java`, `AddEventController.java`).
- **`src/main/resources/com/aa/designmyexperience/`**: Contains FXML files (e.g., `card.fxml`, `ownerProfile.fxml`, `addEvent.fxml`).
- **`src/main/resources/Styles/`**: Contains CSS files (e.g., `styles.css`).
- **`src/main/resources/Images/`**: Contains images (e.g., `LogoDesignMyExperience.png`, `compagnieLogo.jpg`).

## Known Issues

- **FontAwesome Icons**: FXML files using `FontAwesomeIconView` require the FontAwesomeFX library. Ensure it is added to your project and Scene Builder.
- **Database Connection**: The connection must be initialized and passed to controllers (e.g., via `setConnection`).
- **Unimplemented Features**: Managing bookings and promotions (buttons in `ownerProfile.fxml`) is not yet implemented.



## Authors

- **Dossongui Aziz KONE**
- **Ayoub MECHENENE**