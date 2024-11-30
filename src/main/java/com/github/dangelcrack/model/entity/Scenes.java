package com.github.dangelcrack.model.entity;

/**
 * Enum representing various scenes in the application.
 * Each scene is associated with a specific FXML file.
 */
public enum Scenes {
    ROOT("view/layout.fxml"),
    DIETLIST("view/DietaList.fxml"),
    FOODLIST("view/FoodsList.fxml"),
    PERSONLIST("view/PersonsList.fxml"),
    ADDDIET("view/addDiet.fxml"),
    ADDFOOD("view/addFood.fxml"),
    ADDPERSON("view/addPerson.fxml"),
    DELETEDIET("view/deleteDiet.fxml"),
    DELETEFOOD("view/deleteFood.fxml"),
    DELETEPERSON("view/deletePerson.fxml"),
    EDITDIET("view/editDiet.fxml"),
    EDITFOOD("view/editFood.fxml"),
    EDITPERSON("view/editPerson.fxml");
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
