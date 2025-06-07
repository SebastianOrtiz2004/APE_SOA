package service;

import repository.MarcaRepository;
import repository.jdbc.JdbcMarcaRepository;
import domain.Marca;
import java.util.List;

public class MarcaService {
    private MarcaRepository repo = new JdbcMarcaRepository();

    public List<Marca> listarTodas() throws Exception {
        return repo.findAll();
    }
    public Marca obtenerPorId(int id) throws Exception {
        return repo.findById(id);
    }
    public int crear(String nombre) throws Exception {
        if (nombre == null || nombre.isBlank()) throw new Exception("Nombre requerido.");
        return repo.insert(new Marca(0, nombre, null));
    }
    public boolean eliminar(int id) throws Exception {
        return repo.delete(id);
    }
}
