package com.example.mediaplayer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class LesothoTriviaGame extends Application {

    private final String[] questions = {
            "What is the capital city of Lesotho?",
            "What is the highest peak in Lesotho?"
    };

    private final String[][] options = {
            {"Maseru", "Leribe", "Mafeteng", "Quthing"},
            {"Thabana Ntlenyana", "Sani Pass", "Tsehlanyane National Park", "Mokhotlong"}
    };

    private final String[] correctAnswers = {"Maseru", "Thabana Ntlenyana"};

    private final String[] imageFiles = {
            "maseru_image.jpg",
            "thabana_image.jpg"
    };

    private final String[] videoFiles = {
            "maseru_video.mp4",
            "thabana_video.mp4"
    };

    private int currentQuestionIndex = 0;
    private int score = 0;

    private Label questionLabel;
    private Button[] optionButtons;
    private Label feedbackLabel;
    private Label scoreLabel;
    private MediaPlayer mediaPlayer;
    private ImageView imageView;
    private MediaView mediaView;

    @Override
    public void start(Stage primaryStage) {
        questionLabel = new Label();
        imageView = new ImageView();
        mediaView = new MediaView();

        optionButtons = new Button[4]; // Initialize optionButtons array here

        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new Button();
            optionButtons[i].setOnAction(e -> checkAnswer(((Button) e.getSource()).getText()));
        }

        feedbackLabel = new Label();

        Button nextButton = new Button("Next Question");
        nextButton.setOnAction(e -> {
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.length) {
                updateQuestion();
            } else {
                endGame();
            }
        });

        scoreLabel = new Label();

        VBox root = new VBox(10);
        root.getChildren().addAll(questionLabel, imageView, mediaView, optionButtons[0], optionButtons[1], optionButtons[2], optionButtons[3],
                feedbackLabel, nextButton, scoreLabel);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lesotho Trivia Game");
        primaryStage.show();

        updateQuestion(); // Call updateQuestion after initializing optionButtons array
    }

    private void updateQuestion() {
        questionLabel.setText(questions[currentQuestionIndex]);

        // Load image
        try {
            Image image = new Image(getClass().getResourceAsStream(imageFiles[currentQuestionIndex]));
            imageView.setImage(image);
            imageView.setFitWidth(400); // Set the width you desire
            imageView.setFitHeight(300); // Set the height you desire
        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("Error loading image: " + e.getMessage());
            imageView.setImage(null); // Clear image if loading fails
        }

        // Load video
        try {
            playVideo(videoFiles[currentQuestionIndex]);
            mediaPlayer.setOnReady(() -> {
                mediaView.setMediaPlayer(mediaPlayer);
                mediaView.setFitWidth(400); // Set the width you desire
                mediaView.setFitHeight(300); // Set the height you desire
            });
        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("Error loading video: " + e.getMessage());
        }

        // Set options
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options[currentQuestionIndex][i]);
        }

        feedbackLabel.setText("");
    }

    private void playVideo(String fileName) {
        try {
            Media media = new Media(getClass().getResource(fileName).toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("Error loading video: " + e.getMessage());
        }
    }

    private void checkAnswer(String selectedOption) {
        if (selectedOption.equals(correctAnswers[currentQuestionIndex])) {
            feedbackLabel.setText("Correct!");
            score++;
        } else {
            feedbackLabel.setText("Incorrect!");
        }
        updateScore();
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Stop the video playback if it's not null
        }
    }

    private void endGame() {
        questionLabel.setText("Quiz completed!");
        for (Button button : optionButtons) {
            button.setDisable(true);
        }
        scoreLabel.setText("Your score: " + score + " out of " + questions.length);
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Stop the video playback if it's not null
        }
    }

    private void updateScore() {
        scoreLabel.setText("Score: " + score);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
