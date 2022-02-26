package carsharing.dao;

import carsharing.models.Car;
import carsharing.models.Company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarsDao {
    private static final Statement statement = DBConfig.getStatement();

    public static List<Car> getAllCarsByCompany(int companyId) {
        List<Car> cars = new ArrayList<>();
        try {
            var result =
                    statement.executeQuery("select * from CAR where COMPANY_ID = " + companyId);
            while (result.next()) {
                cars.add(new Car(result.getInt("id"),
                        result.getString("name"),
                        result.getInt("company_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public static List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        try {
            var result =
                    statement.executeQuery("select * from CAR");
            while (result.next()) {
                cars.add(new Car(result.getInt("id"),
                        result.getString("name"),
                        result.getInt("company_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public static void createCar(String name, int companyId) {
        try {
            statement.executeUpdate("insert into CAR (name, company_id) VALUES ( '" + name + "', " + companyId + " );");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Car getCarById(Integer id) {
        try {
            var result =
                    statement.executeQuery("select * from car where id = " + id + ";");
            if (result.next())
                return new Car(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getInt("company_id")
                );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isRentedById(Integer id) {
        try {
            var result = statement.executeQuery("select is_rented from CAR where ID = " + id + ";");
            if (result.next())
                return result.getBoolean("1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void switchRentedById(int id) {
        try {

            statement.executeUpdate("update CAR " +
                    "set IS_RENTED = not IS_RENTED " +
                    "where ID = " + id + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Car> getNotRentedCarsByCompanyId(int id) {
        List<Car> cars = new ArrayList<>();
        try {
            var result =
                    statement.executeQuery("select * from CAR where is_rented = false and COMPANY_ID = " + id + ";");
            while (result.next()) {
                cars.add(new Car(result.getInt("id"),
                        result.getString("name"),
                        result.getInt("company_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
}
