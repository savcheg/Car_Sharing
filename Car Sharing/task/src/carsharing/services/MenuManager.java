package carsharing.services;

import carsharing.dao.*;
import carsharing.models.*;

import java.util.List;
import java.util.Scanner;

public class MenuManager {
    enum Status {
        LOG_IN,
        MAIN_MENU,
        CREATE_COMPANY,
        COMPANY_MENU,
        CUSTOMER_MAIN_MENU,
        CREATE_CUSTOMER,
        CHOOSE_CUSTOMER,
        CREATE_CAR
    }

    private static final Scanner in = new Scanner(System.in);
    private static Status status;
    private static String currentCompanyName;
    private static int currentCompanyId;
    private static Customer currentCustomer;

    public static void start() {
        status = Status.LOG_IN;
        printMenu();
    }

    private static void printMenu() {
        switch (status) {
            case LOG_IN:
                LogIn();
                break;
            case MAIN_MENU:
                managerMainMenu();
                break;
            case CREATE_COMPANY:
                managerCreateCompany();
                break;
            case COMPANY_MENU:
                managerCompanyMenu();
                break;
            case CREATE_CAR:
                managerCreateCar();
                break;
            case CREATE_CUSTOMER:
                createCustomer();
                break;
            case CHOOSE_CUSTOMER:
                chooseCustomer();
                break;
            case CUSTOMER_MAIN_MENU:
                customerMainMenu();
                break;
        }
    }

    private static void customerMainMenu() {
        System.out.println("1. Rent a car\n" +
                "2. Return a rented car\n" +
                "3. My rented car\n" +
                "0. Back");
        switch (Integer.parseInt(input())) {
            case 1:
                customerChooseCar();
                break;
            case 2:
                customerReturnRentedCar();
                break;
            case 3:
                customerPrintRentedCar();
                break;
            case 0:
                status = Status.LOG_IN;
                break;
        }
        printMenu();
    }

    private static void chooseCustomer() {
        var customers = CustomerDao.getAllCustomers();
        if (customers.isEmpty()) {
            status = Status.LOG_IN;
            System.out.println("The customer list is empty!");
            printMenu();
            return;
        }
        System.out.println("Choose a customer:");
        customers.forEach(customer -> System.out.println(customer.getId() + ". " + customer.getName()));
        int choice = Integer.parseInt(input());
        if (choice == 0) {
            status = Status.LOG_IN;
            printMenu();
            return;
        }
        currentCustomer = CustomerDao.getCustomerById(choice);
        status = Status.CUSTOMER_MAIN_MENU;
        printMenu();
    }

    private static void createCustomer() {
        System.out.println("Enter the customer name:");
        CustomerDao.createCustomer(input());
        System.out.println("The customer was added!");
        status = Status.LOG_IN;
        printMenu();
    }

    private static void customerChooseCar() {
        if (currentCustomer.getRentedCarId() != 0) {
            System.out.println("You've already rented a car!");
            return;
        }
        System.out.println("Choose a company:");
        List<Company> companies = CompaniesDao.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }
        companies.forEach(company -> System.out.println(company.getId()
                + ". " + company.getName()));
        System.out.println("0. Back");
        int choice = Integer.parseInt(input());
        if (choice == 0)
            return;

        var company = companies.get(choice - 1);
        List<Car> cars = CarsDao.getNotRentedCarsByCompanyId(company.getId());

        if (cars.isEmpty()) {
            System.out.println("No available cars in the " +
                    company.getName() +
                    " company");
            customerChooseCar();
            return;
        }

        for (int i = 0; i < cars.size(); i++) {
            System.out.println(i + 1 + ". " + cars.get(i).getName());
        }

        choice = Integer.parseInt(input());
        if (choice == 0)
            return;

        var car = cars.get(choice - 1);
        currentCustomer.setRentedCarId(car.getId());
        CarsDao.switchRentedById(car.getId());
        CustomerDao.updateCustomer(currentCustomer);
        System.out.println("You rented '" + car.getName() + "'");
    }

    private static void customerPrintRentedCar() {
        if (currentCustomer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!");
            return;
        }
        var car = CarsDao.getCarById(currentCustomer.getRentedCarId());
        assert car != null : "Rented car is null";
        var companyName = CompaniesDao.getCompanyNameById(car.getCompanyId());
        System.out.println("Your rented car:\n" +
                car.getName() + "\n" +
                "Company:\n" +
                companyName);
    }

    private static void customerReturnRentedCar() {
        if (currentCustomer.getRentedCarId() == null || currentCustomer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!");
            return;
        }
        currentCustomer.setRentedCarId(0);
        CustomerDao.updateCustomer(currentCustomer);
        System.out.println("You've returned a rented car!");
    }

    private static void managerCreateCar() {
        System.out.println("Enter the car name:");
        String st = input();
        CarsDao.createCar(st, currentCompanyId);
        System.out.println("The car was added!");
        status = Status.COMPANY_MENU;
        printMenu();
    }

    private static void managerCompanyMenu() {
        System.out.println(currentCompanyName + " company:\n" +
                "1. Car list\n" +
                "2. Create a car\n" +
                "0. Back");
        switch (Integer.parseInt(input())) {
            case 1:
                managerPrintCars();
                break;
            case 2:
                status = Status.CREATE_CAR;
                break;
            case 0:
                status = Status.MAIN_MENU;
                break;
        }
        printMenu();
    }

    private static void managerPrintCars() {
        List<Car> list = CarsDao.getAllCarsByCompany(currentCompanyId);
        if (list.isEmpty()) {
            System.out.println("The car list is empty!");
            return;
        }
        System.out.println("Car list:");
        int id = 1;
        for (Car car : list) {
            System.out.println(id + ". " + car.getName());
            id++;
        }
    }

    private static void managerCreateCompany() {
        System.out.println("Enter the company name:");
        String st = input();
        CompaniesDao.createCompany(st);
        System.out.println("The company was created!");
        status = Status.MAIN_MENU;
        printMenu();
    }

    private static void managerMainMenu() {
        System.out.println(
                "1. Company list\n" +
                        "2. Create a company\n" +
                        "0. Back"
        );
        switch (Integer.parseInt(input())) {
            case 1:
                managerChooseCompany();
                break;
            case 2:
                status = Status.CREATE_COMPANY;
                break;
            case 0:
                status = Status.LOG_IN;
                break;
        }
        printMenu();
    }

    private static void managerChooseCompany() {
        var list = CompaniesDao.getAllCompanies();
        if (list.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }
        System.out.println("Choose a company:");
        for (Company company : list)
            System.out.println(company.getId() + ". " + company.getName());
        System.out.println("0. Back");

        int chosen = Integer.parseInt(input());

        if (chosen == 0)
            status = Status.MAIN_MENU;
        else {
            currentCompanyId = chosen;
            currentCompanyName = CompaniesDao.getCompanyNameById(currentCompanyId);
            status = Status.COMPANY_MENU;
        }
        printMenu();
    }

    private static String input() {
        return in.nextLine().replaceFirst("> ", "");
    }

    private static void LogIn() {
        System.out.println("1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
                "0. Exit");
        String st = input();
        switch (Integer.parseInt(st)) {
            case 1:
                status = Status.MAIN_MENU;
                break;
            case 2:
                status = Status.CHOOSE_CUSTOMER;
                break;
            case 3:
                status = Status.CREATE_CUSTOMER;
                break;
            case 0:
                return;
        }
        printMenu();
    }
}
