package components;

import controller.ManageTaskController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lib.Env;
import lib.Fetch;
import org.fxmisc.richtext.CodeArea;
import org.json.JSONObject;

public class TextEditor {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private CodeArea editorArea;
    @FXML
    private TextField judulField, deadlineField;
    private String dataTugas;
    private String pelatihanId;
    private ManageTaskController manageTask;

    @FXML
    public void initialize() {
        rootPane.setOnMouseClicked(event -> {
            if (!editorArea.isFocused()) {
                editorArea.setEditable(false);
            }
        });

        // Saat CodeArea di klik/fokus, aktifkan kembali edit
        editorArea.setOnMouseClicked(event -> {
            editorArea.setEditable(true);
        });

        editorArea.replaceText("Ketik di sini");
    }

    @FXML
    public void addTugas() throws Exception {
        this.dataTugas = editorArea.getText();
        String judul = judulField.getText();
        String deadline = deadlineField.getText();

        JSONObject dataTask = new JSONObject();
        dataTask.put("title", judul);
        dataTask.put("content", dataTugas);
        dataTask.put("deadline", deadline);
        dataTask.put("pelatihanId", this.pelatihanId);
        dataTask.put("fileUrl", "helloworld.png");

        Fetch fetcher = new Fetch();

        fetcher.fetch(Env.URL_API + "/tugas", "POST", dataTask.toString(), null);
        JSONObject result = fetcher.getObj();

        if(result.getBoolean("status")) {
            editorArea.replaceText("Ketik di sini!");
            this.judulField.setText("");
            this.deadlineField.setText("");
            manageTask.showtTaskClass(this.pelatihanId);
        }
    }

    public void setPelatihanId(String pelatihanId) {
        this.pelatihanId = pelatihanId;
    }

    public void setManageTask(ManageTaskController manage) {
        this.manageTask = manage;
    }
}
