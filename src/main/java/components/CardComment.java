package components;

import com.example.bungkarnoacademy.Main;
import controller.StudyController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lib.Env;
import lib.Fetch;
import org.json.JSONArray;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class CardComment {
    @FXML
    private ImageView profileImageView;

    @FXML
    private Label nameLabel;

    @FXML
    private Label createdAtLabel;
    @FXML
    private Button buttonDelete;

    @FXML
    private Label commentLabel;
    private String commentId;
    private StudyController studyController;

    public void setStudyController(StudyController studyController) {
        this.studyController = studyController;
    }

    // Public method to set the data dynamically
    public void setCommentData(String commentId, String name, String createdAt, String comment, String userId) {
        this.commentId = commentId;
        nameLabel.setText(name);
        createdAtLabel.setText(formattedDate(createdAt));
        commentLabel.setText(comment);
        Image image = new Image("https://i.pinimg.com/736x/1c/7f/c1/1c7fc1bbf4feadf87dcc9c29f973e44d.jpg");
        profileImageView.setImage(image);
        if(!Objects.equals(Main.getSession.getString("id"), userId)) {
            buttonDelete.setVisible(false);
        }
    }

    private String formattedDate(String createdAt) {
        OffsetDateTime dateTime = OffsetDateTime.parse(createdAt);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));
        return dateTime.format(formatter);
    }

    public void deleteComment() throws Exception {
        Fetch fetcher = new Fetch();
        fetcher.fetch(Env.URL_API + "/comment/" + commentId, "DELETE", null, null);

        JSONArray comments = studyController.getComment();
        studyController.loadComments(comments);
    }
}
