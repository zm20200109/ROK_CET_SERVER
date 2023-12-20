/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbb;

/**
 *
 * @author Acer
 */
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Konekcija {
    
    private Connection connection;
    private static Konekcija instance;
    
    private Konekcija(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ps_cet_klk","root","");
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(Konekcija.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    public static Konekcija getInstance(){
        if(instance == null){
            instance = new Konekcija();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

  
    
}
