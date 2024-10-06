package com.mycompany.proyectoii;

import java.io.Serializable;

public class Funcionario extends Elemento implements Serializable {
    private String nombre;
    private String puesto;

    public Funcionario(String nombre, String puesto) {
        this.nombre = nombre;
        this.puesto = puesto;
    }

    @Override
    public String getNombre() {
        return nombre; // Implementación del método abstracto
    }

    // Métodos getters y setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    @Override
    public String toString() {
        return "Funcionario: \n" + "nombre: " + nombre + '\'' + ", puesto: " + puesto + '\'';
    }
}
