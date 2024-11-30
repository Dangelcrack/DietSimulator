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
 * The AppController class is responsible for managing the main application layout.
 * It handles scene transitions, manages the central layout region, and facilitates modal dialogs.
 */
public class AppController extends Controller implements Initializable {
    @FXML
    private BorderPane borderPane;
    private Controller centerController;

    /**
     * Called when the controller is opened. Sets the initial scene to the Diet List.
     *
     * @param input Input data passed to the controller, if any (not used here).
     * @throws IOException if the initial scene cannot be loaded.
     */
    @Override
    public void onOpen(Object input) throws IOException {
        changeScene(Scenes.DIETLIST, null);
    }

    /**
     * Changes the scene displayed in the center of the BorderPane.
     *
     * @param scene The scene to load and display.
     * @param data  Optional data to pass to the new scene's controller.
     * @throws IOException if the FXML file for the scene cannot be loaded.
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
     * @param scene  The scene to load and display in the modal dialog.
     * @param title  The title for the modal window.
     * @param parent The parent controller (context from which the modal is opened).
     * @param data   Optional data to pass to the modal scene's controller.
     * @throws IOException if the FXML file for the modal scene cannot be loaded.
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
     *
     * @param output Output data passed from the controller on close, if any (not used here).
     */
    @Override
    public void onClose(Object output) {
    }

    /**
     * Initializes the controller. This method is automatically invoked after the FXML file has been loaded.
     *
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Loads an FXML file and returns the associated View object containing the scene and controller.
     *
     * @param scenes The scene to load, specified as an enum constant.
     * @return A View object containing the loaded scene and its controller.
     * @throws IOException if the FXML file cannot be loaded.
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
     * Navigates to the Diet List scene.
     *
     * @throws IOException if the Diet List scene cannot be loaded.
     */
    @FXML
    private void goToDiets() throws IOException {
        changeScene(Scenes.DIETLIST, null);
    }

    /**
     * Navigates to the Food List scene.
     *
     * @throws IOException if the Food List scene cannot be loaded.
     */
    @FXML
    private void goToFoods() throws IOException {
        changeScene(Scenes.FOODLIST, null);
    }

    /**
     * Navigates to the Persons List scene.
     *
     * @throws IOException if the Persons List scene cannot be loaded.
     */
    @FXML
    private void goToPersons() throws IOException {
        changeScene(Scenes.PERSONLIST, null);
    }
}
