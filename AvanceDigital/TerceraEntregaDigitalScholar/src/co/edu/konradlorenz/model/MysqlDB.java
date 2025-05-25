package co.edu.konradlorenz.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlDB {
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String Url = "jdbc:mysql://localhost:3306/gestion_educativa";
    private String user = "root";
    private String pss = "";
    private Connection cnn = null;
    private static MysqlDB cnx = null;

    private MysqlDB() {
        try {
            Class.forName(driver);
            cnn = DriverManager.getConnection(Url, user, pss);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error de Driver");
        } catch (SQLException ex) {
            System.out.println("Error al abrir la conexión");
        }
    }

    public static synchronized MysqlDB getCnx() {
        if (cnx == null) {
            cnx = new MysqlDB();
        }
        return cnx;
    }

    public Connection getCnn() {
        return cnn;
    }

    public void cerrarCnn() {
        try {
            if (cnn != null) {
                cnn.close();
                System.out.println("Se cierra la conexion");
            }
        } catch (SQLException ex) {
            System.out.println("Error al cerrar la conexión");
        }
    }

}
