// Anthony Ramnarain: CS212 Project 2

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Pr25255 extends Application{
    private String p1 = "";
    private String p2 = "";
    private String p1Tile = "";
    private String p2Tile = "";

    private boolean alternateTurn = true;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        primaryStage.show();
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Tic Tac Toe Game");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));

        Label firstPlayerLabel = new Label("Player 1");
        TextField firstPlayerTxt = new TextField();
        Label secondPlayerLabel = new Label("Player 2");
        TextField secondPlayerTxt = new TextField();
        Label errorLabel = new Label("");
        Button letsPlayBtn = new Button("Let's Play !");
        Label message = new Label("To play against computer that makes random moves, enter 'random' " +
                "\nTo play against a computer that makes smart moves, enter 'smart'");


        GridPane.setMargin(firstPlayerLabel, new Insets(10));
        GridPane.setMargin(firstPlayerTxt, new Insets(10));
        GridPane.setMargin(secondPlayerLabel, new Insets(10));
        GridPane.setMargin(secondPlayerTxt, new Insets(10));
        GridPane.setMargin(letsPlayBtn, new Insets(10));
        GridPane.setMargin(errorLabel, new Insets(10));

        grid.add(firstPlayerLabel, 0, 0);
        grid.add(firstPlayerTxt, 1, 0);
        grid.add(secondPlayerLabel, 0, 1);
        grid.add(secondPlayerTxt, 1, 1);
        grid.add(errorLabel, 0, 2);
        grid.add(message, 0, 2);
        grid.add(letsPlayBtn, 0, 3);
        GridPane.setColumnSpan(errorLabel, 2);
        GridPane.setColumnSpan(letsPlayBtn, 2);

        Scene firstScene = new Scene(grid);
        primaryStage.setScene(firstScene);

        letsPlayBtn.setOnAction(arg0 -> {

            if (firstPlayerTxt.getText().isEmpty()
                    || secondPlayerTxt.getText().isEmpty()) {
                errorLabel.setText("Names are mandatory !");
                errorLabel.setStyle("-fx-text-fill: red");
                firstPlayerTxt.requestFocus();
                return;
            }

            p1 = firstPlayerTxt.getText();
            p2 = secondPlayerTxt.getText();
            new GameBoard(primaryStage);
        });

        primaryStage.sizeToScene();
    }

    class GameBoard {

        private Button[] buttons = new Button[9];
        private int counter = 0;
        private Stage stage;

        GameBoard(Stage stage) {

            createOrganizeAndAssignActionToButtons(buttons);

            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(20));

            int counter = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (counter > 8) {
                        break;
                    }
                    gridPane.add(buttons[counter], i, j);
                    counter++;
                }
            }

            Scene scene = new Scene(gridPane);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
            stage.sizeToScene();
            this.stage = stage;

            if (alternateTurn) {
                p1Tile = "O";
                p2Tile = "X";
                playTurn(null);
            } else {
                p1Tile = "X";
                p2Tile = "O";
            }

        }

        private void createOrganizeAndAssignActionToButtons(Button[] buttons) {

            for (int i = 0; i < 9; i++) {
                Button button = new Button();
                buttons[i] = button;
                button.setPrefWidth(80);
                button.setMaxWidth(80);
                button.setPrefHeight(80);
                button.setMaxHeight(80);
                button.setStyle(" -fx-padding: 0; -fx-spacing: 0; -fx-alignment: center; -fx-font: 75 arial;");
                changeTurn(button);

            }
        }

        private boolean checkWinner(String tile) {
            // first column
            if (buttons[0].getText().equals(tile) && buttons[1].getText().equals(tile) && buttons[2].getText().equals(tile) ||
                    // second column
                    buttons[3].getText().equals(tile) && buttons[4].getText().equals(tile) && buttons[5].getText().equals(tile) ||
                    // third column
                    buttons[6].getText().equals(tile) && buttons[7].getText().equals(tile) && buttons[8].getText().equals(tile) ||
                    // first row
                    buttons[0].getText().equals(tile) && buttons[3].getText().equals(tile) && buttons[6].getText().equals(tile) ||
                    // second row
                    buttons[1].getText().equals(tile) && buttons[4].getText().equals(tile) && buttons[7].getText().equals(tile) ||
                    // third row
                    buttons[2].getText().equals(tile) && buttons[5].getText().equals(tile) && buttons[8].getText().equals(tile) ||
                    // first diagonal
                    buttons[0].getText().equals(tile) && buttons[4].getText().equals(tile) && buttons[8].getText().equals(tile) ||
                    // second diagonal
                    buttons[2].getText().equals(tile) && buttons[4].getText().equals(tile) && buttons[6].getText().equals(tile)) {
                PopUp popup = new PopUp(stage);
                String winner;
                if (tile.equals(p1Tile)) {
                    winner = p1;
                } else {
                    winner = p2;
                }
                popup.setMessage(winner+" wins");
                return true;
            } else if (counter >= 9) {
                PopUp popup = new PopUp(stage);
                popup.setMessage("Draw... ");
                return true;
            }
            return false;
        }

        private boolean placeTile(Button button) {
            String tile;
            if (alternateTurn) {
                button.setText(p2Tile);
                alternateTurn = false;
            } else {
                button.setText(p1Tile);
                alternateTurn = true;
            }
            tile = button.getText();
            counter++;
            return checkWinner(tile);
        }

        private void playTurn(Button button) {
            boolean hasWinner = false;
            if (button != null) {
                hasWinner = placeTile(button);
            }

            if (!hasWinner && p2.equals("smart")) {
                int winSpot = findWinSpot(p2Tile);
                if (winSpot > -1) {
                    placeTile(buttons[winSpot]);
                    return;
                }
                winSpot = findWinSpot(p1Tile);
                if (winSpot > -1) {
                    placeTile(buttons[winSpot]);
                    return;
                }
            }

            if (!hasWinner && (p2.toLowerCase().equals("random") || p2.toLowerCase().equals("smart"))) {
                ArrayList<Button> buttonList = new ArrayList<>(Arrays.asList(buttons));
                Collections.shuffle(buttonList);
                for (Button butto:buttonList) {
                    if (butto.getText().isEmpty()) {
                        placeTile(butto);
                        break;
                    }
                }
            }
        }

        private int findWinSpot(String tile) {
            int winSpot;

            if (buttons[0].getText().equals(tile) && buttons[1].getText().equals(tile)) {
                winSpot = 2;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }
            if (buttons[3].getText().equals(tile) && buttons[4].getText().equals(tile)) {
                winSpot = 5;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }
            if (buttons[6].getText().equals(tile) && buttons[7].getText().equals(tile)) {
                winSpot = 8;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }
            if (buttons[0].getText().equals(tile) && buttons[3].getText().equals(tile)) {
                winSpot = 6;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }
            if (buttons[1].getText().equals(tile) && buttons[4].getText().equals(tile)) {
                winSpot = 7;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }
            if (buttons[2].getText().equals(tile) && buttons[5].getText().equals(tile)) {
                winSpot = 8;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }
            if (buttons[0].getText().equals(tile) && buttons[4].getText().equals(tile)) {
                winSpot = 8;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }
            if (buttons[2].getText().equals(tile) && buttons[4].getText().equals(tile)) {
                winSpot = 6;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }
            if (buttons[1].getText().equals(tile) && buttons[2].getText().equals(tile)) {
                winSpot = 0;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }
            if (buttons[4].getText().equals(tile) && buttons[5].getText().equals(tile)) {
                winSpot = 3;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }
            if (buttons[7].getText().equals(tile) && buttons[8].getText().equals(tile)) {
                winSpot = 6;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }
            if (buttons[3].getText().equals(tile) && buttons[6].getText().equals(tile)) {
                winSpot = 0;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }
            if (buttons[4].getText().equals(tile) && buttons[7].getText().equals(tile)) {
                winSpot = 1;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }
            if (buttons[5].getText().equals(tile) && buttons[8].getText().equals(tile)) {
                winSpot = 2;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }
            if (buttons[4].getText().equals(tile) && buttons[8].getText().equals(tile)) {
                winSpot = 0;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }
            if (buttons[4].getText().equals(tile) && buttons[6].getText().equals(tile)) {
                winSpot = 2;
                if (buttons[winSpot].getText().isEmpty()) return winSpot;
            }

            return -1;
        }

        private void changeTurn(Button button) {
            button.setOnAction(arg0 -> {
                if (button.getText().isEmpty()) {
                    playTurn(button);
                }
            });
        }
    }

    class PopUp {

        private Label message;

        PopUp(Stage stage) {
            message = new Label();
            message.setStyle("-fx-font-color:red");
            Button quitButton = new Button("Quit");
            Button continueButton = new Button("Continue Playing");

            VBox layout = new VBox();

            layout.setAlignment(Pos.CENTER);
            VBox.setMargin(message, new Insets(10));
            VBox.setMargin(quitButton, new Insets(10));
            VBox.setMargin(continueButton, new Insets(10));
            layout.getChildren().add(message);
            layout.getChildren().add(quitButton);
            layout.getChildren().add(continueButton);

            Scene scene = new Scene(layout);

            stage.setScene(scene);

            quitButton.setOnAction(arg0 -> stage.close());

            continueButton.setOnAction(arg0 -> new GameBoard(stage));
            stage.sizeToScene();
        }


        void setMessage(String message) {
            this.message.setText(message);
        }
    }
}