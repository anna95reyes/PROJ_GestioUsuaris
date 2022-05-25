/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.Usuari;

/**
 *
 * @author anna9
 */
public class AssignarProjectesAUsuari extends JFrame {
    
    private JPanel panell;
    private JLabel labelFiltre;
    private JTextField textFiltre;
    private JButton buttonBuscarFiltre;
    private JButton buttonGuardar;
    private JButton buttonCancelar;
    
    private JTable taulaProjectesNoAssignats;
    private DefaultTableModel tProjectesNoAssignats;
    
    private List<Usuari> usuaris = new ArrayList();
    private List<Projecte> projectes = new ArrayList();
    private List<String> columnesTaulaProjectes = new ArrayList();
    
    private GestioBotons gestionador;
    
    private GridBagConstraints gbc;
    
    public AssignarProjectesAUsuari(String titol) {
        setTitle(titol);
        setLayout(new GridBagLayout());
        entornGrafic();
        pack();
        setVisible(true);
        //setResizable(false);
        setLocation(10,10);
        setDefaultCloseOperation(JFrame.ABORT);
        
        
    }

    private void entornGrafic() {
        setFont(new Font("Arial", Font.PLAIN, 19));
        
        gestionador = new GestioBotons();
        
        panell = new JPanel();
        panell.setLayout(new GridBagLayout());
        
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        labelFiltre = new JLabel("Nom projecte: ", JLabel.LEFT);
        textFiltre = new JTextField(10);
        buttonBuscarFiltre = new JButton("Buscar");
        buttonGuardar = new JButton("Guardar");
        buttonCancelar = new JButton("Cancelar");
        
        buttonBuscarFiltre.addActionListener(gestionador);
        buttonGuardar.addActionListener(gestionador);
        buttonCancelar.addActionListener(gestionador);
        
        buttonGuardar.setEnabled(false);
        
        definirTaulaProjectesAssignats();
        taulaProjectesNoAssignats.setRowSelectionAllowed(true);
        taulaProjectesNoAssignats.setColumnSelectionAllowed(false);
        taulaProjectesNoAssignats.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taulaProjectesNoAssignats.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) // si hi ha canvi de seleccio en el JTable
                {
                    if (taulaProjectesNoAssignats.getSelectedRow() > -1){
                        buttonGuardar.setEnabled(true);
                    }
                    
                }
            }
        });
        
        
        borderElementsGrid(10, 10, 10, 10);
        grid(0, 0, 1, 1, 1, 1);
        panell.add(labelFiltre, gbc);
        
        borderElementsGrid(10, 10, 10, 10);
        grid(1, 0, 1, 1, 1, 1);
        panell.add(textFiltre, gbc);
        
        borderElementsGrid(10, 10, 10, 10);
        grid(2, 0, 1, 1, 1, 1);
        panell.add(buttonBuscarFiltre, gbc);
        
        gbc.fill = GridBagConstraints.BOTH;
        borderElementsGrid(10, 10, 10, 10);
        grid(0, 1, 1, 1, 3, 10);
        panell.add(new JScrollPane(taulaProjectesNoAssignats), gbc);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        borderElementsGrid(10, 10, 10, 10);
        grid(0, 2, 1, 1, 1, 1);
        panell.add(buttonGuardar, gbc);
        
        borderElementsGrid(10, 10, 10, 10);
        grid(2, 2, 1, 1, 1, 1);
        panell.add(buttonCancelar, gbc);
        setLayout(new BorderLayout());
        //panell.setBackground(Color.YELLOW);
        add(panell, BorderLayout.CENTER);
    }
    
    private void definirTaulaProjectesAssignats(){
        taulaProjectesNoAssignats = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // fem que no sigui editable
            }
        };
        
        usuaris.add(new Usuari(1, "Usuari 1", "Cognom 1", new Date(2000-1900,1-1,1), "usuari", "password"));
        usuaris.add(new Usuari(2, "Usuari 1", "Cognom 1", new Date(2000-1900,1-1,1), "usuari", "password"));
        usuaris.add(new Usuari(3, "Usuari 1", "Cognom 1", new Date(2000-1900,1-1,1), "usuari", "password"));
        usuaris.add(new Usuari(4, "Usuari 1", "Cognom 1", new Date(2000-1900,1-1,1), "usuari", "password"));
        
        projectes.add(new Projecte(1, "Projecte 1", "Projecte 1", usuaris.get(0)));
        projectes.add(new Projecte(2, "Projecte 2", "Projecte 2", usuaris.get(1)));
        projectes.add(new Projecte(3, "Projecte 3", "Projecte 3", usuaris.get(2)));
        projectes.add(new Projecte(4, "Projecte 4", "Projecte 4", usuaris.get(3)));
        projectes.add(new Projecte(12, "Projecte 12", "Projecte 12", usuaris.get(0)));
        
        tProjectesNoAssignats = new DefaultTableModel();//columnNames, usuaris.size());
        columnesTaulaProjectes.add("Nom");
        columnesTaulaProjectes.add("Descripcio");
        
        for (int i = 0; i < columnesTaulaProjectes.size(); i++){
            tProjectesNoAssignats.addColumn(columnesTaulaProjectes.get(i));
        }
        
        for (Projecte proj: projectes) {
            Object[] fila = new Object[2];
            fila[0] = proj.getNom();
            fila[1] = proj.getDescripcio();

            tProjectesNoAssignats.addRow(fila);
        }
        
        taulaProjectesNoAssignats.setModel(tProjectesNoAssignats);
        
    }
    
    
    class GestioBotons implements ActionListener { 
        
        @Override
        public void actionPerformed(ActionEvent e) { 
            String quinBotoPremut = e.getActionCommand(); 
            JButton botoPremut = (JButton) e.getSource();
            
            if (botoPremut.equals(buttonBuscarFiltre)) {
                int files = tProjectesNoAssignats.getRowCount();
                if (textFiltre.getText().length() > 0){
                    for (int i = files - 1; i >=0 ; i--) {
                        tProjectesNoAssignats.removeRow(i);
                    }
                    for (int i = 0; i < projectes.size(); i++){
                        if (projectes.get(i).getNom().toLowerCase().contains(textFiltre.getText().toLowerCase())){
                            Object[] fila = new Object[2];
                            fila[0] = projectes.get(i).getNom();
                            fila[1] = projectes.get(i).getDescripcio();
                            tProjectesNoAssignats.addRow(fila);
                        }
                    }
                    
                }
                
                
            } else if (botoPremut.equals(buttonGuardar)) {
                if (taulaProjectesNoAssignats.getSelectedRow() > -1){
                        
                }
            } else if (botoPremut.equals(buttonCancelar)) {
                dispose();
            } 
        }
    
    }
    
    
    private void grid(int gridx, int gridy, double weightx, double weighty, int gridwidth, int ipady) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.gridwidth = gridwidth;
        gbc.ipady = ipady;
        
    }
    
    private void borderElementsGrid(int top, int left, int bottom, int right) {
        gbc.insets = new Insets(top, left, bottom, right);
    }
    
    
}
