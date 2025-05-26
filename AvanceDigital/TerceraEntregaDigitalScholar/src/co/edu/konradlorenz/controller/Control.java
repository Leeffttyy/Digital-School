package co.edu.konradlorenz.controller;

import co.edu.konradlorenz.model.*;
import co.edu.konradlorenz.view.*;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;

public class Control implements ActionListener {
    private Principal vPrincipal;
    private Admin vAdmin;
    private Estudiantes vEstudiantes;
    private EAsignaturas vEA;
    private ETalleres vET;
    private ECalificaciones vEC;
    private ProfesorPrincipal vProfesores;
    
    private Estudiante estudiante;
    private Profesor profesor;
    private Asignatura asignatura;
    
    private UsuarioDAO daoU = new UsuarioDAO();
    private AsignaturaDAO daoA = new AsignaturaDAO();
    private UsuarioAsignaturaDAO daoUA = new UsuarioAsignaturaDAO();
    private TallerDAO daoT = new TallerDAO();
    
    private Registrable aux;
    private boolean seleccionadoUsuario = true;
    private boolean seleccionadoAsignatura = false;
    private boolean seleccionadoRelacion = false;
    private HashMap<String, Usuario> listaUsuarios;

    public Control() {
        vPrincipal = new Principal();
        vPrincipal.getBtnIniciarSesion().addActionListener(this);
        
        vAdmin = new Admin();
        vAdmin.getBtnMenuUsuario().addActionListener(this);
        vAdmin.getBtnMenuAsignatura().addActionListener(this);
        vAdmin.getBtnMenuUsuarioAsignatura().addActionListener(this);
        vAdmin.getBtnListar().addActionListener(this);
        vAdmin.getBtnMenuInsertar().addActionListener(this);
        vAdmin.getBtnMenuActualizar().addActionListener(this);
        vAdmin.getBtnEliminar().addActionListener(this);
        vAdmin.getBtnConfirmar().addActionListener(this);
        vAdmin.getBtnRegistrar().addActionListener(this);
        vAdmin.getBtnHecho().addActionListener(this);
        vAdmin.getBtnVolver().addActionListener(this);
        
        vEstudiantes = new Estudiantes();
        vEA = new EAsignaturas();
        vET = new ETalleres();
        vEC = new ECalificaciones();
        vEstudiantes.getEscritorio().add(vEA);
        vEstudiantes.getEscritorio().add(vET);
        vEstudiantes.getEscritorio().add(vEC);
        
        vEstudiantes.getBtnAsignaturas().addActionListener(this);
        vEstudiantes.getBtnCalificaciones().addActionListener(this);
        vEstudiantes.getBtnCerrarSesion().addActionListener(this);
        vEstudiantes.getBtnTalleres().addActionListener(this);
        
        vEA.getBtn1().addActionListener(this);
        vEA.getBtn2().addActionListener(this);
        vEA.getBtn3().addActionListener(this);
        vEA.getBtn4().addActionListener(this);
        vEA.getBtn5().addActionListener(this);
        vEA.getBtn6().addActionListener(this);
        vEA.getBtnSeleccionar().addActionListener(this);
        
    }

    public void run() {
        vPrincipal.setVisible(true);
        
        try {
            HashMap<Integer, Asignatura> listaAsignaturas = daoA.cargarAsignaturas();
            daoT.cargarRelaciones(listaAsignaturas);
            listaUsuarios = daoU.cargarUsuarios();
            if (listaUsuarios==null) {
                listaUsuarios = new HashMap();
                JOptionPane.showMessageDialog(vPrincipal, "Bienvenido por primera vez, señor Administrador. Su código de usuario es 0000");
                JPasswordField pswClave = new JPasswordField();
                do{
                    JOptionPane.showConfirmDialog(null, pswClave, "Ingrese su contraseña", JOptionPane.OK_CANCEL_OPTION);
                } while (pswClave.getPassword()!=null);
                
                agregarUsuario(new Profesor(
                        "Administrador",
                        new String(pswClave.getPassword())));
            } else {
                daoU.cargarRelaciones(listaUsuarios);
            }
        } catch (SQLException ex) {
            System.out.println("Error SQL al cargar datos");
        }
    }
    
