package com.matthieu42.steamtradertools.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import com.matthieu42.steamtradertools.model.I18n;
import com.matthieu42.steamtradertools.model.ImageCache.ImageCacheError;
import com.matthieu42.steamtradertools.model.ImageCache.ImageCacheHandler;
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
public class SettingsController extends AbstractController implements Initializable
{
    @FXML
    private JFXComboBox<String> languageList;

    private final ControllerBinder controllerBinder;
    private ImageCacheHandler imageCacheHandler;
    private Preferences prefs = Preferences.userNodeForPackage(com.matthieu42.steamtradertools.model.Main.class);

    public SettingsController(ControllerBinder cb, ImageCacheHandler imageCacheHandler)
    {
        this.controllerBinder = cb;
        this.imageCacheHandler = imageCacheHandler;
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
        try
        {
            imageCacheHandler.deleteImageCache();
        } catch (ImageCacheError imageCacheError)
        {
            JFXSnackbar error = new JFXSnackbar(root);
            error.show(I18n.getMessage("errorcachefolder"), 3000);
        }
    }

    @FXML
    void downloadImageCache(ActionEvent event) {
        for(AbstractSteamAppWithKey app : controllerBinder.appController.userAppList.getAppList()){
            if(app instanceof LinkedSteamAppWithKey){
                imageCacheHandler.addImageToCache((LinkedSteamAppWithKey) app);
            }
        }
        try
        {
            imageCacheHandler.saveImageCache();
        } catch (ImageCacheError imageCacheError)
        {
            JFXSnackbar error = new JFXSnackbar(root);
            error.show(I18n.getMessage("errorcachefolder"), 3000);
        }
        imageCacheHandler.loadImageCache();
    }
}
