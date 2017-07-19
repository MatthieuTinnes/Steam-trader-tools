package com.matthieu42.steamtradertools.controller;
import com.github.goive.steamapi.exceptions.SteamApiException;
import com.jfoenix.controls.*;
import com.matthieu42.steamtradertools.model.*;
import com.matthieu42.steamtradertools.model.Point;
import com.matthieu42.steamtradertools.model.key.KeyCurrentUse;
import com.matthieu42.steamtradertools.model.key.KeyState;
import com.matthieu42.steamtradertools.model.key.SteamKey;
import com.matthieu42.steamtradertools.model.steamapp.AbstractSteamAppWithKey;
import com.matthieu42.steamtradertools.model.steamapp.LinkedSteamAppWithKey;
import com.matthieu42.steamtradertools.model.steamapp.NotLinkedSteamAppWithKey;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.prefs.Preferences;

public class AppController implements Initializable
{

    @FXML
    public BorderPane root;
    @FXML
    JFXListView<AbstractSteamAppWithKey> appList;
    @FXML
    private JFXButton addGameButton;
    @FXML
    private ImageView gameBanner;
    @FXML
    private AnchorPane rightPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private TableView<SteamKey> keyList;
    @FXML
    private TableColumn<SteamKey, String> key;
    @FXML
    private TableColumn<SteamKey, KeyState> state;
    @FXML
    private TableColumn<SteamKey, KeyCurrentUse> currentUse;
    @FXML
    private TableColumn<SteamKey, Boolean> used;
    @FXML
    private TableColumn<SteamKey, String> dateAdded;
    @FXML
    private Label gameNumber;
    @FXML
    private Label keyNumber;
    @FXML
    private Label achievementsLabel;
    @FXML
    private Label cardLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private ToolBar toolbar;
    @FXML
    private Label gameInfoLabel;
    @FXML
    private Label linksLabel;
    @FXML
    private Label titlePriceLabel;
    @FXML
    private Label titleCardLabel;
    @FXML
    private Label titleAchievementsLabel;
    @FXML
    private JFXTextField searchText;
    @FXML
    private ImageView searchGraphic;
    @FXML
    private JFXButton searchButton;
    @FXML
    private JFXButton filterButton;
    @FXML
    private MenuBar menubar;
    @FXML
    private ButtonBar windowsButton;
    @FXML
    private AnchorPane footer;
    @FXML
    private Hyperlink steamLink;
    @FXML
    private Hyperlink steamDBLink;
    @FXML
    private Hyperlink isThereAnyDealLink;
    @FXML
    private Label steamLabel;
    @FXML
    private Label steamDbLabel;
    @FXML
    private Label itadLabel;

    private AllAppList allAppList;
    private UserAppList userAppList;
    private ControllerBinder controllerBinder;
    private final HostServices hostServices;
    private boolean noGameSelected;
    private Preferences prefs = Preferences.userNodeForPackage(com.matthieu42.steamtradertools.model.Main.class);
    private HashMap<Integer, Image> imageCache;
    private final Point dragDelta;
    private boolean filterMode;
    private TreeSet<AbstractSteamAppWithKey> currentAppList;
    public AppController(AllAppList appList, UserAppList userApp, HostServices hostServices)
    {
        this.allAppList = appList;
        this.userAppList = userApp;
        this.noGameSelected = true;
        this.imageCache = new HashMap<>();
        this.dragDelta = new Point();
        this.hostServices = hostServices;
        filterMode = false;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        if (prefs.get(PreferencesKeys.SAVE_PATH.toString(), null) != null)
        {
            try
            {
                userAppList.loadFromXml(new File(prefs.get(PreferencesKeys.SAVE_PATH.toString(), null)));
            } catch (JAXBException | SteamApiException e)
            {
                e.printStackTrace();
            }
        }
        currentAppList = userAppList.getAppList();
        updateListApp();

        /* Define the Table View */
        key.setCellValueFactory(new PropertyValueFactory<>("key"));
        key.setCellFactory(TextFieldTableCell.forTableColumn());
        state.setCellValueFactory(new PropertyValueFactory<>("state"));
        state.setCellFactory(ComboBoxTableCell.forTableColumn(KeyState.values()));
        state.setOnEditCommit(
                (TableColumn.CellEditEvent<SteamKey,KeyState> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setState(t.getNewValue()));

        currentUse.setCellValueFactory(new PropertyValueFactory<>("currentUse"));
        currentUse.setCellFactory(ComboBoxTableCell.forTableColumn(KeyCurrentUse.values()));
        currentUse.setOnEditCommit(
                (TableColumn.CellEditEvent<SteamKey,KeyCurrentUse> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setCurrentUse(t.getNewValue()));

        used.setCellValueFactory(new PropertyValueFactory<>("used"));
        used.setCellFactory(CheckBoxTableCell.forTableColumn(used));

        dateAdded.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));

        /* Context Menu for the filter button */
        ContextMenu contextMenu = new ContextMenu();
        MenuItem showAll = new MenuItem();
        MenuItem showUsed = new MenuItem();
        showAll.setText(I18n.getMessage("allGame"));
        showUsed.setText(I18n.getMessage("gameWithUsedKey"));
        showAll.setOnAction(event ->
        {
            currentAppList = userAppList.getAppList();
            updateListApp();
            filterMode = false;
        });
        showUsed.setOnAction(event ->
        {
            currentAppList = getGamesWithUsedKey();
            updateListApp();
            filterMode = true;
        });
        contextMenu.getItems().addAll(showAll,showUsed);
        filterButton.setContextMenu(contextMenu);

        loadImageCache();

    }

