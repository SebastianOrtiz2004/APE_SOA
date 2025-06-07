package domain;

import java.sql.Timestamp;

public class Vehiculo {
    private int id;
    private int idMarca;
    private int idModelo;
    private String placa;
    private String chasis;
    private int anio;
    private int idColor;
    private Timestamp fechaCreacion;

    public Vehiculo() {}
    public Vehiculo(int id, int idMarca, int idModelo,
                    String placa, String chasis, int anio, int idColor, Timestamp fecha) {
        this.id = id; this.idMarca = idMarca; this.idModelo = idModelo;
        this.placa = placa; this.chasis = chasis; this.anio = anio;
        this.idColor = idColor; this.fechaCreacion = fecha;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdMarca() { return idMarca; }
    public void setIdMarca(int idMarca) { this.idMarca = idMarca; }
    public int getIdModelo() { return idModelo; }
    public void setIdModelo(int idModelo) { this.idModelo = idModelo; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getChasis() { return chasis; }
    public void setChasis(String chasis) { this.chasis = chasis; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    public int getIdColor() { return idColor; }
    public void setIdColor(int idColor) { this.idColor = idColor; }
    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
