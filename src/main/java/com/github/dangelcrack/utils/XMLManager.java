package com.github.dangelcrack.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Utility class for reading and writing objects to and from XML files using JAXB.
 */
public class XMLManager {

    /**
     * Writes an object to an XML file.
     *
     * @param <T>      the type of the object
     * @param c        the object to write to XML
     * @param filename the name of the file to write to
     * @return true if the write operation was successful, false otherwise
     */
    public static <T> boolean writeXML(T c, String filename) {
        boolean result = false;
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(c.getClass());
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            m.marshal(c, new File(filename));
            result = true;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Reads an object from an XML file.
     *
     * @param <T>      the type of the object
     * @param c        an instance of the object type to read
     * @param filename the name of the file to read from
     * @return the object read from the XML file
     */
    public static <T> T readXML(T c, String filename) {
        T result = c;
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(c.getClass());
            Unmarshaller um = context.createUnmarshaller();
            result = (T) um.unmarshal(new File(filename));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return result;
    }
}
