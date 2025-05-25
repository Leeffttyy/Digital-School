package co.edu.konradlorenz.model;

import java.util.ArrayList;

public abstract class Usuario implements Registrable {
    private int id;
    private String codigo, nombre, clave;
    private ArrayList<Asignatura> asignaturas = new ArrayList();
    
    public Usuario() {}
    public Usuario(String nombre, String clave) {
        this.nombre = nombre;
        this.clave = clave;
    }
    public Usuario(int id, int codigo, String nombre, String clave) {
        this.id = id;
        this.codigo = obtenerCodigo(codigo);
        this.nombre = nombre;
        this.clave = clave;
    }

    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getClave() {
        return clave;
    }
    public ArrayList<Asignatura> getAsignaturas() {
        return asignaturas;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setClave(String clave) {
        this.clave = clave;
    }
    public void setAsignaturas(ArrayList<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    @Override
    public String toString() {
        return nombre;
    }
    

    private String obtenerCodigo(int n) {
        return String.format("%04d", n);
    }
    
    public abstract TipoUsuario getTipo();
    
    @Override
    public Object[] obtenerCampos(){
        Object[] campos = {codigo, this, clave, getTipo()};
        return campos;
    }
}