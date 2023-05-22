package carsharing;

import java.util.List;

public interface CompanyDao {
    public List<Company> getAllCompanies();
    public Company getCompany(int id);
    public void addCompany(String name);
    public List<Car> getAllCars();
    public List<Car> getAllCarsOfCompany(int companyId);
    List<Car> getAllNotRentedCarsOfCompany(int companyId);
    public Car getCar(int id);
    public void addCar(int companyId, String carName);
    void returnRentedCar(int customerId);
    void rentACar(int customerId, int carId);
    int getRentedCarID(int customerId);
    public List<Customer> getAllCustomers();
    public void addCustomer(String name);
    public Customer getCustomer(int id);
    public void updateCompany(Company company);
    public void deleteCompany(Company company);
}
