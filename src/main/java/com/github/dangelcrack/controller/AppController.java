package com.github.dangelcrack.controller;

import com.github.dangelcrack.App;
import com.github.dangelcrack.model.entity.Scenes;
import com.github.dangelcrack.view.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The AppController class is responsible for handling the main application layout,
 * switching scenes, and opening modal dialogs.
 */
public class AppController extends Controller implements Initializable {
    @FXML
    private BorderPane borderPane;
    private Controller centerController;
    /**
     * Called when the controller is opened. It sets the initial scene.
     */
    @Override
    public void onOpen(Object input) throws IOException{
        changeScene(Scenes.DIETLIST, null);
    }

    /**
     * Changes the scene displayed in the center of the BorderPane.
     *
     * @param scene the scene to be displayed
     * @param data  the data to be passed to the controller
     * @throws IOException if the FXML file cannot be loaded
     */
    public void changeScene(Scenes scene, Object data) throws IOException {
        View view = loadFXML(scene);
        borderPane.setCenter(view.scene);
        this.centerController = view.controller;
        this.centerController.onOpen(data);
    }
    /**
     * Opens a modal dialog with the specified scene and title.
     *
     * @param scene  the scene to be displayed in the modal
     * @param title  the title of the modal window
     * @param parent the parent controller
     * @param data   the data to be passed to the controller
     * @throws IOException if the FXML file cannot be loaded
     */
    public void openModal(Scenes scene, String title, Controller parent, Object data) throws IOException {
        View view = loadFXML(scene);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.stage);
        Scene _scene = new Scene(view.scene);
        stage.setScene(_scene);
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/github/dangelcrack/media/ModalImageUtils/icon.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.controller.onOpen(parent);
        stage.showAndWait();
    }

    /**
     * Called when the controller is closed.
     */
    @Override
    public void onClose(Object output) {
        //nothing to do
    }
    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    /**
     * Loads the FXML file for the specified scene.
     *
     * @param scenes the scene to be loaded
     * @return the View object containing the scene and controller
     * @throws IOException if the FXML file cannot be loaded
     */
    public static View loadFXML(Scenes scenes) throws IOException {
        String url = scenes.getURL();
        FXMLLoader loader = new FXMLLoader(App.class.getResource(url));
        Parent p = loader.load();
        Controller c = loader.getController();
        View view = new View();
        view.scene = p;
        view.controller = c;
        return view;
    }
    /**
     * Navigates to the About scene.
     */
    @FXML
    private void goToAbout() throws IOException {
        changeScene(Scenes.ABOUT, null);
    }
    /**
     * Navigates to the Pokémon List scene.
     */
    @FXML
    private void goToDiets() throws IOException {
        changeScene(Scenes.DIETLIST,null);
    }
    /**
     * Navigates to the Moves List scene.
     */
    @FXML
    private void goToFoods() throws IOException{
        changeScene(Scenes.FOODLIST,null);
    }
    /**
     * Navigates to the Objects List scene.
     */
    @FXML
    private void goToPersons() throws IOException{
        changeScene(Scenes.PERSONLIST,null);
    }
    /**
     * Navigates to the Combats scene.
     */
    @FXML
    private void goToCalculator() throws IOException{
        changeScene(Scenes.CALCULATE,null);
    }
}