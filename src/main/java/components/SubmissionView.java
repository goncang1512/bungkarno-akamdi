package components;

import controller.SubmissionController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lib.Env;
import lib.Fetch;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SubmissionView {
    @FXML
    private Label titleLabel, contentLabel, submitDateLabel;
    @FXML
    private Button buttonDelete;
    private SubmissionController submission;

    public void setSubmissionView(String subId, String titleLabel, String contentLabel,String submitDateLabel, SubmissionController controller) {
        this.titleLabel.setText(titleLabel);
        this.contentLabel.setText(contentLabel);
        this.submitDateLabel.setText(formatDate(submitDateLabel));
        this.buttonDelete.setUserData(subId);
        this.submission = controller;

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

    public void deleteTugas() throws Exception {
        Fetch fetcher = new Fetch();
        fetcher.fetch(Env.URL_API + "/submission/" + buttonDelete.getUserData(), "DELETE", null, null);

        JSONObject data = fetcher.getObj();
        if(data.getBoolean("status")) {
            submission.getTask(data.getJSONObject("results").getString("tugasId"));
        }
    }
}