    private DAO obtenerDAOActual() {
        if(seleccionadoUsuario) {
            return new UsuarioDAO();
        } else if (seleccionadoAsignatura) {
            return new AsignaturaDAO();
        } else if (seleccionadoRelacion) {
            return new UsuarioAsignaturaDAO();
        }
        return null;
    }
    
    private void agregarUsuario(Usuario plantilla) {
        try {
            int id = daoU.insertar(plantilla);
            if (id!=-1) {
                Usuario nuevo = daoU.obtenerPorId(id);
                listaUsuarios.put(nuevo.getCodigo(), nuevo);
            }
        } catch (SQLException ex) {
            System.out.println("Error SQL al insertar");
        }
    }
    
    private void asignarAsignatura(Usuario usuario, Asignatura asignatura) {
        try {
            usuario.getAsignaturas().add(asignatura);
            daoUA.insertar(new UsuarioAsignatura(usuario, asignatura));
        } catch (SQLException ex) {
            System.out.println("Error SQL al insertar");
        }
    }
    
    private void rellenarPaneles(ArrayList<Asignatura> asignaturas) {
        try {
            vEA.getBtn1().setText(asignaturas.get(0).getNombre());
            vEA.getBtn1().setEnabled(true);
            vEA.getBtn2().setText(asignaturas.get(1).getNombre());
            vEA.getBtn2().setEnabled(true);
            vEA.getBtn3().setText(asignaturas.get(2).getNombre());
            vEA.getBtn3().setEnabled(true);
            vEA.getBtn4().setText(asignaturas.get(3).getNombre());
            vEA.getBtn4().setEnabled(true);
            vEA.getBtn5().setText(asignaturas.get(4).getNombre());
            vEA.getBtn5().setEnabled(true);
            vEA.getBtn6().setText(asignaturas.get(5).getNombre());
            vEA.getBtn6().setEnabled(true);
        } catch(IndexOutOfBoundsException ex) {
            
        }
    }
    
    private void btnIniciarSesionActionPerformed() {
        try {
                    vPrincipal.getTxtCodigo().setText("1000");/////////////////////////////////////////
            boolean correcto = daoU.autenticar(
                    vPrincipal.getTxtCodigo().getText(),
                    new String(vPrincipal.getPswClave().getPassword()));
            if (correcto) {
                Usuario usuario = listaUsuarios.get(vPrincipal.getTxtCodigo().getText());
                
                vPrincipal.getTxtCodigo().setText("");
                vPrincipal.getPswClave().setText("");
                if (usuario.getCodigo().equals("0000")) {
                    irDesdeAVentana(vPrincipal, vAdmin);
                    listarEntidad(obtenerDAOActual());
                } else if (usuario.getTipo()==TipoUsuario.ESTUDIANTE) {
                    estudiante = (Estudiante)usuario;
                    irDesdeAVentana(vPrincipal, vEstudiantes);
                } else if (usuario.getTipo()==TipoUsuario.PROFESOR) {
                    profesor = (Profesor)usuario;
                    irDesdeAVentana(vPrincipal, vProfesores);
                }
            } else {
                JOptionPane.showMessageDialog(vPrincipal, "Código o Contraseña incorrectos");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vPrincipal, "Formato de código incorrecto");
        } catch (SQLException ex) {
            System.out.println("Error SQL al autenticar");
        }
    }
    
    private void listarAsignaturasDeUsuario(Usuario usuario, JTable tabla) {
        ArrayList<Asignatura> lista = usuario.getAsignaturas();
        DefaultTableModel md = (DefaultTableModel)tabla.getModel();
        md.setRowCount(0);
        
        for (Asignatura i : lista) {
            Object fila[] = {i.getId(), i, i.getProfesor().getNombre()};
            md.addRow(fila);
        }
    }
    
