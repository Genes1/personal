public class Food {
    public final int cookTime;
    public final String name;

    Food(String name, int cookTime) {
        this.cookTime = cookTime;
        this.name = name;
    }


    public String toString() {
        return this.name;
    }

}
