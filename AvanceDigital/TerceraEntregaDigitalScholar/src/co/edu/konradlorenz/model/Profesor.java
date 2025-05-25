package co.edu.konradlorenz.model;

public class Profesor extends Usuario {
    
    public Profesor() {}
    public Profesor(int id, int codigo, String nombre, String clave) {
        super(id, codigo, nombre, clave);
    }

    @Override
    public TipoUsuario getTipo() {
        return TipoUsuario.PROFESOR;
    }
    
}
