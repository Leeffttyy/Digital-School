package co.edu.konradlorenz.model;

public class Estudiante extends Usuario {
    
    public Estudiante() {}
    public Estudiante(int id, int codigo, String nombre, String clave) {
        super(id, codigo, nombre, clave);
    }

    @Override
    public TipoUsuario getTipo() {
        return TipoUsuario.ESTUDIANTE;
    }
    
}
