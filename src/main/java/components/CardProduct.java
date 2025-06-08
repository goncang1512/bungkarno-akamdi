package components;

import controller.ScreenController;
import controller.StudyController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.application.Platform;

import java.io.IOException;

public class CardProduct {
    @FXML
    private ImageView avatarImageView;
    @FXML
    private Region spacer;
    @FXML
    private Label nameCourse, description, participant, username;
    @FXML
    private VBox boxTitle;

    public void initialize() {
        // Menunda eksekusi clip sampai layout selesai

        Platform.runLater(() -> {
            double radius = avatarImageView.getFitWidth() / 2;
            Circle clip = new Circle(radius, radius, radius);
            avatarImageView.setClip(clip);
        });
    }

    public void setProfile(String url) {
        Image image = new Image(url);
        avatarImageView.setImage(image);
    }

    public void setNameCourse(String name) {
        this.nameCourse.setText(name);
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public void setUserData(String id) {
        this.boxTitle.setUserData(id);
    }

    public  void setParticipant(int participant) {
        this.participant.setText(String.valueOf(participant));
    }

    public void setUsername (String username) {
        this.username.setText(username);
    }

    public void linkToDetail() throws IOException {
        String data = boxTitle.getUserData().toString();
        ScreenController.loadPage("study", controller -> {
            if (controller instanceof StudyController sc) {
                try {
                    sc.setPelatihanId(data);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
