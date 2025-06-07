package repository.jdbc;

import repository.MarcaRepository;
import domain.Marca;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcMarcaRepository implements MarcaRepository {
    @Override
    public List<Marca> findAll() throws Exception {
        List<Marca> res = new ArrayList<>();
        String sql = "SELECT id_marca, nombre, fecha_creacion FROM marca";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {
            while (rs.next()) {
                res.add(new Marca(
                    rs.getInt("id_marca"),
                    rs.getString("nombre"),
                    rs.getTimestamp("fecha_creacion")
                ));
            }
        }
        return res;
    }

    @Override
    public Marca findById(int id) throws Exception {
        String sql = "SELECT id_marca, nombre, fecha_creacion FROM marca WHERE id_marca = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new Marca(
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
    public int insert(Marca m) throws Exception {
        String sql = "INSERT INTO marca (nombre) VALUES (?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, m.getNombre());
            int rows = p.executeUpdate();
            if (rows == 0) throw new Exception("No se insertó marca.");
            try (ResultSet rs = p.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new Exception("No se obtuvo ID de marca.");
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM marca WHERE id_marca = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        }
    }
}
