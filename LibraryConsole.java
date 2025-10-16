package library;
import java.util.*;
import java.sql.*;
public class LibraryConsole {
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Library Management - Console (uses JDBC)");
        while(true){
            System.out.println("1.Add book  2.Issue book  3.Return book  4.List books  5.Exit");
            System.out.print("Choose: ");
            String c = sc.nextLine();
            try{
                int ch = Integer.parseInt(c);
                switch(ch){
                    case 1: addBook(); break;
                    case 2: issueBook(); break;
                    case 3: returnBook(); break;
                    case 4: listBooks(); break;
                    case 5: System.exit(0);
                    default: System.out.println("Invalid");
                }
            }catch(Exception e){ System.out.println("Error: " + e.getMessage()); }
        }
    }
    // NOTE: These methods use JDBC. Make sure DBConfig is updated and MySQL driver is on classpath.
    private static Connection conn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASS);
    }
    private static void addBook() {
        try(Connection c = conn()){
            System.out.print("Title: "); String t = sc.nextLine();
            System.out.print("Author: "); String a = sc.nextLine();
            PreparedStatement ps = c.prepareStatement("INSERT INTO books(title,author,issued) VALUES(?,?,0)");
            ps.setString(1,t); ps.setString(2,a); ps.executeUpdate();
            System.out.println("Book added.");
        }catch(Exception e){ System.out.println("DB error: " + e.getMessage()); }
    }
    private static void issueBook() {
        try(Connection c = conn()){
            System.out.print("Book ID to issue: "); int id = Integer.parseInt(sc.nextLine());
            PreparedStatement ps = c.prepareStatement("UPDATE books SET issued=1 WHERE id=? AND issued=0");
            ps.setInt(1,id);
            int r = ps.executeUpdate();
            System.out.println(r>0?"Book issued":"Book not available / wrong ID");
        }catch(Exception e){ System.out.println("DB error: " + e.getMessage()); }
    }
    private static void returnBook() {
        try(Connection c = conn()){
            System.out.print("Book ID to return: "); int id = Integer.parseInt(sc.nextLine());
            PreparedStatement ps = c.prepareStatement("UPDATE books SET issued=0 WHERE id=? AND issued=1");
            ps.setInt(1,id);
            int r = ps.executeUpdate();
            System.out.println(r>0?"Book returned":"Book not issued or wrong ID");
        }catch(Exception e){ System.out.println("DB error: " + e.getMessage()); }
    }
    private static void listBooks() {
        try(Connection c = conn()){
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT id,title,author,issued FROM books");
            while(rs.next()){
                System.out.println(rs.getInt(1)+" - "+rs.getString(2)+" by "+rs.getString(3)+(rs.getInt(4)==1?" (issued)":""));
            }
        }catch(Exception e){ System.out.println("DB error: " + e.getMessage()); }
    }
}
