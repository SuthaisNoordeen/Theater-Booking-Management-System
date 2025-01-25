import java.util.Scanner;

public class CinemaManagement {
    private static final int ROW_COUNT = 3;
    private static final int SEATS_PER_ROW = 16;
    private static final int MAX_TICKETS = ROW_COUNT * SEATS_PER_ROW;
    private static final int[] PRICES = {12, 10, 8};
    private static final boolean[][] seatAvailability = new boolean[ROW_COUNT][SEATS_PER_ROW];
    private static final Ticket[] tickets = new Ticket[MAX_TICKETS];
    private static int ticketsSold = 0;
    private static final Scanner scanner = new Scanner(System.in);

    static {
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                seatAvailability[i][j] = true;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("\n\t\t  Welcome to The London Lumiere");
        int option;
        do {
            showMenu();
            option = getValidIntInput(8, "Enter your choice (1-8) : ");
            switch (option) {
                case 1:
                    buy_ticket();
                    break;
                case 2:
                    cancel_ticket();
                    break;
                case 3:
                    print_seating_area();
                    break;
                case 4:
                    find_first_available();
                    break;
                case 5:
                    print_tickets_info();
                    break;
                case 6:
                    search_ticket();
                    break;
                case 7:
                    sort_tickets();
                    break;
                case 8:
                    System.out.println("\n--------------------------------------------------");
                    System.out.println("           Exiting Program... Goodbye!");
                    System.out.println("--------------------------------------------------");
                    break;
            }
        } while (option != 8);
    }

    private static void showMenu() {
        System.out.println("\n--------------------------------------------------");
        System.out.println("                       Menu");
        System.out.println("--------------------------------------------------");
        System.out.println("\t1) Buy a ticket");
        System.out.println("\t2) Cancel a ticket");
        System.out.println("\t3) Show seating area");
        System.out.println("\t4) Find first available seat");
        System.out.println("\t5) Print tickets information and total sales");
        System.out.println("\t6) Search ticket");
        System.out.println("\t7) Sort tickets by price");
        System.out.println("\t8) Exit");
        System.out.println("--------------------------------------------------");
    }

    private static void buy_ticket() {
        int row = getValidIntInput(ROW_COUNT, "\nEnter row number (1-3) : ");
        int seat = getValidIntInput(SEATS_PER_ROW, "\nEnter seat number (1-16) : ");

        if (!seatAvailability[row - 1][seat - 1]) {
            System.out.println("\nThis seat is not available.");
            return;
        }

        String name = getValidStringInput("\nEnter Name : ");
        String surname = getValidStringInput("Enter Surname : ");
        String email = getValidEmailInput();

        Person person = new Person(name, surname, email);
        Ticket ticket = new Ticket(row, seat, PRICES[row - 1], person);
        tickets[ticketsSold++] = ticket;
        seatAvailability[row - 1][seat - 1] = false;

        System.out.println("\nThe seat has been booked.");
    }

    private static void cancel_ticket() {
        int row = getValidIntInput(ROW_COUNT, "\nEnter row number (1-3) : ");
        int seat = getValidIntInput(SEATS_PER_ROW, "\nEnter seat number (1-16) : ");

        if (seatAvailability[row - 1][seat - 1]) {
            System.out.println("\nThis seat is already available.");
            return;
        }

        seatAvailability[row - 1][seat - 1] = true;
        for (int i = 0; i < ticketsSold; i++) {
            if (tickets[i].getRow() == row && tickets[i].getSeat() == seat) {
                for (int j = i; j < ticketsSold - 1; j++) {
                    tickets[j] = tickets[j + 1];
                }
                ticketsSold--;
                break;
            }
        }

        System.out.println("\nThe seat has been cancelled.");
    }

    private static void print_seating_area() {
        System.out.println("\n   ******************************************************");
        System.out.println("   *                        SCREEN                      *");
        System.out.println("   ******************************************************");
        System.out.println("      1  2  3  4  5  6  7  8    9 10 11 12 13 14 15 16");
        System.out.println("   ------------------------------------------------------");
        for (int i = 0; i < ROW_COUNT; i++) {
            // Ensuring the row number is always two digits
            System.out.printf("%2d |", i + 1);
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                if (j == 8) {
                    System.out.print("  ");
                }
                // Ensuring each seat has two spaces
                System.out.print(seatAvailability[i][j] ? "  O" : "  X");
            }
            System.out.println("  |");
        }
    }


    private static void find_first_available() {
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                if (seatAvailability[i][j]) {
                    System.out.println("\nFirst available seat : Row " + (i + 1) + ", Seat " + (j + 1));
                    return;
                }
            }
        }
        System.out.println("\nNo available seats.");
    }

    private static void print_tickets_info() {
        int totalSales = 0;
        System.out.println("\n---------------------");
        System.out.println("Tickets Information :");
        System.out.println("---------------------\n");
        for (int i = 0; i < ticketsSold; i++) {
            tickets[i].displayTicketInfo();
            totalSales += tickets[i].getPrice();
            System.out.println();
        }
        System.out.println("---------------------");
        System.out.println("Total sales : £" + totalSales);
        System.out.println("---------------------");
    }

    private static void search_ticket() {
        int row = getValidIntInput(ROW_COUNT, "\nEnter row number (1-3) : ");
        int seat = getValidIntInput(SEATS_PER_ROW, "\nEnter seat number (1-16) : ");
        System.out.println();

        for (int i = 0; i < ticketsSold; i++) {
            if (tickets[i].getRow() == row && tickets[i].getSeat() == seat) {
                tickets[i].displayTicketInfo();
                return;
            }
        }

        System.out.println("This seat is available.");
    }

    private static void sort_tickets() {
        for (int i = 0; i < ticketsSold - 1; i++) {
            for (int j = 0; j < ticketsSold - i - 1; j++) {
                if (tickets[j].getPrice() > tickets[j + 1].getPrice()) {
                    Ticket temp = tickets[j];
                    tickets[j] = tickets[j + 1];
                    tickets[j + 1] = temp;
                }
            }
        }

        System.out.println("\n---------------------------");
        System.out.println("Sorted Tickets (by price) :");
        System.out.println("---------------------------\n");
        for (int i = 0; i < ticketsSold; i++) {
            tickets[i].displayTicketInfo();
            System.out.println();
        }
    }

    private static int getValidIntInput(int max, String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input >= 1 && input <= max) {
                    return input;
                } else {
                    System.out.println("Please enter a number between " + 1 + " and " + max + ".\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static String getValidStringInput(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
    }

    private static String getValidEmailInput() {
        String email;
        while (true) {
            System.out.print("Enter email : ");
            email = scanner.nextLine().trim();
            if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return email;
            } else {
                System.out.println("Invalid email format. Please try again.");
            }
        }
    }
}