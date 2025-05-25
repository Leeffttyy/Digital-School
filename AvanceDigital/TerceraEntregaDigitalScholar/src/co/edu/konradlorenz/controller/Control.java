package co.edu.konradlorenz.controller;

import java.util.HashMap;
import co.edu.konradlorenz.model.*;
import co.edu.konradlorenz.view.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class Control implements ActionListener {
    private Principal vPrincipal;
    private EstudianteCalificaciones vEC;
    private HashMap<String, Usuario> listaUsuarios;

    public Control() {
        vPrincipal = new Principal();
        vPrincipal.getBtnIniciarSesion().addActionListener(this);

        vEC = new EstudianteCalificaciones();
        vEC.getBtnAsignaturas().addActionListener(this);
        vEC.getBtnCalificaciones().addActionListener(this);
        vEC.getBtnCerrarSesion().addActionListener(this);
        vEC.getBtnTalleres().addActionListener(this);
        vEC.getBtnVolver().addActionListener(this);
        vEC.getCbbCalificaciones().addActionListener(this);
        
        listaUsuarios = new HashMap<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vPrincipal.getBtnIniciarSesion()) {
            btnIniciarSesionActionPerformed();
        }
        if (e.getSource() == vEC.getBtnAsignaturas()) {
            btnAsignaturasActionPerformed();
        }
        if (e.getSource() == vEC.getBtnCalificaciones()) {
            btnCalificacionesActionPerformed();
        }
        if (e.getSource() == vEC.getBtnCerrarSesion()) {
            btnCerrarSesionActionPerformed();
        }
        if (e.getSource() == vEC.getBtnTalleres()) {
            btnTalleresActionPerformed();
        }
        if (e.getSource() == vEC.getBtnVolver()) {
            btnVolverActionPerformed();
        }
        if (e.getSource() == vEC.getBtnCalificaciones()) {
            btnCalificacionesActionPerformed();
        }
        if (e.getSource() == vEC.getCbbCalificaciones()) {
            cbbCalificacionesActionPerformed();
        }
    }

    private void btnIniciarSesionActionPerformed() {
        if (vPrincipal.getRbtEstudiante().isSelected()) {
            EstudiantesAsignaturas Easignaturas = new EstudiantesAsignaturas();
            Easignaturas.setVisible(true);
            vPrincipal.dispose();
        }
        if (vPrincipal.getBtnProfesor().isSelected()) {
            ProfesorPrincipal Pprincipal = new ProfesorPrincipal();
            Pprincipal.setVisible(true);
            vPrincipal.dispose();

        } else {
            JOptionPane.showMessageDialog(vPrincipal, "Por favor selecciona Estudiante o Profesor.");
        }
    }
    
    private void btnAsignaturasActionPerformed() {
        EstudiantesAsignaturas Easignaturas = new EstudiantesAsignaturas();
        Easignaturas.setVisible(true);
        vPrincipal.dispose();
    }

    private void btnTalleresActionPerformed() {
        EstudianteTalleres Etalleres = new EstudianteTalleres();
        Etalleres.setVisible(true);
        vPrincipal.dispose();
    }

    private void btnCalificacionesActionPerformed() {
        EstudianteCalificaciones Ecalificaciones = new EstudianteCalificaciones();
        Ecalificaciones.setVisible(true);
        vPrincipal.dispose();
    }

    private void btnCerrarSesionActionPerformed() {
        Principal Pri = new Principal();
        Pri.setVisible(true);
        vPrincipal.dispose();
    }

    private void btnVolverActionPerformed() {
        EstudiantesAsignaturas Easignaturas = new EstudiantesAsignaturas();
        Easignaturas.setVisible(true);
        vPrincipal.dispose();
    }

    private void cbbCalificacionesActionPerformed() {
        String msj = "Calificaciones en ";
        msj = msj + vEC.getCbbCalificaciones().getSelectedItem().toString();
        vEC.getLblCalificaciones().setText(msj);
    }

    /*public void run() {
        objV.mostrarTexto("DIGITAL SCHOLAR v1.0");
     Principal principal = new Principal();
     principal.setVisible(true);
        int opcion = -1;
        do {
            try {
                opcion = objV.pedirEntero(
                        "\n0. Salir" +
                        "\n1. Agregar nuevo Usuario" +
                        "\n2. Acceder con Usuario" +
                        "\nDigite la opción: ");

                switch (opcion) {
                    case 1:
                        agregarUsuario();
                        break;
                    case 2:
                        accederUsuario();
                        break;
                    case 0:
                        objV.mostrarTexto("Saliendo del sistema...");
                        break;
                    default:
                        objV.mostrarTexto("Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                objV.mostrarTexto("Error: debe ingresar un número válido.");
            } catch (Exception e) {
                objV.mostrarTexto("Error inesperado: " + e.getMessage());
                
            }
        } while (opcion!=0);
    }

    private void agregarUsuario() {
    int seleccion = 1;
    try {
        seleccion = objV.pedirEntero("\n1: Estudiante 2: Profesor 0: Atrás\nDigite la opción: ");

        if (seleccion == 0) return;

        if (seleccion != 1 && seleccion != 2) {
            objV.mostrarTexto("Opción no válida.");
            return;
        }

        String clave = objV.pedirTexto("Ingrese la clave: ");
        if (clave == null || clave.trim().isEmpty()) {
            throw new ExcepcionNombreVacio("La clave no puede estar vacía.");
        }

        Usuario usuario = (seleccion == 1) ?
            new Estudiante(clave) :
            new Profesor(clave);

        String codigo = usuario.getCodigo();
        if (listaUsuarios.containsKey(codigo)) {
            throw new IllegalStateException("El código de usuario ya existe.");
        }

        listaUsuarios.put(codigo, usuario);
        objV.mostrarTexto("Usuario creado con código " + codigo);

    } catch (ExcepcionNombreVacio e) {
        objV.mostrarTexto("Error: " + e.getMessage());
    } catch (IllegalArgumentException e) {
        objV.mostrarTexto("Error de validación: " + e.getMessage());
    } catch (Exception e) {
        objV.mostrarTexto("Error al agregar usuario: " + e.getMessage());
    }
}


    private void accederUsuario() {
        final int MAX_INTENTOS = 3;
        int intentos = 0;
        
        while (intentos < MAX_INTENTOS) {
            try {
                String codigo = objV.pedirTexto("Digite su código: ");
                String clave = objV.pedirTexto("Digite su clave: ");

                if (codigo == null || codigo.trim().isEmpty() || 
                    clave == null || clave.trim().isEmpty()) {
                    throw new IllegalArgumentException("Código y clave son obligatorios.");
                }

                Usuario usuario = listaUsuarios.get(codigo);
                if (usuario == null) {
                    throw new SecurityException("Usuario no encontrado.");
                }

                if (!usuario.getClave().equals(clave)) {
                    throw new SecurityException("Clave incorrecta.");
                }

              
                if (usuario instanceof Estudiante) {
                    mostrarMenuEstudiante((Estudiante) usuario);
                } else if (usuario instanceof Profesor) {
                    mostrarMenuProfesor((Profesor) usuario);
                }
                return;

            } catch (SecurityException e) {
                intentos++;
                objV.mostrarTexto("Acceso denegado: " + e.getMessage());
                objV.mostrarTexto("Intentos restantes: " + (MAX_INTENTOS - intentos));
            } catch (Exception e) {
                intentos++;
                objV.mostrarTexto("Error: " + e.getMessage());
            }
        }
        
        objV.mostrarTexto("Demasiados intentos fallidos. Por favor intente más tarde.");
    }

    private void mostrarMenuEstudiante(Estudiante estudiante) {
        int opcion = -1;
        do {
            try {
                opcion = objV.pedirEntero("\nMENÚ ESTUDIANTE\n" +
                        "1. Ver notas\n" +
                        "2. Ver talleres pendientes\n" +
                        "3. Subir asignación del taller\n" +
                        "4. Salir\n" +
                        "Seleccione una opción: ");

                switch (opcion) {
                    case 1:
                        verNotas(estudiante);
                        break;
                    case 2:
                        verTalleresPendientes(estudiante);
                        break;
                    case 3:
                        subirAsignacionTaller(estudiante);
                        break;
                    case 4:
                        objV.mostrarTexto("Saliendo del menú de estudiante.");
                        break;
                    default:
                        objV.mostrarTexto("Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                objV.mostrarTexto("Error: debe ingresar un número válido.");
            } catch (Exception e) {
                objV.mostrarTexto("Error inesperado: " + e.getMessage());
            }
        } while (opcion != 4);
    }

    private void mostrarMenuProfesor(Profesor profesor) {
        int opcion = -1;
        do {
            try {
                opcion = objV.pedirEntero("\nMENÚ PROFESOR\n" +
                        "1. Crear asignatura\n" +
                        "2. Agregar estudiante\n" +
                        "3. Crear taller\n" +
                        "4. Asignar nota\n" +
                        "5. Salir\n" +
                        "Seleccione una opción: ");

                switch (opcion) {
                    case 1:
                        crearAsignatura(profesor);
                        break;
                    case 2:
                        agregarEstudiante(profesor);
                        break;
                    case 3:
                        crearTaller(profesor);
                        break;
                    case 4:
                        calificarTaller(profesor);
                        break;
                    case 5:
                        objV.mostrarTexto("Saliendo del menú de profesor.");
                        break;
                    default:
                        objV.mostrarTexto("Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                objV.mostrarTexto("Error: debe ingresar un número válido.");
            } catch (Exception e) {
                objV.mostrarTexto("Error inesperado: " + e.getMessage());
            }
        } while (opcion != 5);
    }

    private void verNotas(Estudiante estudiante) {
        try {
            if (estudiante.getNotasTalleres().isEmpty()) {
                objV.mostrarTexto("No hay notas registradas para " + estudiante.getCodigo());
                return;
            }
            
            objV.mostrarTexto("\nNOTAS DE " + estudiante.getCodigo());
            estudiante.getNotasTalleres().forEach((taller, nota) -> 
                objV.mostrarTexto("Taller: " + taller + " - Nota: " + nota));
                
        } catch (Exception e) {
            objV.mostrarTexto("Error al mostrar notas: " + e.getMessage());
        }
    }

    private void verTalleresPendientes(Estudiante estudiante) {
        try {
            if (estudiante.getAsignaturas().isEmpty()) {
                objV.mostrarTexto("No estás inscrito en ninguna asignatura.");
                return;
            }
            
            boolean tienePendientes = false;
            objV.mostrarTexto("\nTALLERES PENDIENTES");
            
            for (Asignatura asignatura : estudiante.getAsignaturas()) {
                for (Taller taller : asignatura.getTalleres()) {
                    if (!taller.getRespuestas().containsKey(estudiante)) {
                        objV.mostrarTexto("Asignatura: " + asignatura.getNombre() + 
                                          " - Taller: " + taller.getNombre());
                        tienePendientes = true;
                    }
                }
            }
            
            if (!tienePendientes) {
                objV.mostrarTexto("¡No tienes talleres pendientes!");
            }
        } catch (Exception e) {
            objV.mostrarTexto("Error al mostrar talleres pendientes: " + e.getMessage());
        }
    }

    private void subirAsignacionTaller(Estudiante estudiante) {
        try {
            if (estudiante.getAsignaturas().isEmpty()) {
                objV.mostrarTexto("No estás inscrito en ninguna asignatura.");
                return;
            }
            
            String nombreTaller = objV.pedirTexto("Ingrese el nombre del taller: ");
            if (nombreTaller == null || nombreTaller.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del taller es obligatorio.");
            }
            boolean encontrado = false;
            for (Asignatura asignatura : estudiante.getAsignaturas()) {
                var taller = asignatura.buscarTaller(nombreTaller);
                if (taller != null && !taller.getRespuestas().containsKey(estudiante)) {
                    String respuesta = objV.pedirTexto("Ingrese su respuesta: ");
                    if (respuesta == null || respuesta.trim().isEmpty()) {
                        throw new IllegalArgumentException("La respuesta no puede estar vacía.");
                    }
                    
                    taller.subirRespuestas(estudiante, respuesta);
                    objV.mostrarTexto("Respuesta subida exitosamente.");
                    encontrado = true;
                    break;
                }
            }
            
            if (!encontrado) {
                throw new IllegalArgumentException("Taller no encontrado en tus asignaturas.");
            }
        } catch (IllegalArgumentException e) {
            objV.mostrarTexto("Error: " + e.getMessage());
        } catch (Exception e) {
            objV.mostrarTexto("Error al subir asignación: " + e.getMessage());
        }
    }

    private void crearAsignatura(Profesor profesor) {
        try {
            String nombre = objV.pedirTexto("Ingrese el nombre de la asignatura: ");
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre de la asignatura es obligatorio.");
            }
            
            if (profesor.buscarAsignatura(nombre) != null) {
                throw new IllegalStateException("Ya existe una asignatura con ese nombre.");
            }
            
            Asignatura asignatura = new Asignatura(nombre, profesor);
            profesor.agregarAsignatura(asignatura);
            objV.mostrarTexto("Asignatura '" + nombre + "' creada exitosamente.");
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            objV.mostrarTexto("Error: " + e.getMessage());
        } catch (Exception e) {
            objV.mostrarTexto("Error inesperado al crear asignatura: " + e.getMessage());
        }
    }

    private void agregarEstudiante(Profesor profesor) {
        try {
            
            
            String nombreAsignatura = objV.pedirTexto("Ingrese el nombre de la asignatura: ");
            Asignatura asignatura = profesor.buscarAsignatura(nombreAsignatura);
            if (asignatura == null) {
                throw new IllegalArgumentException("Asignatura no encontrada.");
            }

            String codigoEstudiante = objV.pedirTexto("Ingrese el código del estudiante: ");
            Usuario usuario = listaUsuarios.get(codigoEstudiante);
            if (usuario == null) {
                throw new IllegalArgumentException("Estudiante no encontrado.");
            }
            
            if (!(usuario instanceof Estudiante)) {
                throw new IllegalArgumentException("El código corresponde a un profesor, no a un estudiante.");
            }
            
            Estudiante estudiante = (Estudiante) usuario;
            
            if (asignatura.buscarEstudiante(codigoEstudiante) != null) {
                throw new IllegalStateException("El estudiante ya está inscrito en esta asignatura.");
            }
            
            estudiante.agregarAsignatura(asignatura);
            asignatura.agregarEstudiante(estudiante);
            objV.mostrarTexto("Estudiante '" + codigoEstudiante + "' agregado exitosamente.");
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            objV.mostrarTexto("Error: " + e.getMessage());
        } catch (Exception e) {
            objV.mostrarTexto("Error inesperado al agregar estudiante: " + e.getMessage());
        }
        
        
        
    }

    private void crearTaller(Profesor profesor) {
        try {
            String nombreAsignatura = objV.pedirTexto("Ingrese el nombre de la asignatura: ");
            Asignatura asignatura = profesor.buscarAsignatura(nombreAsignatura);
            if (asignatura == null) {
                throw new IllegalArgumentException("Asignatura no encontrada.");
            }

            String nombreTaller = objV.pedirTexto("Ingrese el nombre del taller: ");
            if (nombreTaller == null || nombreTaller.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del taller es obligatorio.");
            }
            
            if (asignatura.buscarTaller(nombreTaller) != null) {
                throw new IllegalStateException("Ya existe un taller con ese nombre.");
            }

            String descripcion = objV.pedirTexto("Ingrese la descripción del taller: ");
            Taller taller = new Taller(nombreTaller, descripcion);
            asignatura.agregarTaller(taller);
            objV.mostrarTexto("Taller '" + nombreTaller + "' creado exitosamente.");
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            objV.mostrarTexto("Error: " + e.getMessage());
        } catch (Exception e) {
            objV.mostrarTexto("Error inesperado al crear taller: " + e.getMessage());
        }
    }

    private void calificarTaller(Profesor profesor) {
        try {
            String nombreAsignatura = objV.pedirTexto("Ingrese el nombre de la asignatura: ");
            Asignatura asignatura = profesor.buscarAsignatura(nombreAsignatura);
            if (asignatura == null) {
                throw new IllegalArgumentException("Asignatura no encontrada.");
            }

            String nombreTaller = objV.pedirTexto("Ingrese el nombre del taller: ");
            var taller = asignatura.buscarTaller(nombreTaller);
            if (taller == null) {
                throw new IllegalArgumentException("Taller no encontrado.");
            }

            String codigoEstudiante = objV.pedirTexto("Ingrese el código del estudiante: ");
            Estudiante estudiante = asignatura.buscarEstudiante(codigoEstudiante);
            if (estudiante == null) {
                throw new IllegalArgumentException("Estudiante no encontrado en esta asignatura.");
            }

            if (!taller.getRespuestas().containsKey(estudiante)) {
                throw new IllegalStateException("El estudiante no ha subido respuesta para este taller.");
            }
            objV.mostrarTexto(
                    "Descripción: " + "\n" + taller.getDescripcion() +
                    "\nRespuesta:" + "\n" + taller.getRespuestas().get(estudiante));

            int nota = objV.pedirEntero("Ingrese la nota (0-100): ");
            if (nota < 0 || nota > 100) {
                throw new IllegalArgumentException("La nota debe estar entre 0 y 100.");
            }

            taller.calificarTaller(estudiante, nota);
            objV.mostrarTexto("Nota asignada con éxito.");
            
        } catch (NumberFormatException e) {
            objV.mostrarTexto("Error: debe ingresar un número válido para la nota.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            objV.mostrarTexto("Error: " + e.getMessage());
        } catch (Exception e) {
            objV.mostrarTexto("Error inesperado al calificar: " + e.getMessage());
        }
    }*/
}
