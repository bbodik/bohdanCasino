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
public class Controller {
    @FXML
    private Pane mainMenuPane;
    @FXML
    private ImageView icon1,icon2,icon3,icon4,icon5;

    public Image[] takeIcon(){
        String[] fileNames = {"lemon.png", "cherry.png", "grape.png", "bar.png", "coin.png", "seven.png"};

        Image[] images = new Image[6];
        for (int i = 0; i < fileNames.length; i++) {
            images[i] = new Image(getClass().getResourceAsStream("/" + fileNames[i]));
        }
        return images;
    }

    @FXML
    protected void startClassicMode(){
        mainMenuPane.setVisible(false);

    }
    @FXML
    protected void startCrashMode(){
        mainMenuPane.setVisible(false);

    }
    @FXML
    protected void spin(){
        Image[] icons = takeIcon();
        icon1.setImage(icons[1]);
    }
}
