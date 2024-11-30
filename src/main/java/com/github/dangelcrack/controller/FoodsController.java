package com.github.dangelcrack.controller;

import com.github.dangelcrack.App;
import com.github.dangelcrack.model.dao.ComidaDAO;
import com.github.dangelcrack.model.dao.DietaDAO;
import com.github.dangelcrack.model.entity.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FoodsController extends Controller implements Initializable {

    @FXML
    private VBox vbox;

    @FXML
    private TableView<Comida> tableView;

    @FXML
    private TableColumn<Comida, String> columnName;

    @FXML
    private TableColumn<Comida, Integer> columnCalories;

    @FXML
    private TableColumn<Comida, String> columnType;

    @FXML
    private TableView<Dieta> tableView2;

    @FXML
    private TableColumn<Dieta, String> dietasNames;

    @FXML
    private TableColumn<Dieta, String> typeDiet;

    public ObservableList<Comida> comidas;

    /**
     * Called when the controller is opened. Retrieves all food items from the database,
     * updates the observable list, and refreshes the table view.
     *
     * @param input The input object, not used in this method.
     */
    @Override
    public void onOpen(Object input) {
        List<Comida> comidas = ComidaDAO.build().findAll();
        this.comidas = FXCollections.observableArrayList(comidas);
        tableView.setItems(this.comidas);
        tableView.refresh();
    }

    /**
     * Deletes the specified food item from the observable list.
     *
     * @param oldComida The food item to be deleted.
     */
    public void deleteOldComida(Comida oldComida) {
        this.comidas.remove(oldComida);
    }

    /**
     * Saves the new food item to the database and adds it to the observable list.
     *
     * @param newComida The food item to be saved.
     */
    public void saveComida(Comida newComida) {
        ComidaDAO.build().save(newComida);
        this.comidas.add(newComida);
    }

    /**
     * Deletes the specified food item from the database and removes it from the observable list.
     *
     * @param deleteComida The food item to be deleted.
     */
    public void deleteComida(Comida deleteComida) {
        ComidaDAO.build().delete(deleteComida);
        this.comidas.remove(deleteComida);
    }

    /**
     * Called when the controller is closed. This method currently does nothing.
     *
     * @param output The output object, not used in this method.
     */
    @Override
    public void onClose(Object output) {
        // No action needed on close
    }

    /**
     * Initializes the TableView and its columns with appropriate cell factories.
     * Also sets listeners for TableView selection changes.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set background image for the VBox
        URL imageUrl = getClass().getResource("/com/github/dangelcrack/media/ModalImageUtils/img.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(imageUrl.toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true)
        );
        vbox.setBackground(new Background(backgroundImage));

        // Configure column to display food name
        columnName.setCellValueFactory(comida -> new SimpleStringProperty(comida.getValue().getName()));
        columnName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnName.setOnEditCommit(event -> {
            // Only allow update if the name is different and within the character limit
            if (!event.getNewValue().equals(event.getOldValue()) && event.getNewValue().length() <= 20) {
                Comida comida = event.getRowValue();
                ComidaDAO.build().delete(comida);  // Delete the old food entry
                comida.setName(event.getNewValue());  // Update the name
                ComidaDAO.build().save(comida);  // Save the updated food entry
            } else if (event.getNewValue().length() > 20) {
                // Show an error if the name exceeds the character limit
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("¡Te has pasado del límite de caracteres!");  // Error message
                alert.show();
            }
        });

        // Set food type column
        columnType.setCellValueFactory(comida -> new SimpleStringProperty(comida.getValue().getTypeFood().toString()));

        // Set diet type column
        typeDiet.setCellValueFactory(dieta -> new SimpleStringProperty(dieta.getValue().getTypeDiet().toString()));

        // Set calories column
        columnCalories.setCellValueFactory(new PropertyValueFactory<>("calories"));

        // Set up selection listener for the food table
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Clear the second table and load diets associated with the selected food
                tableView2.getItems().clear();
                Comida selectedFood = newValue;
                List<Dieta> dietaList = DietaDAO.build().findDietByFoodName(selectedFood.getName());
                ObservableList<Dieta> dietaObservableList = FXCollections.observableArrayList(dietaList);
                tableView2.setItems(dietaObservableList);
                dietasNames.setCellValueFactory(dieta -> new SimpleObjectProperty<>(dieta.getValue().getName()));
            }
        });
    }

    /**
     * Opens the modal to add a new food item.
     *
     * @throws IOException If the modal file cannot be loaded.
     */
    @FXML
    private void addFood() throws IOException {
        App.currentController.openModal(Scenes.ADDFOOD, "Agregando una comida...", this, null);
    }

    /**
     * Opens the modal to delete an existing food item.
     *
     * @throws IOException If the modal file cannot be loaded.
     */
    @FXML
    private void deleteFood() throws IOException {
        App.currentController.openModal(Scenes.DELETEFOOD, "Borrando una comida...", this, null);
    }

    /**
     * Opens the modal to edit an existing food item.
     *
     * @throws IOException If the modal file cannot be loaded.
     */
    @FXML
    private void editFood() throws IOException {
        App.currentController.openModal(Scenes.EDITFOOD, "Editando una comida...", this, null);
    }
}
