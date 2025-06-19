package controller;

import components.TextEditor;
import components.TugasView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lib.Env;
import lib.Fetch;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class ManageTaskController {
    private String pelatihanId;
    @FXML
    private AnchorPane containerEditor;
    @FXML
    private GridPane tugasContainer;

    public void setPelatihanId(String pelatihanId) throws Exception {
        this.pelatihanId = pelatihanId;
        showTextEditor();
        showtTaskClass(pelatihanId);
    }

    public void showTextEditor() throws IOException {
        JSONObject user = AuthController.getSession();
        String role = user.getString("role");

        if(Objects.equals(role, "teacher")) {
            FXMLLoader loader = new FXMLLoader(ScreenController.class.getResource("/component/code-area.fxml"));
            Node textEditor = loader.load();
            containerEditor.getChildren().setAll(textEditor);
            TextEditor textCtrl = loader.getController();
            textCtrl.setPelatihanId(this.pelatihanId);
            textCtrl.setManageTask(this);
        }
    }

    public void showtTaskClass(String pelatihanId) throws Exception {
        int column = 0;
        int row = 0;
        tugasContainer.getChildren().clear();
        Fetch fetcher = new Fetch();
        fetcher.fetch(Env.URL_API + "/tugas/" + pelatihanId, "GET", null, null);

        JSONArray dataTask = fetcher.getObj().getJSONArray("results");
        for (int i =0; i < dataTask.length(); i++) {
            JSONObject item = dataTask.getJSONObject(i);

            FXMLLoader loader = new FXMLLoader(ScreenController.class.getResource("/component/tugas-view.fxml"));
            Node cardTask = loader.load();
            TugasView controller = loader.getController();

            controller.setTugasView(item.getString("title"), item.getString("deadline"), item.getString("id"));

            GridPane.setHgrow(cardTask, Priority.ALWAYS);
            GridPane.setVgrow(cardTask, Priority.ALWAYS);

            tugasContainer.add(cardTask, column, row);

            column++;
            if (column == 2) { // Maksimal 3 kolom
                column = 0;
                row++;
            }
        }
    }
}
