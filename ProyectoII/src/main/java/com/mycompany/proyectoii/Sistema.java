package com.mycompany.proyectoii;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sistema {
    private Map<String, CarteraMinisterial> carteras;

    public Sistema() {
        carteras = new HashMap<>();
        cargarDatos();
    }

    public void agregarCartera(String nombreCartera) {
        if (!carteras.containsKey(nombreCartera)) {
            carteras.put(nombreCartera, new CarteraMinisterial(nombreCartera));
        }
    }

    public void agregarFuncionario(String nombreCartera, String nombreFuncionario, String puesto) {
        CarteraMinisterial cartera = carteras.get(nombreCartera);
        if (cartera != null) {
            cartera.agregarFuncionario(new Funcionario(nombreFuncionario, puesto));
        }
    }
    public void agregarFuncionario(String nombreCartera, String nombreFuncionario) {
        CarteraMinisterial cartera = carteras.get(nombreCartera);
        if (cartera != null) {
            cartera.agregarFuncionario(new Funcionario(nombreFuncionario));
        }
    }
    public Map<String, CarteraMinisterial> getCarteras() {
        return carteras;
    }

    public void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("carteras.dat"))) {
            oos.writeObject(carteras);
        } catch (IOException e) {
            System.out.println("Error al guardar datos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void cargarDatos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("carteras.dat"))) {
            carteras = (Map<String, CarteraMinisterial>) ois.readObject();
        } catch (FileNotFoundException e) {
            // No se encontró el archivo, se crea uno nuevo
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
        }
    }

    // Método para buscar funcionarios por puesto
    public List<Funcionario> buscarFuncionariosPorPuesto(String puesto) {
        List<Funcionario> resultado = new ArrayList<>();
        for (CarteraMinisterial cartera : carteras.values()) {
            for (Funcionario f : cartera.getFuncionarios()) {
                if (f.getPuesto().equalsIgnoreCase(puesto)) {
                    resultado.add(f);
                }
            }
        }
        return resultado;
    }
}
