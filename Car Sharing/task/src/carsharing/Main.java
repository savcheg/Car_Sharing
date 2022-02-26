package carsharing;

import carsharing.dao.DBConfig;
import carsharing.services.MenuManager;

public class Main {

    public static void main(String[] args) {
        DBConfig.init(args);
        MenuManager.start();
    }
}