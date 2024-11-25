package com.github.dangelcrack.model.dao;

import com.github.dangelcrack.model.connection.ConnectionMariaDB;
import com.github.dangelcrack.model.entity.Comida;
import com.github.dangelcrack.model.entity.Dieta;
import com.github.dangelcrack.model.entity.TypeFood;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComidaDAO implements DAO<Comida, String> {

    private static final String FINDALL = "SELECT c.ID, c.Name, c.Type, c.Calories FROM Food AS c";
    private static final String FINDBYNAME = "SELECT c.ID, c.Name, c.Type, c.Calories FROM Food AS c WHERE c.Name = ?";
    private static final String INSERT = "INSERT INTO Food (Name, Type, Calories) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE Food SET Type = ?, Calories = ? WHERE Name = ?";
    private static final String DELETE_FOOD = "DELETE FROM Food WHERE Name = ?";
    private static final String FINDBYDIET = "SELECT c.ID, c.Name, c.Type, c.Calories " +
            "FROM Food AS c " +
            "INNER JOIN DietFood AS df ON c.ID = df.FoodID " +
            "WHERE df.DietID = ?";
    private static final String INSERTFOODTODIET = "INSERT INTO DietFood (DietID, FoodID) VALUES (?, ?)";
    private static final String DELETE_OLD_FOOD = "DELETE FROM DietFood WHERE FoodID = ?";

    private Connection conn;

    /**
     * Constructor initializes the database connection.
     */
    public ComidaDAO() {
        conn = ConnectionMariaDB.getConnection();
    }

    @Override
    public Comida save(Comida comida) {
        if (comida == null || comida.getName() == null || comida.getName().isEmpty()) {
            return null;
        }

        try {
            String comidaName = comida.getName();
            Comida existingComida = findByName(comidaName);

            if (existingComida == null) {
                // INSERT
                try (PreparedStatement pst = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    pst.setString(1, comidaName);
                    pst.setString(2, comida.getTypeFood().toString());
                    pst.setInt(3, comida.getCalories());
                    pst.executeUpdate();

                    try (ResultSet rs = pst.getGeneratedKeys()) {
                        if (rs.next()) {
                            comida.setId(rs.getInt(1)); // Set generated ID
                        }
                    }
                }
            } else {
                // UPDATE
                try (PreparedStatement pst = conn.prepareStatement(UPDATE)) {
                    pst.setString(1, comida.getTypeFood().toString());
                    pst.setInt(2, comida.getCalories());
                    pst.setString(3, comidaName);
                    pst.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comida;
    }

    @Override
    public Comida delete(Comida comida) {
        if (comida == null || comida.getName() == null) return comida;

        try (PreparedStatement pstFood = conn.prepareStatement(DELETE_FOOD);
             PreparedStatement pstDietFood = conn.prepareStatement(DELETE_OLD_FOOD)) {

            // Delete associations in DietFood
            pstDietFood.setInt(1, comida.getId());
            pstDietFood.executeUpdate();

            // Delete the food itself
            pstFood.setString(1, comida.getName());
            pstFood.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comida;
    }

    @Override
    public Comida findByName(String name) {
        Comida result = null;

        try (PreparedStatement pst = conn.prepareStatement(FINDBYNAME)) {
            pst.setString(1, name);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    result = new Comida();
                    result.setId(rs.getInt("ID"));
                    result.setName(rs.getString("Name"));
                    result.setTypeFood(TypeFood.valueOf(rs.getString("Type")));
                    result.setCalories(rs.getInt("Calories"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Comida> findAll() {
        List<Comida> result = new ArrayList<>();

        try (PreparedStatement pst = conn.prepareStatement(FINDALL)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Comida comida = new Comida();
                    comida.setId(rs.getInt("ID"));
                    comida.setName(rs.getString("Name"));
                    comida.setTypeFood(TypeFood.valueOf(rs.getString("Type")));
                    comida.setCalories(rs.getInt("Calories"));
                    result.add(comida);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<Comida> findByDiet(Dieta dieta) {
        List<Comida> result = new ArrayList<>();
        if (dieta == null || dieta.getId() == 0) return result;

        try (PreparedStatement pst = conn.prepareStatement(FINDBYDIET)) {
            pst.setInt(1, dieta.getId());

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Comida comida = new Comida();
                    comida.setId(rs.getInt("ID"));
                    comida.setName(rs.getString("Name"));
                    comida.setTypeFood(TypeFood.valueOf(rs.getString("Type")));
                    comida.setCalories(rs.getInt("Calories"));
                    result.add(comida);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void close() {
        // Close resources if necessary
    }

    public static ComidaDAO build() {
        return new ComidaDAO();
    }
}
