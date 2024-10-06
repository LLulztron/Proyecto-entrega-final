package com.mycompany.proyectoii;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarteraMinisterial extends Elemento implements Serializable {
    private String nombre;
    private List<Funcionario> funcionarios;

    public CarteraMinisterial(String nombre) {
        this.nombre = nombre;
        this.funcionarios = new ArrayList<>();
    }

    @Override
    public String getNombre() {
        return nombre; // ImplementaciÃ³n del mÃ©todo abstracto
    }

    public void agregarFuncionario(Funcionario funcionario) {
        funcionarios.add(funcionario);
    }

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public Funcionario buscarFuncionario(String nombreFuncionario) {
        for (Funcionario f : funcionarios) {
            if (f.getNombre().equals(nombreFuncionario)) {
                return f;
            }
        }
        return null;
    }

    public boolean eliminarFuncionario(String nombreFuncionario) {
        return funcionarios.removeIf(f -> f.getNombre().equals(nombreFuncionario));
    }

    public int contarFuncionarios() {
        return funcionarios.size(); // Contar funcionarios en la cartera
    }

    @Override
    public String toString() {
        return "CarteraMinisterial{" +
                "nombre='" + nombre + '\'' +
                '}';
    }
}
