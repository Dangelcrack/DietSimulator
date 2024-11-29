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
            "SELECT d.ID, d.Name, d.Description, d.Type\n" +
                    "        FROM Diet d\n" +
                    "        JOIN DietFood df ON d.ID = df.DietID\n" +
                    "        JOIN Food f ON df.FoodID = f.ID\n" +
                    "        WHERE f.Name = ?;";
    private static final String FIND_DIETA_BY_ID = "SELECT ID, Name, Description, Type FROM Diet WHERE ID = ?";
    private static final String FIND_BY_OBJ_NAME = "SELECT d.ID, d.Name, d.Description, d.Type " +
            "FROM Diet d " +
            "INNER JOIN Person p ON d.ID = p.DietID " +
            "WHERE p.Name = ?";
    private static Connection conn;

    public DietaDAO() {
        conn = ConnectionMariaDB.getConnection();
    }

    @Override
    public Dieta save(Dieta d) {
        if (d == null || d.getName() == null) return null;

        try {
            // Verifica si la Dieta ya existe
            Dieta existingDieta = findByName(d.getName());
            if (existingDieta == null) {
                // Inserta nueva Dieta
                try (PreparedStatement pst = conn.prepareStatement(INSERT_DIETA, Statement.RETURN_GENERATED_KEYS)) {
                    pst.setString(1, d.getName());
                    pst.setString(2, d.getDescription());
                    pst.setString(3, d.getTypeDiet() != null ? d.getTypeDiet().toString() : null);
                    pst.executeUpdate();

                    try (ResultSet rs = pst.getGeneratedKeys()) {
                        if (rs.next()) {
                            d.setId(rs.getInt(1)); // Asigna el ID generado
                        }
                    }
                }
            } else {
                // Actualiza Dieta existente
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

    @Override
    public Dieta delete(Dieta d) {
        if (d == null || d.getName() == null) return null;

        try {
            // Elimina la Dieta
            try (PreparedStatement pst = conn.prepareStatement(DELETE_DIETA)) {
                pst.setString(1, d.getName());
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return d;
    }

    @Override
    public Dieta findByName(String name) {
        Dieta dieta = null;

        try (PreparedStatement pst = conn.prepareStatement(FIND_DIETA_BY_NAME)) {
            pst.setString(1, name);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    dieta = new Dieta();
                    dieta.setId(rs.getInt("ID"));
                    dieta.setName(rs.getString("Name"));
                    dieta.setDescription(rs.getString("Description"));
                    dieta.setTypeDiet(TypeDiet.valueOf(rs.getString("Type"))); // Asume que TypeDiet es un enum
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dieta;
    }
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
    public static Dieta findById(int id) {
        Dieta dieta = null;
        try (PreparedStatement pst = conn.prepareStatement(FIND_DIETA_BY_ID)) {
            pst.setInt(1, id);

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
                    dieta.setTypeDiet(TypeDiet.valueOf(rs.getString("Type"))); // Asume que TypeDiet es un enum
                    dietas.add(dieta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dietas;
    }

    @Override
    public void close() throws IOException {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DietaDAO build() {
        return new DietaDAO();
    }
}
