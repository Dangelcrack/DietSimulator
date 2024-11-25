package com.github.dangelcrack.model.entity;

import java.util.Objects;

public class Comida {
    protected int id;
    protected String name;
    protected TypeFood typeFood;
    protected int calories;

    public Comida(int id, String name, TypeFood typeFood, int calories) {
        this.id = id;
        this.name = name;
        this.typeFood = typeFood;
        this.calories = calories;
    }

    public Comida() {
        this(-1,"",null,0);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeFood getTypeFood() {
        return typeFood;
    }

    public void setTypeFood(TypeFood typeFood) {
        this.typeFood = typeFood;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Comida comida = (Comida) object;
        return id == comida.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Comida{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", typeFood=" + typeFood +
                ", calories=" + calories +
                '}';
    }
}
