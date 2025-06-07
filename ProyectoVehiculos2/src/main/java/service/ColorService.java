package service;

import repository.ColorRepository;
import repository.jdbc.JdbcColorRepository;
import domain.Color;
import java.util.List;

public class ColorService {
    private ColorRepository repo = new JdbcColorRepository();
        private VehiculoService vehiculoService = new VehiculoService();


    public List<Color> listarTodas() throws Exception {
        return repo.findAll();
    }
    public Color obtenerPorId(int id) throws Exception {
        return repo.findById(id);
    }
    public int crear(String nombre) throws Exception {
        if (nombre==null||nombre.isBlank()) throw new Exception("Nombre requerido.");
        return repo.insert(new Color(0, nombre, null));
    }
    public boolean eliminar(int id) throws Exception {
        if (vehiculoService.existsByColor(id)) {
            throw new Exception("No se puede eliminar el color: existen vehículos asociados.");
        }
        return repo.delete(id);
    }
    
}
