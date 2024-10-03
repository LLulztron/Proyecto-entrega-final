package com.mycompany.proyectoii;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Ventana extends JFrame {
    private Sistema sistema;
    private JTextArea textArea;

    public Ventana() {
        sistema = new Sistema(); // Inicializa el sistema y carga datos
        configurarVentana(); // Configura la ventana principal
    }

    private void configurarVentana() {
        setTitle("Manejo de Funcionarios Públicos");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        configurarMenu(); // Configura el menú

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JButton btnAgregarCartera = new JButton("Agregar Cartera Ministerial");
        JButton btnAgregarFuncionario = new JButton("Agregar Funcionario");
        JButton btnMostrarCarteras = new JButton("Mostrar Carteras y Funcionarios");
        
        panel.add(btnAgregarCartera);
        panel.add(btnAgregarFuncionario);
        panel.add(btnMostrarCarteras);
        
        add(panel, BorderLayout.SOUTH);
        
        // Acción para agregar una cartera
        btnAgregarCartera.addActionListener(e -> {
            String nombreCartera = JOptionPane.showInputDialog("Nombre de la Cartera:");
            if (nombreCartera != null && !nombreCartera.isEmpty()) {
                sistema.agregarCartera(nombreCartera);
                textArea.append("Cartera " + nombreCartera + " agregada.\n");
            }
        });

        // Acción para agregar un funcionario
        btnAgregarFuncionario.addActionListener(e -> {
            String nombreCartera = JOptionPane.showInputDialog("Nombre de la Cartera:");
            String nombreFuncionario = JOptionPane.showInputDialog("Nombre del Funcionario:");
            String puesto = JOptionPane.showInputDialog("Puesto del Funcionario:");
            if (nombreCartera != null && nombreFuncionario != null && puesto != null) {
                sistema.agregarFuncionario(nombreCartera, nombreFuncionario, puesto);
                textArea.append("Funcionario " + nombreFuncionario + " agregado a " + nombreCartera + ".\n");
            }
        });

        // Acción para mostrar carteras y funcionarios
        btnMostrarCarteras.addActionListener(e -> {
            textArea.setText(""); // Limpiar el área de texto
            for (CarteraMinisterial cartera : sistema.getCarteras().values()) {
                textArea.append(cartera.toString() + "\n");
                for (Funcionario funcionario : cartera.getFuncionarios()) {
                    textArea.append("  - " + funcionario + "\n");
                }
            }
        });
    }

    private void configurarMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuEdicion = new JMenu("Edición");
        JMenuItem itemEditar = new JMenuItem("Editar Funcionario");
        itemEditar.addActionListener(e -> editarFuncionario());
        menuEdicion.add(itemEditar);

        JMenu menuEliminacion = new JMenu("Eliminación");
        JMenuItem itemEliminar = new JMenuItem("Eliminar Funcionario");
        itemEliminar.addActionListener(e -> eliminarFuncionario());
        menuEliminacion.add(itemEliminar);

        menuBar.add(menuEdicion);
        menuBar.add(menuEliminacion);
        setJMenuBar(menuBar);
    }

    private void editarFuncionario() {
        String nombreCartera = JOptionPane.showInputDialog("Nombre de la Cartera:");
        String nombreFuncionario = JOptionPane.showInputDialog("Nombre del Funcionario a editar:");

        try {
            if (nombreCartera != null && nombreFuncionario != null) {
                CarteraMinisterial cartera = sistema.getCarteras().get(nombreCartera);
                if (cartera != null) {
                    Funcionario funcionario = cartera.buscarFuncionario(nombreFuncionario);
                    if (funcionario != null) {
                        String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre del Funcionario:", funcionario.getNombre());
                        String nuevoPuesto = JOptionPane.showInputDialog("Nuevo puesto del Funcionario:", funcionario.getPuesto());
                        funcionario.setNombre(nuevoNombre);
                        funcionario.setPuesto(nuevoPuesto);
                        textArea.append("Funcionario " + nombreFuncionario + " editado.\n");
                    } else {
                        throw new FuncionarioNoEncontradoException("Funcionario no encontrado.");
                    }
                }
            }
        } catch (FuncionarioNoEncontradoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarFuncionario() {
        String nombreCartera = JOptionPane.showInputDialog("Nombre de la Cartera:");
        String nombreFuncionario = JOptionPane.showInputDialog("Nombre del Funcionario a eliminar:");

        if (nombreCartera != null && nombreFuncionario != null) {
            CarteraMinisterial cartera = sistema.getCarteras().get(nombreCartera);
            if (cartera != null) {
                boolean eliminado = cartera.eliminarFuncionario(nombreFuncionario);
                if (eliminado) {
                    textArea.append("Funcionario " + nombreFuncionario + " eliminado de " + nombreCartera + ".\n");
                } else {
                    JOptionPane.showMessageDialog(this, "Funcionario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Ventana ventana = new Ventana();
            ventana.setVisible(true);
        });
    }
}
