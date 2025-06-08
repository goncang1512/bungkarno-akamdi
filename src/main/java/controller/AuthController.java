package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import lib.Env;
import lib.Fetch;
import lib.SaveToken;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthController {
    @FXML
    TextField emailField, passwordField, confirmPasswordField, nameField;

    @FXML
    Label labelDescription;

    @FXML
    public void onLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            Fetch fetcher = new Fetch();
            JSONObject loginData = new JSONObject();
            loginData.put("email", email);
            loginData.put("password", password);

            fetcher.fetch(Env.URL_API + "/login", "POST", loginData.toString());
            JSONObject data = fetcher.getObj();
            Boolean status = data.getBoolean("status");

            if(status) {
                SaveToken.saveToken(data.getJSONObject("results").getString("token"));
                emailField.setText("");
                passwordField.setText("");
                ScreenController.loadPage("home");
            } else {
                String message = data.getString("message");
                labelDescription.setText(message);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onLink() throws IOException {
        ScreenController.loadPage("register");
    }

    public void linkLogin() throws IOException {
        ScreenController.loadPage("login");
    }

    public void onRegister() {
        Fetch fethcer = new Fetch();
        JSONObject dataRegis = new JSONObject();
        dataRegis.put("name", nameField.getText());
        dataRegis.put("email", emailField.getText());
        dataRegis.put("password", passwordField.getText());
        dataRegis.put("confirmPassword", confirmPasswordField.getText());

        try {
            fethcer.fetch(Env.URL_API + "/registrasi", "POST", dataRegis.toString());
            JSONObject data = fethcer.getObj();
            Boolean status = data.getBoolean("status");

            if(status) {
                emailField.setText("");
                passwordField.setText("");
                ScreenController.loadPage("login");
            } else {
                String message = data.getString("message");
                labelDescription.setText(message);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onLogout() {
        Fetch fetcher = new Fetch();
        JSONObject dataLogout = new JSONObject();
        dataLogout.put("token", SaveToken.loadToken());

        try {
            fetcher.fetch(Env.URL_API + "/logout", "DELETE", dataLogout.toString());
            SaveToken.deleteToken();
            ScreenController.loadPage("login");
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public static JSONObject getSession() {
        Fetch fetcher = new Fetch();
        Map<String, String> headers = new HashMap<>();

        headers.put("Authorization", "Bearer "+ SaveToken.loadToken());
        headers.put("Accept", "application/json");

        try {
            fetcher.fetch(Env.URL_API + "/session", "GET", null, headers);
            JSONObject user = fetcher.getObj().getJSONObject("results").getJSONObject("user");

            return user;
        } catch(Exception error){
            error.printStackTrace();
            return null;
        }
    }
}
