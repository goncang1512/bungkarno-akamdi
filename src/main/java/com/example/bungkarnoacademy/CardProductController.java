package com.example.bungkarnoacademy;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CardProductController {
    @FXML
    private Label titleLabel;

    @FXML
    public Label descriptionText;

    @FXML
    private Label priceText;

    public void setProductData(String title, String description, String price) {
        titleLabel.setText(title);
        descriptionText.setText(description);
        priceText.setText(price);
    }
}
