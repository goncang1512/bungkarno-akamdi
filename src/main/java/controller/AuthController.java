package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lib.Fetch;
import lib.SaveToken;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class AuthController {
    private String urlApi = "http://localhost:3000/api";
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

            fetcher.fetch(urlApi + "/login", "POST", loginData.toString());
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
            fethcer.fetch(urlApi + "/registrasi", "POST", dataRegis.toString());
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
            fetcher.fetch(urlApi + "/logout", "DELETE", dataLogout.toString());
            SaveToken.deleteToken();
            ScreenController.loadPage("login");
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}
