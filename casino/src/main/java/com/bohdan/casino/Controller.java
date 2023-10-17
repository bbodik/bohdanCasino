package com.bohdan.casino;


import com.almasb.fxgl.dsl.FXGL;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
public class Controller {
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
