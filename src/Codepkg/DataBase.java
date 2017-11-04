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
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.FileOutputStream;
import jdk.nashorn.internal.scripts.JD;

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

        st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    public ResultSet showCreatedGroups(int year) throws SQLException {
        String query = "SELECT * FROM groups where year=" + year + "";
        System.out.println(query);
        ResultSet rs = st.executeQuery(query);
        return rs;
    }

    public ResultSet showTeacherDetail(String groupName) throws SQLException {
        String query = "SELECT * FROM staff where isteaching='" + groupName + "'";
        System.out.println(query);
        ResultSet rs = st.executeQuery(query);
        return rs;
    }

    public ResultSet showStudentsDetail(String groupName) throws SQLException {
        String query = "SELECT * FROM students where ismember='" + groupName + "'";
        System.out.println(query);
        ResultSet rs = st.executeQuery(query);
        return rs;
    }

    public ResultSet showUnGroupedStudents(int year) throws SQLException {
//        String query = "SELECT * FROM students where ismember=null and year='" + year + "'";
        String query = "SELECT * FROM students where year=" + year + " and ismember is NULL";
        System.out.println(query);
        ResultSet rs = st.executeQuery(query);
        return rs;
    }

    public ResultSet showUnGroupedTeacher(int year) throws SQLException {
        String query = "SELECT * FROM staff where isteaching is NULL";
        System.out.println(query);
        ResultSet rs = st.executeQuery(query);
        return rs;
    }

    public ResultSet showGroupedStudents(int year) throws SQLException {
//        String query = "SELECT * FROM students where ismember IS NOT NULL and year='" + year + "'";
        String query = "SELECT * FROM students where year=" + year + " and ismember IS NOT NULL";
        System.out.println(query);
        ResultSet rs = st.executeQuery(query);
        return rs;
    }

    public ResultSet showGroupedTeacher(int year) throws SQLException {
        String query = "SELECT * FROM staff where isteaching IS NOT NULL";
        System.out.println(query);
        ResultSet rs = st.executeQuery(query);
        return rs;
    }

    public ResultSet getSelectedStudent(String UoB) throws SQLException {
        Integer id = Integer.parseInt(UoB);
        String query = "SELECT * FROM students where id=" + id;
        System.out.println(query);
        ResultSet rs = st.executeQuery(query);
        return rs;
    }

    public ResultSet getSelectedTeacher(String id) throws SQLException {
        Integer idt = Integer.parseInt(id);
        String query = "SELECT * FROM staff where id=" + idt;
        System.out.println(query);
        ResultSet rs = st.executeQuery(query);
        return rs;
    }

    void saveTeacher(String id, String teacherName, String department) throws SQLException {

        if (id.equals("")) {
            String query = "insert into staff(name,department) values('" + teacherName + "','" + department + "')";
            System.out.println(query);
            st.executeUpdate(query);
        } else {
            String query = "UPDATE staff set name = '" + teacherName + "', department='" + department + "' where id=" + id;
            System.out.println(query);
            st.executeUpdate(query);
        }

    }

    void saveStudent(String studentID, String studentName, int year) throws Exception {
        if (studentID.equals("")) {
            String query = "insert into students(name,year) values('" + studentName + "','" + year + "')";
            System.out.println(query);
            st.executeUpdate(query);
        } else {
            String query = "UPDATE students set name = '" + studentName + "', year='" + year + "' where id=" + studentID;
            System.out.println(query);
            st.executeUpdate(query);
        }
    }

    void saveGroup(String groupName, String[] sNames, int count, String tid, int year) throws Exception {
        String query = "select * from groups where name='" + groupName + "'";
        ResultSet rs = st.executeQuery(query);
        rs.last();
        int teacherid = Integer.parseInt(tid);
        if (rs.getRow() == 1) {
            System.out.println(query);
            query = "UPDATE groups set istaughtby = " + teacherid + ", members = " + count + " where name='" + groupName + "'";
            st.executeUpdate(query);
        } else {
            query = "Insert into groups(name,members,istaughtby,year) values('" + groupName + "'," + count + "," + tid + "," + year + ")";
            System.out.println(query);
            st.executeUpdate(query);
        }

        int id = Integer.parseInt(tid);
        st.executeUpdate("Update staff set isteaching='" + groupName + "' where id=" + id);

        for (String name : sNames) {
            if (!name.equals("")) {
                int idStu = Integer.parseInt(name);
                st.executeUpdate("Update students set ismember='" + groupName + "' where id=" + idStu);
            }

        }

    }

