package co.edu.konradlorenz.model;

import java.util.ArrayList;

public class Asignatura {
    private String nombre;
    private Profesor profesor;
    private ArrayList<Estudiante> estudiantes = new ArrayList();
    private ArrayList<Taller> talleres = new ArrayList<>();

    public Asignatura(String nombre, Profesor profesor) {
        this.nombre = nombre;
        this.profesor = profesor;
    }

    public Asignatura() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public ArrayList<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(ArrayList<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public ArrayList<Taller> getTalleres() {
        return talleres;
    }

    public void setTalleres(ArrayList<Taller> talleres) {
        this.talleres = talleres;
    }
    
    

    public void agregarEstudiante(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }

    public void agregarTaller(Taller taller) {
        talleres.add(taller);
    }

    public Taller buscarTaller(String nombre) {
        for (Taller t : talleres) {
            if (t.getNombre().equalsIgnoreCase(nombre)) {
                return t;
            }
        }
        return null;
    }

    public Estudiante buscarEstudiante(String codigo) {
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getCodigo().equals(codigo)) {
                return estudiante;
            }
        }
        return null;
    }
}