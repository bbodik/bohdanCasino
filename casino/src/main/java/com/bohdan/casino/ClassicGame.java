package com.bohdan.casino;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Random;

public class ClassicGame {
    private int counter, level = 4;
    private float money = 100, bet;
    Random rnd = new Random();
    private ArrayList<ImageView> iconsArr = new ArrayList<>();

    private boolean spinning = false;
    private final Image[] icons = {
            new Image(getClass().getResourceAsStream("/lemon.png")),
            new Image(getClass().getResourceAsStream("/cherry.png")),
            new Image(getClass().getResourceAsStream("/grape.png")),
            new Image(getClass().getResourceAsStream("/bar.png")),
            new Image(getClass().getResourceAsStream("/coin.png")),
            new Image(getClass().getResourceAsStream("/seven.png"))
    };
    @FXML
    private ImageView icon1, icon2, icon3, icon4;
    @FXML
    private Circle classicCircle;

    @FXML
    private Label classickChanse,TextMoney;
    @FXML
    private TextField betSomeMoney;

    @FXML
    private Button spinButton, backToMainMenuButtonInClassicGame;
    @FXML
    private Pane mainMenuPane, classicGamePane, babloPane, gayWayPane;
    @FXML
    private ComboBox<String> comboBoxClassick;
    @FXML
    protected void startClassicMode() {
        iconsArr.add(icon1);
        iconsArr.add(icon2);
        iconsArr.add(icon3);
        iconsArr.add(icon4);
        mainMenuPane.setVisible(false);
        classicGamePane.setVisible(true);
        babloPane.setVisible(true);
        icon1.setImage(icons[5]);
        icon2.setImage(icons[5]);
        icon3.setImage(icons[5]);
        icon4.setImage(icons[5]);
        ObservableList<String> options = FXCollections.observableArrayList(
                "Легкий",
                "Середній",
                "Важкий"
        );
        comboBoxClassick.setItems(options);
        spinButton.setDisable(true);
    }
    @FXML
    protected void backToMainMenuButton() {
        mainMenuPane.setVisible(true);
        classicGamePane.setVisible(false);
        babloPane.setVisible(false);
    }
    @FXML
    protected void changeDiff() {
        String selectedValue = comboBoxClassick.getValue();

        if (selectedValue != null) {
            if (selectedValue.equals("Легкий")) {
                classickChanse.setText("16.666%");
                level = 2;
                iconsArr.clear();
                iconsArr.add(icon1);
                iconsArr.add(icon2);
                icon3.setImage(null);
                icon4.setImage(null);
            } else if (selectedValue.equals("Середній")) {
                classickChanse.setText("2.777%");
                level = 3;
                iconsArr.clear();
                iconsArr.add(icon1);
                iconsArr.add(icon2);
                iconsArr.add(icon3);
                icon4.setImage(null);
            } else if (selectedValue.equals("Важкий")) {
                level = 4;
                classickChanse.setText("0.462%");
                iconsArr.clear();
                iconsArr.add(icon1);
                iconsArr.add(icon2);
                iconsArr.add(icon3);
                iconsArr.add(icon4);
            }
        }
    }
    @FXML
    protected void spin() {
        bet=Integer.parseInt(betSomeMoney.getText());
        if (spinning||money-bet<0) {
            return;
        }

        money-=bet;
        setMoney();
        counter = iconsArr.size();
        spinning = true;
        spinButton.setDisable(true);
        comboBoxClassick.setDisable(true);
        classicCircle.setFill(Color.WHITE);
        Thread[] threads = new Thread[iconsArr.size()];
        Object lock = new Object();

        for (int i = 0; i < threads.length; i++) {
            threads[i] = createIconThread(iconsArr.get(i), icons, lock);
        }
        for (Thread thread : threads) {
            thread.start();
        }

    }
    private void checkWinInClassick() {
        Image firstImage = iconsArr.get(0).getImage();
        boolean isWinner = true;
        for (ImageView icon : iconsArr) {
            if (!icon.getImage().equals(firstImage)) {
                isWinner = false;
                break; // Перервати цикл, якщо знайдено різні картинки
            }
        }

        if (isWinner) {
            classicCircle.setFill(getColorForImage(firstImage));
            if (level == 2)
                money += bet * level;
            else if (level == 3)
                money += bet * 10;
            else
                money += bet * 100;
            setMoney();
        } else {
            classicCircle.setFill(Color.BLACK);
        }
    }
    private Color getColorForImage(Image image) {
        if (image.equals(icons[0])) return Color.LIGHTYELLOW;
        if (image.equals(icons[1])) return Color.INDIANRED;
        if (image.equals(icons[2])) return Color.PURPLE;
        if (image.equals(icons[3])) return Color.RED;
        if (image.equals(icons[4])) return Color.GOLD;
        if (image.equals(icons[5])) return Color.LIGHTGOLDENRODYELLOW;
        return Color.BLACK;
    }
    private Thread createIconThread(ImageView icon, Image[] icons, Object lock) {
        return new Thread(() -> {

            int gayIndex = 7;
            int randomTime = rnd.nextInt(13, 20);
            for (int i = 0; i < randomTime; i++) {
                int randomIcon = rnd.nextInt(icons.length);
                if (gayIndex != randomIcon) {
                    synchronized (lock) {
                        icon.setImage(icons[randomIcon]);

                    }
                } else if (randomIcon != icons.length - 1) {
                    synchronized (lock) {
                        icon.setImage(icons[randomIcon + 1]);

                    }
                } else if (randomIcon != 0) {
                    synchronized (lock) {
                        icon.setImage(icons[randomIcon - 1]);

                    }
                }
                gayIndex = randomIcon;
                try {
                    Thread.sleep(700 - i * 34);
                } catch (InterruptedException e) {
                    System.out.println("not sleep");
                }
            }
            synchronized (lock) {
                counter--;
                if (counter == 0) {
                    spinning = false;
                    comboBoxClassick.setDisable(false);
                    spinButton.setDisable(false);
                    checkWinInClassick();
                }
            }
        });
    }
    private void setMoney(){
        Platform.runLater(() ->  TextMoney.setText(String.valueOf(money)));
    }
}
