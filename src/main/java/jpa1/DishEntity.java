package jpa1;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table (name = "Dishes")
public class DishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "id")
    private long id;
    @Column (nullable = false)
    private String dishName;
    private double cost;
    private double weight;
    private double discount;

    public DishEntity() {
        super();
    }

    public DishEntity(String dishName, double cost, double weight, double discount) {
        this.dishName = dishName;
        this.cost = cost;
        this.weight = weight;
        this.discount = discount;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishEntity dishEntity = (DishEntity) o;
        return id == dishEntity.id &&
                Double.compare(dishEntity.cost, cost) == 0 &&
                Double.compare(dishEntity.weight, weight) == 0 &&
                Double.compare(dishEntity.discount, discount) == 0 &&
                Objects.equals(dishName, dishEntity.dishName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dishName, cost, weight, discount);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", dishName='" + dishName + '\'' +
                ", cost=" + cost +
                ", weight=" + weight +
                ", discount=" + discount +
                '}';
    }

}
