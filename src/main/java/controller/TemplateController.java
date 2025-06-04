package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Node;

import java.io.IOException;

public class TemplateController {
    @FXML
    private AnchorPane contentPane;

    public void setContent(Node node) {
        contentPane.getChildren().setAll(node);
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

    public void onLink(ActionEvent event) throws IOException {
        Button btn = (Button) event.getSource();
        String data = (String) btn.getUserData();

        ScreenController.loadPage(data);
    }

    public void onLogout() {
        AuthController auth = new AuthController();
        auth.onLogout();
    }
}
