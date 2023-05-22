package carsharing;

import java.util.List;
import java.util.Scanner;

public class TUIHandler {
    CompanyDaoImpl dao;
    PROGRAM_STATUS programStatus = PROGRAM_STATUS.START_MENU;
    Scanner scanner = new Scanner(System.in);
    Company chosenCompany = null;
    Customer chosenCustomer = null;
    Car chosenCar = null;


    TUIHandler(CompanyDaoImpl dao) {
        this.dao = dao;
    }

    public void run() {
        while (programStatus != PROGRAM_STATUS.STOPPED) {
            switch (programStatus) {
                case START_MENU -> startMenuHandler();
                case MANAGER_MENU -> managerMenuHandler();
                case SELECT_CUSTOMER_MENU -> selectCustomerMenuHandler();
                case CREATE_CUSTOMER_MENU -> createCustomerMenuHandler();
                case SELECT_COMPANY_MENU -> companySelectionMenuHandler();
                case CREATE_COMPANY_MENU -> createCompanyMenuHandler();
                case COMPANY_MENU -> companyMenuHandler();
                case CUSTOMER_MENU -> customerMenuHandler();
                case CREATE_CAR_MENU -> createCarMenuHandler();
                case RENTING_A_CAR -> rentingMenuHandler();
            }
        }
    }

    private void startMenuHandler() {
        System.out.println("""
                1. Log in as a manager
                2. Log in as a customer
                3. Create a customer
                0. Exit
                """);
        int userChoice = Integer.parseInt(scanner.nextLine());
        switch (userChoice) {
            case 0 -> programStatus = PROGRAM_STATUS.STOPPED;
            case 1 -> programStatus = PROGRAM_STATUS.MANAGER_MENU;
            case 2 -> programStatus = PROGRAM_STATUS.SELECT_CUSTOMER_MENU;
            case 3 -> programStatus = PROGRAM_STATUS.CREATE_CUSTOMER_MENU;
        }
    }

    private void managerMenuHandler() {
        System.out.println("""
            1. Company list
            2. Create a company
            0. Back
            """);
        int userChoice = Integer.parseInt(scanner.nextLine());
        switch (userChoice) {
            case 0 -> {
                programStatus = PROGRAM_STATUS.START_MENU;
                chosenCustomer = null;
                chosenCompany = null;
                chosenCar = null;
            }
            case 1 -> programStatus = PROGRAM_STATUS.SELECT_COMPANY_MENU;
            case 2 -> programStatus = PROGRAM_STATUS.CREATE_COMPANY_MENU;
        }
    }

    private void selectCustomerMenuHandler() {
        List<Customer> customers = dao.getAllCustomers();
        if (customers.size() != 0) {
            System.out.println("Customer list:");
            customers.forEach(System.out::println);
            System.out.println("0. Back");
        } else {
            System.out.println("The customer list is empty!");
            programStatus = PROGRAM_STATUS.START_MENU;
            chosenCustomer = null;
            chosenCompany = null;
            chosenCar = null;
            return;
        }

        int customerId = Integer.parseInt(scanner.nextLine());
        if (customerId == 0) {
            programStatus = PROGRAM_STATUS.START_MENU;
            chosenCustomer = null;
            chosenCompany = null;
            chosenCar = null;
            return;
        }
        chosenCustomer = customers.get(customerId - 1);
        chosenCompany = null;
        chosenCar = null;
        programStatus = PROGRAM_STATUS.CUSTOMER_MENU;

    }


    private void createCustomerMenuHandler() {
        System.out.println("Enter the customer name:");
        String customerName = scanner.nextLine();
        dao.addCustomer(customerName);
        System.out.println("The customer was added!");
        programStatus = PROGRAM_STATUS.START_MENU;
        chosenCustomer = null;
        chosenCompany = null;
        chosenCar = null;
    }

    private void customerMenuHandler() {
        int carId = dao.getRentedCarID(chosenCustomer.getId());
        System.out.printf("""
                1. Rent a car ID:%d NAME:%s CAR:%d
                2. Return a rented car
                3. My rented car
                0. Back
                %n""", chosenCustomer.getId(), chosenCustomer.getName(), carId);
        int userChoice = Integer.parseInt(scanner.nextLine());
        switch (userChoice) {
            case 0 -> {
                programStatus = PROGRAM_STATUS.START_MENU;
                chosenCustomer = null;
                chosenCompany = null;
                chosenCar = null;
            }
            case 1 -> customerRentACar(); // rent a car
            case 2 -> returnCustomerRentedCar(); // return a car
            case 3 -> showCustomerRentedCar(); // show my rented cars
        }
    }

