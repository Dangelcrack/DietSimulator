package com.github.dangelcrack.model.dao;

import com.github.dangelcrack.model.connection.ConnectionMariaDB;
import com.github.dangelcrack.model.entity.Dieta;
import com.github.dangelcrack.model.entity.Persona;
import com.github.dangelcrack.model.entity.TypeDiet;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DietaDAO implements DAO<Dieta, String> {

    // SQL Query Constants
    private static final String INSERT_DIETA =
            "INSERT INTO Diet (Name, Description, Type) VALUES (?, ?, ?)";

    private static final String UPDATE_DIETA =
            "UPDATE Diet SET Description = ?, Type = ? WHERE Name = ?";

    private static final String DELETE_DIETA =
            "DELETE FROM Diet WHERE Name = ?";

    private static final String FIND_ALL_DIETAS =
            "SELECT ID, Name, Description, Type FROM Diet";

    private static final String FIND_DIETA_BY_NAME =
            "SELECT ID, Name, Description, Type FROM Diet WHERE Name = ?";

    private static final String FIND_DIET_BY_FOOD_NAME =
            "SELECT d.ID, d.Name, d.Description, d.Type " +
                    "FROM Diet d " +
                    "JOIN DietFood df ON d.ID = df.DietID " +
                    "JOIN Food f ON df.FoodID = f.ID " +
                    "WHERE f.Name = ?;";

    private static final String FIND_DIETA_BY_ID =
            "SELECT ID, Name, Description, Type FROM Diet WHERE ID = ?";

    private static final String FIND_BY_OBJ_NAME =
            "SELECT d.ID, d.Name, d.Description, d.Type " +
                    "FROM Diet d " +
                    "INNER JOIN Person p ON d.ID = p.DietID " +
                    "WHERE p.Name = ?";

    private static Connection conn;

    /** Constructor initializing the connection to the database */
    public DietaDAO() {
        conn = ConnectionMariaDB.getConnection();
    }

    /**
     * Saves a Diet object, either by inserting a new record or updating an existing one.
     * @param d The Diet object to be saved.
     * @return The saved Diet object.
     */
    @Override
    public Dieta save(Dieta d) {
        if (d == null || d.getName() == null) return null;

        try {
            // Check if the Diet already exists
            Dieta existingDieta = findByName(d.getName());
            if (existingDieta == null) {
                // Insert a new Diet
                try (PreparedStatement pst = conn.prepareStatement(INSERT_DIETA, Statement.RETURN_GENERATED_KEYS)) {
                    pst.setString(1, d.getName());
                    pst.setString(2, d.getDescription());
                    pst.setString(3, d.getTypeDiet() != null ? d.getTypeDiet().toString() : null);
                    pst.executeUpdate();

                    // Retrieve the generated ID and set it to the Diet object
                    try (ResultSet rs = pst.getGeneratedKeys()) {
                        if (rs.next()) {
                            d.setId(rs.getInt(1));
                        }
                    }
                }
            } else {
                // Update existing Diet
                try (PreparedStatement pst = conn.prepareStatement(UPDATE_DIETA)) {
                    pst.setString(1, d.getDescription());
                    pst.setString(2, d.getTypeDiet() != null ? d.getTypeDiet().toString() : null);
                    pst.setString(3, d.getName());
                    pst.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return d;
    }

    /**
     * Deletes the given Diet object from the database.
     * @param d The Diet object to be deleted.
     * @return The deleted Diet object.
     */
    @Override
    public Dieta delete(Dieta d) {
        if (d == null || d.getName() == null) return null;

        try {
            // Execute delete query
            try (PreparedStatement pst = conn.prepareStatement(DELETE_DIETA)) {
                pst.setString(1, d.getName());
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return d;
    }

    /**
     * Finds a Diet by its name.
     * @param name The name of the Diet.
     * @return The found Diet object, or null if not found.
     */
    @Override
    public Dieta findByName(String name) {
        Dieta dieta = null;

        try (PreparedStatement pst = conn.prepareStatement(FIND_DIETA_BY_NAME)) {
            pst.setString(1, name);

            // Execute query and populate the Diet object
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    dieta = new Dieta();
                    dieta.setId(rs.getInt("ID"));
                    dieta.setName(rs.getString("Name"));
                    dieta.setDescription(rs.getString("Description"));
                    dieta.setTypeDiet(TypeDiet.valueOf(rs.getString("Type"))); // Assuming TypeDiet is an enum
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dieta;
    }

    /**
     * Finds diets based on the name of the food they contain.
     * @param foodName The name of the food.
     * @return A list of Diet objects containing the specified food.
     */
    public List<Dieta> findDietByFoodName(String foodName) {
        List<Dieta> dietList = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(FIND_DIET_BY_FOOD_NAME)) {
            pst.setString(1, foodName);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Dieta diet = new Dieta();
                    diet.setId(rs.getInt("ID"));
                    diet.setName(rs.getString("Name"));
                    diet.setDescription(rs.getString("Description"));
                    diet.setTypeDiet(TypeDiet.valueOf(rs.getString("Type")));
                    dietList.add(diet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dietList;
    }

    /**
     * Finds a Diet by its ID.
     * @param id The ID of the Diet.
     * @return The found Diet object, or null if not found.
     */
    public static Dieta findById(int id) {
        Dieta dieta = null;
        try (PreparedStatement pst = conn.prepareStatement(FIND_DIETA_BY_ID)) {
            pst.setInt(1, id);

            // Execute query and populate the Diet object
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    dieta = new Dieta();
                    dieta.setId(rs.getInt("ID"));
                    dieta.setName(rs.getString("Name"));
                    dieta.setDescription(rs.getString("Description"));
                    dieta.setTypeDiet(TypeDiet.valueOf(rs.getString("Type")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dieta;
    }

    /**
     * Finds diets assigned to a specific person.
     * @param p The Person object.
     * @return A list of Diet objects assigned to the person.
     */
    public List<Dieta> findDietByPerson(Persona p) {
        List<Dieta> dietList = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_OBJ_NAME)) {
            pst.setString(1, p.getName());
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Dieta dieta = new Dieta();
                    dieta.setId(rs.getInt("ID"));
                    dieta.setName(rs.getString("Name"));
                    dieta.setDescription(rs.getString("Description"));
                    dieta.setTypeDiet(TypeDiet.valueOf(rs.getString("Type")));
                    dietList.add(dieta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dietList;
    }

    /**
     * Retrieves all diets from the database.
     * @return A list of all Diet objects.
     */
    @Override
    public List<Dieta> findAll() {
        List<Dieta> dietas = new ArrayList<>();

        try (PreparedStatement pst = conn.prepareStatement(FIND_ALL_DIETAS)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Dieta dieta = new Dieta();
                    dieta.setId(rs.getInt("ID"));
                    dieta.setName(rs.getString("Name"));
                    dieta.setDescription(rs.getString("Description"));
                    dieta.setTypeDiet(TypeDiet.valueOf(rs.getString("Type"))); // Assuming TypeDiet is an enum
                    dietas.add(dieta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dietas;
    }

    /**
     * Closes the database connection.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds and returns a new DietaDAO instance.
     * @return A new DietaDAO object.
     */
    public static DietaDAO build() {
        return new DietaDAO();
    }
}
