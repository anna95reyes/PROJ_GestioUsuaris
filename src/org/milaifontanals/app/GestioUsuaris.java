/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.Usuari;

/**
 *
 * @author anna9
 */
public class GestioUsuaris extends JFrame {
    
    private JPanel panelEsquerra;
    private JPanel panelDret;
    private JButton buttonNouUsuari;
    private JButton buttonEsborrarUsuari;
    private JButton buttonEditarUsuari;
    private JLabel labelUsuaris;
    private JLabel labelUsuariNom;
    private JLabel labelUsuariCognom1;
    private JLabel labelUsuariCognom2;
    private JLabel labelUsuariDataNaix;
    private JLabel labelUsuariLogin;
    private JLabel labelUsuariPassword;
    private JTextField textUsuariNom;
    private JTextField textUsuariCognom1;
    private JTextField textUsuariCognom2;
    private JFormattedTextField textUsuariDataNaix;
    private JTextField textUsuariLogin;
    private JPasswordField textUsuariPassword;
    private JButton buttonGuardarUsuari;
    private JButton buttonCancelarUsuari;
    private JLabel labelProjectes;
    private JButton buttonAssignarProjecte;
    private JButton buttonDessasignarProjecte;
    private JButton buttonCancelarProjecte;
    
    private JTable taulaUsuaris;
    private DefaultTableModel tUsuaris;
    
    private JTable taulaProjectesAssignats;
    private DefaultTableModel tProjectesAssignats;
    
    private List<Usuari> usuaris = new ArrayList();
    private List<Projecte> projectes = new ArrayList();
    private List<String> columnesTaulaUsuaris = new ArrayList();
    private List<String> columnesTaulaProjectes = new ArrayList();
    
    private GestioBotons gestionador;
    
    private GridBagConstraints gbc;
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    public enum Estat{
        VIEW,
        MODIFICACIO,
        ALTA
    }
    
