package com.github.dangelcrack.controller;

import com.github.dangelcrack.App;
import com.github.dangelcrack.model.dao.PersonaDAO;
import com.github.dangelcrack.model.entity.Comida;
import com.github.dangelcrack.model.entity.Dieta;
import com.github.dangelcrack.model.entity.Persona;
import com.github.dangelcrack.model.entity.Scenes;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PersonsController extends Controller implements Initializable {

    @FXML
    private VBox vbox;

    @FXML
    private TableView<Persona> tableViewPersona;

    @FXML
    private TableColumn<Persona, String> columnNamePerson;

    @FXML
    private TableColumn<Persona, String> columnNameDiet;

    @FXML
    private TableColumn<Persona, Integer> columnPeso;

    @FXML
    private TableColumn<Persona, Integer> columnAltura;

    @FXML
    private TableColumn<Persona, Integer> columnEdad;

    public ObservableList<Persona> personas;

    /**
     * Called when the controller is opened.
     * Retrieves all personas from the database, updates the observable list, and sets it to the table view.
     *
     * @param input The input object, not used in this method.
     */
    @Override
    public void onOpen(Object input) {
        List<Persona> personas = PersonaDAO.build().findAll();
        this.personas = FXCollections.observableArrayList(personas);
        tableViewPersona.setItems(this.personas);
        tableViewPersona.refresh();
    }

    /**
     * Called when the controller is closed. Currently does nothing.
     *
     * @param output The output object, not used in this method.
     */
    @Override
    public void onClose(Object output) {
        // No action needed on close
    }

    /**
     * Deletes the specified persona from the observable list.
     *
     * @param oldPerson The persona to be deleted.
     */
    public void deleteOldPersona(Persona oldPerson) {
        this.personas.remove(oldPerson);
    }

    /**
     * Saves a new persona to the database and adds it to the observable list.
     *
     * @param newPersona The persona to be saved.
     */
    public void savePersona(Persona newPersona) {
        PersonaDAO.build().save(newPersona);
        this.personas.add(newPersona);
    }

    /**
     * Deletes a persona from the database and removes it from the observable list.
     *
     * @param deletePersona The persona to be deleted.
     */
    public void deletePersona(Persona deletePersona) {
        PersonaDAO.build().delete(deletePersona);
        this.personas.remove(deletePersona);
    }

    /**
     * Finds a persona by its name.
     *
     * @param name The name of the persona to search for.
     * @return The persona object, or null if not found.
     */
    public Persona findPersonaByName(String name) {
        List<Persona> personaList = PersonaDAO.build().findAll();
        for (Persona p : personaList) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Initializes the controller when the corresponding view is loaded.
     * Sets up the table view columns and background image.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableViewPersona.refresh();
        tableViewPersona.setEditable(true);
        tableViewPersona.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Configure background image for the VBox
        URL imageUrl = getClass().getResource("/com/github/dangelcrack/media/ModalImageUtils/img.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(imageUrl.toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true)
        );
        vbox.setBackground(new Background(backgroundImage));

        // Set up column for displaying persona name
        columnNamePerson.setCellValueFactory(persona -> new SimpleStringProperty(persona.getValue().getName()));

        // Handle the event when the persona name is edited
        columnNamePerson.setOnEditCommit(event -> {
            // Only update if the new value is different and within character limit
            if (!event.getNewValue().equals(event.getOldValue())) {
                if (event.getNewValue().length() <= 20) {
                    Persona persona = event.getRowValue();
                    PersonaDAO.build().delete(persona);  // Delete the old persona
                    persona.setName(event.getNewValue());  // Update the name
                    PersonaDAO.build().save(persona);  // Save the updated persona
                } else {
                    // Show an alert if the name exceeds character limit
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("¡Te has pasado del límite de caracteres!");  // Error message
                    alert.show();
                }
            }
        });

        // Set up columns for displaying persona details (peso, altura, edad)
        columnPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        columnAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        columnEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));

        // Set up column for displaying associated diet name
        columnNameDiet.setCellValueFactory(persona -> {
            Persona p = persona.getValue();
            String dietName = PersonaDAO.build().getDietNameByPersonaId(p.getId());
            return new SimpleStringProperty(dietName != null ? dietName : "");
        });
    }

    /**
     * Handles the event when the user wants to add a persona.
     * Opens a modal window to add a new persona.
     *
     * @throws IOException If an I/O error occurs while opening the modal.
     */
    @FXML
    private void addPersona() throws IOException {
        App.currentController.openModal(Scenes.ADDPERSON, "Adding a Persona...", this, null);
    }

    /**
     * Handles the event when the user wants to delete a persona.
     * Opens a modal window to delete a persona.
     *
     * @throws IOException If an I/O error occurs while opening the modal.
     */
    @FXML
    private void deletePersona() throws IOException {
        App.currentController.openModal(Scenes.DELETEPERSON, "Deleting a Person...", this, null);
    }

    /**
     * Handles the event when the user wants to edit a persona.
     * Opens a modal window to edit an existing persona.
     *
     * @throws IOException If an I/O error occurs while opening the modal.
     */
    @FXML
    private void editPersona() throws IOException {
        App.currentController.openModal(Scenes.EDITPERSON, "Editing a Person...", this, null);
    }
}
