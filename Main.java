import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (true) {
            printHeader();
            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Books");
            System.out.println("6. Exit");
            System.out.print("\nEnter choice (1-6): ");

            String input = scanner.nextLine().trim();
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number between 1 and 6.");
                pause(scanner);
                continue;
            }

            switch (choice) {
                case 1 -> library.addBook();
                case 2 -> library.viewBooks();
                case 3 -> library.issueBook();
                case 4 -> library.returnBook();
                case 5 -> library.searchBooks();
                case 6 -> {
                    System.out.println("Exiting... Goodbye!");
                    library.close();
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Choose 1-6.");
            }
            pause(scanner);
        }
    }

    private static void printHeader() {
        System.out.println("============================================================");
        System.out.println("           Library Management System - APP Project          ");
        System.out.println("       RA2411003011554  |  Gummudu Venkata Prudhvi Krishna   ");
        System.out.println("============================================================\n");
    }

    private static void pause(Scanner scanner) {
        System.out.print("\nPress ENTER to continue...");
        scanner.nextLine();
        System.out.println();
    }
}
