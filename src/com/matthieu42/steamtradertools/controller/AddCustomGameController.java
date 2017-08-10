package com.matthieu42.steamtradertools.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.matthieu42.steamtradertools.model.I18n;
import com.matthieu42.steamtradertools.model.UserAppList;
import com.matthieu42.steamtradertools.model.steamapp.NotLinkedSteamAppWithKey;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Matthieu on 21/06/2017.
 */
public class AddCustomGameController implements Initializable
{
    @FXML
    private AnchorPane root;
    @FXML
    private JFXTextField notLinkedAppName;


    @FXML
    private JFXButton addNotLinkedGameButton;

    private final UserAppList userAppList;
    private final ControllerBinder controllerBinder;

    public AddCustomGameController(UserAppList userAppList, ControllerBinder controllerBinder)
    {
        this.userAppList = userAppList;
        this.controllerBinder = controllerBinder;
    }

    @FXML
    void addNotLinkedGame(ActionEvent event) {
        if(notLinkedAppName.getText().isEmpty()){
            return;
        }
        NotLinkedSteamAppWithKey app = new NotLinkedSteamAppWithKey(notLinkedAppName.getText());

        if(userAppList.getAppList().contains(app))
        {
            JFXSnackbar error = new JFXSnackbar(root);
            error.show(I18n.getMessage("erroralreadyaddedgame"),3000);
            return;
        }
        userAppList.addApp(app);
        controllerBinder.appController.modified = true;
        controllerBinder.appController.appList.getSelectionModel().select(app);
        controllerBinder.appController.selectedGameInfo();
        controllerBinder.appController.updateListApp();
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }
}
