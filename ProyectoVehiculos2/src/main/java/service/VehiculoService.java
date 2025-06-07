package service;

import java.util.List;
import java.time.Year;

import domain.Vehiculo;
import domain.Marca;
import domain.Modelo;
import repository.VehiculoRepository;
import repository.jdbc.JdbcVehiculoRepository;

public class VehiculoService {
    private VehiculoRepository repo    = new JdbcVehiculoRepository();
    private MarcaService marcaService  = new MarcaService();
    private ModeloService modeloService= new ModeloService();

    private static final String PATTERN_PLACA  = "^[A-Z]{3}-\\d{4}$";
    private static final String PATTERN_CHASIS = "^[A-HJ-NPR-Z0-9]{17}$";

    public List<Vehiculo> listarTodos() throws Exception {
        return repo.findAll();
    }
    public Vehiculo obtenerPorId(int id) throws Exception {
        return repo.findById(id);
    }

    public boolean existsByColor(int idColor) throws Exception {
    return repo.existsByColor(idColor);
}

    public int crearVehiculo(int idMarca, int idModelo,
                             String placa, String chasis,
                             String anioStr, int idColor) throws Exception {
        Marca m = marcaService.obtenerPorId(idMarca);
        if (m==null) throw new Exception("Marca no encontrada.");
        Modelo mo = modeloService.obtenerPorId(idModelo);
        if (mo==null || mo.getIdMarca()!=idMarca) throw new Exception("Modelo no coincide con marca.");

        placa = placa.trim().toUpperCase();
        if (!placa.matches(PATTERN_PLACA)) throw new Exception("Placa inválida.");
        if (repo.existsByPlaca(placa)) throw new Exception("Placa duplicada.");

        chasis = chasis.trim().toUpperCase();
        if (!chasis.matches(PATTERN_CHASIS)) throw new Exception("Chasis inválido.");
        if (repo.existsByChasis(chasis)) throw new Exception("Chasis duplicado.");

        if (repo.existsByMarcaModeloChasis(idMarca, idModelo, chasis))
            throw new Exception("Duplicado marca+modelo+chasis.");

        int anio;
        try {
            anio = Integer.parseInt(anioStr);
        } catch (NumberFormatException e) {
            throw new Exception("Año inválido.");
        }
        int min=1886, max=Year.now().getValue()+1;
        if (anio<min||anio>max) throw new Exception("Año fuera de rango.");

        if (idColor<=0) throw new Exception("Color inválido.");

        Vehiculo v = new Vehiculo(0,idMarca,idModelo,placa,chasis,anio,idColor,null);
        return repo.insert(v);
    }

    public boolean actualizarVehiculo(int idVeh, int idMarca, int idModelo,
                                      String placa, String chasis,
                                      String anioStr, int idColor) throws Exception {
        Vehiculo ex = repo.findById(idVeh);
        if (ex==null) throw new Exception("Vehículo no hallado.");

        Marca m = marcaService.obtenerPorId(idMarca);
        Modelo mo = modeloService.obtenerPorId(idModelo);
        if (m==null||mo==null||mo.getIdMarca()!=idMarca)
            throw new Exception("Marca/modelo inválidos.");

        placa = placa.trim().toUpperCase();
        if (!placa.matches(PATTERN_PLACA)) throw new Exception("Placa inválida.");
        if (!placa.equals(ex.getPlaca()) && repo.existsByPlaca(placa))
            throw new Exception("Placa duplicada.");

        chasis = chasis.trim().toUpperCase();
        if (!chasis.matches(PATTERN_CHASIS)) throw new Exception("Chasis inválido.");
        if (!chasis.equals(ex.getChasis()) && repo.existsByChasis(chasis))
            throw new Exception("Chasis duplicado.");

        boolean mc = ex.getIdMarca()!=idMarca;
        boolean moc= ex.getIdModelo()!=idModelo;
        boolean cc = !ex.getChasis().equals(chasis);
        if ((mc||moc||cc) && repo.existsByMarcaModeloChasis(idMarca,idModelo,chasis))
            throw new Exception("Duplicado marca+modelo+chasis.");

        int anio;
        try { anio=Integer.parseInt(anioStr); }
        catch(Exception e) { throw new Exception("Año inválido."); }
        int min=1886,max=Year.now().getValue()+1;
        if (anio<min||anio>max) throw new Exception("Año fuera de rango.");
        if (idColor<=0) throw new Exception("Color inválido.");

        ex.setIdMarca(idMarca);
        ex.setIdModelo(idModelo);
        ex.setPlaca(placa);
        ex.setChasis(chasis);
        ex.setAnio(anio);
        ex.setIdColor(idColor);

        return repo.update(ex);
    }

    public boolean eliminarVehiculo(int id) throws Exception {
        return repo.delete(id);
    }
}
