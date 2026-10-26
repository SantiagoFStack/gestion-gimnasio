package dao;

import conexion.Conexion;
import java.sql.*;

public class ConsultaDAO {

    public void imprimirTabla(String tabla) throws SQLException {
        String sql = "SELECT * FROM `" + tabla + "`";
        try (Connection con = Conexion.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ResultSetMetaData md = rs.getMetaData();
            int columnas = md.getColumnCount();

            int filas = 0;
            while (rs.next()) {
                filas++;
                StringBuilder fila = new StringBuilder();
                for (int i = 1; i <= columnas; i++) {
                    fila.append(md.getColumnName(i)).append("=")
                        .append(rs.getString(i)).append("  ");
                }
                System.out.println(fila);
            }
            System.out.println("Total de registros: " + filas);
        }
    }
}
