package co.edu.konradlorenz.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Estudiante extends Usuario implements Registrable {
    private HashMap<String, Integer> notasTalleres = new HashMap<>();

    public Estudiante() {
        asignarCodigoEstudiante();
    }

    public Estudiante(String clave) throws ExcepcionNombreVacio {
        super(clave); 
        asignarCodigoEstudiante();
    }

    public void recibirNotaTaller(String nombreTaller, int nota) {
        notasTalleres.put(nombreTaller, nota);
    }

    public HashMap<String, Integer> getNotasTalleres() {
        return notasTalleres;
    }

    @Override
    public void registrar() {
        asignarCodigoEstudiante();
    }
}
