package student;

import java.util.HashMap;
import java.util.Objects;

public class WeightedNode<T> {

    private Integer weight;
    private T node;

    public WeightedNode(T node){
        this.node = node;
    }

    public  void setWeight(Integer i){
        this.weight = i;
    }
    public Integer getWeight() {
        return this.weight;
    }

}
