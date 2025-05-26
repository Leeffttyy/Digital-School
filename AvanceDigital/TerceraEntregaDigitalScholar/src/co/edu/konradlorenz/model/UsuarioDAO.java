package co.edu.konradlorenz.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UsuarioDAO implements DAO {
    private final String SQL_INSERTAR = "INSERT INTO Usuario (tipo, codigo, nombre_u, clave) VALUES (?, ?, ?, ?)";
    private final String SQL_LISTAR = "SELECT * FROM Usuario";
    private final String SQL_ACTUALIZAR = "UPDATE Usuario SET nombre_u=?, clave=? WHERE id_u=?";
    private final String SQL_ELIMINAR = "DELETE FROM Usuario WHERE id_u=?";
    
    private final String SQL_OBTENER_POR_ID = "SELECT * FROM Usuario WHERE id_u=?";
    private  final String SQL_OBTENER_POR_CODIGO = "SELECT * FROM Usuario WHERE codigo=?";
    
    
    public boolean autenticar(String codigo, String clave) throws NumberFormatException, SQLException {
        Usuario usuario = obtenerPorCodigo(Integer.parseInt(codigo));
        if (usuario!=null && usuario.getClave().equals(clave)) {
            return true;
        }
        return false;
    }
    
    public HashMap<String, Usuario> cargarUsuarios() throws SQLException {
        HashMap<String, Usuario> listaUsuarios = new HashMap();
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_LISTAR)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario u = recrearUsuario(rs);
                listaUsuarios.put(u.getCodigo(), u);
            }
        }
        if (listaUsuarios.isEmpty()) {
            return null;
        }
        return listaUsuarios;
    }
    
    public void cargarRelaciones(HashMap<String, Usuario> usuarios) throws SQLException {
        AsignaturaDAO daoA = new AsignaturaDAO();
        for (Usuario usuario : usuarios.values()) {
            String sql = "SELECT id_a FROM Usuario_Asignatura WHERE id_u=?";
            try (PreparedStatement ps = CNX.getCnn().prepareStatement(sql)) {
                ps.setInt(1, usuario.getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Asignatura asignatura = daoA.obtenerPorId(rs.getInt("id_a"));
                    if (usuario instanceof Estudiante) {
                        asignatura.getEstudiantes().add((Estudiante)usuario);
                    }
                    usuario.getAsignaturas().add(asignatura);
                }
            }
        }
    }
    
    @Override
    public int insertar(Registrable registrable) throws SQLException {
        if (registrable instanceof Usuario) {
            Usuario usuario = (Usuario)registrable;
            String sqlSelect = "SELECT contador FROM ContadorTipo WHERE tipo=?";
            String sqlUpdate = "UPDATE ContadorTipo SET contador=? WHERE tipo=?";
            
            int contador;
            try (PreparedStatement psSelect = CNX.getCnn().prepareStatement(sqlSelect)) {
                psSelect.setString(1, usuario.getTipo().toString());
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        contador = rs.getInt("contador");
                    } else {
                        throw new SQLException("Tipo no encontrado en ContadorTipo: " + usuario.getTipo());
                    }
                }
            }
            contador += 1;

            try (PreparedStatement psUpdate = CNX.getCnn().prepareStatement(sqlUpdate)) {
                psUpdate.setInt(1, contador);
                psUpdate.setString(2, usuario.getTipo().toString());
                psUpdate.executeUpdate();
            }

            try (PreparedStatement psInsert = CNX.getCnn().prepareStatement(SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {
                psInsert.setString(1, usuario.getTipo().toString());
                psInsert.setInt(2, contador);
                psInsert.setString(3, usuario.getNombre());
                psInsert.setString(4, usuario.getClave());
                psInsert.executeUpdate();
                
                ResultSet rs = psInsert.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }
    
    @Override
    public ArrayList<Registrable> listar() {
        ArrayList<Registrable> usuarios = new ArrayList();
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_LISTAR)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usuarios.add(recrearUsuario(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar");
            ex.printStackTrace();
        }
        return usuarios;
    }
    
    @Override
    public void actualizar(Registrable registrable) {
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_ACTUALIZAR)) {
            Usuario usuario = (Usuario)registrable;
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getClave());
            ps.setInt(3, usuario.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar");
            ex.printStackTrace();
        }
    }

    @Override
    public void eliminar(Registrable registrable) {
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_ELIMINAR)) {
            Usuario usuario = (Usuario)registrable;
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar");
            ex.printStackTrace();
        }
    }

    private Usuario recrearUsuario(ResultSet rs) {
        Usuario usuario = null;
        try {
            TipoUsuario tipo = TipoUsuario.valueOf(rs.getString("tipo"));
            int codigo = rs.getInt("codigo");
            String nombreU = rs.getString("nombre_u");
            String clave = rs.getString("clave");
            int idU = rs.getInt("id_u");
            
            if (tipo.equals(TipoUsuario.ESTUDIANTE)) {
                usuario = new Estudiante(idU, codigo, nombreU, clave);
            } else if (tipo.equals(TipoUsuario.PROFESOR)) {
                usuario = new Profesor(idU, codigo, nombreU, clave);
            }
        } catch (SQLException ex) {
            System.out.println("Error al recrear usuario");
        }
        return usuario;
    }
    
    public Usuario obtenerPorId(int id) {
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_OBTENER_POR_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return recrearUsuario(rs);
            }
        } catch (SQLException ex) {
            System.out.println("Usuario con id "+id+"no encontrado");
            ex.printStackTrace();
        }
        return null;
    }
    
    public Usuario obtenerPorCodigo(int codigo) throws SQLException {
        try (PreparedStatement ps = CNX.getCnn().prepareStatement(SQL_OBTENER_POR_CODIGO)) {
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return recrearUsuario(rs);
            }
        } catch (SQLException ex) {
            System.out.println("Usuario con código "+codigo+"no encontrado");
            ex.printStackTrace();
        }
        return null;
    }
    
}