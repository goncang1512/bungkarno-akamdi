package controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import lib.Env;
import lib.Fetch;
import org.json.JSONArray;
import org.json.JSONObject;

public class StudyController {
    @FXML
    private Label titleCourse;
    @FXML
    private VBox mediaVidio, commentContainer;
    @FXML
    private HBox boxControls;
    @FXML
    private Button buttonPlay, buttonSend;
    @FXML
    private TextField inputMessage;

    private String pelatihanId;

    public void setPelatihanId(String id) throws Exception {
        this.pelatihanId = id;
        refreshUIAsync(); // langsung update UI jika sudah ter-load
    }

    private void refreshUIAsync() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Fetch fetcher = new Fetch();
                fetcher.fetch(Env.URL_API + "/pelatihan/" + pelatihanId, "GET", null, null);
                JSONObject course = fetcher.getObj();
                String courseName = course.getJSONObject("results").getString("name");
                JSONArray comments = getComment();

                Platform.runLater(() -> {
                    titleCourse.setText(courseName);
                    String urlVidio = "https://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";
                    playMedia(urlVidio);

                    commentContainer.getChildren().clear();

                    for (int i = 0; i < comments.length(); i++) {
                        JSONObject c = comments.getJSONObject(i);
                        String content = c.optString("content", "No Content");

                        Label commentLabel = new Label(content);
                        commentLabel.setStyle("-fx-padding: 10 0 0 0;"); // style sama seperti di FXML
                        commentLabel.setWrapText(true); // biar teks panjang bisa wrap

                        commentContainer.getChildren().add(commentLabel);
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
        dataComment.put("user_id", user.getString("id"));

        fetcher.fetch(Env.URL_API + "/comment", "POST", dataComment.toString(), null);

        inputMessage.setText("");
        JSONArray comments = getComment();

        loadComments(comments);
    }

    private JSONArray getComment() throws Exception {
        Fetch fetcher = new Fetch();
        fetcher.fetch(Env.URL_API + "/comment/" + pelatihanId, "GET", null, null);
        JSONObject data = fetcher.getObj();
        JSONArray comments = data.getJSONArray("results");

        return comments;
    }

    public void loadComments(JSONArray comments) {
        // Clear dulu commentContainer
        Platform.runLater(() -> {
            commentContainer.getChildren().clear();

            for (int i = 0; i < comments.length(); i++) {
                JSONObject c = comments.getJSONObject(i);
                String content = c.optString("content", "No Content");

                Label commentLabel = new Label(content);
                commentLabel.setStyle("-fx-padding: 10 0 0 0;"); // style sama seperti di FXML
                commentLabel.setWrapText(true); // biar teks panjang bisa wrap

                commentContainer.getChildren().add(commentLabel);
            }
        });
    }
}
