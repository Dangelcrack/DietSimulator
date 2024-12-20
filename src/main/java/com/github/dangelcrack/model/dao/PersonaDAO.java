package com.github.dangelcrack.model.dao;

import com.github.dangelcrack.model.connection.ConnectionMariaDB;
import com.github.dangelcrack.model.entity.Persona;
import com.github.dangelcrack.model.entity.Dieta;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for the 'Persona' entity.
 * Provides methods to perform CRUD operations and other database interactions.
 */
public class PersonaDAO implements DAO<Persona, String> {
    private static final String FINDALL = "SELECT ID, Name, Height, Weight, Age, DietID FROM Person";
    private static final String FINDBYID = "SELECT ID, Name, Height, Weight, Age, DietID FROM Person WHERE ID = ?";
    private static final String FINDBYNAME = "SELECT ID, Name, Height, Weight, Age, DietID FROM Person WHERE Name = ?";
    private static final String INSERT = "INSERT INTO Person (Name, Height, Weight, Age, DietID) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE Person SET Name = ?, Height = ?, Weight = ?, Age = ?, DietID = ? WHERE ID = ?";
    private static final String DELETE = "DELETE FROM Person WHERE ID = ?";

    private Connection conn;

    /**
     * Constructor initializes the database connection.
     */
    public PersonaDAO() {
        conn = ConnectionMariaDB.getConnection();
    }

    /**
     * Saves the 'Persona' entity to the database.
     * If the persona already exists, it updates the existing record.
     *
     * @param persona The 'Persona' entity to save.
     * @return The saved 'Persona' entity.
     */
    @Override
    public Persona save(Persona persona) {
        if (persona == null || persona.getName() == null || persona.getName().isEmpty()) {
            return null;
        }

        try {
            Persona existingPersona = findById(persona.getId());

            if (existingPersona == null) {
                // INSERT operation
                try (PreparedStatement pst = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    pst.setString(1, persona.getName());
                    pst.setInt(2, persona.getAltura()); // Height
                    pst.setInt(3, persona.getPeso()); // Weight
                    pst.setInt(4, persona.getEdad()); // Age

                    if (persona.getDieta() != null) {
                        if (DietaDAO.build().findById(persona.getDieta().getId()) != null) {
                            pst.setInt(5, persona.getDieta().getId()); // DietID
                        } else {
                            throw new SQLException("Invalid Diet ID: " + persona.getDieta().getId());
                        }
                    } else {
                        pst.setNull(5, Types.INTEGER); // No diet
                    }

                    pst.executeUpdate();
                    try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            persona.setId(generatedKeys.getInt(1)); // Set generated ID
                        }
                    }
                }
            } else {
                // UPDATE operation
                try (PreparedStatement pst = conn.prepareStatement(UPDATE)) {
                    pst.setString(1, persona.getName());
                    pst.setInt(2, persona.getAltura());
                    pst.setInt(3, persona.getPeso());
                    pst.setInt(4, persona.getEdad());

                    if (persona.getDieta() != null) {
                        if (DietaDAO.findById(persona.getDieta().getId()) != null) {
                            pst.setInt(5, persona.getDieta().getId());
                        } else {
                            throw new SQLException("Invalid Diet ID: " + persona.getDieta().getId());
                        }
                    } else {
                        pst.setNull(5, Types.INTEGER);
                    }
                    pst.setInt(6, persona.getId());
                    pst.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return persona;
    }

    /**
     * Deletes the 'Persona' entity from the database.
     *
     * @param persona The 'Persona' entity to delete.
     * @return The deleted 'Persona' entity.
     */
    @Override
    public Persona delete(Persona persona) {
        if (persona != null) {
            try (PreparedStatement pst = conn.prepareStatement(DELETE)) {
                pst.setInt(1, persona.getId());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                persona = null; // Return null in case of failure
            }
        }
        return persona;
    }

    /**
     * Finds a 'Persona' entity by its ID.
     *
     * @param id The ID of the 'Persona' entity to find.
     * @return The found 'Persona' entity, or null if not found.
     */
    public Persona findById(Integer id) {
        Persona persona = null;
        try (PreparedStatement pst = conn.prepareStatement(FINDBYID)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    persona = new Persona();
                    persona.setId(rs.getInt("id"));
                    persona.setName(rs.getString("name"));
                    persona.setAltura(rs.getInt("height"));
                    persona.setPeso(rs.getInt("weight"));
                    persona.setEdad(rs.getInt("age"));
                    int dietaId = rs.getInt("DietID");
                    persona.setDieta(dietaId > 0 ? DietaDAO.findById(dietaId) : null); // Set diet
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persona;
    }

    /**
     * Retrieves the diet name associated with a specific persona ID.
     *
     * @param personaId The ID of the persona.
     * @return The name of the diet or null if not found.
     */
    public String getDietNameByPersonaId(int personaId) {
        String dietName = null;
        String query = "SELECT d.Name AS DietName FROM Person p LEFT JOIN Diet d ON p.DietID = d.ID WHERE p.ID = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, personaId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    dietName = rs.getString("DietName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dietName;
    }

    /**
     * Finds a 'Persona' entity by its name.
     *
     * @param name The name of the 'Persona' entity to find.
     * @return The found 'Persona' entity, or null if not found.
     */
    public Persona findByName(String name) {
        Persona persona = null;
        try (PreparedStatement pst = conn.prepareStatement(FINDBYNAME)) {
            pst.setString(1, name);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    persona = new Persona();
                    persona.setId(rs.getInt("id"));
                    persona.setName(rs.getString("name"));
                    persona.setAltura(rs.getInt("height"));
                    persona.setPeso(rs.getInt("weight"));
                    persona.setEdad(rs.getInt("age"));
                    String dietaString = rs.getString("dietid");
                    persona.setDieta(dietaString != null ? Dieta.fromString(dietaString) : null); // Set diet from string
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persona;
    }

    /**
     * Retrieves all 'Persona' entities from the database.
     *
     * @return A list of all 'Persona' entities.
     */
    @Override
    public List<Persona> findAll() {
        List<Persona> personas = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(FINDALL);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Persona persona = new Persona();
                persona.setId(rs.getInt("id"));
                persona.setName(rs.getString("name"));
                persona.setAltura(rs.getInt("height"));
                persona.setPeso(rs.getInt("weight"));
                persona.setEdad(rs.getInt("age"));
                String dietaString = rs.getString("dietid");
                persona.setDieta(dietaString != null ? Dieta.fromString(dietaString) : null); // Set diet
                personas.add(persona);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personas;
    }

    /**
     * Closes the database connection and any associated resources.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close(); // Close connection if open
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Static method to build an instance of PersonaDAO.
     *
     * @return An instance of PersonaDAO.
     */
    public static PersonaDAO build() {
        return new PersonaDAO();
    }
}
