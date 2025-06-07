package repository.jdbc;

import repository.VehiculoRepository;
import domain.Vehiculo;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcVehiculoRepository implements VehiculoRepository {
    @Override
    public List<Vehiculo> findAll() throws Exception {
        List<Vehiculo> res = new ArrayList<>();
        String sql = "SELECT id_vehiculo, id_marca, id_modelo, placa, chasis, anio, id_color, fecha_creacion FROM vehiculo";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {
            while (rs.next()) {
                res.add(new Vehiculo(
                    rs.getInt("id_vehiculo"),
                    rs.getInt("id_marca"),
                    rs.getInt("id_modelo"),
                    rs.getString("placa"),
                    rs.getString("chasis"),
                    rs.getInt("anio"),
                    rs.getInt("id_color"),
                    rs.getTimestamp("fecha_creacion")
                ));
            }
        }
        return res;
    }
    @Override
public boolean existsByColor(int idColor) throws Exception {
    String sql = "SELECT COUNT(*) FROM vehiculo WHERE id_color = ?";
    try (Connection c = DBConnection.getConnection();
         PreparedStatement p = c.prepareStatement(sql)) {
        p.setInt(1, idColor);
        try (ResultSet rs = p.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}


    @Override
    public Vehiculo findById(int id) throws Exception {
        String sql = "SELECT id_vehiculo, id_marca, id_modelo, placa, chasis, anio, id_color, fecha_creacion "
                   + "FROM vehiculo WHERE id_vehiculo = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new Vehiculo(
                        rs.getInt("id_vehiculo"),
                        rs.getInt("id_marca"),
                        rs.getInt("id_modelo"),
                        rs.getString("placa"),
                        rs.getString("chasis"),
                        rs.getInt("anio"),
                        rs.getInt("id_color"),
                        rs.getTimestamp("fecha_creacion")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public int insert(Vehiculo v) throws Exception {
        String sql = "INSERT INTO vehiculo (id_marca, id_modelo, placa, chasis, anio, id_color) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setInt(1, v.getIdMarca());
            p.setInt(2, v.getIdModelo());
            p.setString(3, v.getPlaca());
            p.setString(4, v.getChasis());
            p.setInt(5, v.getAnio());
            p.setInt(6, v.getIdColor());
            int rows = p.executeUpdate();
            if (rows == 0) throw new Exception("No se insertó vehículo.");
            try (ResultSet rs = p.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new Exception("No se obtuvo ID de vehículo.");
    }

    @Override
    public boolean update(Vehiculo v) throws Exception {
        String sql = "UPDATE vehiculo SET id_marca=?, id_modelo=?, placa=?, chasis=?, anio=?, id_color=? "
                   + "WHERE id_vehiculo=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, v.getIdMarca());
            p.setInt(2, v.getIdModelo());
            p.setString(3, v.getPlaca());
            p.setString(4, v.getChasis());
            p.setInt(5, v.getAnio());
            p.setInt(6, v.getIdColor());
            p.setInt(7, v.getId());
            return p.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM vehiculo WHERE id_vehiculo = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        }
    }

    @Override
    public boolean existsByPlaca(String placa) throws Exception {
        String sql = "SELECT COUNT(*) FROM vehiculo WHERE placa = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, placa);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public boolean existsByChasis(String chasis) throws Exception {
        String sql = "SELECT COUNT(*) FROM vehiculo WHERE chasis = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, chasis);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public boolean existsByMarcaModeloChasis(int idMarca, int idModelo, String chasis) throws Exception {
        String sql = "SELECT COUNT(*) FROM vehiculo WHERE id_marca=? AND id_modelo=? AND chasis=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idMarca);
            p.setInt(2, idModelo);
            p.setString(3, chasis);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
