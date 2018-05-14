package com.matthieu42.steamtradertools.controller;

import com.matthieu42.steamtradertools.model.I18n;
import com.matthieu42.steamtradertools.model.PreferencesKeys;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Created by Matthieu on 10/07/2017.
 */
public class AboutController extends AbstractController implements Initializable
{
    private final HostServices hostServices;
    private Preferences prefs;

    @FXML
    private Label saveLocation;

    public AboutController(HostServices hostServices, Preferences prefs)
    {
        this.hostServices = hostServices;
        this.prefs = prefs;
    }

    @FXML
    void openWebsite(ActionEvent event) {
        hostServices.showDocument("https://steam-trader-tools.matthieu42.fr");
    }

    @FXML
    void openDonationPage(ActionEvent event) {
        hostServices.showDocument("https://steam-trader-tools.matthieu42.fr/donation/");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        saveLocation.setText(prefs.get(PreferencesKeys.SAVE_PATH.toString(), I18n.getMessage("noPathSet")));
    }
}
