package carsharing.dao;

import carsharing.models.Customer;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
    private static final Statement statement = DBConfig.getStatement();

    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try {
            var result =
                    statement.executeQuery("select * from customer");
            while (result.next()) {
                customers.add(new Customer(result.getInt("id"),
                        result.getString("name"),
                        result.getInt("rented_car_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public static Customer getCustomerById(int customerId) {
        try {
            var result =
                    statement.executeQuery("select * from CUSTOMER where id = " + customerId + ";");
            if (result.next()) {
                return new Customer(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getInt("rented_car_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void createCustomer(String name) {
        try {
            statement.executeUpdate("insert into CUSTOMER (name)" +
                    " values ('" + name + "');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCustomer(Customer customer) {
        try {
            if (customer.getRentedCarId() == 0)
                customer.setRentedCarId(null);
            statement.executeUpdate("update CUSTOMER set RENTED_CAR_ID = "
                    + customer.getRentedCarId() +
                    " where ID = " + customer.getId() + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}