package com.github.dangelcrack.model.entity;

import java.util.Objects;

public class Dieta {
    protected int id;
    protected String name;
    protected String description;
    protected TypeDiet typeDiet;

    public Dieta(int id, String name, String description, TypeDiet typeDiet) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.typeDiet = typeDiet;
    }

    public Dieta() {
    this(-1,"","",null);
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

    public void setTypeDiet(TypeDiet typeDiet) {
        this.typeDiet = typeDiet;
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
