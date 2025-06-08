package controller;

import components.CardProduct;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import lib.Env;
import lib.Fetch;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class HomeController {
    @FXML
    GridPane pelatihanContainer;

    public void showPelatihan() throws Exception {
        int column = 0;
        int row = 0;
        Fetch fetcher = new Fetch();

        fetcher.fetch(Env.URL_API + "/home", "GET", null, null);
        JSONObject data = fetcher.getObj();

        JSONArray pelatihan = data.getJSONArray("results");

        for (int i = 0; i < pelatihan.length(); i++) {
            JSONObject item = pelatihan.getJSONObject(i);

            FXMLLoader loader = new FXMLLoader(ScreenController.class.getResource("/component/card-product.fxml"));
            Node cardProduct = loader.load();

            CardProduct product = loader.getController();

            product.setDescription(item.getString("description"));
            product.setNameCourse(item.getString("name"));
            product.setUsername(item.getJSONObject("user").getString("name"));
            product.setProfile(item.getJSONObject("user").getString("image"));
            product.setUserData(item.getString("id"));

            // Tambahkan ke dalam container
            GridPane.setColumnIndex(cardProduct, column);
            GridPane.setRowIndex(cardProduct, row);


            pelatihanContainer.getChildren().add(cardProduct);

            column++;
            if (column == 3) { // Maksimal 3 kolom
                column = 0;
                row++;
            }
        }
    }

    public void initialize() {
        try {
            this.showPelatihan();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
