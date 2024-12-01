package com.github.dangelcrack.model.connection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "connection")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConnectionProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private String db;
    private String server;
    private String port;
    private String database;
    private String user;
    private String password;

    public ConnectionProperties(String db, String server, String port, String database, String user, String password) {
        this.db = db;
        this.server = server;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public ConnectionProperties() {
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ConnectionProperties{" +
                "db='" + db + '\'' +
                ", server='" + server + '\'' +
                ", port='" + port + '\'' +
                ", database='" + database + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getURL() {
        if ("mariadb".equalsIgnoreCase(db) || "mysql".equalsIgnoreCase(db)) {
            return "jdbc:" + db + "://" + server + ":" + port + "/" + database;
        } else if ("h2".equalsIgnoreCase(db)) {
            if (server.startsWith("tcp://")) {
                return "jdbc:h2:" + server + "/" + database;
            } else {
                String url = "jdbc:h2:";
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    url += "file:/" + server.replace("~", System.getProperty("user.home")).replace("\\", "/")
                            + "/" + database + ";AUTO_SERVER=TRUE";
                } else {
                    url += "file:" + server.replace("~", System.getProperty("user.home")) + "/" + database + ";AUTO_SERVER=TRUE";
                }
                return url;
            }
        } else {
            throw new UnsupportedOperationException("Database type not supported: " + db);
        }
    }


}