    @FXML
    private void addApp(ActionEvent event) throws IOException
    {
        Stage stage = new Stage();
        ResourceBundle bundle = I18n.getResourceBundle();
        AddGameController addGameController = new AddGameController(allAppList, userAppList, controllerBinder);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/matthieu42/steamtradertools/view/addgameview.fxml"), bundle);
        loader.setController(addGameController);
        AnchorPane root = loader.load();
        Scene addGameScene = new Scene(root);
        String css = AppController.class.getResource("/com/matthieu42/steamtradertools/view/style.css").toExternalForm();
        addGameScene.getStylesheets().add(css);
        stage.setScene(addGameScene);
        stage.show();

    }

    @FXML
    void deleteApp(ActionEvent event)
    {
        if (appList.getSelectionModel().getSelectedItem() == null)
            return;
        AbstractSteamAppWithKey appSelected = appList.getSelectionModel().getSelectedItem();
        userAppList.delApp(appSelected);
        if(appSelected instanceof LinkedSteamAppWithKey)
            delImageFromCache((LinkedSteamAppWithKey) appSelected);
        updateListApp();
        appList.getSelectionModel().select(0);
        selectedGameInfo();
    }

    void updateListApp()
    {
        if(filterMode)
            currentAppList = getGamesWithUsedKey();
        appList.setItems(FXCollections.observableArrayList(currentAppList));
        gameNumber.setText(I18n.getMessage("gamenumber") + " " + userAppList.getNbTotalApp());
        keyNumber.setText(I18n.getMessage("keynumber") + " " + userAppList.getNbTotalKey());
    }

    private void updateListKey()
    {
        keyList.setItems((FXCollections.observableArrayList(appList.getSelectionModel().getSelectedItem().getSteamKeyList())));
        userAppList.updateNbTotalKey();
        keyNumber.setText(I18n.getMessage("keynumber") + " " + userAppList.getNbTotalKey());
    }

