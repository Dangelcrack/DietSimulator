package com.github.dangelcrack.model.connection;

import com.github.dangelcrack.utils.XMLManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionMariaDB {
    private final static String FILE = "connectionh2.xml";
    private static ConnectionMariaDB _instance;
    private static Connection conn;

    /**
     * Constructor privado que inicializa la conexión utilizando las propiedades de conexión leídas desde el archivo XML.
     * También valida la base de datos ejecutando los scripts SQL necesarios.
     */
    private ConnectionMariaDB() {
        ConnectionProperties properties = (ConnectionProperties) XMLManager.readXML(new ConnectionProperties(), FILE);
        try {
            validateDB(properties);
            conn = DriverManager.getConnection(properties.getURL(), properties.getUser(), properties.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
            conn = null;
        }
    }

    private void validateDB(ConnectionProperties properties) {
        try (Connection validationConn = DriverManager.getConnection(properties.getURL(), properties.getUser(), properties.getPassword());
             Statement stmt = validationConn.createStatement()) {

            String sql = "";
            if ("h2".equalsIgnoreCase(properties.getDb())) {
                sql = readFile("src/main/resources/com/github/dangelcrack/DBDietH2.sql");
            } else {
                sql = readFile("src/main/resources/com/github/dangelcrack/DBDiet.sql");
            }

            String[] sqlStatements = sql.split(";");
            for (String sqlStatement : sqlStatements) {
                if (!sqlStatement.trim().isEmpty()) {
                    stmt.execute(sqlStatement);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static Connection getConnection() {
        if (_instance == null) {
            _instance = new ConnectionMariaDB();
        }
        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
