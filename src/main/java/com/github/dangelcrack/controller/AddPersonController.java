package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.DietaDAO;
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
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The AddPersonController class is responsible for handling the UI logic to add a new person to the application.
 */
public class AddPersonController extends Controller implements Initializable {

    /** TextField for entering the person's name */
    @FXML
    private TextField name;

    /** TextField for entering the person's height */
    @FXML
    private TextField altura;

    /** TextField for entering the person's weight */
    @FXML
    private TextField peso;

    /** TextField for entering the person's age */
    @FXML
    private TextField edad;

    /** ComboBox for selecting a diet associated with the person */
    @FXML
    private ComboBox<Dieta> dietawithPerson;

    /** VBox layout for arranging UI components */
    @FXML
    private VBox vbox;

    /** Button to add a diet to the list */
    @FXML
    private Button addDiet;

    /** Button to delete a diet from the list */
    @FXML
    private Button deleteDieta;

    /** TableView for displaying the list of diets associated with the person */
    @FXML
    private TableView<Dieta> tableViewDieta;

    /** TableColumn for displaying the diet names in the TableView */
    @FXML
    private TableColumn<Dieta, String> columnNameDieta;

    /** ObservableList to hold the selected diets for the person */
    private ObservableList<Dieta> dietaList = FXCollections.observableArrayList();

    /** Reference to the PersonsController for saving the person */
    private PersonsController controller;

    /**
     * Initializes the UI components and sets up their behavior.
     * This method is automatically called after the FXML file has been loaded.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL imageUrl = getClass().getResource("/com/github/dangelcrack/media/ModalImageUtils/img.png");
        if (imageUrl != null) {
            BackgroundImage backgroundImage = new BackgroundImage(
                    new javafx.scene.image.Image(imageUrl.toExternalForm()),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(100, 100, true, true, false, true)
            );
            vbox.setBackground(new Background(backgroundImage));
        }
        dietawithPerson.setConverter(new StringConverter<>() {
            @Override
            public String toString(Dieta dieta) {
                return dieta != null ? dieta.getName() : "";
            }

            @Override
            public Dieta fromString(String string) {
                return dietawithPerson.getItems().stream()
                        .filter(dieta -> dieta.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
        dietawithPerson.setItems(FXCollections.observableArrayList(DietaDAO.build().findAll()));
        addDiet.setOnAction(event -> {
            Dieta selectedDieta = dietawithPerson.getValue();
            if (selectedDieta != null && !dietaList.contains(selectedDieta)) {
                dietaList.add(selectedDieta);
                columnNameDieta.setCellValueFactory(dietaname -> new SimpleStringProperty(dietaname.getValue().getName()));
                tableViewDieta.setItems(FXCollections.observableArrayList(dietaList));
                tableViewDieta.refresh();
            }
        });
        deleteDieta.setOnAction(event -> {
            Dieta selectedDieta = dietawithPerson.getValue();
            if (selectedDieta != null && dietaList.contains(selectedDieta)) {
                dietaList.remove(selectedDieta);
                tableViewDieta.setItems(FXCollections.observableArrayList(dietaList));
                dietawithPerson.setValue(null);
                tableViewDieta.refresh();
            }
        });
    }

    /**
     * Called when the controller is opened. Sets the controller reference and initializes the TableView.
     * @param input The input parameter used to pass the PersonsController reference
     */
    @Override
    public void onOpen(Object input) {
        this.controller = (PersonsController) input;
        tableViewDieta.setItems(dietaList);
    }

    /**
     * Called when the controller is closed. Handles any necessary cleanup or post-close actions.
     * @param output The output parameter used to pass any data on close
     */
    @Override
    public void onClose(Object output) {
    }

    /**
     * Handles the closing of the window by saving the person details.
     * @param event The event that triggered the window close action
     */
    @FXML
    private void closeWindow(Event event) {
        String personaName = name.getText().trim();
        String heightText = altura.getText().trim();
        String weightText = peso.getText().trim();
        String ageText = edad.getText().trim();
        if (personaName.isBlank() || heightText.isBlank() || weightText.isBlank() || ageText.isBlank()) {
            showAlert("Error", "Please fill out all fields.", Alert.AlertType.ERROR);
            return;
        }
        int height, weight, age;
        try {
            height = Integer.parseInt(heightText);
            weight = Integer.parseInt(weightText);
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Please ensure numeric fields are valid.", Alert.AlertType.ERROR);
            return;
        }
        Persona existingPersona = controller.findPersonaByName(personaName);
        if (existingPersona != null) {
            showAlert("Error", "A person with this name already exists.", Alert.AlertType.ERROR);
            return;
        }
        Dieta dieta = dietaList.isEmpty() ? null : dietaList.get(0); // Assign a diet if available
        Persona newPersona = new Persona(personaName, height, weight, age, dieta);
        this.controller.savePersona(newPersona);
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    /**
     * Utility method to display alerts to the user.
     * @param title The title of the alert
     * @param content The content of the alert
     * @param type The type of the alert (e.g., ERROR, INFORMATION)
     */
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
