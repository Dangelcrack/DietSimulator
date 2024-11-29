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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.event.Event;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The EditFoodController class handles the UI logic for editing an existing food entry in the application.
 */
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

    @Override
    public void onOpen(Object input) {
        this.controller = (FoodsController) input;

        // Load the food items and diets
        List<Comida> comidas = ComidaDAO.build().findAll();
        this.comidas = FXCollections.observableArrayList(comidas);
        tableViewComidas.setItems(this.comidas);

        List<Dieta> dietas = DietaDAO.build().findAll();
        dietaList = FXCollections.observableArrayList(dietas);
        tableViewDieta.setItems(dietaList);
    }

    @Override
    public void onClose(Object output) {
        // Any clean up logic (if necessary)
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set background image
        URL imageUrl = getClass().getResource("/com/github/dangelcrack/media/ModalImageUtils/img.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(imageUrl.toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true)
        );
        hBox.setBackground(new Background(backgroundImage));
        foodinDiet.setItems(FXCollections.observableArrayList(DietaDAO.build().findAll()));
        type.setItems(FXCollections.observableArrayList(TypeFood.values()));
        calories.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                calories.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Configurar las columnas pero sin permitir edición
        columnNameComida.setCellValueFactory(comida -> new SimpleStringProperty(comida.getValue().getName()));
        typeComida.setCellValueFactory(comida -> new SimpleStringProperty(comida.getValue().getTypeFood().toString()));
        caloriesComida.setCellValueFactory(comida ->
                new SimpleObjectProperty<>(comida.getValue().getCalories())
        );

        columnNameDieta.setCellValueFactory(dieta -> new SimpleStringProperty(dieta.getValue().getName()));

        tableViewComidas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                initializeWithComida(newValue);
                tableViewDieta.getItems().clear();
                Comida selectedComida = newValue;
                List<Dieta> dietaList = DietaDAO.build().findDietByFoodName(selectedComida.getName());
                tableViewDieta.getItems().addAll(dietaList);
            }
        });

        addDiet.setOnAction(event -> addDietToFood());
        deleteDiet.setOnAction(event -> removeDietFromFood());
    }

    private void initializeWithComida(Comida comida) {
        name.setText(comida.getName());
        calories.setText(String.valueOf(comida.getCalories()));
        type.setValue(comida.getTypeFood());
    }

    private void addDietToFood() {
        Dieta selectedDiet = foodinDiet.getValue();
        if (selectedDiet != null && !dietaList.contains(selectedDiet)) {
            dietaList.add(selectedDiet);
            tableViewDieta.refresh();
        }
    }

    private void removeDietFromFood() {
        Dieta selectedDieta = foodinDiet.getValue();
        if (selectedDieta != null && dietaList.contains(selectedDieta)) {
            dietaList.remove(selectedDieta);
            tableViewDieta.refresh();
        }
    }
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
