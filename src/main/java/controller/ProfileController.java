package controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import lib.Env;
import lib.Fetch;
import lib.SaveToken;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileController {

    @FXML
    private Text nameText;

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
            if(!user.isNull("image")) {
                Image image = new Image(user.getString("image"), true);
                imageProfile.setImage(image);
            }
        } catch(Exception error){
            error.printStackTrace();
        }
    }
}
