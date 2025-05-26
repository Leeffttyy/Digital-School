package co.edu.konradlorenz.model;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DAO {
    public static final MysqlDB CNX = MysqlDB.getCnx();
    
    public abstract int insertar(Registrable registrable) throws SQLException;
    public abstract ArrayList<Registrable> listar() throws SQLException;
    public abstract void actualizar(Registrable registrable) throws SQLException;
    public abstract void eliminar(Registrable registrable) throws SQLException;
}
