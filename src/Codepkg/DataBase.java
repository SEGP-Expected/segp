/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Codepkg;

/**
 *
 * @author Mr.Faizan Sh
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {

    String url;
    String name;
    String pass;
    Connection con;
    Statement st;

    public DataBase() throws SQLException {
        url = "jdbc:postgresql://localhost:5432/tgms";
        name = "postgres";
        pass = "123456789";
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error!!! I did not find the class");
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            con = DriverManager.getConnection(url, name, pass);
        } catch (SQLException ex) {
            System.out.println("Error with connection... Check Constructor of database class");
//            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        st = con.createStatement();
        
    }
    
    public ResultSet showCreatedGroups() throws SQLException {
        String query = "SELECT name FROM groups";
        ResultSet rs = st.executeQuery(query);
        return rs;
    }
    public Student[] getGroupStudents(String groupName){
        String query = "SELECT * from Students where name=" + groupName;
        ArrayList<Student> ar = new ArrayList<>(0); 
        try{
            int i =0;
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                //ar.add(new Student(rs.Int(),,));
            }

        }catch(Exception e){
        
        }
       //Check it ##############################################################
       return null;
        
    }
        
}