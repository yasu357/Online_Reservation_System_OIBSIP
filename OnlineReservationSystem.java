import java.sql.*;
import java.util.Scanner;

public class Task_1 {

    // PnrRecord class for handling reservation details
    private static class PnrRecord {
        private final Scanner scanner = new Scanner(System.in);

        public int getpnrNumber() {
            System.out.println("Enter the PNR number: ");
            int pnrNumber = scanner.nextInt();
            scanner.nextLine(); 
            return pnrNumber;
        }

        public String getPassengerName() {
            System.out.println("Enter the passenger name: ");
            return scanner.nextLine();
        }

        public String getTrainNumber() {
            System.out.println("Enter the Train Number: ");
            return scanner.nextLine();
        }

        public String getclassType() {
            System.out.println("Enter the class Type: ");
            return scanner.nextLine();
        }

        public String getjourneyDate() {
            System.out.println("Enter the Journey date: ");
            return scanner.nextLine();
        }

        public String getfrom() {
            System.out.println("Enter the source address: ");
            return scanner.nextLine();
        }

        public String getto() {
            System.out.println("Enter the destination address: ");
            return scanner.nextLine();
        }



        public void displayRecord(ResultSet resultSet) throws SQLException {
            System.out.println("Reservation Details:");

            int pnrNumber = resultSet.getInt("pnr_number");
            String passengerName = resultSet.getString("passenger_name");
            String trainNumber = resultSet.getString("train_number");
            String classType = resultSet.getString("class_type");
            String journeyDate = resultSet.getString("journey_date");
            String fromLocation = resultSet.getString("from_location");
            String toLocation = resultSet.getString("to_location");

            System.out.println("PNR Number: " + pnrNumber);
            System.out.println("Passenger Name: " + passengerName);
            System.out.println("Train Number: " + trainNumber);
            System.out.println("Class Type: " + classType);
            System.out.println("Journey Date: " + journeyDate);
            System.out.println("From Location: " + fromLocation);
            System.out.println("To Location: " + toLocation);
        }
    }

    public static void main(String[] args) {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/yasu?useSSL=false&allowPublicKeyRetrieval=true"; // Update with your database name
        String username = "yasu";
        String password = "12345";

        // SQL queries
        String insertQuery = "INSERT INTO reservations VALUES (?, ?, ?, ?, ?, ?, ?)";
        String showQuery = "SELECT * FROM reservations";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                System.out.println("User Connection Granted.\n");

                try (Scanner userInput = new Scanner(System.in)) {
                    while (true) {
                        System.out.println("Enter the choice: ");
                        System.out.println("1. Reservation");
                        System.out.println("2. Cancellation");
                        System.out.println("3. View Reservation");
                        System.out.println("4. Exit");

                        int choice = userInput.nextInt();

                        if (choice == 1) {
                            PnrRecord p1 = new PnrRecord();
                            int pnr_number = p1.getpnrNumber();
                            String passengerName = p1.getPassengerName();
                            String trainNumber = p1.getTrainNumber();
                            String classType = p1.getclassType();
                            String journeyDate = p1.getjourneyDate();
                            String getfrom = p1.getfrom();
                            String getto = p1.getto();

                            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                                preparedStatement.setInt(1, pnr_number);
                                preparedStatement.setString(2, passengerName);
                                preparedStatement.setString(3, trainNumber);
                                preparedStatement.setString(4, classType);
                                preparedStatement.setString(5, journeyDate);
                                preparedStatement.setString(6, getfrom);
                                preparedStatement.setString(7, getto);

                                int rowsAffected = preparedStatement.executeUpdate();
                                if (rowsAffected > 0) {
                                    System.out.println("Reservation added successfully.");
                                } else {
                                    System.out.println("No reservation were added.");
                                }
                            } catch (SQLException e) {
                                System.err.println("SQLException: " + e.getMessage());
                            }
                        }else if (choice == 2) {
                            Scanner deleteScanner = new Scanner(System.in);
                            try {
                                System.out.println("Enter the PNR number to delete the reservation : ");
                                if (deleteScanner.hasNextInt()) {
                                    int pnrNumber = deleteScanner.nextInt();
                                    deleteScanner.nextLine();  // Consume the newline character
                        
                                    String deleteQuery = "DELETE FROM reservations WHERE pnr_number = ?";
                                    try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                                        preparedStatement.setInt(1, pnrNumber);
                                        int rowsAffected = preparedStatement.executeUpdate();
                        
                                        if (rowsAffected > 0) {
                                            System.out.println("Record with PNR number " + pnrNumber + " deleted successfully.");
                                        } else {
                                            System.out.println("No reservation were deleted for PNR number " + pnrNumber + ".");
                                        }
                                    }
                                } else {
                                    System.out.println("Invalid input for PNR number.");
                                    deleteScanner.nextLine();  
                                }
                            } catch (SQLException e) {
                                System.err.println("SQLException: " + e.getMessage());
                            } finally {
                                if (deleteScanner != null){
                                    deleteScanner.close();
                                }
                            }
                        }
                        else if (choice == 3) {
                            try (PreparedStatement preparedStatement = connection.prepareStatement(showQuery);
                                 ResultSet resultSet = preparedStatement.executeQuery()) {
                                System.out.println("\nAll records printing.\n");
                                while (resultSet.next()) {
                                    PnrRecord pnrRecord = new PnrRecord();
                                    pnrRecord.displayRecord(resultSet);
                                    System.out.println("--------------");
                                }
                            } catch (SQLException e) {
                                System.err.println("SQLException: " + e.getMessage());
                            }
                        } else if (choice == 4) {
                            System.out.println("Exiting the program.");
                            break;
                        } else {
                            System.out.println("Invalid Choice Entered.");
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading JDBC driver: " + e.getMessage());
        }
    }
}
