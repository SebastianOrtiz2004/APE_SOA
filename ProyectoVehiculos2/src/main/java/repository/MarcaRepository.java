package repository;

import java.util.List;
import domain.Marca;

public interface MarcaRepository {
    List<Marca> findAll() throws Exception;
    Marca findById(int id) throws Exception;
    int insert(Marca m) throws Exception;
    boolean delete(int id) throws Exception;
}
