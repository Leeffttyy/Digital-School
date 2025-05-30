package co.edu.konradlorenz.model;

import java.sql.*;
import java.util.ArrayList;

public class AsignaturaDAO implements DAO {
    private static final String SQL_INSERTAR = "INSERT INTO Asignatura (nombre_a, profesor_id) VALUES (?, ?)";
    private static final String SQL_LISTAR = "SELECT * FROM Asignatura";
    private static final String SQL_ACTUALIZAR = "UPDATE Asignatura SET nombre_a=?, profesor_id=? WHERE id_a=?";
    private static final String SQL_ELIMINAR = "DELETE FROM Asignatura WHERE id_a=?";
    
    private static final String SQL_OBTENER_POR_ID = "SELECT * FROM Asignatura WHERE id_a=?";
    
    @Override
    public void insertar(Registrable registrable){
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_INSERTAR)) {
            Asignatura asignatura = (Asignatura)registrable;
            ps.setString(1, asignatura.getNombre());
            ps.setInt(2, asignatura.getProfesor().getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al insertar");
            ex.printStackTrace();
        }
    }
    
    @Override
    public ArrayList<Registrable> listar() {
        ArrayList<Registrable> asignaturas = new ArrayList<>();
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_LISTAR)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                asignaturas.add(recrearAsignatura(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar");
            ex.printStackTrace();
        }
        return asignaturas;
    }

    @Override
    public void actualizar(Registrable registrable) {
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_ACTUALIZAR)) {
            Asignatura asignatura = (Asignatura)registrable;
            ps.setString(1, asignatura.getNombre());
            ps.setInt(2, asignatura.getProfesor().getId());
            ps.setInt(3, asignatura.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar");
            ex.printStackTrace();
        }
    }

    @Override
    public void eliminar(Registrable registrable) {
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_ELIMINAR)) {
            Asignatura asignatura = (Asignatura)registrable;
            ps.setInt(1, asignatura.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar");
            ex.printStackTrace();
        }
    }

    private Asignatura recrearAsignatura(ResultSet rs) {
        Asignatura asignatura = null;
        try {
            int profesorId = rs.getInt("profesor_id");
            UsuarioDAO daoU = new UsuarioDAO();
            Profesor profesor = (Profesor)daoU.obtenerPorId(profesorId);
            asignatura = new Asignatura(
                    rs.getInt("id_a"),
                    rs.getString("nombre_a"),
                    profesor);
        } catch (SQLException ex) {
            System.out.println("Error al recrear asignatura");
        }
        return asignatura;
    }
    
    public Asignatura obtenerPorId(int id) {
        try (PreparedStatement stmt = CNX.getCnn().prepareStatement(SQL_OBTENER_POR_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return recrearAsignatura(rs);
            }
        } catch (SQLException ex) {
            System.out.println("Asignatura con id " + id + "no encontrada");
            ex.printStackTrace();
        }
        return null;
    }
}