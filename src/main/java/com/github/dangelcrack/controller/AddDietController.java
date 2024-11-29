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

    @FXML
    private VBox vbox;
    @FXML
    private TextField name; // For diet name
    @FXML
    private TextArea description; // For diet description
    @FXML
    private ComboBox<TypeDiet> typeDiet; // For selecting diet type

    private DietaController controller;

    /**
     * This method is called when the controller is opened. It sets the controller reference.
     */
    @Override
    public void onOpen(Object input) {
        this.controller = (DietaController) input;
    }

    /**
     * This method is called when the controller is closed. Currently, it has no implementation.
     */
    @Override
    public void onClose(Object output) {
        // No specific actions required on close for now.
    }

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set background image
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
     */
    @FXML
    private void closeWindow(Event event) {
        String nameValue = name.getText();
        String descriptionValue = description.getText();
        TypeDiet typeDietValue = typeDiet.getValue();
        if (nameValue.isBlank() || descriptionValue.isBlank() || typeDietValue == null) {
            return;
        }
        Dieta dieta = new Dieta(-1, nameValue, descriptionValue, typeDietValue,null);
        this.controller.saveDieta(dieta);
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
