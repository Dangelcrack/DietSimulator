package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.PersonaDAO;
import com.github.dangelcrack.model.entity.Persona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DeletePersonController extends Controller implements Initializable {
    @FXML
    private VBox vbox;
    @FXML
    private ComboBox<Persona> personaComboBox;
    private PersonsController controller;
    /**
     * This method is called when the view is opened.
     * @param input The ObjectsController instance passed as input.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void onOpen(Object input) throws IOException {
        this.controller = (PersonsController) input;
    }
    /**
     * This method is called when the view is closed.
     * @param output The output data to be passed, not used in this implementation.
     */
    @Override
    public void onClose(Object output) {
    }
    /**
     * Initializes the controller class, sets up the background image, and populates the objects combo box.
     * @param url The location used to resolve relative paths for the root object, or null if unknown.
     * @param resourceBundle The resources used to localize the root object, or null if not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL imageUrl = getClass().getResource("/com/github/dangelcrack/media/ModalImageUtils/img.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(imageUrl.toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true)
        );
        vbox.setBackground(new Background(backgroundImage));
        List<Persona> personas = PersonaDAO.build().findAll();
        ObservableList<Persona> observableNames = FXCollections.observableArrayList(personas);
        personaComboBox.setItems(observableNames);
    }

    /**
     * Handles the close window event, deletes the selected object, and hides the window.
     * @param event The event that triggered the method call.
     */
    @FXML
    private void closeWindow(Event event) {
        String personaName = personaComboBox.getValue().getName();
        if (personaName != null) {
            Persona persona = PersonaDAO.build().findByName(personaName);
            if (persona != null) {
                this.controller.deletePersona(persona);
            }
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}