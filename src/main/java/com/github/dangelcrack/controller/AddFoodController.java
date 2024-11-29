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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The AddMoveController class is responsible for handling the UI logic for adding moves in the application.
 * It implements the Initializable interface to initialize the controller after its root element has been completely processed.
 */
public class AddFoodController extends Controller implements Initializable {
    @FXML
    private HBox hBox;
    @FXML
    private TextField name;
    @FXML
    private ComboBox<TypeFood> type;
    @FXML
    private ComboBox<Dieta> dietaHaveFood;
    @FXML
    private Button addDiet;
    @FXML
    private Button deleteDiet;
    @FXML
    private Button deleteFood;
    @FXML
    private TextField calories;
    @FXML
    private TableView<Dieta> tableView;
    @FXML
    private TableColumn<Dieta, String> columnName;
    private ObservableList<Dieta> dietaList = FXCollections.observableArrayList();
    private FoodsController controller;

    /**
     * This method is called when the controller is opened. It initializes the list of Pokemon and sets the controller reference.
     */
    @Override
    public void onOpen(Object input) {
        List<Dieta> dietas = new ArrayList<>();
        dietaList = FXCollections.observableArrayList(dietas);
        tableView.setItems(dietaList);
        this.controller = (FoodsController) input;

    }

    /**
     * This method is called when the controller is closed. Currently, it has no implementation.
     */
    @Override
    public void onClose(Object output) {

    }

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
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
            if (!newValue.matches("\\d*")) { // Permitir solo dígitos
                calories.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        dietaHaveFood.setItems(FXCollections.observableArrayList(DietaDAO.build().findAll()));
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
     * Handles the closing of the window. It saves the move details and hides the window.
     */
    @FXML
    private void closeWindow(Event event) {
            String foodName = name.getText().trim();
            String caloriesText = calories.getText().trim();
            TypeFood foodType = type.getValue();
            List<Dieta> selectedDietaList = new ArrayList<>(dietaList);
            int caloriesValue;
            caloriesValue = Integer.parseInt(caloriesText);
            Comida comida = new Comida(foodName, foodType, caloriesValue, selectedDietaList);
            this.controller.saveComida(comida);
            ((Node) event.getSource()).getScene().getWindow().hide();
    }
}