package repository.jdbc;

import repository.ModeloRepository;
import domain.Modelo;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcModeloRepository implements ModeloRepository {
    @Override
    public List<Modelo> findAll() throws Exception {
        List<Modelo> res = new ArrayList<>();
        String sql = "SELECT id_modelo, id_marca, nombre, fecha_creacion FROM modelo";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {
            while (rs.next()) {
                res.add(new Modelo(
                    rs.getInt("id_modelo"),
                    rs.getInt("id_marca"),
                    rs.getString("nombre"),
                    rs.getTimestamp("fecha_creacion")
                ));
            }
        }
        return res;
    }

    @Override
    public Modelo findById(int id) throws Exception {
        String sql = "SELECT id_modelo, id_marca, nombre, fecha_creacion FROM modelo WHERE id_modelo = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new Modelo(
                        rs.getInt("id_modelo"),
                        rs.getInt("id_marca"),
                        rs.getString("nombre"),
                        rs.getTimestamp("fecha_creacion")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Modelo> findByMarca(int idMarca) throws Exception {
        List<Modelo> res = new ArrayList<>();
        String sql = "SELECT id_modelo, id_marca, nombre, fecha_creacion FROM modelo WHERE id_marca = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idMarca);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    res.add(new Modelo(
                        rs.getInt("id_modelo"),
                        rs.getInt("id_marca"),
                        rs.getString("nombre"),
                        rs.getTimestamp("fecha_creacion")
                    ));
                }
            }
        }
        return res;
    }

    @Override
    public int insert(Modelo m) throws Exception {
        String sql = "INSERT INTO modelo (id_marca, nombre) VALUES (?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setInt(1, m.getIdMarca());
            p.setString(2, m.getNombre());
            int rows = p.executeUpdate();
            if (rows == 0) throw new Exception("No se insertó modelo.");
            try (ResultSet rs = p.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new Exception("No se obtuvo ID de modelo.");
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM modelo WHERE id_modelo = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        }
    }
}
