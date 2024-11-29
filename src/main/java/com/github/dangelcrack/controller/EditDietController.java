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
    private ComboBox<Dieta>dietaComboBox;
    @FXML
    private TextField name; // For diet name
    @FXML
    private TextField description; // For diet description
    @FXML
    private ComboBox<TypeDiet> typeDiet; // For selecting diet type

    private DietaController controller;

    /**
     * This method is called when the controller is opened. It sets the controller reference
     * and initializes the selected diet for editing.
     */
    @Override
    public void onOpen(Object input) {
        this.controller = (DietaController) input;
    }

    /**
     * This method is called when the controller is closed. Currently, it has no implementation.
     */
    @Override
    public void onClose(Object output) {
        // No specific actions required on close for now.
    }

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configuración de la imagen de fondo
        URL imageUrl = getClass().getResource("/com/github/dangelcrack/media/ModalImageUtils/img.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                new javafx.scene.image.Image(imageUrl.toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true)
        );
        vbox.setBackground(new Background(backgroundImage));

        // Obtener la lista de Dietas desde la base de datos
        List<Dieta> dietas = DietaDAO.build().findAll();

        // Convertir la lista de Dietas en una lista observable
        ObservableList<Dieta> observableNames = FXCollections.observableArrayList(dietas);

        // Configurar el ComboBox con las dietas
        dietaComboBox.setItems(observableNames);

        // Personalizar las celdas del ComboBox para mostrar el nombre de la dieta
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

        // Configurar el evento de selección del ComboBox
        dietaComboBox.setOnAction(event -> {
            Dieta selectedDieta = dietaComboBox.getValue();
            if (selectedDieta != null) {
                initializeWithDieta(selectedDieta);
            }
        });


        // Configurar los valores del ComboBox para seleccionar el tipo de dieta
        typeDiet.setItems(FXCollections.observableArrayList(TypeDiet.values()));
    }


    /**
     * Initializes the UI components with the attributes of the provided Dieta object.
     * Sets the text fields and combo box based on the Dieta's attributes.
     *
     * @param dieta The Dieta object whose attributes will be used to initialize UI components.
     */
    private void initializeWithDieta(Dieta dieta) {
        // Establecer los valores de los campos según la dieta seleccionada
        name.setText(dieta.getName());
        description.setText(dieta.getDescription());
        typeDiet.setValue(dieta.getTypeDiet());

        // Escuchar cambios en los campos y reflejar los valores actualizados
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            // Lógica adicional en caso de ser necesaria
        });

        description.textProperty().addListener((observable, oldValue, newValue) -> {
            // Lógica adicional en caso de ser necesaria
        });

        typeDiet.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Lógica adicional en caso de ser necesaria
        });

        // Si la dieta tiene datos adicionales que necesitan inicialización, incluirlos aquí
        // Por ejemplo, si hay listas o datos relacionados, cargarlos dinámicamente
    }


    /**
     * Handles the closing of the window. It collects data from UI components, creates a new Dieta object,
     * and saves it through the controller.
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
        Dieta editedDieta = new Dieta(dietaComboBox.getValue().getId(), nameValue, descriptionValue, typeDietValue, null);
        this.controller.deleteOldDieta(dietaComboBox.getValue());
        this.controller.saveDieta(editedDieta);
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
