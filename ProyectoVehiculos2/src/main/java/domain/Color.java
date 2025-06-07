package domain;

import java.sql.Timestamp;

public class Color {
    private int id;
    private String nombre;
    private Timestamp fechaCreacion;

    public Color() {}
    public Color(int id, String nombre, Timestamp fechaCreacion) {
        this.id = id; this.nombre = nombre; this.fechaCreacion = fechaCreacion;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
