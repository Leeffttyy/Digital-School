package co.edu.konradlorenz.model;

import java.util.ArrayList;

public class Asignatura implements Registrable {
    private int id;
    private String nombre;
    private Profesor profesor;
    private ArrayList<Estudiante> estudiantes = new ArrayList();
    private ArrayList<Taller> talleres = new ArrayList();

    public Asignatura() {}
    public Asignatura(String nombre, Profesor profesor) {
        this.nombre = nombre;
        this.profesor = profesor;
    }
    public Asignatura(int id, String nombre, Profesor profesor) {
        this.id = id;
        this.nombre = nombre;
        this.profesor = profesor;
    }
    
    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public Profesor getProfesor() {
        return profesor;
    }
    public ArrayList<Estudiante> getEstudiantes() {
        return estudiantes;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
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
    @Override
    public String toString() {
        return nombre;
    }
    
    
    @Override
    public Object[] obtenerCampos(){
        String codigo = "NULL";
        String profesor = "NULL";
        if (this.profesor!=null) {
            profesor = this.profesor.toString();
            codigo = this.profesor.getCodigo();
        }
        Object[] campos = {id, this, codigo, profesor};
        return campos;
    }

}