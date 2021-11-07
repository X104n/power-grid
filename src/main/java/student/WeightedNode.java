package student;

import java.util.HashMap;
import java.util.Objects;

public class WeightedNode<T> {

    private HashMap<T, Integer> weight;


    public HashMap<T, Integer> getWeight() {
        return weight;
    }

    public void newWeight(T t, Integer i){
        weight.put(t, i);
    }

    public void addWeight(T t, Integer i){
        weight.put(t, weight.get(t) + i);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeightedNode<?> that = (WeightedNode<?>) o;
        return Objects.equals(weight, that.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight);
    }
}