    private void irDesdeAVentana(JFrame origen, JFrame destino) {
        destino.setVisible(true);
        origen.dispose();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==vPrincipal.getBtnIniciarSesion()) {
            btnIniciarSesionActionPerformed();
        }
        
        if (e.getSource()==vAdmin.getBtnMenuUsuario()) {
            btnMenuUsuarioAccionar();
        }
        if (e.getSource()==vAdmin.getBtnMenuAsignatura()) {
            btnMenuAsignaturaAccionar();
        }
        if (e.getSource()==vAdmin.getBtnMenuUsuarioAsignatura()) {
            btnMenuUsuarioAsignaturaAccionar();
        }
        if (e.getSource()==vAdmin.getBtnListar()) {
            btnListarAccionar();
        }
        if (e.getSource()==vAdmin.getBtnMenuInsertar()) {
            btnMenuInsertarAccionar();
        }
        if (e.getSource()==vAdmin.getBtnMenuActualizar()) {
            btnMenuActualizarAccionar();
        }
        if (e.getSource()==vAdmin.getBtnEliminar()) {
            btnEliminarAccionar();
        }
        if (e.getSource()==vAdmin.getBtnConfirmar()) {
            btnConfirmarAccionar();
        }
        if (e.getSource()==vAdmin.getBtnRegistrar()) {
            btnRegistrarAccionar();
        }
        if (e.getSource()==vAdmin.getBtnHecho()) {
            btnHechoAccionar();
        }
        if (e.getSource()==vAdmin.getBtnVolver()) {
            irDesdeAVentana(vAdmin, vPrincipal);
            try {
                HashMap<Integer, Asignatura> listaAsignaturas = daoA.cargarAsignaturas();
                daoT.cargarRelaciones(listaAsignaturas);
                listaUsuarios = daoU.cargarUsuarios();
            } catch (SQLException ex) {
                
            }
        }
        
        if (e.getSource()==vEstudiantes.getBtnAsignaturas()) {
            vEA.setVisible(true);
            vEC.setVisible(false);
            vET.setVisible(false);
            rellenarPaneles(estudiante.getAsignaturas());
            listarAsignaturasDeUsuario(estudiante, vEA.getTblTabla());
        }
        if (e.getSource()==vEstudiantes.getBtnCalificaciones()) {
            vEC.setVisible(true);
            vEA.setVisible(false);
            vET.setVisible(false);
        }
        if (e.getSource()==vEstudiantes.getBtnTalleres()) {
            vET.setVisible(true);
            vEA.setVisible(false);
            vEC.setVisible(false);
        }
        if (e.getSource()==vEstudiantes.getBtnCerrarSesion()) {
            irDesdeAVentana(vEstudiantes, vPrincipal);
        }
        
