package Codepkg;


import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class Admin {
    DataBase db;
    static UserInterface ui;
    public Admin() throws Exception {
        db = new DataBase();
    }
       
    public static void main(String[] args) throws Exception{

       Admin admin = new Admin();
       ui = new UserInterface();
       admin.showGroups();
//        String query = "SELECT * FROM groups";
//        ResultSet rs = st.executeQuery(query);
//        rs.next();
//        String named = rs.getString("id");
//        System.out.println(named);
//        st.close();
//        con.close();
               
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ui.setVisible(true);
            }
        });
    }
    
    public void showGroups() throws Exception{
        ResultSet rs = db.showCreatedGroups();
        DefaultTableModel model;
        model = (DefaultTableModel) ui.jGroups.getModel();
        
//        while(rs.next()) {
//            System.out.println(rs.getString("name"));
//        }
       
        for (int i = 0; rs.next(); i++) {
            model.addRow(new Object[]{rs.getString("name")});
        }

//        while(rs.next()){
          //  model.addRow(new Object[]{"dddd"});

//       }

        db.st.close();
        db.con.close();
    }
    public void createGroup() {
      
    }

    public void deleteGroup() {
    }

    public void editGroup() {
    }

    public void loadList() {
    }

    public void view(int no) {
    }

    public void print() {
    }
}
