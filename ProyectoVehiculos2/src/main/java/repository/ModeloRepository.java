package repository;

import java.util.List;
import domain.Modelo;

public interface ModeloRepository {
    List<Modelo> findAll() throws Exception;
    Modelo findById(int id) throws Exception;
    List<Modelo> findByMarca(int idMarca) throws Exception;
    int insert(Modelo m) throws Exception;
    boolean delete(int id) throws Exception;
}
