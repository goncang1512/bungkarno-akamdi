package components;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CardComment {
    @FXML
    private ImageView profileImageView;

    @FXML
    private Label nameLabel;

    @FXML
    private Label createdAtLabel;

    @FXML
    private Label commentLabel;

    // Public method to set the data dynamically
    public void setCommentData(String name, String createdAt, String comment) {
        nameLabel.setText(name);
        createdAtLabel.setText(formattedDate(createdAt));
        commentLabel.setText(comment);
        Image image = new Image("https://i.pinimg.com/736x/1c/7f/c1/1c7fc1bbf4feadf87dcc9c29f973e44d.jpg");
        profileImageView.setImage(image);
    }

    private String formattedDate(String createdAt) {
        OffsetDateTime dateTime = OffsetDateTime.parse(createdAt);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));
        return dateTime.format(formatter);
    }
}