    @FXML
    void selectedGameInfo()
    {
        AbstractSteamAppWithKey appSelected = appList.getSelectionModel().getSelectedItem();
        if (appSelected == null)
            return;
        if(appSelected instanceof LinkedSteamAppWithKey)
        {
            LinkedSteamAppWithKey app = (LinkedSteamAppWithKey) appSelected;
            if (noGameSelected)
            {
                ArrayList<Node> nodeList = new ArrayList<>();
                nodeList.addAll(Arrays.asList(gameBanner, keyList, toolbar, gameInfoLabel, linksLabel, priceLabel, cardLabel, achievementsLabel, titleAchievementsLabel, titleCardLabel, titlePriceLabel, steamLabel, steamDbLabel, itadLabel));
                for (Node n : nodeList)
                {
                    n.setVisible(true);
                    n.setDisable(false);
                }
                noGameSelected = false;
            }

            if (imageCache.get(app.getId()) == null)
            {
                addImageToCache(app);
            }
            gameBanner.setImage(imageCache.get(app.getId()));
            updateListKey();
            int price = (int) app.getPrice();
            if (price != 0)
                priceLabel.setText(price + " €");
            else
                priceLabel.setText("F2P");
            if (app.hasTradingCards())
                cardLabel.setText(I18n.getMessage("yes"));
            else
                cardLabel.setText(I18n.getMessage("no"));
            if (app.hasAchievements())
                achievementsLabel.setText(I18n.getMessage("yes"));
            else
                achievementsLabel.setText(I18n.getMessage("no"));
            steamDBLink.setText(app.getSteamLink());
            steamLink.setText(app.getSteamDBLink());
            isThereAnyDealLink.setText(app.getItadLink());
        }
        else if(appSelected instanceof NotLinkedSteamAppWithKey)
        {
            NotLinkedSteamAppWithKey selectedApp = (NotLinkedSteamAppWithKey) appSelected;
            ArrayList<Node> nodeList = new ArrayList<>();
            nodeList.addAll(Arrays.asList(gameBanner, keyList, toolbar, gameInfoLabel, linksLabel, priceLabel, cardLabel, achievementsLabel, titleAchievementsLabel, titleCardLabel, titlePriceLabel, steamLabel, steamDbLabel, itadLabel));
            for (Node n : nodeList)
            {
                n.setVisible(false);
                n.setDisable(true);
            }
            noGameSelected = true;
            if(I18n.getLocale() == Locale.ENGLISH){
                gameBanner.setImage(new Image("/com/matthieu42/steamtradertools/bundles/images/notLinkedGameLogoENG.png"));
            }
            else
                gameBanner.setImage(new Image("/com/matthieu42/steamtradertools/bundles/images/notLinkedLogoFR.png"));

            keyList.setItems(FXCollections.observableArrayList(appSelected.getSteamKeyList()));
            gameBanner.setVisible(true);
            gameBanner.setDisable(false);
            toolbar.setVisible(true);
            toolbar.setDisable(false);
            keyList.setVisible(true);
            keyList.setDisable(false);
        }


        }



    public void addControllerBinder(ControllerBinder controller)
    {
        this.controllerBinder = controller;
    }

    @FXML
    private void adSteamKey(ActionEvent event)
    {
        if (appList.getSelectionModel().getSelectedItem() == null)
            return;
        AbstractSteamAppWithKey appSelected = appList.getSelectionModel().getSelectedItem();

        appSelected.addKey("");
        updateListKey();
    }

    @FXML
    private void deleteSteamKey(ActionEvent event)
    {
        if (keyList.getSelectionModel().getSelectedItem() == null || appList.getSelectionModel().getSelectedItem() == null)
            return;
        AbstractSteamAppWithKey appSelected = appList.getSelectionModel().getSelectedItem();
        appSelected.delKey(keyList.getSelectionModel().getSelectedItem());
        updateListKey();

    }

    @FXML
    private void copyKeyToClipBoard(ActionEvent event)
    {
        if (keyList.getSelectionModel().getSelectedItem() == null || appList.getSelectionModel().getSelectedItem() == null)
            return;
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(keyList.getSelectionModel().getSelectedItem().getKey());
        clipboard.setContent(content);
        JFXSnackbar info = new JFXSnackbar(root);
        info.show(I18n.getMessage("infokeycopy"), 3000);

    }

    @FXML
    void saveAppListToXml()
    {
        File file = new File(prefs.get(PreferencesKeys.SAVE_PATH.toString(), null));
        try
        {
            userAppList.saveToXml(file);
        } catch (JAXBException e)
        {
            e.printStackTrace();
            JFXSnackbar info = new JFXSnackbar(root);
            info.show(I18n.getMessage("errorsave"), 3000);
        }
        JFXSnackbar info = new JFXSnackbar(root);
        info.show(I18n.getMessage("savedtoxml"), 3000);
        saveImageCache();
    }

    @FXML
    private void loadAppListFromXml(ActionEvent event)
    {
        try
        {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(null);


            if (file != null)
            {
                prefs.put(PreferencesKeys.SAVE_PATH.toString(), file.getAbsolutePath());
                userAppList.loadFromXml(file);
                this.updateListApp();
            }

        } catch (Exception e)
        { // catches ANY exception
            e.printStackTrace();
            JFXSnackbar info = new JFXSnackbar(root);
            info.show(I18n.getMessage("errorloadsave"), 3000);
        }
    }

