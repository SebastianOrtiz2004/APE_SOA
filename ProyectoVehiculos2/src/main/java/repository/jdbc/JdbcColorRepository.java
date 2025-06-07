package repository.jdbc;

import repository.ColorRepository;
import domain.Color;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcColorRepository implements ColorRepository {
    @Override
    public List<Color> findAll() throws Exception {
        List<Color> res = new ArrayList<>();
        String sql = "SELECT id_color, nombre, fecha_creacion FROM color";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {
            while (rs.next()) {
                res.add(new Color(
                    rs.getInt("id_color"),
                    rs.getString("nombre"),
                    rs.getTimestamp("fecha_creacion")
                ));
            }
        }
        return res;
    }

    @Override
    public Color findById(int id) throws Exception {
        String sql = "SELECT id_color, nombre, fecha_creacion FROM color WHERE id_color = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new Color(
                        rs.getInt("id_color"),
                        rs.getString("nombre"),
                        rs.getTimestamp("fecha_creacion")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public int insert(Color c) throws Exception {
        String sql = "INSERT INTO color (nombre) VALUES (?)";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, c.getNombre());
            int rows = p.executeUpdate();
            if (rows == 0) throw new Exception("No se insertó color.");
            try (ResultSet rs = p.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new Exception("No se obtuvo ID de color.");
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM color WHERE id_color = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        }
    }
}
