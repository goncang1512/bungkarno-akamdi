package components;

import com.example.bungkarnoacademy.Main;
import controller.SubmissionController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lib.Env;
import lib.Fetch;
import org.fxmisc.richtext.CodeArea;
import org.json.JSONObject;

public class SubmissionTask {
    private SubmissionController submission;
    private String taskId;
    @FXML
    private TextField judulField;
    @FXML
    private CodeArea editorArea;

    public void setSubmission(SubmissionController submission, String taskId) {
        this.submission = submission;
        this.taskId = taskId;
    }

    public void addTugas() throws Exception {
        Fetch fetcher = new Fetch();
        JSONObject taskData = new JSONObject();
        taskData.put("title", judulField.getText());
        taskData.put("content", editorArea.getText());
        taskData.put("userId", Main.getSession.getString("id"));
        taskData.put("tugasId", this.taskId);
        taskData.put("fileUrl", "");

        fetcher.fetch(Env.URL_API + "/submission", "POST", taskData.toString(), null);
        JSONObject data = fetcher.getObj();

        System.out.println(data);

        if(data.getBoolean("status")) {
            submission.getTask(taskId);
        }
    }
}
