import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Customer implements Runnable {

    public static int count;
    String name;
    private Order order;
    private static CountDownLatch releaseLatch;

    public Customer(String t2, List<Food> food) {
        this.name = t2;
        this.order = new Order(food, this);
        releaseLatch = new CountDownLatch(count);
    }


    public void run() {

        System.out.println(this.name + " customer was created (going) with foodlist=" + this.order.foods.toString());

        synchronized (releaseLatch) {
            releaseLatch.countDown();
        }

        try {
            releaseLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {


            synchronized (Cook.orders) {
                boolean orderPlaced = false;
                while (!orderPlaced) {
                    if (Cook.orders.size() < TestRunner.numTables) {
                        System.out.println("!!!" + this.name + " entered the restaurant!!!");
                        System.out.println("!!!" + this.name + " placed order!!!");
                        Cook.orders.add(this.order);
                        Cook.orders.notify();
                        orderPlaced = true;
                    } else {
                        System.out.println("===" + this.name + " is waiting for a seat===");
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
