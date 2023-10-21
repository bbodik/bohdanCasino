package com.bohdan.casino;

import com.almasb.fxgl.dsl.FXGL;
import javafx.application.Platform;
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

import java.text.DecimalFormat;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Controller {

    private int counter, level = 4,money;
    Random rnd = new Random();
    private final Image[] icons = {
            new Image(getClass().getResourceAsStream("/lemon.png")),
            new Image(getClass().getResourceAsStream("/cherry.png")),
            new Image(getClass().getResourceAsStream("/grape.png")),
            new Image(getClass().getResourceAsStream("/bar.png")),
            new Image(getClass().getResourceAsStream("/coin.png")),
            new Image(getClass().getResourceAsStream("/seven.png"))
    };
    private Image planeImage = new Image(getClass().getResourceAsStream("/rocket.png")),
            boomImage = new Image(getClass().getResourceAsStream("/boom.png"));

    @FXML
    private Circle classicCircle;

    @FXML
    private Pane mainMenuPane, classicGamePane, crashGamePane, babloPane;
    @FXML
    private Button spinButton, upButton, stopButton, backToMainMenuButtonInClassicGame, enterMoney;
    @FXML
    private ImageView icon1, icon2, icon3, icon4, planeCrash;
    @FXML
    private ComboBox<String> comboBoxClassick;
    @FXML
    private Label classickChanse, numOfWin, TextMoney;
    private ArrayList<ImageView> iconsArr = new ArrayList<>();

    private boolean spinning = false, flying = false;
    double planeX, planeY, presentMultiply;

    @FXML
    protected void Poletily() {
        placeInStartPos();
        double pointOfDeath = generateDeathPoint();
        upButton.setDisable(true);
        stopButton.setDisable(false);
        DecimalFormat df = new DecimalFormat("#.##");
        flying = true;
        new Thread(() -> {
            for (int j = 0; j < pointOfDeath * 100; j += 1) {
                double labelNum = j / 100.0+1;
                presentMultiply = labelNum;
                Platform.runLater(() -> numOfWin.setText(df.format(labelNum)+"x"));
                if (j <= 255) {
                    Platform.runLater(() -> {
                        planeCrash.setX(planeCrash.getX() + 1);
                        if (labelNum * 100 <= 240) {
                            planeCrash.setRotate((80.0 * (1 - (planeCrash.getX() / 255.0))));
                        }
                        if (((int) (labelNum * 100)) % 3 == 0 || ((int) (labelNum * 100)) % 5 == 0) {
                            planeCrash.setY(planeCrash.getY() - 1);
                        }
                    });
                }

                try {
                    if (j < 150)
                        Thread.sleep(100);
                    else if (j < 240)
                        Thread.sleep(70);
                    else if (j < 430)
                        Thread.sleep(60);
                    else if (j < 900)
                        Thread.sleep(50);
                    else if(j<1500)
                        Thread.sleep(30);
                    else Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("not sleep");
                }

            }
            planeCrash.setImage(boomImage);
            flying = false;
            stopButton.setDisable(true);
            upButton.setDisable(false);

        }).start();
    }

    @FXML
    protected void cashOut() {
        System.out.println(presentMultiply);
        stopButton.setDisable(true);
    }

    public void placeInStartPos(){
        planeCrash.setRotate(90);
        planeCrash.setImage(planeImage);
        planeCrash.setX(planeX);
        planeCrash.setY(planeY);
    }


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
    }

    @FXML
    protected void backToMainMenuButton() {
        mainMenuPane.setVisible(true);
        crashGamePane.setVisible(false);
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
    protected void startCrashMode() {
        babloPane.setVisible(true);
        mainMenuPane.setVisible(false);
        crashGamePane.setVisible(true);
        planeCrash.setImage(planeImage);
        double planeX = planeCrash.getX(), planeY = planeCrash.getY();
        placeInStartPos();
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

    public double generateDeathPoint() {
        double multiplier = 1.0; // Початковий множник
        double currentNumber = 1.0; // Початкове число

        while (true) {
            double randomNumber = rnd.nextDouble(); // Генеруємо випадкове число від 0.0 до 1.0

            if (randomNumber > 1.0 / (currentNumber * 0.5)) {
                // Вибух
                return currentNumber;
            }

            // Збільшуємо число
            multiplier += 0.1; // Можете налаштувати збільшення множника за вашими потребами
            currentNumber *= multiplier;
        }
    }
}
