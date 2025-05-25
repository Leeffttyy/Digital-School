package co.edu.konradlorenz.model;

public class Profesor extends Usuario implements Registrable {

    public Profesor() {
        asignarCodigoProfesor();
    }

    public Profesor(String clave) throws ExcepcionNombreVacio {
        super(clave); 
        asignarCodigoProfesor();
    }
    
    public Asignatura buscarAsignatura(String nombre) {
        for (Asignatura a : asignaturas) {
            if (a.getNombre().equalsIgnoreCase(nombre)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public void registrar() {
        asignarCodigoProfesor();
    }
}

