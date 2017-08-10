package com.matthieu42.steamtradertools.controller;

import com.jfoenix.controls.JFXComboBox;
import com.matthieu42.steamtradertools.model.PreferencesKeys;
import com.matthieu42.steamtradertools.model.steamapp.AbstractSteamAppWithKey;
import com.matthieu42.steamtradertools.model.steamapp.LinkedSteamAppWithKey;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Created by Matthieu on 18/04/2017.
 */
public class SettingsController implements Initializable
{
    @FXML
    private JFXComboBox<String> languageList;
    @FXML
    private AnchorPane root;
    private final ControllerBinder controllerBinder;
    private Preferences prefs = Preferences.userNodeForPackage(com.matthieu42.steamtradertools.model.Main.class);

    public SettingsController(ControllerBinder cb)
    {
        this.controllerBinder = cb;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        languageList.getItems().addAll(Locale.FRENCH.getDisplayLanguage(),Locale.ENGLISH.getDisplayLanguage());
        languageList.getSelectionModel().select(resources.getLocale().getDisplayLanguage());
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();

    }

    @FXML
    void save(ActionEvent event) {
        String language = languageList.getSelectionModel().getSelectedItem();

        prefs.put(PreferencesKeys.LANGUAGE.toString(),language);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    void deleteImageCache(ActionEvent event) {

    }

    @FXML
    void downloadImageCache(ActionEvent event) {
        for(AbstractSteamAppWithKey app : controllerBinder.appController.userAppList.getAppList()){
            if(app instanceof LinkedSteamAppWithKey){
                controllerBinder.appController.addImageToCache((LinkedSteamAppWithKey) app);
            }
        }
        controllerBinder.appController.saveImageCache();
        controllerBinder.appController.loadImageCache();
    }
}
