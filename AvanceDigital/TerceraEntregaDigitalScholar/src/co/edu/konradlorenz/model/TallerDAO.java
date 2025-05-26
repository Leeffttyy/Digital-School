package co.edu.konradlorenz.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TallerDAO implements DAO {
    private static final String SQL_INSERTAR = "INSERT INTO Taller (nombre_t, descripcion, plazo, asignatura_id) VALUES (?, ?, ?, ?)";
    private static final String SQL_LISTAR = "SELECT * FROM Taller";
    private static final String SQL_ACTUALIZAR = "UPDATE Taller SET nombre_t=?, descripcion=?, plazo=? WHERE id_t=?";
    private static final String SQL_ELIMINAR = "DELETE FROM Taller WHERE id_t=?";
    
    private static final String SQL_OBTENER_POR_ID = "SELECT * FROM Taller WHERE id_t=?";
    
    private void guardarRespuestas(Taller taller) throws SQLException {
        String sql = "INSERT INTO RespuestaTaller (estudiante_id, taller_id, respuesta) VALUES (?, ?, ?)";
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(sql)) {
            for (Map.Entry<Estudiante, String> entry : taller.getRespuestas().entrySet()) {
                ps.setInt(1, taller.getId());
                ps.setInt(2, entry.getKey().getId());
                ps.setString(3, entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void guardarNotas(Taller taller) throws SQLException {
        String sql = "INSERT INTO NotaTaller (estudiante_id,taller_id,  calificacion) VALUES (?, ?, ?)";
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(sql)) {
            for (Map.Entry<Estudiante, Integer> entry : taller.getNotas().entrySet()) {
                ps.setInt(1, taller.getId());
                ps.setInt(2, entry.getKey().getId());
                ps.setInt(3, entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
    
    public void cargarRelaciones(HashMap<Integer, Asignatura> asignaturas) throws SQLException {
        for (Asignatura asignatura : asignaturas.values()) {
            for (Taller taller : asignatura.getTalleres()) {
                HashMap<Estudiante, String> respuestas = new HashMap();
                HashMap<Estudiante, Integer> calificaciones = new HashMap();

                String sqlResp = "SELECT estudiante_id, respuesta FROM RespuestaTaller WHERE taller_id=?";
                try (PreparedStatement ps = CNX.getCnn().prepareStatement(sqlResp)) {
                    ps.setInt(1, taller.getId());
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Estudiante e = (Estudiante)new UsuarioDAO().obtenerPorId(rs.getInt("estudiante_id"));
                        respuestas.put(e, rs.getString("respuesta"));
                    }
                }

                String sqlCal = "SELECT estudiante_id, calificacion FROM NotaTaller WHERE taller_id = ?";
                try (PreparedStatement ps = CNX.getCnn().prepareStatement(sqlCal)) {
                    ps.setInt(1, taller.getId());
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Estudiante e = (Estudiante)new UsuarioDAO().obtenerPorId(rs.getInt("estudiante_id"));
                        calificaciones.put(e, rs.getInt("calificacion"));
                    }
                }

                taller.setRespuestas(respuestas);
                taller.setNotas(calificaciones);
                asignatura.getTalleres().add(taller);
            }
        }
    }
    
    public ArrayList<Taller> listarPorGrupo(int grupoId) throws SQLException {
        ArrayList<Taller> lista = new ArrayList();
        String sql = "SELECT * FROM Taller WHERE grupo_id = ?";
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(sql)) {
            ps.setInt(1, grupoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(recrearTaller(rs));
            }
        }
        return lista;
    }
    
    private Taller recrearTaller(ResultSet rs) {
        Taller taller = null;
        try {
            AsignaturaDAO daoU = new AsignaturaDAO();
            Asignatura asignatura = daoU.obtenerPorId(rs.getInt("asignatura_id"));
            taller = new Taller(
                    rs.getInt("id_t"),
                    rs.getString("nombre_t"),
                    rs.getString("descripcion"),
                    rs.getTimestamp("plazo").toLocalDateTime(),
                    asignatura);
        } catch (SQLException ex) {
            System.out.println("Error al recrear taller");
        }
        return taller;
    }
    
    public Taller obtenerTallerPorId(int id) throws SQLException {
        Taller taller = null;
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_OBTENER_POR_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                taller = recrearTaller(rs);
            }
        } catch (SQLException ex) {
            System.out.println("Taller con id "+id+" no encontado");
        }
        return taller;
    }
    
    
    @Override
    public int insertar(Registrable registrable) throws SQLException {
        if (registrable instanceof Taller) {
            try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {
                Taller taller = (Taller)registrable;
                ps.setString(1, taller.getNombre());
                ps.setString(2, taller.getDescripcion());
                ps.setTimestamp(3, Timestamp.valueOf(taller.getPlazo()));
                ps.setInt(4, taller.getAsignatura().getId());
                ps.executeUpdate();
                
                guardarRespuestas(taller);
                guardarNotas(taller);
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    @Override
    public ArrayList<Registrable> listar() {
        ArrayList<Registrable> talleres = new ArrayList();
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_LISTAR)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                talleres.add(recrearTaller(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar taller");
            ex.printStackTrace();
        }
        return talleres;
    }

    @Override
    public void actualizar(Registrable registrable) {
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_ACTUALIZAR)) {
            Taller taller = (Taller)registrable;
            ps.setString(1, taller.getNombre());
            ps.setString(2, taller.getDescripcion());
            ps.setTimestamp(3, Timestamp.valueOf(taller.getPlazo()));
            ps.setInt(4, taller.getId());
            ps.executeUpdate();
            
            guardarRespuestas(taller);//
            guardarNotas(taller);//
        } catch (SQLException ex) {
            System.out.println("Error al actualizar taller");
            ex.printStackTrace();
        }
    }

    @Override
    public void eliminar(Registrable registrable) {
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_ELIMINAR)) {
            Taller taller = (Taller)registrable;
            ps.setInt(1, taller.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar taller");
            ex.printStackTrace();
        }
    }
        
}
