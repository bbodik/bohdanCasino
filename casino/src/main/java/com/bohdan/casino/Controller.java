package com.bohdan.casino;

import com.almasb.fxgl.dsl.FXGL;
import javafx.fxml.FXML;
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
    @FXML
    private Pane mainMenuPane, classicGamePane;
    @FXML
    private ImageView icon1, icon2, icon3, icon4, icon5;


    @FXML
    protected void startClassicMode() {
        mainMenuPane.setVisible(false);
        classicGamePane.setVisible(true);
        icon1.setImage(icons[5]);
        icon2.setImage(icons[5]);
        icon3.setImage(icons[5]);
        icon4.setImage(icons[5]);
        icon5.setImage(icons[5]);
    }

    @FXML
    protected void startCrashMode() {
        mainMenuPane.setVisible(false);
    }

    @FXML
    protected void spin() {
        Thread[] threads = new Thread[5];
        threads[0] = createIconThread(icon1, icons);
        threads[1] = createIconThread(icon2, icons);
        threads[2] = createIconThread(icon3, icons);
        threads[3] = createIconThread(icon4, icons);
        threads[4] = createIconThread(icon5, icons);
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
    }

    private Thread createIconThread(ImageView icon, Image[] icons) {
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
        });
    }
}

