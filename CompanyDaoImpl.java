package carsharing;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class CompanyDaoImpl implements CompanyDao{
    private ConnectionFactory factory;

    public CompanyDaoImpl(String fileName) {
        this.factory = new ConnectionFactory(fileName);
    }

    public void dropCompanyTable() {
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    DROP TABLE IF EXISTS company;
                    """;
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    public void createCompanyTable() {
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    CREATE TABLE IF NOT EXISTS company (
                        id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE
                    );
                    """;
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    public void dropCarTable() {
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    DROP TABLE IF EXISTS car;
                    """;
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    public void createCarTable() {
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    CREATE TABLE IF NOT EXISTS car (
                        id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE,
                        company_id INT NOT NULL,
                        are_rented BOOLEAN NOT NULL DEFAULT FALSE,
                        CONSTRAINT fk_company_id FOREIGN KEY (company_id)
                        REFERENCES company(id)
                    );
                    """;
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    public void dropCustomerTable() {
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    DROP TABLE IF EXISTS customer;
                    """;
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    public void createCustomerTable() {
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    CREATE TABLE IF NOT EXISTS customer (
                        id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE,
                        rented_car_id INT DEFAULT NULL,
                        CONSTRAINT fk_rented_car_id FOREIGN KEY (rented_car_id)
                        REFERENCES car(id)
                    );
                    """;
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    SELECT * FROM company;
                    """;
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                companies.add(new Company(id, name));
            }
            return companies;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    @Override
    public Company getCompany(int id) {
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    SELECT *
                    FROM company
                    WHERE (id = %d);
                    """.formatted(id);
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            String name = rs.getString("name");
            return new Company(id, name);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    @Override
    public void addCompany(String name) {
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    INSERT INTO company(name)
                    VALUES ('%s');
                    """.formatted(name);
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    @Override
    public List<Car> getAllCars() {
        return null;
    }

    @Override
    public List<Car> getAllCarsOfCompany(int companyId) {
        List<Car> companies = new ArrayList<>();
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    SELECT *
                    FROM car
                    WHERE (company_id = %d);
                    """.formatted(companyId);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                companies.add(new Car(id, name, companyId));
            }
            return companies;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    @Override
    public List<Car> getAllNotRentedCarsOfCompany(int companyId) {
        List<Car> companies = new ArrayList<>();
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    SELECT *
                    FROM car
                    WHERE (company_id = %d) AND (are_rented = FALSE);
                    """.formatted(companyId);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                companies.add(new Car(id, name, companyId));
            }
            return companies;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    @Override
    public Car getCar(int id) {
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    SELECT *
                    FROM car
                    WHERE (id = %d);
                    """.formatted(id);
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            String name = rs.getString("name");
            int companyId = rs.getInt("company_id");
            return new Car(id, name, companyId);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    @Override
    public void addCar(int companyId, String carName) {
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    INSERT INTO car(name, company_id)
                    VALUES ('%s', %d);
                    """.formatted(carName, companyId);
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    @Override
    public void returnRentedCar(int customerId) {
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    SELECT rented_car_id
                    FROM customer
                    WHERE id = %d;
                    """.formatted(customerId);
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int rentedCarId = rs.getInt("rented_car_id");
            sql = """
                    UPDATE customer
                    SET rented_car_id = NULL
                    WHERE id = %d;
                    """.formatted(customerId);
            stmt.executeUpdate(sql);
            sql = """
                    UPDATE car
                    SET are_rented = FALSE
                    WHERE id = %d;
                    """.formatted(rentedCarId);
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    @Override
    public void rentACar(int customerId, int carId) {
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    UPDATE customer
                    SET rented_car_id = %d
                    WHERE id = %d;
                    """.formatted(carId, customerId);
            stmt.executeUpdate(sql);
            sql = """
                    UPDATE car
                    SET are_rented = TRUE
                    WHERE id = %d;
                    """.formatted(carId);
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    @Override
    public int getRentedCarID(int customerId) {
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    SELECT *
                    FROM customer
                    WHERE (id = %d);
                    """.formatted(customerId);
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getInt("rented_car_id");
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    SELECT * FROM customer;
                    """;
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int rentedCarId = rs.getInt("rented_car_id");
                customers.add(new Customer(id, name, rentedCarId));
            }
            return customers;
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    @Override
    public void addCustomer(String name) {
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    INSERT INTO customer(name)
                    VALUES ('%s');
                    """.formatted(name);
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    @Override
    public Customer getCustomer(int id) {
        try (Connection connection = this.factory.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            String sql = """
                    SELECT *
                    FROM customer
                    WHERE (id = %d);
                    """.formatted(id);
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            String name = rs.getString("name");
            int rentedCarId = rs.getInt("rented_car_id");
            return new Customer(id, name, rentedCarId);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new RuntimeException(se);
        }
    }

    @Override
    public void updateCompany(Company company) {

    }

    @Override
    public void deleteCompany(Company company) {

    }
}
