package com.matthieu42.steamtradertools.controller;

import com.jfoenix.controls.JFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.*;

/**
 * Created by Matthieu on 21/06/2017.
 */

public class ImportFromCSVLoadingController {

    private final ControllerBinder controllerBinder;
    @FXML
    public JFXProgressBar progressBar;

    @FXML
    private Label statusLabel;

    public ImportFromCSVLoadingController(ControllerBinder controllerBinder)
    {
        this.controllerBinder = controllerBinder;
    }
}
