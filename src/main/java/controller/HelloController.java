package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lib.Fetch;
import lib.SaveToken;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HelloController {
//    @FXML
//    private Label welcomeText;
//
//    @FXML
//    private VBox container;
//
//    @FXML
//    Button linkLogin, logoutButton;
//
//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//
//        try {
//            Fetch login = new Fetch();
//            Map<String, String> headers = new HashMap<>();
//            headers.put("Authorization", "Bearer fuydNj0HNM7MG4WfZf8hJmIc4ZazJIq8"); // atau bisa juga "Bearer token123"
//            headers.put("Accept", "application/json");
//            login.fetch("http://localhost:3000/api/session", "GET", null);
//            JSONObject data = login.getObj().getJSONObject("results").getJSONObject("user");
//            System.out.println("NAMA : " + data.getString("name"));
//
//        } catch(Exception e) {
//            System.out.println(e);
//        }
//    }
//
//    @FXML
//    public void initialize() {
//        String token = SaveToken.loadToken();
//        if(token != null) {
//            linkLogin.setVisible(false);
//            logoutButton.setVisible(true);
//        } else {
//            logoutButton.setVisible(false);
//            linkLogin.setVisible(true);
//        }
//    }
//
//    @FXML
//    public void onLink() {
//        ScreenController.activate("login");
//    }
//
//    @FXML
//    public void onLogout() {
//        AuthController auth = new AuthController();
//        auth.onLogout();
//    }
}