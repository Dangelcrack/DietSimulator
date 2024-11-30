package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.ComidaDAO;
import com.github.dangelcrack.model.dao.DietaDAO;
import com.github.dangelcrack.model.entity.Comida;
import com.github.dangelcrack.model.entity.Dieta;
import com.github.dangelcrack.model.entity.TypeFood;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.event.Event;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditFoodController extends Controller implements Initializable {

    @FXML
    private HBox hBox;

    @FXML
    private TableView<Comida> tableViewComidas;

    @FXML
    private TableColumn<Comida, String> columnNameComida;

    @FXML
    private TableColumn<Comida, String> typeComida;

    @FXML
    private TableColumn<Comida, Integer> caloriesComida;

    @FXML
    private TextField name;

    @FXML
    private TextField calories;

    @FXML
    private ComboBox<TypeFood> type;

    @FXML
    private Button addDiet;

    @FXML
    private Button deleteDiet;

    @FXML
    private TableView<Dieta> tableViewDieta;

    @FXML
    private TableColumn<Dieta, String> columnNameDieta;

    @FXML
    private ComboBox<Dieta> foodinDiet;

    private ObservableList<Comida> comidas;
    private ObservableList<Dieta> dietaList = FXCollections.observableArrayList();
    private FoodsController controller;

    /**
     * This method is called when the controller is opened. It initializes the food and diet data
     * to be displayed and used in the UI.
     *
     * @param input The input object, which should be a reference to the parent controller.
     */
    @Override
    public void onOpen(Object input) {
        this.controller = (FoodsController) input;
        List<Comida> comidas = ComidaDAO.build().findAll();
        this.comidas = FXCollections.observableArrayList(comidas);
        tableViewComidas.setItems(this.comidas);
        List<Dieta> dietas = DietaDAO.build().findAll();
        dietaList.setAll(dietas);
        foodinDiet.setItems(FXCollections.observableArrayList(dietas));
        tableViewDieta.setItems(FXCollections.observableArrayList());
    }

    /**
     * This method is called when the controller is closed. Currently, there is no specific action taken on close.
     *
     * @param output The output object, which is currently unused.
     */
    @Override
    public void onClose(Object output) {}

    /**
     * Initializes the controller class after the FXML file is loaded. This method sets up the UI components,
     * including the background image, ComboBoxes, and TableView cell factories.
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
        hBox.setBackground(new Background(backgroundImage));
        type.setItems(FXCollections.observableArrayList(TypeFood.values()));
        calories.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                calories.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        foodinDiet.setConverter(new StringConverter<>() {
            @Override
            public String toString(Dieta dieta) {
                return dieta != null ? dieta.getName() : "";
            }

            @Override
            public Dieta fromString(String string) {
                return foodinDiet.getItems().stream()
                        .filter(dieta -> dieta.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
        columnNameComida.setCellValueFactory(comida -> new SimpleStringProperty(comida.getValue().getName()));
        typeComida.setCellValueFactory(comida -> new SimpleStringProperty(comida.getValue().getTypeFood().toString()));
        caloriesComida.setCellValueFactory(comida -> new SimpleIntegerProperty(comida.getValue().getCalories()).asObject());
        columnNameDieta.setCellValueFactory(dieta -> new SimpleStringProperty(dieta.getValue().getName()));
        tableViewComidas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                initializeWithComida(newValue);
                updateDietTable(newValue);
            }
        });
        addDiet.setOnAction(event -> addDietToFood());
        deleteDiet.setOnAction(event -> removeDietFromFood());
    }

    /**
     * Initializes the UI components with the attributes of the selected food (Comida).
     * Populates the name, calories, and type fields with the selected food's data.
     *
     * @param comida The selected food object whose attributes will be used to populate the UI.
     */
    private void initializeWithComida(Comida comida) {
        name.setText(comida.getName());
        calories.setText(String.valueOf(comida.getCalories()));
        type.setValue(comida.getTypeFood());
    }

    /**
     * Updates the list of diets associated with the selected food item.
     * Displays the associated diets in the diet TableView.
     *
     * @param comida The selected food object whose associated diets are fetched and displayed.
     */
    private void updateDietTable(Comida comida) {
        List<Dieta> associatedDietas = DietaDAO.build().findDietByFoodName(comida.getName());
        dietaList.setAll(associatedDietas);
        tableViewDieta.setItems(FXCollections.observableArrayList(associatedDietas));
    }

    /**
     * Adds the selected diet to the list of diets for the currently selected food.
     * Updates the diet TableView to reflect the change.
     */
    private void addDietToFood() {
        Dieta selectedDiet = foodinDiet.getValue();
        if (selectedDiet != null && !dietaList.contains(selectedDiet)) {
            dietaList.add(selectedDiet);
            tableViewDieta.setItems(FXCollections.observableArrayList(dietaList));
            tableViewDieta.refresh();
        }
    }

    /**
     * Removes the selected diet from the list of diets for the currently selected food.
     * Updates the diet TableView to reflect the change.
     */
    private void removeDietFromFood() {
        Dieta selectedDieta = foodinDiet.getValue();
        if (dietaList.contains(selectedDieta)) {
            dietaList.remove(selectedDieta);
            tableViewDieta.setItems(FXCollections.observableArrayList(dietaList));
            tableViewDieta.refresh();
        }
    }

    /**
     * Handles the closing of the window. It collects data from UI components, creates a new food object
     * with the updated values, and saves it through the parent controller.
     *
     * @param event The event triggering the method call.
     */
    @FXML
    private void closeWindow(Event event) {
        String foodName = name.getText().trim();
        String caloriesText = calories.getText().trim();
        TypeFood foodType = type.getValue();
        if (foodName.isBlank() || caloriesText.isBlank() || foodType == null) {
            return;
        }
        int caloriesValue = Integer.parseInt(caloriesText);
        Comida updatedComida = new Comida(foodName, foodType, caloriesValue, new ArrayList<>(dietaList));
        this.controller.saveComida(updatedComida);
        this.controller.deleteOldComida(tableViewComidas.getSelectionModel().getSelectedItem());
        this.controller.onOpen(null);
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
}
