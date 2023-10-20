package com.bohdan.casino;

import com.almasb.fxgl.dsl.FXGL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Controller {

    private int counter, level = 4;
    private final Image[] icons = {
            new Image(getClass().getResourceAsStream("/lemon.png")),
            new Image(getClass().getResourceAsStream("/cherry.png")),
            new Image(getClass().getResourceAsStream("/grape.png")),
            new Image(getClass().getResourceAsStream("/bar.png")),
            new Image(getClass().getResourceAsStream("/coin.png")),
            new Image(getClass().getResourceAsStream("/seven.png"))
    };

    @FXML
    private Circle classicCircle;
    @FXML
    private Pane mainMenuPane, classicGamePane, crashGamePane;
    @FXML
    private Button spinButton, upButton, stopButton,backToMainMenuButtonInClassicGame;
    @FXML
    private ImageView icon1, icon2, icon3, icon4, planeCrash;
    @FXML
    private ComboBox<String> comboBoxClassick;
    @FXML
    private Label classickChanse;
    private ArrayList<ImageView> iconsArr = new ArrayList<>();

    private boolean spinning = false;

    @FXML
    protected void startClassicMode() {
        iconsArr.add(icon1);
        iconsArr.add(icon2);
        iconsArr.add(icon3);
        iconsArr.add(icon4);
        mainMenuPane.setVisible(false);
        classicGamePane.setVisible(true);
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
    }
    @FXML
    protected void backToMainMenuButton(){
        mainMenuPane.setVisible(true);
        crashGamePane.setVisible(false);
        classicGamePane.setVisible(false);
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
    protected void startCrashMode() {
        mainMenuPane.setVisible(false);
        crashGamePane.setVisible(true);
    }

    @FXML
    protected void spin() {

        if (spinning) {
            return;
        }
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
        for (ImageView icon : iconsArr) {
            if (!icon.getImage().equals(firstImage)) {
                classicCircle.setFill(Color.BLACK);
                return;
            } else {
                if (firstImage.equals(icons[0])) classicCircle.setFill(Color.LIGHTYELLOW);
                if (firstImage.equals(icons[1])) classicCircle.setFill(Color.INDIANRED);
                if (firstImage.equals(icons[2])) classicCircle.setFill(Color.PURPLE);
                if (firstImage.equals(icons[3])) classicCircle.setFill(Color.RED);
                if (firstImage.equals(icons[4])) classicCircle.setFill(Color.GOLD);
                if (firstImage.equals(icons[5])) classicCircle.setFill(Color.LIGHTGOLDENRODYELLOW);
            }
        }
    }

    private Thread createIconThread(ImageView icon, Image[] icons, Object lock) {
        return new Thread(() -> {
            Random rnd = new Random();
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
}
