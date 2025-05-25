package co.edu.konradlorenz.model;

import java.util.HashMap;

public class Taller {
    private String nombre;
    private String descripcion;
    private HashMap<Estudiante, String> respuestas;
    private HashMap<Estudiante, Integer> calificaciones;

    public Taller(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.respuestas = new HashMap<>();
        this.calificaciones = new HashMap<>();
    }

    public Taller() {}

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

    public HashMap<Estudiante, String> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(HashMap<Estudiante, String> respuestas) {
        this.respuestas = respuestas;
    }

    public HashMap<Estudiante, Integer> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(HashMap<Estudiante, Integer> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public void subirRespuestas(Estudiante estudiante, String respuesta) {
        respuestas.put(estudiante, respuesta);
    }

    public void calificarTaller(Estudiante estudiante, int nota) {
        if (respuestas.containsKey(estudiante)) {
            calificaciones.put(estudiante, nota);
            estudiante.recibirNotaTaller(nombre, nota);
        }
    }
}