package components;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class TugasView {
    @FXML
    private Label titleLabel, deadlineLabel;
    @FXML
    private TextArea contentTextArea;
    @FXML
    private Hyperlink fileLink;

    public void setTugasView(String titleLabel, String contentTextArea, String deadline) {
        this.titleLabel.setText(titleLabel);
        this.contentTextArea.setText(contentTextArea);
        this.deadlineLabel.setText(deadline);
    }
}
