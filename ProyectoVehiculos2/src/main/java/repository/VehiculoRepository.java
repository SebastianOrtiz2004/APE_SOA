package repository;

import java.util.List;
import domain.Vehiculo;

public interface VehiculoRepository {
    List<Vehiculo> findAll() throws Exception;
    Vehiculo findById(int id) throws Exception;
    int insert(Vehiculo v) throws Exception;
    boolean update(Vehiculo v) throws Exception;
    boolean delete(int id) throws Exception;

    boolean existsByPlaca(String placa) throws Exception;
    boolean existsByChasis(String chasis) throws Exception;
    boolean existsByMarcaModeloChasis(int idMarca, int idModelo, String chasis) throws Exception;
    boolean existsByColor(int idColor) throws Exception;

}