    @FXML
    private void saveNewAppListToXml(ActionEvent event)
    {
        JAXBContext context;
        try
        {
            context = JAXBContext.newInstance(UserAppList.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            FileChooser fc = new FileChooser();
            File file = fc.showSaveDialog(null);
            if (file != null)
            {
                marshaller.marshal(userAppList, file);
                JFXSnackbar info = new JFXSnackbar(root);
                info.show(I18n.getMessage("savedtoxml") + file.getAbsolutePath(), 3000);
                prefs.put(PreferencesKeys.SAVE_PATH.toString(), file.getAbsolutePath());
            }
        } catch (JAXBException e)
        {
            e.printStackTrace();
        }

    }

    private void saveImageCache()
    {
        File dir = new File("cache/");
        if (!dir.exists())
            if (!dir.mkdir())
            {
                JFXSnackbar error = new JFXSnackbar(root);
                error.show(I18n.getMessage("errorcachefolder"), 3000);
            }
        for (Map.Entry<Integer, Image> i : imageCache.entrySet())
        {
            BufferedImage bi = SwingFXUtils.fromFXImage(i.getValue(), null);
            File file = new File("cache/" + i.getKey());
            if (!file.exists() && bi != null)
            {
                try
                {
                    ImageIO.write(bi, "png", file);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }
    }

    private void loadImageCache()
    {
        File dir = new File("cache/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null)
        {
            for (File child : directoryListing)
            {
                if (Objects.equals(child.getName(), "Thumbs.db"))
                    child.delete();
                else
                {
                    Image ima = new Image("file:///" + child.getAbsolutePath());
                    imageCache.put(Integer.parseInt(child.getName()), ima);
                }


            }
        }
    }

    void addImageToCache(LinkedSteamAppWithKey app)
    {
        Image banner = new Image(app.getHeaderImage());
        imageCache.put(app.getId(), banner);
    }

    void delImageFromCache(LinkedSteamAppWithKey app)
    {
        int id = app.getId();
        imageCache.remove(id);
        File cached = new File("cache/" + id);
        if (cached.exists())
            if (!cached.delete())
            {
                JFXSnackbar info = new JFXSnackbar(root);
                info.show(I18n.getMessage("errordeletecache"), 3000);
            }


    }

    @FXML
    void handleSaveCache(ActionEvent event)
    {
        saveImageCache();

    }

    @FXML
    void openSettings(ActionEvent event)
    {
        Stage stage = new Stage();
        stage.setTitle(I18n.getMessage("settings"));
        ResourceBundle bundle = I18n.getResourceBundle();
        SettingsController settingsController = new SettingsController(controllerBinder);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/matthieu42/steamtradertools/view/settingsview.fxml"), bundle);
        loader.setController(settingsController);
        AnchorPane root = null;
        try
        {
            root = loader.load();
        } catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        Scene addGameScene = new Scene(root);
        String css = AppController.class.getResource("/com/matthieu42/steamtradertools/view/style.css").toExternalForm();
        addGameScene.getStylesheets().add(css);
        stage.setScene(addGameScene);
        stage.show();
    }

    @FXML
    void searchApp(KeyEvent event)
    {
        ArrayList<AbstractSteamAppWithKey> searchResult = new ArrayList<>();
        String search = searchText.getText();
        if (!search.isEmpty())
            searchGraphic.setImage(new Image("/com/matthieu42/steamtradertools/bundles/images/close.png"));
        else
            searchGraphic.setImage(new Image("/com/matthieu42/steamtradertools/bundles/images/magnify.png"));

        search = search.toLowerCase();
        for (AbstractSteamAppWithKey curVal : currentAppList)
        {
            if (curVal.getName().toLowerCase().contains(search))
            {
                searchResult.add(curVal);
            }
        }
        appList.setItems(FXCollections.observableArrayList(searchResult));

    }

    @FXML
    void handleSearchAction(ActionEvent event)
    {
        String search = searchText.getText();
        if (!search.isEmpty())
        {
            searchText.clear();
            updateListApp();
            searchGraphic.setImage(new Image("/com/matthieu42/steamtradertools/bundles/images/magnify.png"));
        } else
            searchApp(null);
    }

    @FXML
    void maximize(ActionEvent event)
    {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
        if (stage.isMaximized())
        {
            menubar.setPrefWidth(stage.getWidth());
            windowsButton.setLayoutX(stage.getWidth() - windowsButton.getWidth());
            keyList.setPrefHeight(keyList.getPrefHeight() + 190);
        }
        if (!stage.isMaximized())
        {
            windowsButton.setLayoutX(stage.getWidth() - windowsButton.getWidth());
            menubar.setPrefWidth(stage.getWidth());
            keyList.setPrefHeight(keyList.getPrefHeight() - 190);
        }
    }

    @FXML
    void minimize(ActionEvent event)
    {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.toBack();
    }

    @FXML
    void close(ActionEvent event)
    {
        JFXDialogLayout content = new JFXDialogLayout();
        Text header = new Text(I18n.getMessage("save"));
        header.setTextAlignment(TextAlignment.CENTER);
        content.setHeading(header);
        content.setBody(new Text(I18n.getMessage("savechangesdialog")));
        JFXDialog savingDialog = new JFXDialog(stackPane,content,JFXDialog.DialogTransition.CENTER);
        JFXButton yes = new JFXButton(I18n.getMessage("yes"));
        JFXButton no = new JFXButton(I18n.getMessage("no"));
        yes.setOnAction(event1 ->
        {
            saveAppListToXml();
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        });

        no.setOnAction(event12 ->
        {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        });

        content.setActions(yes,no);
        savingDialog.show();

    }

    @FXML
    void moveWindows(MouseEvent me)
    {
        if (me.getButton() != MouseButton.MIDDLE)
        {
            root.getScene().getWindow().setX(me.getScreenX() - dragDelta.getX());
            root.getScene().getWindow().setY(me.getScreenY() - dragDelta.getY());
        }
    }

    @FXML
    void moveWindowsRecorder(MouseEvent me)
    {
        if (me.getButton() != MouseButton.MIDDLE)
        {
            dragDelta.setX(me.getSceneX());
            dragDelta.setY(me.getSceneY());
        }
    }

    @FXML
    void openLink(ActionEvent event)
    {
        Hyperlink link = (Hyperlink) event.getSource();
        hostServices.showDocument(link.getText());
    }

    @FXML
    void importFromCsv(ActionEvent event)
    {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(null);
        if (file != null && file.exists())
        {
            try
            {
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                ResourceBundle bundle = I18n.getResourceBundle();
                ImportFromCSVLoadingController importFromCSVLoadingController = new ImportFromCSVLoadingController(controllerBinder);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/matthieu42/steamtradertools/view/importfromcsvloadingview.fxml"), bundle);
                loader.setController(importFromCSVLoadingController);
                AnchorPane rootPane;
                rootPane = loader.load();
                Scene loading = new Scene(rootPane);
                String css = AppController.class.getResource("/com/matthieu42/steamtradertools/view/style.css").toExternalForm();
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

    @FXML
    void exportToListNameHandling(ActionEvent event) {
        copyToClipboard(userAppList.exportAsNameString());
    }

    @FXML
    void exportToListLinkNameHandling(ActionEvent event)
    {
        copyToClipboard(userAppList.exportAsLinkNameString());
    }

    private void copyToClipboard(String text){
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
        JFXSnackbar info = new JFXSnackbar(root);
        info.show(I18n.getMessage("infolistcopy"), 3000);
    }

    @FXML
    void openFilterMenu(ActionEvent event) {
        Bounds bounds = filterButton.getBoundsInLocal();
        filterButton.getContextMenu().show(filterButton,filterButton.localToScreen(bounds).getMinX(),filterButton.localToScreen(bounds).getMinY());

    }
    @FXML
    void openAbout(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setResizable(false);
        ResourceBundle bundle = I18n.getResourceBundle();
        AboutController aboutController = new AboutController(hostServices, prefs);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/matthieu42/steamtradertools/view/aboutview.fxml"), bundle);
        loader.setController(aboutController);
        AnchorPane rootPane;
        rootPane = loader.load();
        Scene loading = new Scene(rootPane);
        String css = AppController.class.getResource("/com/matthieu42/steamtradertools/view/style.css").toExternalForm();
        loading.getStylesheets().add(css);
        stage.setScene(loading);
        stage.show();

    }

    @FXML
    void openHelpWebsite(ActionEvent event) {
        hostServices.showDocument("https://steam-trader-tools.matthieu42.fr/help/");
    }

    private TreeSet<AbstractSteamAppWithKey> getGamesWithUsedKey(){
        TreeSet<AbstractSteamAppWithKey> usedResult = new TreeSet<>();
        for (AbstractSteamAppWithKey steamApp : userAppList.getAppList())
        {
            for(SteamKey key : steamApp.getSteamKeyList()){
                if(key.isUsed()) {
                    usedResult.add(steamApp);
                    break;
                }

            }
        }
        return usedResult;
    }

}



