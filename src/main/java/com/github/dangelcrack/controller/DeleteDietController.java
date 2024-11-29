package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.DietaDAO;
import com.github.dangelcrack.model.entity.Dieta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DeleteDietController extends Controller implements Initializable {
    @FXML
    private VBox vbox;
    @FXML
    private ComboBox<Dieta> dietaComboBox;
    private DietaController controller;

    /**
     * This method is called when the view is opened with the given input.
     *
     * @param input The PokemonController instance passed as input.
     */
    @Override
    public void onOpen(Object input) {
        this.controller = (DietaController) input;
    }

    /**
     * Initializes the controller class, sets up the background image, and populates the Pokemon combo box.
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
        List<Dieta> dietas = DietaDAO.build().findAll();
        ObservableList<Dieta> observableNames = FXCollections.observableArrayList(dietas);
        dietaComboBox.setItems(observableNames);
        dietaComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Dieta item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });
        dietaComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Dieta item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });
    }


    /**
     * Handles the close window event, deletes the selected Pokemon, and hides the window.
     *
     * @param event The event that triggered the method call.
     */
    @FXML
    private void closeWindow(Event event) {
        String dietaName = dietaComboBox.getValue().getName();
        if (dietaName != null) {
            Dieta dieta = DietaDAO.build().findByName(dietaName);
            if (dieta != null) {
                this.controller.deleteDieta(dieta);
            }
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    /**
     * This method is called when the view is closed.
     *
     * @param output The output data to be passed, not used in this implementation.
     */
    @Override
    public void onClose(Object output) {
    }
}
