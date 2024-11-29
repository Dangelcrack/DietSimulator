package com.github.dangelcrack.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Comida {
    protected int id;
    protected String name;
    protected TypeFood typeFood;
    protected int calories;
    protected List<Dieta> dietaList;

    public Comida( String name, TypeFood typeFood, int calories,List<Dieta> dietaList) {
        this.name = name;
        this.typeFood = typeFood;
        this.calories = calories;
        this.dietaList = dietaList;
    }

    public Comida() {
        this("",null,0,null);

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

    public List<Dieta> getDietaList() {
        return dietaList;
    }

    public void setDietaList(List<Dieta> dietaList) {
        this.dietaList = dietaList;
    }
    public void addDiet(Dieta dieta){
        if(dietaList == null){
            dietaList = new ArrayList<>();
        }
        if(!dietaList.contains(dieta)){
            dietaList.add(dieta);
        }
    }
    public void removeDiet(Dieta dieta){
        if(dietaList != null){
            dietaList.remove(dieta);
        }
    }
    public Dieta getComida(Dieta dieta){
        Dieta result = null;
        if(dietaList != null){
            int i = dietaList.indexOf(dieta);
            result = dietaList.get(i);
        }
        return result;
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
