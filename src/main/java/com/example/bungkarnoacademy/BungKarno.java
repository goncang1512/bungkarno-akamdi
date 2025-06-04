package com.example.bungkarnoacademy;

import controller.ScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lib.SaveToken;

import java.io.IOException;

public class BungKarno extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(new Pane(), 1200, 500);
        ScreenController.init(scene); // penting

        if(SaveToken.loadToken() != null) {
            Parent home = ScreenController.loadWithTemplate("com/example/bungkarnoacademy/home-page.fxml");
            ScreenController.addScreen("home", home);
            ScreenController.activate("home");
        } else {
            Parent login = FXMLLoader.load(getClass().getResource("login-page.fxml"));
            Parent register = FXMLLoader.load(getClass().getResource("register-page.fxml"));
            ScreenController.addScreen("login", login);
            ScreenController.addScreen("register", register);

            ScreenController.activate("login");
        }

        stage.setTitle("Bung Karno Academy");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}