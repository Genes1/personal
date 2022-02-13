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

    static int numCooks, numCustomers, numTables, machineCapacity;




    public static void main(String[] args) throws InterruptedException {

        numCooks = 1;
        numCustomers = 2;
        numTables = 1;
        machineCapacity = 2;

        System.out.println("Simulation started.");


        Machine[] m = {
            new Machine("fries", 150, TestRunner.machineCapacity),
            new Machine("soda", 50, TestRunner.machineCapacity),
            new Machine("subs", 250, TestRunner.machineCapacity),
            new Machine("pizza", 500, TestRunner.machineCapacity)
        };


        Cook.machineMap.put("fries", m[0]);
        Cook.machineMap.put("soda", m[1]);
        Cook.machineMap.put("subs", m[2]);
        Cook.machineMap.put("pizza", m[3]);

        Thread[] machines = new Thread[4];
        for (int i = 0; i < machines.length; i++) {
            machines[i] = new Thread(m[i]);
            machines[i].start();
        }

        Thread[] cooks = new Thread[numCooks];
        for (int i = 0; i < cooks.length; i++) {
            cooks[i] = new Thread(new Cook( "cook" + i));
            cooks[i].start();
        }


        // test pizza data
        ArrayList<Food> ls = new ArrayList<Food>();
        ls.add(new Food("pizza"));
        ls.add(new Food("pizza"));
        ls.add(new Food("pizza"));


        Customer.count = numCustomers; // be sure to initialize this before making any customers
        Thread[] customers = new Thread[numCustomers];
        for (int i = 0; i < customers.length; i++) {
            customers[i] = new Thread(new Customer("customer" + i, genFoodList()));
//            customers[i] = new Thread(new Customer("customer" + i, ls));
            customers[i].start();
        }




        try {


            for (int i = 0; i < customers.length; i++)
                customers[i].join();
            System.out.println("customers are gone!");

            for (int i = 0; i < cooks.length; i++) {
                cooks[i].interrupt();
                cooks[i].join();
            }


            // might be the wrong order
            for (int i = 0; i < machines.length; i++) {
                machines[i].interrupt();
                machines[i].join();
            }


        } catch (InterruptedException e) {
            System.out.println("Simulation thread interrupted.");
        }


        System.out.println("Simulation ended.");

    }



    public static ArrayList<Food> genFoodList() {
        ArrayList<Food> ls = new ArrayList<Food>();
        if (Math.random() < 0.5) ls.add(new Food("soda"));
        if (Math.random() < 0.5) ls.add(new Food("subs"));
        if (Math.random() < 0.5) ls.add(new Food("fries"));
        if (Math.random() < 0.5) ls.add(new Food("pizza"));
        if (ls.isEmpty()) ls.add(new Food("soda"));
        return ls;
    }




}
