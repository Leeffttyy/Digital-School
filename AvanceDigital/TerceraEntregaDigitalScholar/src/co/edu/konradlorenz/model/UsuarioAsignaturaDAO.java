package co.edu.konradlorenz.model;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioAsignaturaDAO implements DAO {
    private static final String SQL_INSERTAR = "INSERT IGNORE INTO Usuario_Asignatura (id_u, id_a) VALUES (?, ?)";
    private static final String SQL_LISTAR = "SELECT * FROM Usuario_Asignatura";
    private static final String SQL_ELIMINAR = "DELETE FROM Usuario_Asignatura WHERE id_u=? AND id_a=?";
    
    private UsuarioDAO daoU = new UsuarioDAO();
    private AsignaturaDAO daoA = new AsignaturaDAO();
    
    @Override
    public void insertar(Registrable registrable) {
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_INSERTAR)) {
            UsuarioAsignatura ua = (UsuarioAsignatura)registrable;
            ps.setInt(1, ua.getUsuario().getId());
            ps.setInt(2, ua.getAsignatura().getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al insertar");
            ex.printStackTrace();
        }
    }

    @Override
    public ArrayList<Registrable> listar() {
        ArrayList<Registrable> relaciones = new ArrayList();
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_LISTAR)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UsuarioAsignatura ua = new UsuarioAsignatura(
                        daoU.obtenerPorId(rs.getInt("id_u")),
                        daoA.obtenerPorId(rs.getInt("id_a")));
                relaciones.add(ua);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar");
            ex.printStackTrace();
        }
        return relaciones;
    }

    @Override
    public void actualizar(Registrable registrable) {
        eliminar(registrable);
        insertar(registrable);
    }
    
    @Override
    public void eliminar(Registrable registrable) {
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_ELIMINAR)) {
            UsuarioAsignatura ua = (UsuarioAsignatura)registrable;
            ps.setInt(1, ua.getAuxU().getId());
            ps.setInt(2, ua.getAuxA().getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar");
            ex.printStackTrace();
        }
    }   

}