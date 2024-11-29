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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditPersonController extends Controller implements Initializable {
    @FXML
    protected HBox hboxPrincipal;
    @FXML
    protected TableView<Dieta> tableViewDietas;
    @FXML
    protected TableColumn<Dieta, String> columnNameDieta;
    @FXML
    protected ComboBox<Persona> personaComboBox;
    @FXML
    protected ComboBox<Dieta> dietComboBox;
    @FXML
    protected Button addDietaButton;
    @FXML
    protected Button deleteDietaButton;
    @FXML
    protected TextField personName;
    @FXML
    protected TextField personHeight;
    @FXML
    protected TextField personWeight;
    @FXML
    protected TextField personAge;

    private ObservableList<Dieta> dietaList = FXCollections.observableArrayList();
    private ObservableList<Dieta> availableDietas;
    private ObservableList<Persona> availablePersonas;
    private Persona selectedPersona;
    private PersonsController controller;

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

    @Override
    public void onClose(Object output) {
    }

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
        hboxPrincipal.setBackground(new Background(backgroundImage));
        columnNameDieta.setCellValueFactory(dieta -> new SimpleStringProperty(dieta.getValue().getName()));
        tableViewDietas.setItems(dietaList);
        addDietaButton.setOnAction(event -> addDietToPersona());
        deleteDietaButton.setOnAction(event -> removeDietFromPersona());
        restrictToNumericInput(personHeight);
        restrictToNumericInput(personWeight);
        restrictToNumericInput(personAge);
    }

    private void loadPersonaData(Persona persona) {
        if (persona != null) {
            selectedPersona = persona;
            personName.setText(persona.getName());
            personHeight.setText(String.valueOf(persona.getAltura()));
            personWeight.setText(String.valueOf(persona.getPeso()));
            personAge.setText(String.valueOf(persona.getEdad()));
            dietaList.clear();
            if (persona.getDieta() != null) {
                dietaList.add(persona.getDieta());
            }
            tableViewDietas.refresh();
        }
    }

    @FXML
    private void addDietToPersona() {
        Dieta selectedDieta = dietComboBox.getValue();
        if (selectedDieta != null && !dietaList.contains(selectedDieta)) {
            dietaList.clear();
            dietaList.add(selectedDieta);
            tableViewDietas.refresh();
        }
    }

    @FXML
    private void removeDietFromPersona() {
        Dieta selectedDieta = tableViewDietas.getSelectionModel().getSelectedItem();
        if (selectedDieta != null) {
            dietaList.clear();
            tableViewDietas.refresh();
        }
    }

    private void restrictToNumericInput(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    protected void closeWindow(Event event) {
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
        Dieta dieta = dietaList.isEmpty() ? null : dietaList.get(0);
        Persona updatedPersona = new Persona(selectedPersona.getId(), personaName, height, weight, age, dieta);
        this.controller.deleteOldPersona(selectedPersona);
        this.controller.savePersona(updatedPersona);
        this.controller.onOpen(null);
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
}
