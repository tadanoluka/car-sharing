package carsharing;

public class Customer {
    private int id;
    private String name;
    private Integer rentedCarId;


    public Customer(int id, String name, int rentedCarId) {
        this.id = id;
        this.name = name;
        if (rentedCarId == 0) {
            this.rentedCarId = null;
        } else {
            this.rentedCarId = rentedCarId;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCarId(Integer rentedCarId) {
        this.rentedCarId = rentedCarId;
    }

    @Override
    public String toString() {
        return "%d. %s".formatted(id, name);
    }

}
