package com.github.dangelcrack;

import com.github.dangelcrack.controller.AppController;
import com.github.dangelcrack.model.entity.Scenes;
import com.github.dangelcrack.view.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    /** Static scene that can be accessed across the application. */
    public static Scene scene;
    /** Primary stage of the application. */
    public static Stage stage;
    /** Current controller in use. */
    public static AppController currentController;
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws Exception {
        View view = AppController.loadFXML(Scenes.ROOT);
        scene = new Scene(view.scene, 1105, 654);
        currentController = (AppController) view.controller;
        currentController.onOpen(null);
        stage.setScene(scene);
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/github/dangelcrack/media/ModalImageUtils/icon.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        stage.setTitle("Diet Generator");
        stage.show();
    }
}