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
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.text.DecimalFormat;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Controller {

    private int counter, level = 4, money = 100, bet;
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
    private Button spinButton, upButton, stopButton, backToMainMenuButtonInClassicGame, enterMoney, getMoney;
    @FXML
    private ImageView icon1, icon2, icon3, icon4, planeCrash;
    @FXML
    private ComboBox<String> comboBoxClassick;
    @FXML
    private Label classickChanse, numOfWin, TextMoney;

    @FXML
    private TextField betSomeMoney, betSomeMoney1;
    private ArrayList<ImageView> iconsArr = new ArrayList<>();

    private boolean spinning = false, flying = false;
    double planeX, planeY, presentMultiply;

    @FXML
    protected void keyPressedBet(KeyEvent event) {
        String character = event.getCode().getName();
        if (!(character.length() == 1 && Character.isDigit(character.charAt(0)))) {
            betSomeMoney.setText("");
        }
        upButton.setDisable(false);
        spinButton.setDisable(false);
    }

    @FXML
    protected void getSomeMoney(){
        if(money<100)
            money+=10;
    }

    @FXML
    protected void Poletily() {
        bet=Integer.parseInt(betSomeMoney1.getText());
        stopButton.setDisable(false);
        if (flying||money-bet<0) {
            return;
        }

        money-=bet;
        setMoney();
        placeInStartPos();
        double pointOfDeath = generateDeathPoint();
        upButton.setDisable(true);
        stopButton.setDisable(false);
        DecimalFormat df = new DecimalFormat("#.##");
        flying = true;
        new Thread(() -> {
            for (int j = 0; j < pointOfDeath * 100; j += 1) {
                double labelNum = j / 100.0 + 1;
                presentMultiply = labelNum;
                Platform.runLater(() -> numOfWin.setText(df.format(labelNum) + "x"));
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
                    else if (j < 1500)
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
        money+=bet*presentMultiply;
        setMoney();
        stopButton.setDisable(true);
    }

    public void placeInStartPos() {
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
        spinButton.setDisable(true);
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
        stopButton.setDisable(true);
        mainMenuPane.setVisible(false);
        crashGamePane.setVisible(true);
        upButton.setDisable(true);
        planeCrash.setImage(planeImage);
        double planeX = planeCrash.getX(), planeY = planeCrash.getY();
        placeInStartPos();
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

    private void setMoney(){
        Platform.runLater(() ->  TextMoney.setText(String.valueOf(money)));
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
