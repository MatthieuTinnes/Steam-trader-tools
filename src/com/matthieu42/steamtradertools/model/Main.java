package com.matthieu42.steamtradertools.model;

import com.matthieu42.steamtradertools.controller.AppController;
import com.matthieu42.steamtradertools.controller.ControllerBinder;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.prefs.Preferences;

public class Main extends Application {

    @Override
    public void start(final Stage primaryStage) throws Exception
    {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.getIcons().add(new Image("/com/matthieu42/steamtradertools/bundles/images/Icon_STTBluepng.png"));
        Preferences prefs = Preferences.userNodeForPackage(com.matthieu42.steamtradertools.model.Main.class);
        String language = prefs.get(PreferencesKeys.LANGUAGE.toString(),Locale.ENGLISH.getDisplayLanguage());
        Locale locale;
        if(language.equals(Locale.FRENCH.getDisplayLanguage()))
        {
            locale = Locale.FRENCH;
        }
        else
            locale = Locale.ENGLISH;
        I18n.setLocale(locale);
        I18n.setBundle("com/matthieu42/steamtradertools/bundles/lang",locale);
        final AllAppList allAppList = new AllAppList();
        final UserAppList userAppList = new UserAppList();

        File steamAppList = new File("steamAppList.xml");
        if (!steamAppList.exists())
        {
            FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("/com/matthieu42/steamtradertools/view/loadview.fxml"),I18n.getResourceBundle());
            AnchorPane pane = splashLoader.load();
            primaryStage.setScene(new Scene(pane));
            primaryStage.show();
            Task<Void> syncApp = allAppList.init();
            syncApp.setOnSucceeded(t ->
            {
                allAppList.saveToXml();
                continueLaunch(allAppList,userAppList,primaryStage);
            });

            syncApp.setOnFailed(t ->
            {
                return;
            });
            new Thread(syncApp).start();
        } else
        {
            allAppList.loadFromXml();
            continueLaunch(allAppList,userAppList,primaryStage);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void continueLaunch(AllAppList allAppList, UserAppList userAppList, Stage primaryStage){

        AppController appController = new AppController(allAppList, userAppList,getHostServices());
        ControllerBinder controllerBinder = new ControllerBinder(appController);
        appController.addControllerBinder(controllerBinder);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/matthieu42/steamtradertools/view/mainview.fxml"), I18n.getResourceBundle());
        loader.setController(appController);
        BorderPane root;
        try
        {
            root = loader.load();
        } catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        primaryStage.setTitle("Steam Trader Tools");
        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add("/com/matthieu42/steamtradertools/view/style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }
}
