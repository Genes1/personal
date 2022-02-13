public class Food {
    public int cookTime = 0;
    public final String name;

    Food(String name) {
        this.name = name;
    }

    public void setCookTime(int ck) {
        this.cookTime = ck;
    }


    public String toString() {
        return this.name;
    }

}
