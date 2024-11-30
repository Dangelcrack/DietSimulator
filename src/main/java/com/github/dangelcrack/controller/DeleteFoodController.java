package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.ComidaDAO;
import com.github.dangelcrack.model.entity.Comida;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The DeleteFoodController class is responsible for managing the removal of food items.
 * It allows users to select a food item from a ComboBox and delete it.
 */
public class DeleteFoodController extends Controller implements Initializable {
    @FXML
    private VBox vbox;

    @FXML
    private ComboBox<Comida> comidasComboBox;

    private FoodsController controller;

    /**
     * This method is called when the view is opened. It initializes the reference
     * to the parent FoodsController to manage the deletion process.
     *
     * @param input The FoodsController instance passed as input.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void onOpen(Object input) throws IOException {
        this.controller = (FoodsController) input;
    }

    /**
     * This method is called when the view is closed. It currently has no additional actions.
     *
     * @param output The output data to be passed, not used in this implementation.
     */
    @Override
    public void onClose(Object output) {
    }

    /**
     * Initializes the controller class. This method sets up the background image and
     * populates the ComboBox with a list of food items retrieved from the database.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL imageUrl = getClass().getResource("/com/github/dangelcrack/media/ModalImageUtils/img.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(imageUrl.toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true)
        );
        vbox.setBackground(new Background(backgroundImage));
        List<Comida> comidas = ComidaDAO.build().findAll();
        ObservableList<Comida> observableNames = FXCollections.observableArrayList(comidas);
        comidasComboBox.setItems(observableNames);
        comidasComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Comida comida) {
                return comida != null ? comida.getName() : "";
            }

            @Override
            public Comida fromString(String string) {
                return comidas.stream()
                        .filter(comida -> comida.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
        if (!observableNames.isEmpty()) {
            comidasComboBox.setValue(observableNames.get(0));
        }
    }

    /**
     * Handles the close window event. This method deletes the selected food item
     * from the database and then closes the window.
     *
     * @param event The event that triggered the method call.
     */
    @FXML
    private void closeWindow(Event event) {
        String foodName = comidasComboBox.getValue().getName();
        if (foodName != null) {
            Comida comida = ComidaDAO.build().findByName(foodName);
            if (comida != null) {
                this.controller.deleteComida(comida);
            }
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
