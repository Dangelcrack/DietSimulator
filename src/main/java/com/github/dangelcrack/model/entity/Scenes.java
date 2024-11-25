package com.github.dangelcrack.model.entity;

/**
 * Enum representing various scenes in the application.
 * Each scene is associated with a specific FXML file.
 */
public enum Scenes {
    ROOT("view/layout.fxml"),
    DIETLIST("view/DietaList.fxml"),
    MOVESLIST("view/MovesList.fxml"),
    OBJECTSLIST("view/ObjectsList.fxml"),
    ABOUT("view/about.fxml"),
    ADDDIET("view/addDieta.fxml"),
    ADDMOVE("view/addMove.fxml"),
    ADDOBJECT("view/addObject.fxml"),
    DELETEDIET("view/deleteDieta.fxml"),
    DELETEMOVE("view/deleteMove.fxml"),
    DELETEOBJECT("view/deleteObject.fxml"),
    EDITDIET("view/editDieta.fxml"),
    EDITMOVE("view/editMove.fxml"),
    EDITOBJECT("view/editObject.fxml"),
    COMBATS("view/combats.fxml");
    private String url;
    /**
     * Constructor for the enum constant.
     * @param url The URL of the FXML file.
     */
    Scenes(String url){
        this.url = url;
    }
    /**
     * Retrieves the URL of the FXML file.
     * @return The URL as a String.
     */
    public String getURL(){
        return url;
    }
}
