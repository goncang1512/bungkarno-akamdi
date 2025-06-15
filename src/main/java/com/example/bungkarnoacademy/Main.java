package com.example.bungkarnoacademy;

import controller.AuthController;
import controller.ScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lib.SaveToken;
import org.json.JSONObject;

import java.io.IOException;

public class Main extends Application {
    public static JSONObject getSession;

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(new Pane(), 1200, 500);
        ScreenController.init(scene); // penting

        if(SaveToken.loadToken() != null) {
            ScreenController.loadPage("home");
            JSONObject user = AuthController.getSession();
            getSession = user;
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