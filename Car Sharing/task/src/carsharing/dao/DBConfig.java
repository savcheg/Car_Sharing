package carsharing.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConfig {
    private static Statement statement;

    static final String JDBC_DRIVER = "org.h2.Driver";
    static String DB_URL = "jdbc:h2:~/IdeaProjects/Car Sharing/Car Sharing/task/src/carsharing/db/";

    public static void init(String[] args) {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (args.length > 1) DB_URL += args[1];
        else DB_URL += "test";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            statement = conn.createStatement();
            conn.setAutoCommit(true);

//            System.out.println("Dropping tables customer, car and company if exists");
//            statement.executeUpdate("drop table if exists customer");
//            statement.executeUpdate("drop table if exists car");
//            statement.executeUpdate("drop table if exists company");

            System.out.println("Creating table company in given database");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS company (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) UNIQUE NOT NULL" +
                    ");");

            System.out.println("Creating table car in given database");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS car (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) UNIQUE NOT NULL," +
                    "company_id INT NOT NULL," +
                    "is_rented boolean default false," +
                    "FOREIGN KEY (company_id) " +
                    "REFERENCES company(id)" +
                    ");");

            System.out.println("Creating table customer in given database");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS customer (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) UNIQUE NOT NULL," +
                    "rented_car_id INT default null," +
                    "FOREIGN KEY (rented_car_id) " +
                    "REFERENCES car(id)" +
                    ");");

            statement.executeUpdate("ALTER TABLE COMPANY ALTER COLUMN ID RESTART WITH 1");
            statement.executeUpdate("ALTER TABLE CAR ALTER COLUMN ID RESTART WITH 1");
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public static Statement getStatement() {
        return statement;
    }
}
