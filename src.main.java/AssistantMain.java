import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class AssistantMain {

    static Connection con = null;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/roadside",
                    "root",
                    "root");

            while (true) {

                System.out.println("1. Register Customer");
                System.out.println("2. Create Breakdown Ticket");
                System.out.println("3. Assign Mechanic");
                System.out.println("4. Generate Bill");
                System.out.println("5. Exit");

                int choice = sc.nextInt();

                if (choice == 1) {

                    System.out.println("Enter customer name:");
                    String name = sc.next();

                    System.out.println("Enter vehicle:");
                    String vehicle = sc.next();

                    Statement stmt = con.createStatement();

                    String sql =
                            "insert into customer values('" +
                                    name + "','" + vehicle + "')";

                    stmt.execute(sql);

                    System.out.println("Customer Registered");

                } else if (choice == 2) {

                    System.out.println("Enter customer name:");
                    String name = sc.next();

                    Statement stmt = con.createStatement();

                    String sql =
                            "insert into ticket values('" +
                                    name + "','OPEN')";

                    stmt.execute(sql);

                    System.out.println("Ticket Created");

                } else if (choice == 3) {

                    System.out.println("Enter ticket id:");
                    int id = sc.nextInt();

                    Statement stmt = con.createStatement();

                    String sql =
                            "update ticket set mechanic='John' where id=" + id;

                    stmt.execute(sql);

                    System.out.println("Mechanic Assigned");

                } else if (choice == 4) {

                    System.out.println("Enter ticket id:");
                    int id = sc.nextInt();

                    Statement stmt = con.createStatement();

                    ResultSet rs =
                            stmt.executeQuery(
                                    "select * from ticket where id=" + id);

                    while (rs.next()) {

                        System.out.println("Generating bill...");

                        double amount = 5000;

                        System.out.println("Total Amount = " + amount);

                        Statement stmt2 = con.createStatement();

                        stmt2.execute(
                                "insert into payment values(" +
                                        id + "," + amount + ")");

                    }

                } else {

                    System.exit(0);
                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}