    public GestioUsuaris(String titol) {
        setTitle(titol);
        setLayout(new GridBagLayout());
        entornGrafic();
        pack();
        setVisible(true);
        setResizable(false);
        setLocation(10,10);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
    private void entornGrafic(){
        setFont(new Font("Arial", Font.PLAIN, 12));
        
        gestionador = new GestioBotons();
        
        partEsquerra();
        omplirFormulari();
        parDreta();
        
        gbc.fill = GridBagConstraints.BOTH;
        grid(0, 0, 1, 1, 1, 1);
        add(panelEsquerra, gbc);
        gbc.fill = GridBagConstraints.BOTH;
        grid(1, 0, 1, 1, 1, 1);
        add(panelDret, gbc);
    }

    private void partEsquerra(){
        panelEsquerra = new JPanel();
        panelEsquerra.setLayout(new GridBagLayout());
        
        labelUsuaris = new JLabel("Usuaris", JLabel.LEFT);
        labelUsuaris.setFont(new Font("Arial", Font.BOLD, 16));
        labelUsuaris.setBorder(BorderFactory.createLineBorder(labelUsuaris.getBackground(), 5));
        
        gbc = new GridBagConstraints();
        borderElementsGrid(10, 10, 10, 10);
      
        definirTaulaUsuaris();
        taulaUsuaris.setRowSelectionAllowed(true);
        taulaUsuaris.setColumnSelectionAllowed(false);
        taulaUsuaris.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taulaUsuaris.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) // si hi ha canvi de seleccio en el JTable
                {
                    omplirFormulari();
                }
            }
        });
        
        
        buttonNouUsuari = new JButton("Nou");
        buttonEsborrarUsuari = new JButton("Esborrar");
        buttonEditarUsuari = new JButton("Editar");
        
        buttonNouUsuari.addActionListener(gestionador);
        buttonEsborrarUsuari.addActionListener(gestionador);
        buttonEditarUsuari.addActionListener(gestionador);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        borderElementsGrid(20, 10, 10, 10);
        grid(0, 0, 1, 1, 1, 1);
        panelEsquerra.add(labelUsuaris, gbc);
        
        gbc.fill = GridBagConstraints.BOTH;
        borderElementsGrid(10, 10, 10, 10);
        grid(0, 1, 1, 1, 3, 1);
        panelEsquerra.add(new JScrollPane(taulaUsuaris), gbc);
        
        borderElementsGrid(10, 10, 10, 10);
        grid(0, 2, 1, 1, 1, 1);
         gbc.fill = GridBagConstraints.HORIZONTAL;
        panelEsquerra.add(buttonNouUsuari, gbc);
        borderElementsGrid(10, 10, 10, 10);
        grid(1, 2, 1, 1, 1, 1);
        panelEsquerra.add(buttonEsborrarUsuari, gbc);
        borderElementsGrid(10, 10, 10, 10);
        grid(2, 2, 1, 1, 1, 1);
        panelEsquerra.add(buttonEditarUsuari, gbc);
        borderElementsGrid(10, 10, 10, 10);
        
    }

    private void parDreta(){
        panelDret = new JPanel();
        panelDret.setLayout(new GridBagLayout());
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formulariUsuari();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        projectesAssignats();
    }
    
    private void definirTaulaUsuaris(){
        taulaUsuaris = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // fem que no sigui editable
            }
        };
  
        //------------
        usuaris.add(new Usuari(1, "Usuari 1", "Cognom 1", "Cognom 1", new Date(2000-1900,1-1,1), "usuari1", "password1"));
        usuaris.add(new Usuari(2, "Usuari 2", "Cognom 2", new Date(2000-1900,1-1,1), "usuari2", "password2"));
        usuaris.add(new Usuari(3, "Usuari 3", "Cognom 3", new Date(2000-1900,1-1,1), "usuari3", "password3"));
        usuaris.add(new Usuari(4, "Usuari 4", "Cognom 4", new Date(2000-1900,1-1,1), "usuari4", "password4"));
        //---------------
        
        tUsuaris = new DefaultTableModel();//columnNames, usuaris.size());
        columnesTaulaUsuaris.add("Nom");
        columnesTaulaUsuaris.add("1r. Cognom");
        columnesTaulaUsuaris.add("2n. Cognom");
        columnesTaulaUsuaris.add("Data naixement");
        columnesTaulaUsuaris.add("Login");
        columnesTaulaUsuaris.add("Password");
        
        for (int i = 0; i < columnesTaulaUsuaris.size(); i++){
            tUsuaris.addColumn(columnesTaulaUsuaris.get(i));
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
        
        taulaUsuaris.setModel(tUsuaris);
     
    }
    
    private void formulariUsuari(){
        labelUsuariNom = new JLabel("Nom: ", JLabel.RIGHT);
        labelUsuariCognom1 = new JLabel("1r. Cognom: ", JLabel.RIGHT);
        labelUsuariCognom2 = new JLabel("2n. Cognom: ", JLabel.RIGHT);
        labelUsuariDataNaix = new JLabel("Data de naixement: ", JLabel.RIGHT);
        labelUsuariLogin = new JLabel("Login: ", JLabel.RIGHT);
        labelUsuariPassword = new JLabel("Password: ", JLabel.RIGHT);
    
        textUsuariNom = new JTextField(20);
        textUsuariCognom1 = new JTextField(20);
        textUsuariCognom2 = new JTextField(20);
        textUsuariDataNaix = new JFormattedTextField(sdf);
        textUsuariDataNaix.setToolTipText("YYYY-MM-DD");
        textUsuariDataNaix.setColumns(10);
        textUsuariLogin = new JTextField(20);
        textUsuariPassword = new JPasswordField(20);
                
        buttonGuardarUsuari = new JButton("Guardar");
        buttonCancelarUsuari = new JButton("Cancelar");
        
        buttonGuardarUsuari.addActionListener(gestionador);
        buttonCancelarUsuari.addActionListener(gestionador);
        
        borderElementsGrid(5, 10, 5, 10);
        grid(0, 0, 1, 1, 1, 1);
        panelDret.add(labelUsuariNom, gbc);
        
        borderElementsGrid(5, 10, 5, 10);
        grid(0, 1, 1, 1, 1, 1);
        panelDret.add(labelUsuariCognom1, gbc);
        
        borderElementsGrid(5, 10, 5, 10);
        grid(0, 1, 1, 1, 1, 1);
        panelDret.add(labelUsuariCognom2, gbc);
        
        borderElementsGrid(5, 10, 5, 10);
        grid(0, 2, 1, 1, 1, 1);
        panelDret.add(labelUsuariCognom2, gbc);
        
        borderElementsGrid(5, 10, 5, 10);
        grid(0, 3, 1, 1, 1, 1);
        panelDret.add(labelUsuariDataNaix, gbc);
        
        borderElementsGrid(5, 10, 5, 10);
        grid(0, 4, 1, 1, 1, 1);
        panelDret.add(labelUsuariLogin, gbc);
        
        borderElementsGrid(5, 10, 5, 10);
        grid(0, 5, 1, 1, 1, 1);
        panelDret.add(labelUsuariPassword, gbc);
        
        borderElementsGrid(5, 10, 5, 10);
        grid(1, 0, 1, 1, 2, 1);
        panelDret.add(textUsuariNom, gbc);
        
        borderElementsGrid(5, 10, 5, 10);
        grid(1, 1, 1, 1, 2, 1);
        panelDret.add(textUsuariCognom1, gbc);
        
        borderElementsGrid(5, 10, 5, 10);
        grid(1, 1, 1, 1, 2, 1);
        panelDret.add(textUsuariCognom2, gbc);
        
        borderElementsGrid(5, 10, 5, 10);
        grid(1, 2, 1, 1, 2, 1);
        panelDret.add(textUsuariCognom2, gbc);
        
        borderElementsGrid(5, 10, 5, 10);
        grid(1, 3, 1, 1, 2, 1);
        panelDret.add(textUsuariDataNaix, gbc);
        
        borderElementsGrid(5, 10, 5, 10);
        grid(1, 4, 1, 1, 2, 1);
        panelDret.add(textUsuariLogin, gbc);
        
        borderElementsGrid(5, 10, 5, 10);
        grid(1, 5, 1, 1, 2, 1);
        panelDret.add(textUsuariPassword, gbc);
        
        //gbc.fill = GridBagConstraints.CENTER;
        borderElementsGrid(5, 10, 5, 10);
        grid(0, 6, 1, 1, 1, 1);
        panelDret.add(buttonGuardarUsuari, gbc);
        
        //gbc.fill = GridBagConstraints.CENTER;
        borderElementsGrid(5, 10, 5, 10);
        grid(2, 6, 1, 1, 1, 1);
        panelDret.add(buttonCancelarUsuari, gbc);
    }
    
    private void projectesAssignats(){
        labelProjectes = new JLabel("Projectes ", JLabel.LEFT);
        labelProjectes.setFont(new Font("Arial", Font.BOLD, 16));
        labelProjectes.setBorder(BorderFactory.createLineBorder(labelUsuaris.getBackground(), 5));
        
        definirTaulaProjectesAssignats();
        taulaProjectesAssignats.setRowSelectionAllowed(true);
        taulaProjectesAssignats.setColumnSelectionAllowed(false);
        taulaProjectesAssignats.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        /*taulaUsuaris.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) // si hi ha canvi de seleccio en el JTable
                {
                    //Assignar, Dessasignar, Cancelar
                }
            }
        });*/
        
        buttonAssignarProjecte = new JButton("Assignar");
        buttonDessasignarProjecte = new JButton("Dessasignar");
        buttonCancelarProjecte = new JButton("Cancelar");
        
        buttonAssignarProjecte.addActionListener(gestionador);
        buttonDessasignarProjecte.addActionListener(gestionador);
        buttonCancelarProjecte.addActionListener(gestionador);
        
        borderElementsGrid(25, 10, 10, 10);
        grid(0, 7, 1, 1, 1, 1);
        panelDret.add(labelProjectes, gbc);
        
        borderElementsGrid(10, 10, 10, 10);
        grid(0, 8, 1, 1, 3, 1);
        panelDret.add(new JScrollPane(taulaProjectesAssignats), gbc);
        
        //panelDret.add(new JScrollPane(taulaProjectesAssignats), gbc);
        borderElementsGrid(10, 10, 10, 10);
        grid(0, 9, 1, 1, 1, 1);
        panelDret.add(buttonAssignarProjecte, gbc);
        borderElementsGrid(10, 10, 10, 10);
        grid(1, 9, 1, 1, 1, 1);
        panelDret.add(buttonDessasignarProjecte, gbc);
        borderElementsGrid(10, 10, 10, 10);
        grid(2, 9, 1, 1, 1, 1);
        panelDret.add(buttonCancelarProjecte, gbc);
        
    }
    
    private void definirTaulaProjectesAssignats(){
        taulaProjectesAssignats = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // fem que no sigui editable
            }
        };
        
        projectes.add(new Projecte(1, "Projecte 1", "Projecte 1", usuaris.get(0)));
        projectes.add(new Projecte(2, "Projecte 2", "Projecte 2", usuaris.get(1)));
        projectes.add(new Projecte(3, "Projecte 3", "Projecte 3", usuaris.get(2)));
        projectes.add(new Projecte(4, "Projecte 4", "Projecte 4", usuaris.get(3)));
        
        tProjectesAssignats = new DefaultTableModel();
        columnesTaulaProjectes.add("Nom");
        columnesTaulaProjectes.add("Descripcio");
        
        for (int i = 0; i < columnesTaulaProjectes.size(); i++){
            tProjectesAssignats.addColumn(columnesTaulaProjectes.get(i));
        }
        
        for (Projecte proj: projectes) {
            Object[] fila = new Object[2];
            fila[0] = proj.getNom();
            fila[1] = proj.getDescripcio();

            tProjectesAssignats.addRow(fila);
        }
        
        taulaProjectesAssignats.setModel(tProjectesAssignats);
     
    }
    
    private void novaFinestra() {
        try {           
            AssignarProjectesAUsuari frameAssignarProjectes = new AssignarProjectesAUsuari("Projectes no assignats");
            setVisible(false);
            frameAssignarProjectes.setLocationRelativeTo(this);
            frameAssignarProjectes.setVisible(true);
            frameAssignarProjectes.setResizable(true);
            
        } catch (Exception e) {
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

    private void omplirFormulari() {
        int fila = taulaUsuaris.getSelectedRow();
        
        System.out.println(fila);
        
        if (fila > -1) {
            textUsuariNom.setText((String)taulaUsuaris.getValueAt(fila, 0));
            textUsuariCognom1.setText((String)taulaUsuaris.getValueAt(fila, 1));
            textUsuariCognom2.setText((String)taulaUsuaris.getValueAt(fila, 2));
            textUsuariDataNaix.setText((String)taulaUsuaris.getValueAt(fila, 3));
            textUsuariLogin.setText((String)taulaUsuaris.getValueAt(fila, 4));        
            textUsuariPassword.setText((String)taulaUsuaris.getValueAt(fila, 5));         
        }
    }
    
    class GestioBotons implements ActionListener { 
        @Override public void actionPerformed(ActionEvent e) { 
            String quinBotoPremut = e.getActionCommand(); 
            JButton botoPremut = (JButton) e.getSource();
            if (botoPremut.equals(buttonNouUsuari)) {
                if (taulaUsuaris.getSelectedRow() > -1) {
                    taulaUsuaris.clearSelection();
                }
            } else if (botoPremut.equals(buttonEsborrarUsuari)) {
                
            } else if (botoPremut.equals(buttonEditarUsuari)) {
                
            } else if (botoPremut.equals(buttonGuardarUsuari)) {
                
            } else if (botoPremut.equals(buttonCancelarUsuari)) {
                omplirFormulari();
            } else if (botoPremut.equals(buttonAssignarProjecte)) {
                if (taulaProjectesAssignats.getSelectedRow() > -1) {
                    novaFinestra();
                }
            } else if (botoPremut.equals(buttonDessasignarProjecte)) {
                
            } else if (botoPremut.equals(buttonCancelarProjecte)) {
                if (taulaProjectesAssignats.getSelectedRow() > -1) {
                    taulaProjectesAssignats.clearSelection();
                } 
            }
            
            
        }
    
    }
}
