import java.util.List;

public class Order {
    public List<Food> foods;
    public Customer customer;
    public boolean isReady;

    Order (List<Food> foods, Customer customer) {
        this.foods = foods;
        this.customer = customer;
        this.isReady = false;
    }

}
