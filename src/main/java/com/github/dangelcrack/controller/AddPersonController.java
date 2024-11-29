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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddPersonController extends Controller implements Initializable {
    @FXML
    private TextField name; // Nombre de la persona
    @FXML
    private TextField altura; // Altura de la persona
    @FXML
    private TextField peso; // Peso de la persona
    @FXML
    private TextField edad; // Edad de la persona
    @FXML
    private ComboBox<Dieta> dietawithPerson; // ComboBox para asociar dietas
    @FXML
    private VBox vbox; // Contenedor para UI
    @FXML
    private Button addDiet; // Botón para añadir dieta
    @FXML
    private Button deleteDieta; // Botón para eliminar dieta
    @FXML
    private TableView<Dieta> tableViewDieta; // Tabla para mostrar dietas
    @FXML
    private TableColumn<Dieta, String> columnNameDieta; // Columna para nombres de dietas

    private ObservableList<Dieta> dietaList = FXCollections.observableArrayList(); // Lista de dietas asociadas
    private PersonsController controller; // Controlador principal

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Establecer imagen de fondo
        URL imageUrl = getClass().getResource("/com/github/dangelcrack/media/ModalImageUtils/imgAddPerson.png");
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

        // Inicializar ComboBox con dietas desde el DAO
        dietawithPerson.setItems(FXCollections.observableArrayList(DietaDAO.build().findAll()));

        // Configurar acciones de botones
        deleteDieta.setOnAction(event -> {
            Dieta selectedDieta = dietawithPerson.getValue();
            if (selectedDieta != null) {
                dietaList.remove(selectedDieta);
                tableViewDieta.refresh();
            }
        });

        addDiet.setOnAction(event -> {
            Dieta selectedDieta = dietawithPerson.getValue();
            if (selectedDieta != null && !dietaList.contains(selectedDieta)) {
                dietaList.add(selectedDieta);
                columnNameDieta.setCellValueFactory(dietaname -> new SimpleStringProperty(dietaname.getValue().getName()));
                tableViewDieta.setItems(FXCollections.observableArrayList(dietaList));
                tableViewDieta.refresh();
            }
        });
    }

    @Override
    public void onOpen(Object input) {
        this.controller = (PersonsController) input;
        tableViewDieta.setItems(dietaList);
    }

    @Override
    public void onClose(Object output) {
        // Implementación opcional al cerrar
    }

    /**
     * Maneja el cierre de la ventana guardando la información de la persona.
     */
    @FXML
    private void closeWindow(Event event) {
        try {
            String personName = name.getText();
            int personAltura = Integer.parseInt(altura.getText());
            int personPeso = Integer.parseInt(peso.getText());
            int personEdad = Integer.parseInt(edad.getText());
            List<Dieta> associatedDietas = new ArrayList<>(dietaList);
            Persona newPerson = new Persona(0, personName, personAltura, personPeso, personEdad, null);
            if (!associatedDietas.isEmpty()) {
                newPerson.setDieta(associatedDietas.get(0));
            }
            this.controller.savePersona(newPerson);
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (NumberFormatException e) {
            showAlert("Error", "Por favor, revisa los campos numéricos.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Método auxiliar para mostrar alertas.
     */
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
