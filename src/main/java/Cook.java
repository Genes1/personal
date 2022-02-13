import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class Cook implements Runnable {

    public static ArrayList<Order> orders = new ArrayList<Order>();
    public static HashMap<String, Machine> machineMap = new HashMap<>();
    String name;
    CountDownLatch doneSignal;


    public Cook(String name) { // OrderRouter should tell cook how long each item should take
        this.name = name;


    }

//    public Cook(List<Food> s, int n, OrderRouter or) { // OrderRouter should tell cook how long each item should take
//        this.name="bob";
//        this.food = s;
//        this.cookTime = n;
//        this.doneSignal = new CountDownLatch(s.size());
//        this.or = or;
//    }


    public void run() {

        System.out.println(this.name + " cook was created" );

        try {


            while (!Thread.currentThread().isInterrupted()) {

                Order order = null;

                synchronized (orders) { // try to pick up an order from the list
                    if (orders.size() > 0) {
                        order = orders.remove(0);
                        synchronized (order) { // lock it
                            System.out.println(this.name + " picked up order for " + order.customer.name);

                            this.doneSignal = new CountDownLatch(order.foods.size()); // make the requests to the machine
                            for (Food f : order.foods) {
                                synchronized (machineMap.get(f.name).foods) {
                                    synchronized (machineMap.get(f.name).latches) {
                                        machineMap.get(f.name).latches.add(doneSignal);
                                        machineMap.get(f.name).foods.add(f);
                                        System.out.println(f.name + " queued (cook->machine)");
                                    }
                                }
                            }
                            this.doneSignal.await(); // and wait for them to complete.

                            synchronized (orders) { // then,
                                System.out.println("***food cooking done for " + order.customer.name + " by " + name + "***");
                                order.notify();
                            }

                        }
                    }
                }


                if (order != null) { // if we actually end up with an order,




                }



            }

        }  catch (InterruptedException e)  {
            System.out.println(name + " interrupted (cook)");
        }

        System.out.println(name + " is leaving! xxx>");


    }

}
