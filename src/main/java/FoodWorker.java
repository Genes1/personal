import java.util.concurrent.CountDownLatch;

public class FoodWorker implements Runnable {

    String food;
    int cookTime;
    CountDownLatch ownerLatch;

    FoodWorker(CountDownLatch doneSignal, int cooktime, String foodType) {
        this.ownerLatch = doneSignal;
        this.cookTime = cooktime;
        this.food = foodType;
    }


    @Override
    public void run() {
        System.out.println(food + " queued");
        try {
            Thread.sleep(cookTime);
            System.out.println("    "+ food + " was cooked in " + cookTime + " ms");
            ownerLatch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
