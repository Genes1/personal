import java.util.List;

public class Customer implements Runnable {

    String name;
    private Order order;

    public Customer(String t2, List<Food> food) {
        this.name = t2;
        this.order = new Order(food, this);
    }


    public void run() {
        System.out.println(this.name + " customer was created with foodlist=" + this.order.foods.toString());

        try {


                synchronized (Cook.orders) {
                    boolean orderPlaced = false;
                    while (!orderPlaced) {
                        if (Cook.orders.size() < TestRunner.numTables) {
                            System.out.println("!!!" + this.name + " placed order!!!");
                            Cook.orders.add(this.order);
                            Cook.orders.notify();
                            orderPlaced = true;
                        } else {
                            System.out.println("===" + this.name + " is waiting for orders lock===");
                            Cook.orders.wait();
                        }
                    }
                }

            synchronized (this.order) {

                this.order.wait();
                System.out.println("---" + this.name + " received order and left!-->" );
                synchronized (Cook.orders) {
                    Cook.orders.notify();
                }

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
