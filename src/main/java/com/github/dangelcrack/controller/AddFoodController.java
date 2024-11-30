package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.DietaDAO;
import com.github.dangelcrack.model.entity.Comida;
import com.github.dangelcrack.model.entity.Dieta;
import com.github.dangelcrack.model.entity.TypeFood;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The AddFoodController class is responsible for handling the UI logic for adding foods in the application.
 * It implements the Initializable interface to initialize the controller after its root element has been completely processed.
 */
public class AddFoodController extends Controller implements Initializable {
    /** HBox for arranging UI components horizontally */
    @FXML
    private HBox hBox;

    /** TextField for entering the food name */
    @FXML
    private TextField name;

    /** ComboBox for selecting the food type */
    @FXML
    private ComboBox<TypeFood> type;

    /** ComboBox for selecting the diet associated with the food */
    @FXML
    private ComboBox<Dieta> dietaHaveFood;

    /** Button for adding a diet to the table */
    @FXML
    private Button addDiet;

    /** Button for removing a diet from the table */
    @FXML
    private Button deleteDiet;

    /** TextField for entering the calorie count */
    @FXML
    private TextField calories;

    /** TableView for displaying the list of associated diets */
    @FXML
    private TableView<Dieta> tableView;

    /** TableColumn for displaying diet names */
    @FXML
    private TableColumn<Dieta, String> columnName;

    /** List of available diets fetched from the database */
    private ObservableList<Dieta> availableDietas;

    /** List of diets currently associated with the food */
    private ObservableList<Dieta> dietaList = FXCollections.observableArrayList();

    /** Reference to the FoodsController for saving foods */
    private FoodsController controller;

    /**
     * This method is called when the controller is opened. It initializes the list of diets and sets the controller reference.
     * @param input Input parameter to set the controller reference
     */
    @Override
    public void onOpen(Object input) {
        this.controller = (FoodsController) input;
        List<Dieta> allDietas = DietaDAO.build().findAll();
        this.availableDietas = FXCollections.observableArrayList(allDietas);
        dietaHaveFood.setItems(availableDietas);
        dietaHaveFood.setConverter(new StringConverter<>() {
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
        dietaList = FXCollections.observableArrayList();
        tableView.setItems(dietaList);
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
     * @param url The location of the FXML file
     * @param resourceBundle Resource bundle for internationalization
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
        hBox.setBackground(new Background(backgroundImage));
        type.setItems(FXCollections.observableArrayList(TypeFood.values()));
        calories.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                calories.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        deleteDiet.setOnAction(event -> {
            Dieta selectedDieta = dietaHaveFood.getValue();
            if (selectedDieta != null && dietaList.contains(selectedDieta)) {
                dietaList.remove(selectedDieta);
                if (tableView != null) {
                    tableView.setItems(null);
                    tableView.setItems(dietaList);
                    tableView.refresh();
                }
            }
        });
        addDiet.setOnAction(event -> {
            Dieta selectedDiet = dietaHaveFood.getValue();
            if (selectedDiet != null && (!dietaList.contains(selectedDiet))) {
                dietaList.add(selectedDiet);
                columnName.setCellValueFactory(dietaName -> new SimpleStringProperty(dietaName.getValue().getName()));
            }
            tableView.refresh();
        });
    }

    /**
     * Handles the closing of the window. It saves the food details and hides the window.
     * @param event Event that triggered the window close action
     */
    @FXML
    private void closeWindow(Event event) {
        String foodName = name.getText().trim();
        String caloriesText = calories.getText().trim();
        TypeFood foodType = type.getValue();
        List<Dieta> selectedDietaList = new ArrayList<>(dietaList);
        int caloriesValue = Integer.parseInt(caloriesText);
        Comida comida = new Comida(foodName, foodType, caloriesValue, selectedDietaList);
        this.controller.saveComida(comida);
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
}
