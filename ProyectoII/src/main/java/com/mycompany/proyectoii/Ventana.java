package com.mycompany.proyectoii;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
        panel.setLayout(new GridLayout(6, 1)); // Aumentamos el tamaño del grid para el nuevo botón

        JButton btnAgregarCartera = new JButton("Agregar Cartera Ministerial");
        JButton btnAgregarFuncionario = new JButton("Agregar Funcionario");
        JButton btnMostrarCarteras = new JButton("Mostrar Carteras y Funcionarios");
        JButton btnExportarCSV = new JButton("Exportar a CSV");

        panel.add(btnAgregarCartera);
        panel.add(btnAgregarFuncionario);
        panel.add(btnMostrarCarteras);
        panel.add(btnExportarCSV); // Agregamos el botón para exportar

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
    // Solicitar el nombre de la cartera
    String nombreCartera = JOptionPane.showInputDialog("Nombre de la Cartera:");
    
    // Verificamos si la cartera existe en el sistema
    if (nombreCartera == null || nombreCartera.isEmpty() || 
            sistema.getCarteras().get(nombreCartera) == null) {
        JOptionPane.showMessageDialog(null, "La cartera no existe o no ha sido ingresada. Por favor, ingrese una cartera válida.",
                "Error", JOptionPane.ERROR_MESSAGE);
        return; // Si la cartera no existe, salimos de la acción
    }
    
    // Si la cartera existe, solicitamos los datos del funcionario
    String nombreFuncionario = JOptionPane.showInputDialog("Nombre del Funcionario:");
    String puesto = JOptionPane.showInputDialog("Puesto del Funcionario:");

    // Verificamos si los datos del funcionario son válidos
    if (nombreFuncionario != null && !nombreFuncionario.isEmpty() && puesto != null && !puesto.isEmpty()) {
        sistema.agregarFuncionario(nombreCartera, nombreFuncionario, puesto);
        textArea.append("Funcionario " + nombreFuncionario + " agregado a " + nombreCartera + ".\n");
    } else {
        JOptionPane.showMessageDialog(null, "Datos del funcionario inválidos. Asegúrese de ingresar un nombre y puesto válidos.", "Error", JOptionPane.ERROR_MESSAGE);
    }
});


        // Acción para mostrar carteras y funcionarios
        btnMostrarCarteras.addActionListener(e -> {
            textArea.setText(""); // Limpiar el área de texto
            for (CarteraMinisterial cartera : sistema.getCarteras().values()) {
                textArea.append(cartera.toString() + "\n");
                for (Funcionario funcionario : cartera.getFuncionarios()) {
                    textArea.append(" - " + funcionario + "\n\n");
                }
            }
        });

        // Acción para exportar a CSV
        btnExportarCSV.addActionListener(e -> exportarACSV());
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
        CarteraMinisterial cartera = sistema.getCarteras().get(nombreCartera);
        if (cartera != null) {
            String[] nombresFuncionarios = cartera.getFuncionarios().stream().map(Funcionario::getNombre).toArray(String[]::new);
            String funcionarioSeleccionado = (String) JOptionPane.showInputDialog(null, "Seleccione un funcionario a editar:", "Editar Funcionario",
                    JOptionPane.QUESTION_MESSAGE, null, nombresFuncionarios, nombresFuncionarios[0]);

            if (funcionarioSeleccionado != null) {
                Funcionario funcionario = cartera.buscarFuncionario(funcionarioSeleccionado);
                String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre del Funcionario:", funcionario.getNombre());
                String nuevoPuesto = JOptionPane.showInputDialog("Nuevo puesto del Funcionario:", funcionario.getPuesto());
                funcionario.setNombre(nuevoNombre);
                funcionario.setPuesto(nuevoPuesto);
                textArea.append("Funcionario " + funcionarioSeleccionado + " editado.\n");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Cartera no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarFuncionario() {
        String nombreCartera = JOptionPane.showInputDialog("Nombre de la Cartera:");
        CarteraMinisterial cartera = sistema.getCarteras().get(nombreCartera);
        if (cartera != null) {
            String[] nombresFuncionarios = cartera.getFuncionarios().stream().map(Funcionario::getNombre).toArray(String[]::new);
            if (nombresFuncionarios.length == 0) {
                JOptionPane.showMessageDialog(this, "No hay funcionarios para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String funcionarioSeleccionado = (String) JOptionPane.showInputDialog(null, "Seleccione un funcionario a eliminar:", "Eliminar Funcionario",
                    JOptionPane.QUESTION_MESSAGE, null, nombresFuncionarios, nombresFuncionarios[0]);

            if (funcionarioSeleccionado != null) {
                boolean eliminado = cartera.eliminarFuncionario(funcionarioSeleccionado);
                if (eliminado) {
                    textArea.append("Funcionario " + funcionarioSeleccionado + " eliminado de " + nombreCartera + ".\n");
                } else {
                    JOptionPane.showMessageDialog(this, "Funcionario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Cartera no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportarACSV() {
        String rutaEscritorio = System.getProperty("user.home") + "/Desktop/funcionarios.csv"; // Ruta del escritorio
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaEscritorio))) {
            writer.write("Cartera,Funcionario,Puesto\n"); // Encabezados del CSV

            // Iterar sobre las carteras y sus funcionarios
            for (CarteraMinisterial cartera : sistema.getCarteras().values()) {
                for (Funcionario funcionario : cartera.getFuncionarios()) {
                    writer.write(cartera.getNombre() + "," + funcionario.getNombre() + "," + funcionario.getPuesto() + "\n"); // Escribe cada fila
                }
            }
            JOptionPane.showMessageDialog(null, "Archivo CSV exportado exitosamente en: " + rutaEscritorio);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al exportar el archivo CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Ventana ventana = new Ventana();
            ventana.setVisible(true);
        });
    }
}
    