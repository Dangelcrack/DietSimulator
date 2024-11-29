package com.github.dangelcrack.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Dieta {
    protected int id;
    protected String name;
    protected String description;
    protected TypeDiet typeDiet;
    protected List<Comida> comidas;


    public Dieta(int id, String name, String description, TypeDiet typeDiet,List<Comida> comidas) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.typeDiet = typeDiet;
        this.comidas = comidas;
    }
    public Dieta(String nombre) {
        this.name = nombre;
    }

    public Dieta() {
    this(-1,"","",null,null);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeDiet getTypeDiet() {
        return typeDiet;
    }
    public static Dieta fromString(String dietaString) {
        if (dietaString == null || dietaString.isEmpty()) {
            return null;
        }
        return new Dieta(dietaString);
    }


    public void setTypeDiet(TypeDiet typeDiet) {
        this.typeDiet = typeDiet;
    }
    public String getFoodsString(Dieta dieta){
        List<Comida> comidasList = dieta.getFoods();
        return comidasList == null ? "" :
                comidasList.stream()
                        .map(Comida::getName)
                        .collect(Collectors.joining(", "));
    }
    public List<Comida> getFoods(){
        return comidas;
    }
    public void setFoods(List<Comida> comidas){
        this.comidas = comidas;
    }
    public void addFood(Comida comida){
        if(comidas == null){
            comidas = new ArrayList<>();
        }
        if(!comidas.contains(comida)){
            comidas.add(comida);
        }
    }
    public Comida getFood(Comida comida){
        Comida result = null;
        if(comidas == null){
            int i = comidas.indexOf(comida);
            if(i>=0){
                result = comidas.get(i);
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Dieta dieta = (Dieta) object;
        return id == dieta.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Dieta{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", typeDiet=" + typeDiet +
                '}';
    }
}
