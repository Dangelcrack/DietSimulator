package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.entity.Dieta;
import com.github.dangelcrack.model.entity.TypeDiet;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.event.Event;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The AddDietController class handles the UI logic for adding a diet to the application.
 */
public class AddDietController extends Controller implements Initializable {

    /** VBox for arranging UI components vertically */
    @FXML
    private VBox vbox;

    /** TextField for entering the diet name */
    @FXML
    private TextField name;

    /** TextArea for entering the diet description */
    @FXML
    private TextArea description;

    /** ComboBox for selecting the diet type */
    @FXML
    private ComboBox<TypeDiet> typeDiet;

    /** Reference to the DietaController for saving diets */
    private DietaController controller;

    /**
     * This method is called when the controller is opened. It sets the controller reference.
     * @param input Input parameter to set the controller reference
     */
    @Override
    public void onOpen(Object input) {
        this.controller = (DietaController) input;
    }

    /**
     * This method is called when the controller is closed. Currently, it has no implementation.
     * @param output Output parameter for any data returned on close
     */
    @Override
    public void onClose(Object output) {
    }

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
     * @param location The location of the FXML file
     * @param resources Resource bundle for internationalization
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL imageUrl = getClass().getResource("/com/github/dangelcrack/media/ModalImageUtils/img.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                new javafx.scene.image.Image(imageUrl.toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true)
        );
        vbox.setBackground(new Background(backgroundImage));
        typeDiet.setItems(FXCollections.observableArrayList(TypeDiet.values()));
    }

    /**
     * Handles the closing of the window. It saves the diet details and hides the window.
     * @param event Event that triggered the window close action
     */
    @FXML
    private void closeWindow(Event event) {
        String nameValue = name.getText();
        String descriptionValue = description.getText();
        TypeDiet typeDietValue = typeDiet.getValue();
        if (nameValue.isBlank() || descriptionValue.isBlank() || typeDietValue == null) {
            return;
        }
        Dieta dieta = new Dieta(-1, nameValue, descriptionValue, typeDietValue, null);
        this.controller.saveDieta(dieta);
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
