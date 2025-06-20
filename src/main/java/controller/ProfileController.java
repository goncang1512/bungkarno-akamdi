package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lib.Env;
import lib.Fetch;
import lib.SaveToken;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileController {

    @FXML
    private Label nameText, roleUser, emailUser, joinAt;

    @FXML
    private ImageView imageProfile;

    public void initialize() {
        Fetch fetcher = new Fetch();
        Map<String, String> headers = new HashMap<>();

        headers.put("Authorization", "Bearer "+ SaveToken.loadToken());
        headers.put("Accept", "application/json");

        try {
            fetcher.fetch(Env.URL_API + "/session", "GET", null, headers);
            JSONObject user = fetcher.getObj().getJSONObject("results").getJSONObject("user");

            nameText.setText(user.getString("name"));
            String role = user.getString("role"); // misal: "admin"
            String capitalizedRole = role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase();
            roleUser.setText(capitalizedRole);
            emailUser.setText(user.getString("email"));
            joinAt.setText("Bergabung pada " + formatDate(user.getString("createdAt")));
            if(!user.isNull("image")) {
                Image image = new Image(user.getString("image"), true);
                imageProfile.setImage(image);
            }
        } catch(Exception error){
            error.printStackTrace();
        }
    }

    private String formatDate(String isoDate) {
        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(isoDate);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id"));
            return zonedDateTime.format(formatter);
        } catch (Exception e) {
            return isoDate; // fallback kalau gagal parsing
        }
    }
}
