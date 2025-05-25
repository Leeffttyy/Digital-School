package co.edu.konradlorenz.model;

import java.util.ArrayList;

public abstract class Usuario {
    protected ArrayList<Asignatura> asignaturas = new ArrayList<>();
    protected static int contadorEstudiantes = 1000;
    protected static int contadorProfesores = 0;
    protected String codigo, clave;

    public Usuario() {}

    public Usuario(String clave) throws ExcepcionNombreVacio {
        if (clave == null || clave.trim().isEmpty()) {
            throw new ExcepcionNombreVacio("La clave no puede estar vacía.");
        }
        this.clave = clave;
    }

    public static int getContadorEstudiantes() {
        return contadorEstudiantes;
    }

    public static int getContadorProfesores() {
        return contadorProfesores;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCodigo() {
        return codigo;
    }

    public ArrayList<Asignatura> getAsignaturas() {
        return asignaturas;
    }
    
    public void agregarAsignatura(Asignatura asignatura) {
        asignaturas.add(asignatura);
    }
    
    protected String generarCodigo(int num) {
        return String.format("%04d", num);
    }

    public void asignarCodigoEstudiante() {
        this.codigo = generarCodigo(contadorEstudiantes++);
    }

    public void asignarCodigoProfesor() {
        this.codigo = generarCodigo(contadorProfesores++);
    }
}

