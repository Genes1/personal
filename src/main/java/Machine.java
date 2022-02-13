import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Machine implements Runnable {

    private final int cookTime;
    public String foodType;
    public ArrayList<Food> foods = new ArrayList<>();
    public ArrayList<CountDownLatch> latches = new ArrayList<>();
    public Semaphore semaphore;

    public Machine(String foodType, int cookTime, int machineCapacity) {
        this.foodType = foodType;
        this.cookTime = cookTime;
        this.semaphore = new Semaphore(machineCapacity);
    }


    @Override
    public void run() {

        System.out.println("machine " + foodType + " started");

        while (!Thread.currentThread().isInterrupted()) {

                try {

                    Food f = null;
                    CountDownLatch l = null;

                    synchronized (foods) {
                        synchronized (latches) {
                            if (!latches.isEmpty()) {
                                semaphore.acquire();
                                f = foods.remove(0);
                                f.setCookTime(this.cookTime);
                                l = latches.remove(0);
                                Thread t = new Thread(new FoodWorker(l, f, semaphore));
                                t.start();
                            }
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        }

        System.out.println("machine " + foodType + " shut down");


    }

}
