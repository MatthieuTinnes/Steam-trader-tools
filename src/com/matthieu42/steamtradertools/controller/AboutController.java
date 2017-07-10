package com.matthieu42.steamtradertools.controller;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Created by Matthieu on 10/07/2017.
 */
public class AboutController
{
    private final HostServices hostServices;

    public AboutController(HostServices hostServices)
    {
        this.hostServices = hostServices;
    }

    @FXML
    void openWebsite(ActionEvent event) {
        hostServices.showDocument("https://steam-trader-tools.matthieu42.fr");
    }

    @FXML
    void openDonationPage(ActionEvent event) {
        hostServices.showDocument("https://steam-trader-tools.matthieu42.fr/donation/");
    }

}
