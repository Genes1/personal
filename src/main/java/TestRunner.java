import java.awt.image.AreaAveragingScaleFilter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRunner {

    /*
        Things to figure out
            - getting customers to wait in order semaphore
            - distributing orders to waiting cooks (order has ref to customer?)
            - cooks cooking all their ingredients
     */

    static int numCooks, numCustomers, numTables;




    public static void main(String[] args) throws InterruptedException {

        numCooks = 2;
        numCustomers = 3;
        numTables = 2;



        Thread[] cooks = new Thread[numCooks];
        for (int i = 0; i < cooks.length; i++) {
            cooks[i] = new Thread(new Cook( "cook" + i));
            cooks[i].start();
        }



        Thread[] customers = new Thread[numCustomers];
        for (int i = 0; i < customers.length; i++) {
            customers[i] = new Thread(new Customer("customer" + i, genFoodList()));
            customers[i].start();
        }


//        if (Cook.orders.isEmpty()) {
//            for (int i = 0; i < cooks.length; i++) {
//                cooks[i].interrupt();
//            }
//        }



        try {
            /*
             * Wait for customers to finish
             * -- you need to add some code here...
             */


            for (int i = 0; i < customers.length; i++)
                customers[i].join();

            System.out.println("customers are gone!");
            /*
             * Then send cooks home...
             * The easiest way to do this might be the following, where we interrupt their
             * threads. There are other approaches though, so you can change this if you
             * want to.
             */
            for (int i = 0; i < cooks.length; i++) {
                cooks[i].interrupt();
            }


            for (int i = 0; i < cooks.length; i++)
                cooks[i].join();

        } catch (InterruptedException e) {
            System.out.println("Simulation thread interrupted.");
        }


        System.out.println("Simulation ended.");

    }



    public static ArrayList<Food> genFoodList() {
        ArrayList<Food> ls = new ArrayList<Food>();
        if (Math.random() < 0.5) ls.add(new Food("soda", 100));
        if (Math.random() < 0.5) ls.add(new Food("subs", 400));
        if (Math.random() < 0.5) ls.add(new Food("fries", 200));
        if (Math.random() < 0.5) ls.add(new Food("pizza", 500));
        if (ls.isEmpty()) ls.add(new Food("soda", 100));
        return ls;
    }




}
