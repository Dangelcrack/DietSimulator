package com.github.dangelcrack.model.connection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * This class represents the properties required to establish a database connection.
 * It implements Serializable for object serialization and uses JAXB annotations for XML binding.
 */
@XmlRootElement(name="connection")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConnectionProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private String server;
    private String port;
    private String database;
    private String user;
    private String password;

    /**
     * Constructor with parameters to initialize all connection properties.
     * @param server The server address.
     * @param port The server port.
     * @param database The database name.
     * @param user The username for the database.
     * @param password The password for the database.
     */
    public ConnectionProperties(String server, String port, String database, String user, String password) {
        this.server = server;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    /**
     * Default no-argument constructor.
     */
    public ConnectionProperties() {
    }

    /**
     * Gets the server address.
     * @return The server address.
     */
    public String getServer() {
        return server;
    }

    /**
     * Sets the server address.
     * @param server The server address to set.
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * Gets the server port.
     * @return The server port.
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the server port.
     * @param port The server port to set.
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Gets the database name.
     * @return The database name.
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Sets the database name.
     * @param database The database name to set.
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * Gets the username for the database.
     * @return The username.
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the username for the database.
     * @param user The username to set.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Gets the password for the database.
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the database.
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns a string representation of the connection properties.
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "ConnectionProperties{" +
                "server='" + server + '\'' +
                ", port='" + port + '\'' +
                ", database='" + database + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    /**
     * Constructs the JDBC URL for the MariaDB connection.
     * @return The constructed JDBC URL.
     */
    public String getURL() {
        return "jdbc:mariadb://" + server + ":" + port;
    }
}
