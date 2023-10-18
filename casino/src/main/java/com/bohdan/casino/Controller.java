package com.bohdan.casino;

import com.almasb.fxgl.dsl.FXGL;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Random;

public class Controller {

    private final Image[] icons = {
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
    private Button spinButton;
    @FXML
    private ImageView icon1, icon2, icon3, icon4;

    private ImageView[] iconsArr=new ImageView[4];

    private boolean spinning = false;

    @FXML
    protected void startClassicMode() {
        iconsArr = new ImageView[]{icon1, icon2, icon3, icon4};
        mainMenuPane.setVisible(false);
        classicGamePane.setVisible(true);
        for (ImageView icon : iconsArr) {
            icon.setImage(icons[5]);
        }
    }
    @FXML
    protected void startCrashMode() {
        mainMenuPane.setVisible(false);
    }

    @FXML
    protected void spin() {
        if (spinning) {
            return;
        }
        spinning = true;
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
                synchronized (lock) {
                    icon.setImage(icons[randomIcon]);
                }
                try {
                    Thread.sleep(1200 - i * 59);
                } catch (InterruptedException e) {
                    System.out.println("not sleep");
                }
            }
            synchronized (lock) {
                spinning = false;
                spinButton.setDisable(false);
            }
        });
    }
}
