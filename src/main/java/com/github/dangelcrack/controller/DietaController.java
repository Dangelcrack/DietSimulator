package com.github.dangelcrack.controller;

import com.github.dangelcrack.App;
import com.github.dangelcrack.model.dao.DietaDAO;
import com.github.dangelcrack.model.entity.Dieta;
import com.github.dangelcrack.model.entity.Scenes;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DietaController extends Controller implements Initializable {
    @FXML
    private VBox vbox;
    @FXML
    private TableView<Dieta> tableView;
    @FXML
    private TableColumn<Dieta, String> columnName;
    @FXML
    private TableColumn<Dieta, String> columnDescription;
    @FXML
    private TableColumn<Dieta , String> dietType;
    public ObservableList<Dieta> dietas;
    /**
     * Called when the view is opened.
     * @param input The input provided when the view is opened.
     */
    @Override
    public void onOpen(Object input) {
        List<Dieta> dietas = DietaDAO.build().findAll();
        this.dietas = FXCollections.observableArrayList(dietas);
        tableView.setItems(this.dietas);
    }

    /**
     * Called when the view is closed.
     * @param output The output provided when the view is closed.
     */
    @Override
    public void onClose(Object output) {
    }

    /**
     * Deletes an old Dieta from the observable list.
     * @param oldDieta The Dieta to be removed from the list.
     */
    public void deleteOldDieta(Dieta oldDieta){
        this.dietas.remove(oldDieta);
    }

    /**
     * Saves a new Pokemon both to the database and the observable list.
     * @param newDieta The new Pokemon to be added.
     */
    public void saveDieta(Dieta newDieta) {
        DietaDAO.build().save(newDieta);
        this.dietas.add(newDieta);
    }

    /**
     * Deletes a Dieta from both the database and the observable list.
     * @param deleteDieta The Dieta to be deleted.
     */
    public void deleteDieta(Dieta deleteDieta) {
        DietaDAO.build().delete(deleteDieta);
        this.dietas.remove(deleteDieta);
    }
    /**
     * Initializes the controller when the corresponding view is loaded.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root dieta, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableView.refresh();
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        URL imageUrl = getClass().getResource("/com/github/dangelcrack/media/ModalImageUtils/img.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(imageUrl.toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true)
        );
        vbox.setBackground(new Background(backgroundImage));
        columnDescription.setCellValueFactory(dieta -> new SimpleStringProperty(dieta.getValue().getDescription().toString()));
        columnName.setCellValueFactory(dieta -> new SimpleStringProperty(dieta.getValue().getName().toString()));
        columnName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnName.setOnEditCommit(event ->
        {
            if (event.getNewValue() == event.getOldValue()) {
                return;
            }

            if (event.getNewValue().length() <= 20) {
                Dieta dieta = event.getRowValue();
                DietaDAO.build().delete(dieta);
                dieta.setName(event.getNewValue());
                DietaDAO.build().save(dieta);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("¡Te has pasao!!!!");
                alert.show();
            }
        });
    }
    /**
     * Handles the event when the user wants to add a Pokemon.
     *
     * @throws IOException If an I/O error occurs.
     */
    @FXML
    private void addDieta() throws IOException {
        App.currentController.openModal(Scenes.ADDDIET, "Adding a Diet...", this, null);
    }
    /**
     * Handles the event when the user wants to delete a Pokemon.
     *
     * @throws IOException If an I/O error occurs.
     */
    @FXML
    private void deleteDieta() throws IOException {
        App.currentController.openModal(Scenes.DELETEDIET, "Deleting a Diet...", this, null);
    }
    /**
     * Handles the event when the user wants to edit a Pokemon.
     *
     * @throws IOException If an I/O error occurs.
     */
    @FXML
    private void editDieta() throws IOException {
        App.currentController.openModal(Scenes.EDITDIET, "Editing a Diet...", this, null);
    }
}
