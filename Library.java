import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Library {
    private Connection connection;
    private Scanner scanner = new Scanner(System.in);

    // NOTE: Credentials set as requested for the project.
    private final String DB_URL = "jdbc:mysql://localhost:3306/librarydb?useSSL=false&allowPublicKeyRetrieval=true";
    private final String DB_USER = "root";
    private final String DB_PASS = "Aiphechoh8bie8ie!";

    public Library() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            ensureTable();
        } catch (SQLException e) {
            System.err.println("ERROR: Can't connect to database. Check credentials and that MySQL is running.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void ensureTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS books ("
                   + "id INT AUTO_INCREMENT PRIMARY KEY,"
                   + "title VARCHAR(200) NOT NULL,"
                   + "author VARCHAR(150) NOT NULL,"
                   + "isIssued BOOLEAN DEFAULT FALSE"
                   + ")";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        stmt.close();
    }

    // Add a new book
    public void addBook() {
        try {
            System.out.println("\n--- Add New Book ---");
            System.out.print("Title: ");
            String title = scanner.nextLine().trim();
            if (title.isEmpty()) {
                System.out.println("Title cannot be empty.");
                return;
            }
            System.out.print("Author: ");
            String author = scanner.nextLine().trim();
            if (author.isEmpty()) {
                System.out.println("Author cannot be empty.");
                return;
            }

            String sql = "INSERT INTO books (title, author, isIssued) VALUES (?, ?, false)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.executeUpdate();
            ps.close();

            System.out.println("\u2705 Book added successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to add book:");
            e.printStackTrace();
        }
    }

    // View all books
    public void viewBooks() {
        try {
            String sql = "SELECT * FROM books ORDER BY id";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n--- Library Books ---");
            System.out.printf("%-4s | %-40s | %-25s | %-8s\n", "ID", "Title", "Author", "Status");
            System.out.println("----------------------------------------------------------------------------------------");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                boolean isIssued = rs.getBoolean("isIssued");
                System.out.printf("%-4d | %-40s | %-25s | %-8s\n", id, title, author, (isIssued ? "Issued" : "Available"));
            }
            System.out.println("----------------------------------------------------------------------------------------\n");

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Failed to fetch books:");
            e.printStackTrace();
        }
    }

    // Issue a book
    public void issueBook() {
        try {
            System.out.print("Enter Book ID to issue: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            String checkSql = "SELECT isIssued FROM books WHERE id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                if (!rs.getBoolean("isIssued")) {
                    String sql = "UPDATE books SET isIssued = true WHERE id = ?";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    ps.close();
                    System.out.println("\uD83D\uDCD6 Book issued successfully!");
                } else {
                    System.out.println("Book is already issued.");
                }
            } else {
                System.out.println("Book not found with ID: " + id);
            }

            rs.close();
            checkStmt.close();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid numeric ID.");
        } catch (SQLException e) {
            System.err.println("Failed to issue book:");
            e.printStackTrace();
        }
    }

    // Return a book
    public void returnBook() {
        try {
            System.out.print("Enter Book ID to return: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            String checkSql = "SELECT isIssued FROM books WHERE id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                if (rs.getBoolean("isIssued")) {
                    String sql = "UPDATE books SET isIssued = false WHERE id = ?";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    ps.close();
                    System.out.println("\u2705 Book returned successfully!");
                } else {
                    System.out.println("This book is not currently issued.");
                }
            } else {
                System.out.println("Book not found with ID: " + id);
            }

            rs.close();
            checkStmt.close();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid numeric ID.");
        } catch (SQLException e) {
            System.err.println("Failed to return book:");
            e.printStackTrace();
        }
    }

    // Search books by title or author (simple contains)
    public void searchBooks() {
        try {
            System.out.print("Enter search term (title or author): ");
            String term = scanner.nextLine().trim();
            if (term.isEmpty()) {
                System.out.println("Search term cannot be empty.");
                return;
            }

            String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? ORDER BY id";
            PreparedStatement ps = connection.prepareStatement(sql);
            String like = "%" + term + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ResultSet rs = ps.executeQuery();

            List<Book> results = new ArrayList<>();
            while (rs.next()) {
                results.add(new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getBoolean("isIssued")
                ));
            }

            if (results.isEmpty()) {
                System.out.println("No books matched your search.");
            } else {
                System.out.println("\n--- Search Results ---");
                System.out.printf("%-4s | %-40s | %-25s | %-8s\n", "ID", "Title", "Author", "Status");
                System.out.println("----------------------------------------------------------------------------------------");
                for (Book b : results) {
                    System.out.println(b.toString());
                }
                System.out.println("----------------------------------------------------------------------------------------\n");
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Search failed:");
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException ignored) {}
    }
}
