package com.matthieu42.steamtradertools.model;

import com.matthieu42.steamtradertools.controller.AbstractController;
import com.matthieu42.steamtradertools.controller.AppController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ResourceBundle;

public class OpenNewWindows
{
    public void Open(String title, String fxml, AbstractController controller){
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(false);
        ResourceBundle bundle = I18n.getResourceBundle();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml), bundle);
        loader.setController(controller);
        AnchorPane rootPane;
        try
        {
            rootPane = loader.load();
            Scene loading = new Scene(rootPane);
            String css = AppController.class.getResource("/view/style.css").toExternalForm();
            loading.getStylesheets().add(css);
            stage.setScene(loading);
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
