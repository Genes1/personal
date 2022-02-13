import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class FoodWorker implements Runnable {

    Food food;
    Semaphore semaphore;
    CountDownLatch ownerLatch;

    FoodWorker(CountDownLatch doneSignal, Food food, Semaphore s) {
        this.ownerLatch = doneSignal;
        this.food = food;
        this.semaphore = s;
    }


    @Override
    public void run() {
        System.out.println(food + " queued (machine->foodworker) " + food.cookTime);
        try {
            Thread.sleep(food.cookTime);
            System.out.println("    " + food + " was cooked in " + food.cookTime + " ms");
            ownerLatch.countDown(); // let the order know this ingredient is done
            semaphore.release(); // release for the machine to reacquire cooking capacity
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
