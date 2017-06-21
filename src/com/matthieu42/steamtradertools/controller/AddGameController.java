package com.matthieu42.steamtradertools.controller;

/**
 * Created by Matthieu on 06/03/2017.
 */

import com.github.goive.steamapi.data.SteamApp;
import com.github.goive.steamapi.exceptions.SteamApiException;
import com.jfoenix.controls.*;
import com.matthieu42.steamtradertools.model.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class AddGameController implements Initializable
{
        @FXML
        private AnchorPane root;

        @FXML
        private JFXListView<String> listResult;

        @FXML
        private JFXTextField searchText;
        @FXML
        private JFXButton refreshListButton;
        @FXML
        private JFXButton addGameButton;
        private final AllAppList allAppList;
        private final UserAppList userAppList;
        private final ControllerBinder controllerBinder;
        private TreeSet<String> currentAppList;


    AddGameController(AllAppList appList,UserAppList userApp, ControllerBinder controller)
    {
        this.allAppList = appList;
        this.userAppList = userApp;
        this.currentAppList = new TreeSet<>();
        this.controllerBinder = controller;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        setAppNameList();
        listResult.setItems(FXCollections.observableArrayList(currentAppList));
    }

    @FXML
    public void search()
    {
        ArrayList<String> searchResult = new ArrayList<>();
        String search = searchText.getText();
        search = search.toLowerCase();
        for (String curVal : currentAppList){
            if (curVal.toLowerCase().contains(search)){
                searchResult.add(curVal);
            }
        }
        listResult.setItems(FXCollections.observableArrayList(searchResult));

    }

    @FXML
    void addGame(ActionEvent event) throws SteamApiException
    {

        if (listResult.getSelectionModel().getSelectedItem() == null)
            return;

        String stringSelectedApp = listResult.getSelectionModel().getSelectedItem();
        int id = Integer.parseInt(stringSelectedApp.substring(0,stringSelectedApp.indexOf(':')-1));

        /* Retrieving an steamApp object corresponding to selected item*/
        try{

            Task<SteamApp> selectedApp = SteamApiStatic.retrieve(id);
            addGameButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            addGameButton.setGraphic(new JFXSpinner());
            selectedApp.setOnSucceeded((WorkerStateEvent t) ->
            {
                SteamAppWithKey newApp = new SteamAppWithKey(id);
                newApp.setApp(selectedApp.getValue());

                /* Add the app to the user list */
                if(userAppList.getAppList().contains(newApp))
                {
                    JFXSnackbar error = new JFXSnackbar(root);
                    error.show(I18n.getMessage("erroralreadyaddedgame"),3000);
                    return;
                }
                userAppList.addApp(newApp);
                controllerBinder.appController.updateListApp();
                controllerBinder.appController.addImageToCache(newApp);
                addGameButton.setGraphic(null);
                addGameButton.setContentDisplay(ContentDisplay.TEXT_ONLY);
                Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            });

            selectedApp.setOnFailed(t ->
            {
                JFXSnackbar error = new JFXSnackbar(root);
                error.show(I18n.getMessage("errorappid"),3000);
                addGameButton.setGraphic(null);
                addGameButton.setContentDisplay(ContentDisplay.TEXT_ONLY);
            });
            new Thread(selectedApp).start();
        }
        catch (Exception e){
            JFXSnackbar error = new JFXSnackbar(root);
            error.show(I18n.getMessage("errorappid"),3000);
            return;
        }


    }

    @FXML
    void refreshAllAppList(ActionEvent event)
    {
        try
        {
            Task<Void> syncApp = allAppList.init();
            refreshListButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            refreshListButton.setGraphic(new JFXSpinner());
            syncApp.setOnSucceeded((WorkerStateEvent t) ->
            {
                allAppList.saveToXml();
                setAppNameList();
                listResult.setItems(FXCollections.observableArrayList(currentAppList));
                listResult.refresh();
                refreshListButton.setGraphic(null);
                refreshListButton.setContentDisplay(ContentDisplay.TEXT_ONLY);
            });

            syncApp.setOnFailed(t ->
            {
                JFXSnackbar error = new JFXSnackbar(root);
                error.show(I18n.getMessage("errorrefreshapplist"),3000);
            });
            new Thread(syncApp).start();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setAppNameList(){
        currentAppList.addAll(allAppList.getAppNameList());
    }


}
