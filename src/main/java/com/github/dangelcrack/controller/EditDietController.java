package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.DietaDAO;
import com.github.dangelcrack.model.entity.Dieta;
import com.github.dangelcrack.model.entity.TypeDiet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.event.Event;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The EditDietController class handles the UI logic for editing an existing diet in the application.
 */
public class EditDietController extends Controller implements Initializable {

    @FXML
    private VBox vbox;

    @FXML
    private ComboBox<Dieta> dietaComboBox;

    @FXML
    private TextField name;

    @FXML
    private TextArea description;

    @FXML
    private ComboBox<TypeDiet> typeDiet;

    private DietaController controller;

    /**
     * This method is called when the controller is opened. It sets the controller reference
     * and prepares the environment for editing.
     *
     * @param input The input object, which should be a reference to the parent controller.
     */
    @Override
    public void onOpen(Object input) {
        this.controller = (DietaController) input;
    }

    /**
     * This method is called when the controller is closed.
     * Currently, no specific actions are required when closing.
     *
     * @param output The output object, which is currently unused.
     */
    @Override
    public void onClose(Object output) {
    }

    /**
     * Initializes the controller class. This method is called after the FXML file is loaded.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL imageUrl = getClass().getResource("/com/github/dangelcrack/media/ModalImageUtils/img.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                new javafx.scene.image.Image(imageUrl.toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true)
        );
        vbox.setBackground(new Background(backgroundImage));
        List<Dieta> dietas = DietaDAO.build().findAll();
        ObservableList<Dieta> observableNames = FXCollections.observableArrayList(dietas);
        dietaComboBox.setItems(observableNames);
        dietaComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Dieta item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });
        dietaComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Dieta item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });
        dietaComboBox.setOnAction(event -> {
            Dieta selectedDieta = dietaComboBox.getValue();
            if (selectedDieta != null) {
                initializeWithDieta(selectedDieta);
            }
        });
        typeDiet.setItems(FXCollections.observableArrayList(TypeDiet.values()));
    }

    /**
     * Initializes the UI components with the attributes of the provided Dieta object.
     *
     * @param dieta The Dieta object whose attributes will populate the UI components.
     */
    private void initializeWithDieta(Dieta dieta) {
        name.setText(dieta.getName());
        description.setText(dieta.getDescription());
        typeDiet.setValue(dieta.getTypeDiet());
        name.textProperty().addListener((observable, oldValue, newValue) -> {
        });
        description.textProperty().addListener((observable, oldValue, newValue) -> {
        });
        typeDiet.valueProperty().addListener((observable, oldValue, newValue) -> {
        });
    }

    /**
     * Handles the closing of the window. It collects data from UI components,
     * creates a new Dieta object with the updated values, and saves it through the parent controller.
     *
     * @param event The event triggering the method call.
     */
    @FXML
    private void closeWindow(Event event) {
        String nameValue = name.getText();
        String descriptionValue = description.getText();
        TypeDiet typeDietValue = typeDiet.getValue();

        if (nameValue.isBlank() || descriptionValue.isBlank() || typeDietValue == null) {
            return;
        }

        Dieta editedDieta = new Dieta(
                dietaComboBox.getValue().getId(),
                nameValue,
                descriptionValue,
                typeDietValue,
                null
        );
        this.controller.deleteOldDieta(dietaComboBox.getValue());
        this.controller.saveDieta(editedDieta);
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
