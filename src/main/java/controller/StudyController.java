package controller;

import components.CardComment;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lib.Env;
import lib.Fetch;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudyController implements Initializable {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Text titleCourse;
    @FXML
    private VBox mediaVidio, commentContainer, mainContainer;
    @FXML
    private HBox boxControls;
    @FXML
    private Button buttonPlay, buttonSend;
    @FXML
    private TextField inputMessage;

    private String pelatihanId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setPelatihanId(String id) throws Exception {
        this.pelatihanId = id;
        refreshUIAsync(); // langsung update UI jika sudah ter-load
    }

    private void refreshUIAsync() {
        JSONObject user = AuthController.getSession();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Fetch fetcher = new Fetch();
                fetcher.fetch(Env.URL_API + "/pelatihan/" + pelatihanId  + "?user_id=" + user.getString("id"), "GET", null, null);
                JSONObject course = fetcher.getObj();
                String courseName = course.getJSONObject("results").getString("name");
                JSONArray comments = getComment();

                Platform.runLater(() -> {
                    titleCourse.setText(courseName);
                    String urlVidio = course.getJSONObject("results").getString("vidioUrl");
                    playMedia(urlVidio);

                    commentContainer.getChildren().clear();

                    for (int i = 0; i < comments.length(); i++) {
                        JSONObject c = comments.getJSONObject(i);
                        commentPlatform(c);
                    }
                });

                return null;
            }
        };
        new Thread(task).start();
    }

    private void playMedia(String url) {
        Media media = new Media(url);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        mediaView.setFitHeight(500);
        mediaView.setPreserveRatio(true);

        buttonPlay.setOnAction(e -> {
            mediaPlayer.play();
        });

        // Listener status mediaPlayer
        mediaPlayer.statusProperty().addListener((obs, oldStatus, newStatus) -> {
            switch (newStatus) {
                case PLAYING:
                    buttonPlay.setText("Pause");
                    buttonPlay.setOnAction(e -> {
                        mediaPlayer.pause();
                    });
                    break;
                case PAUSED:
                case STOPPED:
                case READY:
                case HALTED:
                case STALLED:
                default:
                    buttonPlay.setText("Play");
                    buttonPlay.setOnAction(e -> {
                        mediaPlayer.play();
                    });
                    break;
            }
        });

        // Slider waktu
        Slider timeSlider = new Slider();
        timeSlider.setMin(0);

        mediaPlayer.setOnReady(() -> {
            timeSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
        });

        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!timeSlider.isValueChanging()) {
                timeSlider.setValue(newTime.toSeconds());
            }
        });

        timeSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                mediaPlayer.seek(Duration.seconds(timeSlider.getValue()));
            }
        });

        boxControls.getChildren().add(timeSlider);
        mediaVidio.getChildren().addAll(mediaView);

        mediaPlayer.pause();
    }

    public void sendMessage() throws Exception {
        JSONObject user = AuthController.getSession();
        String message = inputMessage.getText();
        Fetch fetcher = new Fetch();

        JSONObject dataComment = new JSONObject();
        dataComment.put("comment", message);
        dataComment.put("pelatihan_id", pelatihanId);
        assert user != null;
        dataComment.put("user_id", user.getString("id"));

        fetcher.fetch(Env.URL_API + "/comment", "POST", dataComment.toString(), null);

        inputMessage.setText("");
        JSONArray comments = getComment();
        inputMessage.requestFocus();

        loadComments(comments);
    }

    public JSONArray getComment() throws Exception {
        Fetch fetcher = new Fetch();
        fetcher.fetch(Env.URL_API + "/comment/" + pelatihanId, "GET", null, null);
        JSONObject data = fetcher.getObj();

        return data.getJSONArray("results");
    }

    public void loadComments(JSONArray comments)  {
        // Clear dulu commentContainer
        Platform.runLater(() -> {
            commentContainer.getChildren().clear();

            for (int i = 0; i < comments.length(); i++) {
                JSONObject c = comments.getJSONObject(i);
                commentPlatform(c);
            }
        });
    }

    public void commentPlatform(JSONObject comment) {
        FXMLLoader loader = new FXMLLoader(ScreenController.class.getResource("/component/card-comment.fxml"));
        try {
            Node cardComment = loader.load();
            CardComment commentCtrl = loader.getController();

            commentCtrl.setCommentData(
                    comment.getString("id"),
                    comment.getJSONObject("user").getString("name"),
                    comment.getString("createdAt"),
                    comment.getString("content"),
                    comment.getJSONObject("user").getString("id")
            );

            commentCtrl.setStudyController(this);

            commentContainer.getChildren().add(cardComment);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void gotoTask() throws IOException {
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