        if (e.getSource()==vEA.getBtn1()) {
            
        }
        if (e.getSource()==vEA.getBtn2()) {
            
        }
        if (e.getSource()==vEA.getBtn3()) {
            
        }
        if (e.getSource()==vEA.getBtn4()) {
            
        }
        if (e.getSource()==vEA.getBtn5()) {
            
        }
        if (e.getSource()==vEA.getBtn6()) {
            
        }
        if (e.getSource()==vEA.getBtnSeleccionar()) {
            
        }
    }
    
    private boolean confirmarSeleccionado(Registrable registrable) {
        boolean noNulo = (registrable!=null);
        if (noNulo) {
            vAdmin.getBtnEliminar().setEnabled(true);
            vAdmin.getBtnHecho().setEnabled(true);
        }
        return noNulo;
    }
    private void btnConfirmarAccionar() {
        confirmarSeleccionado((Registrable)obtenerSeleccionTabla(1));
    }             
    private void listarEntidad(DAO dao) {
        cambiarVariables(null);
        vAdmin.getTblTabla().setVisible(true);
        vAdmin.getSpnTabla().setVisible(true);
        vAdmin.getPlRegistros().setVisible(false);
        vAdmin.getBtnConfirmar().setVisible(true);
        try {
            ArrayList<Registrable> lista = dao.listar();
            DefaultTableModel md = (DefaultTableModel) vAdmin.getTblTabla().getModel();
            md.setRowCount(0);
            
            if (seleccionadoUsuario) {
                lista.removeFirst();
            }
            for (Registrable i : lista) {
                Object fila[] = i.obtenerCampos();
                md.addRow(fila);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar con "+dao.getClass());
        }
    }
    private Object obtenerSeleccionTabla(int columna) {
        int fila = vAdmin.getTblTabla().getSelectedRow();
        if (fila==-1) {
            return null;
        }
        return vAdmin.getTblTabla().getModel().getValueAt(fila, columna);
    }
    private void btnMenuInsertarAccionar() {
        cambiarVariables((Registrable)obtenerSeleccionTabla(1));
        
        vAdmin.getTblTabla().setVisible(false);
        vAdmin.getSpnTabla().setVisible(false);
        vAdmin.getPlRegistros().setVisible(true);
        
        vAdmin.getBtnConfirmar().setVisible(false);
        vAdmin.getBtnHecho().setVisible(false);
        vAdmin.getBtnRegistrar().setVisible(true);
    }
    private void btnRegistrarAccionar() {
        DAO dao = obtenerDAOActual();
        try {
            if (dao instanceof UsuarioDAO) {
                Usuario dto = null;
                if (vAdmin.getCbbCampo3().getSelectedItem().equals(TipoUsuario.ESTUDIANTE)) {
                    dto = new Estudiante();
                } else if (vAdmin.getCbbCampo3().getSelectedItem().equals(TipoUsuario.PROFESOR)) {
                    dto = new Profesor();
                }
                if (dto!=null) {
                    dto.setNombre(vAdmin.getTxtCampo1().getText());
                    dto.setClave(vAdmin.getTxtCampo2().getText());
                    dao.insertar(dto);
                }
            }
            if (dao instanceof AsignaturaDAO) {
                try {
                    Usuario u = daoU.obtenerPorCodigo(Integer.parseInt(vAdmin.getTxtCampo2().getText()));
                    if (u!=null && u instanceof Profesor) {
                        Asignatura dto = new Asignatura(vAdmin.getTxtCampo1().getText(), (Profesor)u);
                        dao.insertar(dto);
                    } else {
                        System.out.println("El usuario con código "+vAdmin.getTxtCampo2().getText()+" no es profesor");
                    }
                } catch(NumberFormatException ex) {
                    System.out.println("El usuario con código "+vAdmin.getTxtCampo2().getText()+" no es profesor");
                }
            }
            if (dao instanceof UsuarioAsignaturaDAO) {
                try {
                    Usuario u = daoU.obtenerPorCodigo(Integer.parseInt(vAdmin.getTxtCampo1().getText()));
                    Asignatura a = daoA.obtenerPorId(Integer.parseInt(vAdmin.getTxtCampo2().getText()));
                    if (u!=null && a!=null) {
                        UsuarioAsignatura dto = new UsuarioAsignatura(u, a);
                        dao.insertar(dto);
                    } else {
                        System.out.println("Los códigos no existen");
                    }
                } catch(NumberFormatException ex) {
                    System.out.println("Los códigos no existen");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al insertar con "+dao.getClass());
        }
        listarEntidad(dao);
    }
    private void btnEliminarAccionar() {
        try{
            Registrable dto = (Registrable)obtenerSeleccionTabla(1);
            if (dto!=null) {
                DAO dao = obtenerDAOActual();
                try {
                    dao.eliminar(dto);
                } catch (SQLException ex) {
                    System.out.println("Error al eliminar con "+dao.getClass());
                }
                listarEntidad(dao);
            }
            cambiarVariables(dto);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("No hay fila seleccionada");
        }
    }
    private void btnMenuActualizarAccionar() {
        Registrable dto = (Registrable)obtenerSeleccionTabla(1);
        cambiarVariables(dto);
        if (confirmarSeleccionado(dto)) {
            aux = dto;
        }
        
        vAdmin.getBtnConfirmar().setVisible(true);
        vAdmin.getTblTabla().setVisible(true);
        vAdmin.getSpnTabla().setVisible(true);
        vAdmin.getPlRegistros().setVisible(true);
        
        TitledBorder tb = (TitledBorder)vAdmin.getPlRegistros().getBorder();
        tb.setTitle("Seleccione el registro de la tabla para actualizarlo");
        
        vAdmin.getBtnRegistrar().setVisible(false);
        vAdmin.getLblCampo3Cbb().setVisible(false);
        vAdmin.getCbbCampo3().setVisible(false);
        vAdmin.getBtnHecho().setVisible(true);
    }
    private void btnHechoAccionar() {
        DAO dao = obtenerDAOActual();
        try{
            Registrable registrable = aux;
            if (registrable==null) {
                registrable = (Registrable)obtenerSeleccionTabla(1);
            }
            if (registrable!=null) {
                if (dao instanceof UsuarioDAO) {
                    Usuario dto = (Usuario)registrable;
                    dto.setNombre(vAdmin.getTxtCampo1().getText());
                    dto.setClave(vAdmin.getTxtCampo2().getText());
                    dao.actualizar(dto);
                }
                if (dao instanceof AsignaturaDAO) {
                    Asignatura dto = (Asignatura)registrable;
                    dto.setNombre(vAdmin.getTxtCampo1().getText());
                    try {
                        Usuario u = daoU.obtenerPorCodigo(Integer.parseInt(vAdmin.getTxtCampo2().getText()));
                        if (u!=null && u instanceof Profesor) {
                            dto.setProfesor((Profesor)u);
                            dao.actualizar(dto);
                        } else {
                            System.out.println("El usuario con código "+vAdmin.getTxtCampo2().getText()+" no es profesor");
                        }
                    } catch(NumberFormatException ex) {
                        System.out.println("El formato del código es inválido");
                    }
                }
                if (dao instanceof UsuarioAsignaturaDAO) {
                    UsuarioAsignatura dto = (UsuarioAsignatura)registrable;
                    try {
                        Usuario u = daoU.obtenerPorCodigo(Integer.parseInt(vAdmin.getTxtCampo1().getText()));
                        Asignatura a = daoA.obtenerPorId(Integer.parseInt(vAdmin.getTxtCampo2().getText()));
                        if (u!=null && a!=null) {
                            dto.setUsuario(u);
                            dto.setAsignatura(a);
                            dao.actualizar(dto);
                        } else {
                            System.out.println("Los códigos no existen");
                        }
                    } catch(NumberFormatException ex) {
                        System.out.println("El formato de los códigos es inválido");
                    }
                }
                listarEntidad(dao);
            }
        } catch (SQLException ex) {
            System.out.println("Error al actualizar con "+dao.getClass());
        }
    }
    private void btnListarAccionar() {
        vAdmin.getBtnConfirmar().setVisible(false);
        listarEntidad(obtenerDAOActual());
    }
    private void cambiarVariables(Registrable registrable) {
        vAdmin.getBtnEliminar().setEnabled(false);
        vAdmin.getBtnHecho().setEnabled(false);
        
        vAdmin.getTxtCampo1().setText("");
        vAdmin.getTxtCampo2().setText("");
        
        TitledBorder tb = (TitledBorder)vAdmin.getPlRegistros().getBorder();
        DefaultTableModel md = (DefaultTableModel) vAdmin.getTblTabla().getModel();
        if(seleccionadoUsuario) {
            vAdmin.getLblCampo1().setVisible(true);
            vAdmin.getLblCampo2().setVisible(true);
            vAdmin.getLblCampo3Cbb().setVisible(true);
            vAdmin.getCbbCampo3().setVisible(true);
            
            tb.setTitle("Registrar Usuario");
            vAdmin.getLblCampo1().setText("Nombre del Usuario:");
            vAdmin.getLblCampo2().setText("Clave:");
            vAdmin.getCbbCampo3().setModel(new DefaultComboBoxModel(TipoUsuario.values()));
            if (registrable!=null) {
                Usuario usuario = (Usuario)registrable;
                vAdmin.getTxtCampo1().setText(usuario.getNombre());
                vAdmin.getTxtCampo2().setText(usuario.getClave());
                vAdmin.getCbbCampo3().setSelectedItem(usuario.getTipo());
            }
            md.setColumnIdentifiers(new Object[]{"Código", "Nombre", "Clave", "Tipo"});
        }
        if (seleccionadoAsignatura) {
            vAdmin.getLblCampo3Cbb().setVisible(false);
            vAdmin.getCbbCampo3().setVisible(false);
            vAdmin.getLblCampo1().setVisible(true);
            vAdmin.getLblCampo2().setVisible(true);
            
            tb.setTitle("Registrar Asignatura");
            vAdmin.getLblCampo1().setText("Nombre de la Asignatura:");
            vAdmin.getLblCampo2().setText("Código del Profesor:");
            if (registrable!=null) {
                Asignatura asignatura = (Asignatura)registrable;
                vAdmin.getTxtCampo1().setText(asignatura.getNombre());
                if (asignatura.getProfesor()!=null) {
                    vAdmin.getTxtCampo2().setText(asignatura.getProfesor().getCodigo());
                }
            }
            md.setColumnIdentifiers(new Object[]{"ID", "Nombre", "Código Profesor", "Nombre Profesor"});
        }
        if (seleccionadoRelacion) {
            vAdmin.getLblCampo3Cbb().setVisible(false);
            vAdmin.getCbbCampo3().setVisible(false);
            vAdmin.getLblCampo1().setVisible(true);
            vAdmin.getLblCampo2().setVisible(true);
            
            tb.setTitle("Registrar relación");
            vAdmin.getLblCampo1().setText("Código del Usuario");
            vAdmin.getLblCampo2().setText("ID de la Asignatura");
            if (registrable!=null) {
                UsuarioAsignatura ua = (UsuarioAsignatura)registrable;
                vAdmin.getTxtCampo1().setText(ua.getUsuario().getCodigo());
                vAdmin.getTxtCampo2().setText(""+ua.getAsignatura().getId());
            }
            md.setColumnIdentifiers(new Object[]{"Código Usuario", "Nombre Usuario", "ID Asignatura", "Nombre Asignatura"});
        }
        vAdmin.repaint();
    }
    private void btnMenuUsuarioAccionar() {
        seleccionadoUsuario = true;
        seleccionadoAsignatura = false;
        seleccionadoRelacion = false;
        
        cambiarVariables(null);
        listarEntidad(obtenerDAOActual());
    }
    private void btnMenuAsignaturaAccionar() {
        seleccionadoAsignatura = true;
        seleccionadoUsuario = false;
        seleccionadoRelacion = false;
        
        cambiarVariables(null);
        listarEntidad(obtenerDAOActual());
    }
    private void btnMenuUsuarioAsignaturaAccionar() {
        seleccionadoRelacion = true;
        seleccionadoUsuario = false;
        seleccionadoAsignatura = false;
        
        cambiarVariables(null);
        listarEntidad(obtenerDAOActual());
    }
    

    /*private void cbbECCalificacionesActionPerformed() {
        String msj = "Calificaciones en ";
        msj = msj + vEC.getCbbCalificaciones().getSelectedItem().toString();
        vEC.getLblCalificaciones().setText(msj);
    }*/

}
