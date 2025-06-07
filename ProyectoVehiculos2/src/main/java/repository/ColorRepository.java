package repository;

import java.util.List;
import domain.Color;

public interface ColorRepository {
    List<Color> findAll() throws Exception;
    Color findById(int id) throws Exception;
    int insert(Color c) throws Exception;
    boolean delete(int id) throws Exception;
}
