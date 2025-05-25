package co.edu.konradlorenz.model;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Taller implements Registrable {
    private int id;
    private String nombre, descripcion;
    private LocalDateTime plazo;
    private Asignatura asignatura;
    private HashMap<Estudiante, String> respuestas = new HashMap();
    private HashMap<Estudiante, Integer> notas = new HashMap();

    public Taller() {}
    public Taller(String nombre, String descripcion, LocalDateTime plazo, Asignatura asignatura) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.plazo = plazo;
        this.asignatura = asignatura;
    }
    public Taller(int id, String nombre, String descripcion, LocalDateTime plazo, Asignatura asignatura) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.plazo = plazo;
        this.asignatura = asignatura;
    }
    
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public LocalDateTime getPlazo() {
        return plazo;
    }
    public void setPlazo(LocalDateTime plazo) {
        this.plazo = plazo;
    }
    public Asignatura getAsignatura() {
        return asignatura;
    }
    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }
    public HashMap<Estudiante, String> getRespuestas() {
        return respuestas;
    }
    public void setRespuestas(HashMap<Estudiante, String> respuestas) {
        this.respuestas = respuestas;
    }
    public HashMap<Estudiante, Integer> getNotas() {
        return notas;
    }
    public void setNotas(HashMap<Estudiante, Integer> notas) {
        this.notas = notas;
    }
    @Override
    public String toString() {
        return nombre;
    }

    
    @Override
    public Object[] obtenerCampos() {
        Object[] campos = {id, this, asignatura.getId(), asignatura, descripcion, plazo};
        return campos;
    }
}
