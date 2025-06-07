package domain;

import java.sql.Timestamp;

public class Modelo {
    private int id;
    private int idMarca;
    private String nombre;
    private Timestamp fechaCreacion;

    public Modelo() {}
    public Modelo(int id, int idMarca, String nombre, Timestamp fechaCreacion) {
        this.id = id; this.idMarca = idMarca; this.nombre = nombre; this.fechaCreacion = fechaCreacion;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdMarca() { return idMarca; }
    public void setIdMarca(int idMarca) { this.idMarca = idMarca; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
