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
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.ScrollPane;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.milaifontanals.model.Usuari;

/**
 *
 * @author anna9
 */
public class GestioUsuaris  extends JFrame {
    
    private JPanel gridEsquerra;
    private JPanel gridDret;
    private JButton nouUsuari;
    private JButton esborrarUsuari;
    private JButton editarUsuari;
    private JLabel textUsuaris;
    
    private JTable taula;
    private DefaultTableModel tUsuaris;
    
    private List<Usuari> usuaris = new ArrayList();
    private List<String> columnesTaula = new ArrayList();
    
    private GridBagConstraints gbc;
    
    public GestioUsuaris(String titol) {
        setTitle(titol);
        setLayout(new GridBagLayout());
        entornGrafic();
        pack();
        setVisible(true);
        //setResizable(false);
        setLocation(10,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void entornGrafic(){
        setFont(new Font("Arial", Font.PLAIN, 12));
        
        partEsquerra();
        parDreta();
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        grid(0, 0, 1, 1, 1, 1);
        add(gridEsquerra);
        add(gridDret);
    }

    private void partEsquerra(){
        gridEsquerra = new JPanel();
        gridEsquerra.setLayout(new GridBagLayout());
        
        textUsuaris = new JLabel("Usuaris", JLabel.LEFT);
        textUsuaris.setFont(new Font("Arial", Font.BOLD, 16));
        textUsuaris.setBorder(BorderFactory.createLineBorder(textUsuaris.getBackground(), 5));
        
        gbc = new GridBagConstraints();
        borderElementsGrid(10, 10, 10, 10);
      
        usuaris.add(new Usuari(1, "Usuari 1", "Cognom 1", new Date(2000-1900,1-1,1), "usuari", "password"));
        usuaris.add(new Usuari(2, "Usuari 1", "Cognom 1", new Date(2000-1900,1-1,1), "usuari", "password"));
        usuaris.add(new Usuari(13, "Usuari 1", "Cognom 1", new Date(2000-1900,1-1,1), "usuari", "password"));
        usuaris.add(new Usuari(14, "Usuari 1", "Cognom 1", new Date(2000-1900,1-1,1), "usuari", "password"));
        

        definirTaula();
        
        nouUsuari = new JButton("Nou");
        esborrarUsuari = new JButton("Esborrar");
        editarUsuari = new JButton("Editar");
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        borderElementsGrid(20, 10, 10, 10);
        grid(0, 0, 1, 1, 1, 1);
        gridEsquerra.add(textUsuaris, gbc);
        grid(0, 1, 1.0, 1.0, 3, 1);
        gridEsquerra.add(new JScrollPane(taula), gbc);
        borderElementsGrid(10, 30, 10, 20);
        grid(0, 2, 1, 1, 1, 1);
        gridEsquerra.add(nouUsuari, gbc);
        borderElementsGrid(10, 20, 10, 20);
        grid(1, 2, 1, 1, 1, 1);
        gridEsquerra.add(esborrarUsuari, gbc);
        borderElementsGrid(10, 20, 10, 30);
        grid(2, 2, 1, 1, 1, 1);
        gridEsquerra.add(editarUsuari, gbc);
        borderElementsGrid(10, 10, 10, 10);
        
    }

    private void parDreta(){
        gridDret = new JPanel();
        gridDret.setLayout(new GridBagLayout());
    }
    
    private void definirTaula(){
         taula = new JTable();
  
        tUsuaris = new DefaultTableModel();//columnNames, usuaris.size());
        columnesTaula.add("Nom");
        columnesTaula.add("1r. Cognom");
        columnesTaula.add("2n. Cognom");
        columnesTaula.add("Data naixement");
        columnesTaula.add("Login");
        columnesTaula.add("Password");
        
        for (int i = 0; i < columnesTaula.size(); i++){
            tUsuaris.addColumn(columnesTaula.get(i));
        }
        
        for (Usuari usus : usuaris) {
            Object[] fila = new Object[6];
            fila[0] = usus.getNom();
            fila[1] = usus.getCognom1();
            fila[2] = usus.getCognom2();
            fila[3] = usus.getDataNaixementFormatada();
            fila[4] = usus.getLogin();
            fila[5] = usus.getPasswrdHash();

            tUsuaris.addRow(fila);
        }
        
        taula.setModel(tUsuaris);
     
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
