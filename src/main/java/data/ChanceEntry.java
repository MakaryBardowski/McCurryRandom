package data;

public class ChanceEntry<T>{
    private float chance;
    private T item;

    public ChanceEntry(float chance, T item){
        this.chance = chance;
        this.item = item;
    }

    public float getChance() {
        return chance;
    }

    public void setChance(float chance) {
        this.chance = chance;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
