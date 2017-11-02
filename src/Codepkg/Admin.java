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
       ui.updateGroups();
//       admin.showGroups();               
        java.awt.EventQueue.invokeLater(() -> {  //anonymous inner class creation can be turned in to lambda (->) expression
            ui.setVisible(true);
       });
    }
//    
//    public void showGroups() throws Exception{
//        ResultSet rs = db.showCreatedGroups(ui.selectedGroup);
//        DefaultTableModel model;
//        model = (DefaultTableModel) ui.jGroups.getModel();
//        
//        while (model.getRowCount() > 0) {
//            for (int i = 0; i < model.getRowCount(); i++) {
//                model.removeRow(i);
//            }
//        }
//        
//        for (int i = 0; rs.next(); i++) {
//            model.addRow(new Object[]{rs.getString("name")});
//        }
//
//        db.st.close();
//        db.con.close();
//    }
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
