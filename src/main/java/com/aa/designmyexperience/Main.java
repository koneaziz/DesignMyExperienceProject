package com.aa.designmyexperience;

import com.aa.designmyexperience.Util.NavigationManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        NavigationManager.init(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Design My Experience");
        scene.getStylesheets().add(getClass().getResource("/Styles/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setMinWidth(1600);
        stage.setMinHeight(900);
        stage.setMaxWidth(1600);
        stage.setMaxHeight(900);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
