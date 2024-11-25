package com.github.dangelcrack.test;

import com.github.dangelcrack.model.connection.ConnectionProperties;
import com.github.dangelcrack.utils.XMLManager;

/**
 * This class demonstrates loading connection properties from an XML file
 * and printing them to the console.
 * The ConnectionProperties object is populated by reading the "connection.xml" file
 * using the XMLManager utility class.
 */
public class loadConnection {
    public static void main(String[] args) {
        ConnectionProperties c = XMLManager.readXML(new ConnectionProperties(), "connection.xml");
        System.out.println(c);
    }
}
