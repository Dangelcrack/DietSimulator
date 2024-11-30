package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.DietaDAO;
import com.github.dangelcrack.model.dao.PersonaDAO;
import com.github.dangelcrack.model.entity.Dieta;
import com.github.dangelcrack.model.entity.Persona;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditPersonController extends Controller implements Initializable {

    @FXML
    private HBox hboxPrincipal;
    @FXML
    private TableView<Dieta> tableViewDietas;
    @FXML
    private TableColumn<Dieta, String> columnNameDieta;
    @FXML
    private ComboBox<Persona> personaComboBox;
    @FXML
    private ComboBox<Dieta> dietComboBox;
    @FXML
    private Button addDietaButton;
    @FXML
    private Button deleteDietaButton;
    @FXML
    private TextField personName;
    @FXML
    private TextField personHeight;
    @FXML
    private TextField personWeight;
    @FXML
    private TextField personAge;

    private ObservableList<Dieta> dietaList = FXCollections.observableArrayList();
    private ObservableList<Dieta> availableDietas;
    private ObservableList<Persona> availablePersonas;
    private Persona selectedPersona;
    private PersonsController controller;

    /**
     * This method is called when the screen is opened. It initializes the data and
     * sets up the ComboBoxes for Personas and Dietas.
     *
     * @param input The controller that opened this view, containing personas data.
     */
    @Override
    public void onOpen(Object input) {
        this.controller = (PersonsController) input;
        List<Dieta> allDietas = DietaDAO.build().findAll();
        this.availableDietas = FXCollections.observableArrayList(allDietas);
        dietComboBox.setItems(availableDietas);
        List<Persona> personas = PersonaDAO.build().findAll();
        this.availablePersonas = FXCollections.observableArrayList(personas);
        personaComboBox.setItems(availablePersonas);
        personaComboBox.setOnAction(event -> loadPersonaData(personaComboBox.getValue()));
    }

    /**
     * This method is called when the screen is closed. It can handle cleanup if necessary.
     *
     * @param output Any data to be passed when the view is closed.
     */
    @Override
    public void onClose(Object output) {
    }

    /**
     * This method is called to initialize the view. It sets up the ComboBoxes, TableView,
     * and binds the appropriate data to each control.
     *
     * @param location The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setBackgroundImage();
        dietComboBox.setItems(dietaList);
        dietComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Dieta dieta) {
                return dieta != null ? dieta.getName() : "";
            }

            @Override
            public Dieta fromString(String string) {
                return availableDietas.stream()
                        .filter(dieta -> dieta.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        columnNameDieta.setCellValueFactory(dieta ->
                new SimpleStringProperty(dieta.getValue().getName())
        );

        tableViewDietas.setItems(dietaList);

        personaComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Persona persona) {
                return persona != null ? persona.getName() : "";
            }

            @Override
            public Persona fromString(String string) {
                return availablePersonas.stream()
                        .filter(persona -> persona.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        personaComboBox.setOnAction(event -> loadPersonaData(personaComboBox.getValue()));
        addDietaButton.setOnAction(event -> {
            Dieta selectedDieta = dietComboBox.getValue();
            if (selectedDieta != null) {
                dietaList.clear();
                dietaList.add(selectedDieta);
                columnNameDieta.setCellValueFactory(dieta ->
                        new SimpleStringProperty(dieta.getValue().getName())
                );
                ObservableList<Dieta> observableDietaList = FXCollections.observableArrayList(dietaList);
                tableViewDietas.setItems(observableDietaList);
            }
            tableViewDietas.refresh();
        });

        deleteDietaButton.setOnAction(event -> {
            Dieta selectedDieta = dietComboBox.getValue();
            if (selectedDieta != null) {
                dietaList.remove(selectedDieta);
                if (selectedPersona != null && selectedPersona.getDieta() != null
                        && selectedPersona.getDieta().equals(selectedDieta)) {
                    selectedPersona.setDieta(null);
                }
                removeDietaFromPersona();
                dietaList.clear();
                ObservableList<Dieta> observableDietaList = FXCollections.observableArrayList(dietaList);
                tableViewDietas.setItems(observableDietaList);
            }
            tableViewDietas.refresh();
        });

        restrictToNumericInput(personHeight);
        restrictToNumericInput(personWeight);
        restrictToNumericInput(personAge);
    }

    /**
     * Sets the background image for the main layout.
     */
    private void setBackgroundImage() {
        URL imageUrl = getClass().getResource("/com/github/dangelcrack/media/ModalImageUtils/img.png");
        if (imageUrl != null) {
            BackgroundImage backgroundImage = new BackgroundImage(
                    new Image(imageUrl.toExternalForm()),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(100, 100, true, true, false, true)
            );
            hboxPrincipal.setBackground(new Background(backgroundImage));
        }
    }

    /**
     * Loads the selected person's data into the view.
     *
     * @param persona The selected person whose data will be loaded.
     */
    private void loadPersonaData(Persona persona) {
        if (persona != null) {
            selectedPersona = persona;
            personName.setText(persona.getName());
            personHeight.setText(String.valueOf(persona.getAltura()));
            personWeight.setText(String.valueOf(persona.getPeso()));
            String nameofDietaPerson;
            if (selectedPersona != null) {
                nameofDietaPerson = PersonaDAO.build().getDietNameByPersonaId(selectedPersona.getId());
            } else {
                nameofDietaPerson = "";
            }
            columnNameDieta.setCellValueFactory(dieta -> new SimpleStringProperty(nameofDietaPerson));
            personAge.setText(String.valueOf(persona.getEdad()));
            dietaList.clear();
            if (persona.getDieta() != null) {
                dietaList.add(persona.getDieta());
            }
            tableViewDietas.refresh();
        }
    }
    /**
     * Removes the selected diet from the selected person.
     */
    private void removeDietaFromPersona() {
        Dieta selectedDieta = tableViewDietas.getSelectionModel().getSelectedItem();
        if (selectedDieta != null) {
            selectedPersona.setDieta(null);
            dietaList.clear();
            tableViewDietas.refresh();
        }
    }

    /**
     * Restricts the input of the text field to only numeric values.
     *
     * @param textField The text field to apply the restriction to.
     */
    private void restrictToNumericInput(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Closes the window after saving the updated person data.
     *
     * @param event The event that triggers the window close action.
     */
    @FXML
    private void closeWindow(Event event) {
        String personaName = personName.getText().trim();
        String heightText = personHeight.getText().trim();
        String weightText = personWeight.getText().trim();
        String ageText = personAge.getText().trim();
        if (personaName.isBlank() || heightText.isBlank() || weightText.isBlank() || ageText.isBlank()) {
            return;
        }
        int height = Integer.parseInt(heightText);
        int weight = Integer.parseInt(weightText);
        int age = Integer.parseInt(ageText);
        Dieta dieta;
        dieta = dietaList.isEmpty() ? null : dietaList.get(0);
        Persona updatedPersona = new Persona(selectedPersona.getId(), personaName, height, weight, age, dieta);
        this.controller.deleteOldPersona(selectedPersona);
        this.controller.savePersona(updatedPersona);
        this.controller.onOpen(null);

        ((Node) event.getSource()).getScene().getWindow().hide();
    }
}