//    public int getTeacherID(String tName) throws Exception {
//        ResultSet rs1 = st.executeQuery("select id from staff where name='" + tName + "'");
//        rs1.next();
//        int id = rs1.getInt("id");
//        return id;
//        
//    }
    int checkException() throws SQLException {
        String faltu = "select count(members) from groups where members < 4";
        ResultSet rs = st.executeQuery(faltu);
        rs.next();
        int i = Integer.parseInt(rs.getString("count"));
        return i;

    }

    void unAllocateStudentsfromGroup(String gName) throws SQLException {
        st.executeUpdate("Update students set ismember=null where ismember='" + gName + "'");
    }

    void unAllocateAstudent(int id) throws Exception {
        st.executeUpdate("Update students set ismember = null where id=" + id);
    }

    void unAllocateTeacher(String groupName) throws SQLException {
        st.executeUpdate("Update staff set isteaching=null where isteaching='" + groupName + "'");
    }

    void deleteGroup(String groupName) throws SQLException {
        //To change body of generated methods, choose Tools | Templates.
        st.executeUpdate("delete from groups where name='" + groupName + "'");
    }

    void printStudents() throws SQLException, FileNotFoundException, DocumentException {
        ResultSet rs = st.executeQuery("select * from students");

        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream("Students.pdf"));
        doc.open();
        PdfPTable table = new PdfPTable(4);

        PdfPCell cell1 = new PdfPCell(new Paragraph("ID"));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell cell2 = new PdfPCell(new Paragraph("Name"));
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell cell3 = new PdfPCell(new Paragraph("Year"));
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell cell4 = new PdfPCell(new Paragraph("Group"));
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setBackgroundColor(BaseColor.DARK_GRAY);

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        rs.next();
        do {
            table.addCell(rs.getInt("id") + "");
            table.addCell(rs.getString("name"));
            table.addCell(rs.getInt("year") + "");
            table.addCell(rs.getString("ismember"));
        } while (rs.next());
        doc.add(table);

        doc.close();
    }

    void printGroups() throws SQLException, FileNotFoundException, DocumentException {
        ResultSet rs = st.executeQuery("select * from groups");

        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream("Groups.pdf"));
        doc.open();
        PdfPTable table = new PdfPTable(4);

        PdfPCell cell1 = new PdfPCell(new Paragraph("Name"));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell cell2 = new PdfPCell(new Paragraph("Members"));
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell cell3 = new PdfPCell(new Paragraph("isTaughtBy"));
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell cell4 = new PdfPCell(new Paragraph("Year"));
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setBackgroundColor(BaseColor.DARK_GRAY);

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        rs.next();
        do {
            table.addCell(rs.getString("name"));
            table.addCell(rs.getString("members"));
            table.addCell(rs.getString("istaughtby"));
            table.addCell(rs.getInt("year") + "");
        } while (rs.next());
        doc.add(table);
        doc.close();
    }

    void printTeachers() throws SQLException, FileNotFoundException, DocumentException {
        ResultSet rs = st.executeQuery("select * from staff");

        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream("Teachers.pdf"));
        doc.open();
        PdfPTable table = new PdfPTable(4);

        PdfPCell cell1 = new PdfPCell(new Paragraph("ID"));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell cell2 = new PdfPCell(new Paragraph("Name"));
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell cell3 = new PdfPCell(new Paragraph("Department"));
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell cell4 = new PdfPCell(new Paragraph("is_Teaching"));
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setBackgroundColor(BaseColor.DARK_GRAY);

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        rs.next();
        do {
            table.addCell(rs.getInt("id") + "");
            table.addCell(rs.getString("name"));
            table.addCell(rs.getString("department") + "");
            table.addCell(rs.getString("isteaching"));
        } while (rs.next());
        doc.add(table);
        doc.close();
    }

    void printDatabase() throws SQLException, FileNotFoundException, DocumentException {
        ResultSet rs = st.executeQuery("select * from groups");

        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream("Database.pdf"));
        doc.open();
        doc.add(new Paragraph("Groups"));
        PdfPTable Groupstable = new PdfPTable(4);

        PdfPCell cell1 = new PdfPCell(new Paragraph("Name"));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell cell2 = new PdfPCell(new Paragraph("Members"));
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell cell3 = new PdfPCell(new Paragraph("isTaughtBy"));
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell cell4 = new PdfPCell(new Paragraph("Year"));
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setBackgroundColor(BaseColor.DARK_GRAY);

        Groupstable.addCell(cell1);
        Groupstable.addCell(cell2);
        Groupstable.addCell(cell3);
        Groupstable.addCell(cell4);
        rs.next();
        do {
            Groupstable.addCell(rs.getString("name"));
            Groupstable.addCell(rs.getString("members"));
            Groupstable.addCell(rs.getString("istaughtby"));
            Groupstable.addCell(rs.getInt("year") + "");
        } while (rs.next());
        doc.add(Groupstable);
        // NOW STARTING STUDENTS
        doc.add(new Paragraph("Students"));
        doc.add(Chunk.NEWLINE);

        rs = st.executeQuery("select * from students");

        PdfPTable Studentstable = new PdfPTable(4);

        PdfPCell scell1 = new PdfPCell(new Paragraph("ID"));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell scell2 = new PdfPCell(new Paragraph("Name"));
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell scell3 = new PdfPCell(new Paragraph("Year"));
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell scell4 = new PdfPCell(new Paragraph("Group"));
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setBackgroundColor(BaseColor.DARK_GRAY);

        Studentstable.addCell(scell1);
        Studentstable.addCell(scell2);
        Studentstable.addCell(scell3);
        Studentstable.addCell(scell4);
        rs.next();
        do {
            Studentstable.addCell(rs.getInt("id") + "");
            Studentstable.addCell(rs.getString("name"));
            Studentstable.addCell(rs.getInt("year") + "");
            Studentstable.addCell(rs.getString("ismember"));
        } while (rs.next());
        doc.add(Studentstable);

        //NOW STARTING PRINTInG TEACHERS
        doc.add(new Paragraph("Teachers"));
        doc.add(Chunk.NEWLINE);

        rs = st.executeQuery("select * from staff");

        PdfPTable Teacherstable = new PdfPTable(4);

        PdfPCell tcell1 = new PdfPCell(new Paragraph("ID"));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell tcell2 = new PdfPCell(new Paragraph("Name"));
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell tcell3 = new PdfPCell(new Paragraph("Department"));
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setBackgroundColor(BaseColor.DARK_GRAY);

        PdfPCell tcell4 = new PdfPCell(new Paragraph("is_Teaching"));
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setBackgroundColor(BaseColor.DARK_GRAY);

        Teacherstable.addCell(tcell1);
        Teacherstable.addCell(tcell2);
        Teacherstable.addCell(tcell3);
        Teacherstable.addCell(tcell4);
        rs.next();
        do {
            Teacherstable.addCell(rs.getInt("id") + "");
            Teacherstable.addCell(rs.getString("name"));
            Teacherstable.addCell(rs.getString("department") + "");
            Teacherstable.addCell(rs.getString("isteaching"));
        } while (rs.next());
        doc.add(Teacherstable);

        doc.close();
    }

    void deleteIt(String id) throws SQLException {

        String query = "delete from staff where id="+id;
        System.out.println(query);
        st.executeUpdate(query);

    }
    void deleteItStudent(String id) throws SQLException {

        String query = "delete from students where id="+id;
        System.out.println(query);
        st.executeUpdate(query);

    }

}
