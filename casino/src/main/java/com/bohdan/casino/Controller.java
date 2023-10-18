package com.bohdan.casino;

import com.almasb.fxgl.dsl.FXGL;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
public class Controller {

    public Image[] takeIcon(){
        String[] fileNames = {"lemon.png", "cherry.png", "grape.png", "bar.png", "coin.png", "seven.png"};

        Image[] images = new Image[6];
        for (int i = 0; i < fileNames.length; i++) {
            images[i] = new Image(getClass().getResourceAsStream("/" + fileNames[i]));
        }
        return images;
    }
@FXML
private Pane mainMenuPane;
    @FXML
    protected void startClassicMode(){
        mainMenuPane.setVisible(false);

    }
    @FXML
    protected void startCrashMode(){
        mainMenuPane.setVisible(false);

    }
}
