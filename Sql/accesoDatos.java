package Sql;
import java.sql.*;
import javax.swing.JOptionPane;



public class accesoDatos {
    
    String url = "jdbc:sqlite:/C:/Users/Tango/Documents/NetBeansProjects/GestiosVerduleria/src/verduleriaFinal.db";
    Connection con;
    
    
    public Connection getConnection() throws SQLException{
        try {
            con = DriverManager.getConnection(url);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con bd");
        }
        if(con == null){
            JOptionPane.showMessageDialog(null, "la conexion devuelve nulo");
            
        }
        return con;
        
    }
    
    public void desconectar() throws SQLException{
        con.close();
       
    }
    
    
    public void consultarTabla(){
        
    }
    
    
    
    
    
}
