package com.github.dangelcrack.controller;

import com.github.dangelcrack.App;

import java.io.IOException;

/**
 * Abstract class Controller - serves as a base for all controllers within the application.
 * It contains common properties and methods that can be used by its subclasses.
 */
public abstract class Controller {
    // Reference to the main application class
    App app;

    /**
     * Sets the reference to the main application class.
     * @param app The main application object.
     */
    public void setApp(App app){
        this.app = app;
    }

    /**
     * Abstract method onOpen - to be implemented by subclasses to define behavior upon opening.
     * @param input The input object passed to the controller when opened.
     * @throws IOException If an I/O error occurs.
     */
    public abstract void onOpen(Object input) throws IOException;

    /**
     * Abstract method onClose - to be implemented by subclasses to define behavior upon closing.
     * @param output The output object to be passed from the controller when closed.
     */
    public abstract void onClose(Object output);
}