package com.bohdan.casino;

import com.almasb.fxgl.dsl.FXGL;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Controller {
    Image[] icons = {
            new Image(getClass().getResourceAsStream("/lemon.png")),
            new Image(getClass().getResourceAsStream("/cherry.png")),
            new Image(getClass().getResourceAsStream("/grape.png")),
            new Image(getClass().getResourceAsStream("/bar.png")),
            new Image(getClass().getResourceAsStream("/coin.png")),
            new Image(getClass().getResourceAsStream("/seven.png"))
    };
    private int counter;
    @FXML
    private Pane mainMenuPane, classicGamePane;
    @FXML
    private Button spinButton;
    @FXML
    private ImageView icon1, icon2, icon3, icon4;

    private ImageView[] iconsArr = {icon1, icon2, icon3, icon4,};

    @FXML
    protected void startClassicMode() {
        mainMenuPane.setVisible(false);
        classicGamePane.setVisible(true);
        iconsArr[0] = icon1;
        iconsArr[1] = icon2;
        iconsArr[2] = icon3;
        iconsArr[3] = icon4;
        icon1.setImage(icons[5]);
        icon2.setImage(icons[5]);
        icon3.setImage(icons[5]);
        icon4.setImage(icons[5]);

    }

    @FXML
    protected void startCrashMode() {
        mainMenuPane.setVisible(false);
    }

    @FXML
    protected void spin() {
        counter = iconsArr.length;
        spinButton.setDisable(true);
        Thread[] threads = new Thread[iconsArr.length];


        Object lock = new Object();

        for (int i = 0; i < threads.length; i++) {
            threads[i] = createIconThread(iconsArr[i], icons, lock);
        }
        for (Thread thread : threads) {
            thread.start();
        }

    }

    private Thread createIconThread(ImageView icon, Image[] icons, Object lock) {
        return new Thread(() -> {
            Random rnd = new Random();
            int randomTime = rnd.nextInt(15, 20);
            for (int i = 0; i < randomTime; i++) {
                int randomIcon = rnd.nextInt(6);
                icon.setImage(icons[randomIcon]);
                try {
                    Thread.sleep(1200 - i * 59);
                } catch (InterruptedException e) {
                    System.out.println("not sleep");
                }
            }
            synchronized (lock) {
                counter--;
                if (counter == 0) {
                    spinButton.setDisable(false);
                }
            }
        });
    }

}