    private void showCustomerRentedCar() {
        int carId = dao.getRentedCarID(chosenCustomer.getId());
        if (carId == 0) {
            System.out.println("You didn't rent a car!");
        } else {
            Car rentedCar = dao.getCar(carId);
            Company company = dao.getCompany(rentedCar.getCompanyId());
            System.out.println("Your rented car:");
            System.out.println(rentedCar.getName());
            System.out.println("Company:");
            System.out.println(company.getName());
        }
    }

    private void returnCustomerRentedCar() {
        int carId = dao.getRentedCarID(chosenCustomer.getId());
        if (carId == 0) {
            System.out.println("You didn't rent a car!");
        } else {
            dao.returnRentedCar(chosenCustomer.getId());
            System.out.println("You've returned a rented car!");
        }
    }

    private void customerRentACar() {
        int carId = dao.getRentedCarID(chosenCustomer.getId());
        if (carId != 0) {
            System.out.println("You've already rented a car!");
            return;
        }
        if (showCompanyList()) {
            int companyId = Integer.parseInt(scanner.nextLine());
            if (companyId == 0) {
                programStatus = PROGRAM_STATUS.START_MENU;
                chosenCustomer = null;
                chosenCompany = null;
                chosenCar = null;
                return;
            }
            chosenCompany = dao.getCompany(companyId);
            chosenCar = null;
            programStatus = PROGRAM_STATUS.RENTING_A_CAR;
        } else {
            programStatus = PROGRAM_STATUS.START_MENU;
            chosenCustomer = null;
            chosenCompany = null;
            chosenCar = null;
        }
    }

    private void rentingMenuHandler() {
        if (choseACar()) {
            int carId = Integer.parseInt(scanner.nextLine());
            if (carId == 0) {
                programStatus = PROGRAM_STATUS.CUSTOMER_MENU;
                return;
            }
            List<Car> carsOfCompany = dao.getAllNotRentedCarsOfCompany(chosenCompany.getId());
            chosenCar = carsOfCompany.get(carId-1);
            dao.rentACar(chosenCustomer.getId(), chosenCar.getId());
            System.out.printf("You rented '%s'%n", chosenCar.getName());
        }
        programStatus = PROGRAM_STATUS.CUSTOMER_MENU;
    }

    private boolean showCompanyList() {
        List<Company> companies = dao.getAllCompanies();
        if (companies.size() != 0) {
            System.out.println("Choose a company:");
            companies.forEach(System.out::println);
            System.out.println("0. Back");
            return true;
        } else {
            System.out.println("The company list is empty!");
            return false;
        }
    }

    private void createCompanyMenuHandler() {
        System.out.println("Enter the company name:");
        String companyName = scanner.nextLine();
        dao.addCompany(companyName);
        System.out.println("The company was created!");
        programStatus = PROGRAM_STATUS.MANAGER_MENU;
    }

    private void companySelectionMenuHandler() {
        if (showCompanyList()) {
            int companyId = Integer.parseInt(scanner.nextLine());
            if (companyId == 0) {
                programStatus = PROGRAM_STATUS.MANAGER_MENU;
                return;
            }
            chosenCompany = dao.getCompany(companyId);
            programStatus = PROGRAM_STATUS.COMPANY_MENU;
        } else {
            programStatus = PROGRAM_STATUS.MANAGER_MENU;
        }
    }

    private void companyMenuHandler() {
        System.out.printf("""
                '%s' company:
                1. Car list
                2. Create a car
                0. Back
                %n""", chosenCompany.getName());
        int userChoice = Integer.parseInt(scanner.nextLine());
        switch (userChoice) {
            case 0 -> programStatus = PROGRAM_STATUS.MANAGER_MENU;
            case 1 -> showCarList();
            case 2 -> programStatus = PROGRAM_STATUS.CREATE_CAR_MENU;
        }
    }

    private void showCarList() {
        List<Car> cars = dao.getAllCarsOfCompany(chosenCompany.getId());
        if (cars.size() != 0) {
            System.out.println("Car list:");
            int i = 1;
            for (Car car : cars) {
                System.out.printf("%d. %s", i, car.getName());
                i++;
            }
        } else {
            System.out.println("The car list is empty!");
        }
    }

    private boolean choseACar() {
        List<Car> cars = dao.getAllNotRentedCarsOfCompany(chosenCompany.getId());
        if (cars.size() != 0) {
            System.out.println("Choose a car::");
            int i = 1;
            for (Car car : cars) {
                System.out.printf("%d. %s%n", i, car.getName());
                i++;
            }
            System.out.println("0. Back");
            return true;
        } else {
            System.out.println("The car list is empty!");
            return false;
        }
    }

    private void createCarMenuHandler() {
        System.out.println("Enter the car name:");
        String carName = scanner.nextLine();
        dao.addCar(chosenCompany.getId(), carName);
        System.out.println("The car was added!");
        programStatus = PROGRAM_STATUS.COMPANY_MENU;
    }
}
