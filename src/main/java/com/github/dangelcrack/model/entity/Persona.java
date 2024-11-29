package com.github.dangelcrack.model.entity;

import java.util.Objects;

public class Persona {
    protected int id;
    protected String name;
    protected int altura;
    protected int peso;
    protected int edad;
    protected Dieta dieta;

    public Persona(int id, String name, int altura, int peso, int edad,Dieta dieta) {
        this.id = id;
        this.name = name;
        this.altura = altura;
        this.peso = peso;
        this.edad = edad;
        this.dieta = dieta;
    }

    public Persona() {
        this(-1,"",-1,-1,-1,null);
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

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Dieta getDieta() {
        return dieta;
    }

    public void setDieta(Dieta dieta) {
        this.dieta = dieta;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Persona persona = (Persona) object;
        return id == persona.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", altura=" + altura +
                ", peso=" + peso +
                ", edad=" + edad +
                '}';
    }
}
