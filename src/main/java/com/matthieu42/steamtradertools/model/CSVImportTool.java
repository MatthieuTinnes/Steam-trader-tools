package com.matthieu42.steamtradertools.model;

import com.jfoenix.controls.JFXSnackbar;
import com.matthieu42.steamtradertools.controller.AppController;
import com.matthieu42.steamtradertools.controller.ImportFromCSVLoadingController;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class CSVImportTool
{

    private UserAppList userAppList;

    public CSVImportTool(UserAppList userAppList){

        this.userAppList = userAppList;
    }
    public void launchImport(File file){
        try
        {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            ResourceBundle bundle = I18n.getResourceBundle();
            ImportFromCSVLoadingController importFromCSVLoadingController = new ImportFromCSVLoadingController();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/importfromcsvloadingview.fxml"), bundle);
            loader.setController(importFromCSVLoadingController);
            AnchorPane root;
            root = loader.load();
            Scene loading = new Scene(root);
            String css = AppController.class.getResource("/view/style.css").toExternalForm();
            loading.getStylesheets().add(css);
            stage.setScene(loading);
            stage.show();

            Task<Void> importFromCSV = userAppList.importFromCSV(file);

            importFromCSVLoadingController.progressBar.progressProperty().bind(importFromCSV.progressProperty());
            importFromCSV.progressProperty().addListener((obs, oldProgress, newProgress) ->
            {
                double progress = (double) newProgress*100;
                DecimalFormat df = new DecimalFormat("#.##");
                importFromCSVLoadingController.statusLabel.setText(I18n.getMessage("percentageOfGameImported") + " " + df.format(progress) + "%");
            });

            importFromCSV.setOnSucceeded(t ->
            {
                System.out.println("done !");
                JFXSnackbar info = new JFXSnackbar(root);
                info.show(I18n.getMessage("CSVImportSuccess") + "importedData.xml", 3000);
                stage.close();
            });

            importFromCSV.setOnFailed(t ->
            {
                JFXSnackbar error = new JFXSnackbar(root);
                error.show(I18n.getMessage("errorImportingCSV"), 3000);
                return;
            });
            new Thread(importFromCSV).start();


        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
