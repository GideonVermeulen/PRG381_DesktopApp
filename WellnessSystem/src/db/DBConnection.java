package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import org.apache.derby.iapi.jdbc.JDBC;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */

// 	Handles connecting to the JavaDB database
public class DBConnection {

    public static final String DRIVER = "org.apache.derby.jdc.EmbeddedDriver";
    
    
    // JDBC_URL connects to an apache derby database
    // database name = wellnessDB
    // create = true means that the db will be created if it does not already exist
    public static final String JDBC_URL = "jdbc:derby:wellnessDB;create=true";

    static Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    // connection object
    Connection con;
    
    //constructor
    public DBConnection(){
    }
    
    // mehod to connect to the database
    public void connect() throws ClassNotFoundException{
        try{
            Class.forName(DRIVER);
            this.con = DriverManager.getConnection(JDBC_URL);
            if (this.con != null){
                System.out.println("Database connection successfull");
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
