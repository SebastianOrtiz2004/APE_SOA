package service;

import repository.ModeloRepository;
import repository.jdbc.JdbcModeloRepository;
import domain.Modelo;
import java.util.List;

public class ModeloService {
    private ModeloRepository repo = new JdbcModeloRepository();

    public List<Modelo> listarTodas() throws Exception {
        return repo.findAll();
    }
    public Modelo obtenerPorId(int id) throws Exception {
        return repo.findById(id);
    }
    public List<Modelo> listarPorMarca(int idMarca) throws Exception {
        return repo.findByMarca(idMarca);
    }
    public int crear(int idMarca, String nombre) throws Exception {
        if (idMarca <= 0) throw new Exception("Marca inválida.");
        if (nombre == null||nombre.isBlank()) throw new Exception("Nombre requerido.");
        return repo.insert(new Modelo(0, idMarca, nombre, null));
    }
    public boolean eliminar(int id) throws Exception {
        return repo.delete(id);
    }
}
