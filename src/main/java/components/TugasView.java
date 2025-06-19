package components;

import controller.ScreenController;
import controller.StudyController;
import controller.SubmissionController;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TugasView {
    @FXML
    private Label titleLabel, deadlineLabel;
    @FXML
    private AnchorPane cardRoot;

    @FXML
    private Hyperlink fileLink;
    private String taskId;

    public void setCardWidth(double width) {
        cardRoot.setPrefWidth(width);
    }

    public void setTugasView(String titleLabel, String deadline, String taskId) {
        this.titleLabel.setText(titleLabel);
        this.deadlineLabel.setText(deadline);
        this.taskId = taskId;
    }

    public void seeTask() throws IOException {
        ScreenController.loadPage("submission", controller -> {
            if (controller instanceof SubmissionController sc) {
                try {
                    sc.setTaskId(taskId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
