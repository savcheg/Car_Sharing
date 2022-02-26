package carsharing.dao;

import carsharing.models.Company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompaniesDao {
    private static final Statement statement = DBConfig.getStatement();

    public static List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        try {
            var result = statement.executeQuery("select * from company");
            while (result.next()) {
                companies.add(new Company(result.getInt("id"), result.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    public static void createCompany(String name) {
        try {
            statement.executeUpdate("insert into company (name)" +
                    " values ('" + name + "');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getCompanyNameById(int currentCompanyId) {
        try {
            var result =
                    statement.executeQuery("select name from company where id = " + currentCompanyId + ";");
            result.next();
            return result.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
