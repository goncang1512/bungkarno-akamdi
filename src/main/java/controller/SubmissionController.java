package controller;

import com.example.bungkarnoacademy.Main;
import components.SubmissionTask;
import components.SubmissionView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lib.Env;
import lib.Fetch;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class SubmissionController {
    private String taskId;
    @FXML
    private Label titleLabel, titlePelatihan, deadlineLabel;
    @FXML
    private Text contentTask;
    @FXML
    private VBox inputSubmission;
    private String pelatihanId;

    public void setTaskId(String taskId) throws Exception {
        this.taskId = taskId;

        getTask(taskId);
    }

    public void getTask(String taskId) throws Exception {
        Fetch fetcher = new Fetch();

        fetcher.fetch(Env.URL_API + "/submission/" + taskId, "GET", null, null);
        JSONObject task = fetcher.getObj().getJSONObject("results");
        titleLabel.setText(task.getString("title"));
        titlePelatihan.setText(task.getJSONObject("pelatihan").getString("name"));
        deadlineLabel.setText(task.getString("deadline"));
        contentTask.setText(task.getString("content"));
        this.pelatihanId = task.getString("pelatihanId");

        JSONArray submission = task.getJSONArray("pengumpulan");
        String currentUserId = Main.getSession.getString("id");

        JSONObject userSubmission = null;

        for (int i = 0; i < submission.length(); i++) {
            JSONObject obj = submission.getJSONObject(i);
            if (obj.getString("userId").equals(currentUserId)) {
                userSubmission = obj;
                break; // keluar dari loop karena sudah ketemu
            }
        }

        if(userSubmission == null) {
            FXMLLoader loader = new FXMLLoader(ScreenController.class.getResource("/component/submission-task.fxml"));
            Node textEditor = loader.load();
            inputSubmission.getChildren().setAll(textEditor);
            SubmissionTask textCtrl = loader.getController();
            textCtrl.setSubmission(this, this.taskId);
        } else {
            FXMLLoader loader = new FXMLLoader(ScreenController.class.getResource("/component/submission-view.fxml"));
            Node submissionView = loader.load();
            inputSubmission.getChildren().setAll(submissionView);
            SubmissionView controller = loader.getController();
            controller.setSubmissionView(
                    userSubmission.getString("id"),
                    userSubmission.getString("title"),
                    userSubmission.getString("content"),
                    userSubmission.getString("subDate"),
                    this);
        }
    }

    public void backToTask(ActionEvent actionEvent) throws IOException {
        ScreenController.loadPage("task", controller -> {
            if (controller instanceof ManageTaskController sc) {
                try {
                    sc.setPelatihanId(pelatihanId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
