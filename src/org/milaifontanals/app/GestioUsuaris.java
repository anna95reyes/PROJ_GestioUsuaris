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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
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
import org.hibernate.type.ObjectType;
import org.milaifontanals.interficie.CPSingleton;
import org.milaifontanals.interficie.GestioProjectesException;
import org.milaifontanals.interficie.IGestioProjectes;
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
    
    private List<String> columnesTaulaUsuaris = new ArrayList();
    private List<String> columnesTaulaProjectes = new ArrayList();
    
    private GestioBotons gestionador;
    private GridBagConstraints gbc;
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    private IGestioProjectes cp;
    
    private int filaSeleccionada = 0;
    private int idUsuari = 0;
    
    public enum Estat {
        VIEW,
        MODIFICACIO_USUARI,
        MODIFICACIO_PROJECTE,
        ALTA
    }
    
    private Estat estat = Estat.VIEW;
    
    public GestioUsuaris(String titol, IGestioProjectes interficie) {
        cp = interficie;
        setTitle(titol);
        setLayout(new GridBagLayout());
        entornGrafic();
        pack();
        setVisible(true);
        setResizable(false);
        setLocation(10,10);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e); 
                //JOptionPane.showConfirmDialog(null,"Are sure to close!");
                int resposta = JOptionPane.showConfirmDialog(null, "Segur que vols tancar l'aplicacio?", "Tancar aplicacio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resposta == 0) {
                    tancarCapaPersistencia();
                }
            }

        });
    }
    
    
    
    private void entornGrafic(){
        setFont(new Font("Arial", Font.PLAIN, 12));
        
        gestionador = new GestioBotons();
        
        partEsquerra();
        omplirFormulari();
        parDreta();
        
        canviEstat(Estat.VIEW);
        
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
                    if (taulaUsuaris.getSelectedRow() > -1){
                        canviEstat(Estat.MODIFICACIO_USUARI);
                    }
                    omplirFormulari();
                    filaSeleccionada = taulaUsuaris.getSelectedRow();
                    idUsuari = (int)taulaUsuaris.getValueAt(taulaUsuaris.getSelectedRow(), 0);
                    
                    netejarTaulaProjectesAssignats();
                    omplirTaulaProjectesAssignats();
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
        tUsuaris = new DefaultTableModel();//columnNames, usuaris.size());
        columnesTaulaUsuaris.add("Id");
        columnesTaulaUsuaris.add("Nom");
        columnesTaulaUsuaris.add("1r. Cognom");
        columnesTaulaUsuaris.add("2n. Cognom");
        columnesTaulaUsuaris.add("Data naixement");
        columnesTaulaUsuaris.add("Login");
        columnesTaulaUsuaris.add("Password");
        
        for (int i = 0; i < columnesTaulaUsuaris.size(); i++){
            tUsuaris.addColumn(columnesTaulaUsuaris.get(i));
        }
        
        omplirTaulaUsuaris();
        
        taulaUsuaris.setModel(tUsuaris);
     
    }
    
    private void omplirTaulaUsuaris() {
        try {
            for (Usuari usus : cp.getLlistaUsuaris()) {
                Object[] fila = new Object[7];
                fila[0] = usus.getId();
                fila[1] = usus.getNom();
                fila[2] = usus.getCognom1();
                fila[3] = usus.getCognom2();
                fila[4] = usus.getDataNaixementFormatada();
                fila[5] = usus.getLogin();
                fila[6] = usus.getPasswrdHash();
                
                tUsuaris.addRow(fila);
            }
        } catch (GestioProjectesException ex) {
            System.out.println("Problema en omplir la taula d'usuaris: " + ex.getMessage());
        }
    }

    private void netejarTaulaUsuaris() {
        if (tUsuaris.getRowCount() > 0) {
            for (int i = tUsuaris.getRowCount() - 1; i >=0 ; i--) {
                tUsuaris.removeRow(i);
            }
        }
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
        taulaProjectesAssignats.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) // si hi ha canvi de seleccio en el JTable
                {
                    if (taulaUsuaris.getSelectedRow() > -1){
                        canviEstat(Estat.MODIFICACIO_PROJECTE);
                    }
                }
            }
        });
        
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

        tProjectesAssignats = new DefaultTableModel();
        columnesTaulaProjectes.add("Id");
        columnesTaulaProjectes.add("Nom");
        columnesTaulaProjectes.add("Descripcio");
        
        for (int i = 0; i < columnesTaulaProjectes.size(); i++){
            tProjectesAssignats.addColumn(columnesTaulaProjectes.get(i));
        }
        
        omplirTaulaProjectesAssignats();
        
        taulaProjectesAssignats.setModel(tProjectesAssignats);
     
    }

    private void omplirTaulaProjectesAssignats() {
        try {
            if (filaSeleccionada > -1 && idUsuari > 0) {
                
                List<Projecte> projectes = cp.getLlistaProjectesAssignats(cp.getUsuari(idUsuari));
                for (int i = 0; i < projectes.size(); i++) {
                    Object[] fila = new Object[3];
                    fila[0] = projectes.get(i).getId();
                    fila[1] = projectes.get(i).getNom();
                    fila[2] = projectes.get(i).getDescripcio();

                    tProjectesAssignats.addRow(fila);  
                }
            }
        } catch (GestioProjectesException ex) {
            System.out.println("Problema en omplir la taula de projectes assignats: "+ ex.getMessage());
        }
    }

    private void netejarTaulaProjectesAssignats() {
        if (tProjectesAssignats.getRowCount() > 0) {
            for (int i = tProjectesAssignats.getRowCount() - 1; i >=0 ; i--) {
                tProjectesAssignats.removeRow(i);
            }
        }
    }
    
    private void novaFinestra() {
        try {           
            AssignarProjectesAUsuari frameAssignarProjectes = new AssignarProjectesAUsuari("Projectes no assignats", cp, idUsuari, tProjectesAssignats);
            setVisible(false);
            frameAssignarProjectes.setLocationRelativeTo(this);
            frameAssignarProjectes.setVisible(true);
            frameAssignarProjectes.setResizable(true);
            
        } catch (Exception e) {
        }
    }
    
    
    
    

    private void omplirFormulari() {
        int fila = taulaUsuaris.getSelectedRow();
        
        if (fila > -1) {
            textUsuariNom.setText((String)taulaUsuaris.getValueAt(fila, 1));
            textUsuariCognom1.setText((String)taulaUsuaris.getValueAt(fila, 2));
            textUsuariCognom2.setText((String)taulaUsuaris.getValueAt(fila, 3));
            textUsuariDataNaix.setText((String)taulaUsuaris.getValueAt(fila, 4));
            textUsuariLogin.setText((String)taulaUsuaris.getValueAt(fila, 5));        
            textUsuariPassword.setText((String)taulaUsuaris.getValueAt(fila, 6));         
        }
    }
    
    private Boolean comprobarDadesFormulari() {
        Date data = dataFormulari(textUsuariDataNaix.getText());
        String cognom2 = textUsuariCognom2.getText();

        if (textUsuariCognom2.getText().length() < 1){
            cognom2 = null;
        } 
        return Usuari.comprobarDadesObligatories(textUsuariNom.getText()) &&
               Usuari.comprobarDadesObligatories(textUsuariCognom1.getText()) &&
               Usuari.comprobarDadesOpcionals(cognom2) &&
               Usuari.comprobarDataNaixement(data) &&
               Usuari.comprobarDadesObligatories(textUsuariLogin.getText()) &&
               Usuari.comprobarDadesObligatories(textUsuariPassword.getText());
    }

    public Date dataFormulari(String dataFormulari) {
        int any = Integer.valueOf(dataFormulari.substring(0, 4));
        int mes = Integer.valueOf(dataFormulari.substring(5, 7));
        int dia = Integer.valueOf(dataFormulari.substring(8, 10));
        Date data = new Date(any-1900, mes-1, dia);
        return data;
    }
    
    private void netejarFormulari() {
        int fila = taulaUsuaris.getSelectedRow();
        
        if (fila == -1) {
            textUsuariNom.setText(null);
            textUsuariCognom1.setText(null);
            textUsuariCognom2.setText(null);
            textUsuariDataNaix.setText(null);
            textUsuariLogin.setText(null);        
            textUsuariPassword.setText(null);         
        }
    }
    
    class GestioBotons implements ActionListener { 
        @Override
        public void actionPerformed(ActionEvent e) { 
            String quinBotoPremut = e.getActionCommand(); 
            JButton botoPremut = (JButton) e.getSource();
            
            //botoPremut.setEnabled(false);
            
            if (botoPremut.equals(buttonNouUsuari)) {
                if (taulaUsuaris.getSelectedRow() > -1) {
                    taulaUsuaris.clearSelection();
                }
                netejarFormulari();
                canviEstat(Estat.ALTA);
                
            } else if (botoPremut.equals(buttonEsborrarUsuari)) {
                if (taulaUsuaris.getSelectedRow() > -1) {
                    try {
                        cp.deleteUsuari(idUsuari);
                        cp.commit();
                    } catch (GestioProjectesException ex) {
                        JOptionPane.showMessageDialog(null, "No es pot esborrar aquest usuari perque esta sent utilitzat", 
                                "Error eliminar usuari", JOptionPane.ERROR_MESSAGE);
                    }
                    taulaUsuaris.clearSelection();
                    netejarTaulaUsuaris();
                    omplirTaulaUsuaris();
                    netejarFormulari();
                    netejarTaulaProjectesAssignats();
                    canviEstat(Estat.VIEW);
                    
                }
            } else if (botoPremut.equals(buttonEditarUsuari)) {
                if (taulaUsuaris.getSelectedRow() > -1) {
                    canviEstat(Estat.MODIFICACIO_USUARI);
                }
            } else if (botoPremut.equals(buttonGuardarUsuari)) {
                Date data = dataFormulari(textUsuariDataNaix.getText());
                String cognom2 = textUsuariCognom2.getText();
            
                if (textUsuariCognom2.getText().length() < 1){
                    cognom2 = null;
                } 
                if (comprobarDadesFormulari()) {
                    if (estat == Estat.MODIFICACIO_USUARI) {
                        Usuari usu = new Usuari (idUsuari, textUsuariNom.getText(),textUsuariCognom1.getText(), 
                                                 cognom2, data, textUsuariLogin.getText(), 
                                                 textUsuariPassword.getText());
                        try {
                            cp.modificarUsuari(usu);
                            cp.commit();
                            netejarTaulaUsuaris();
                            omplirTaulaUsuaris();
                        } catch (GestioProjectesException ex) {
                            System.out.println("Problemes al modificar usauri: " + ex);
                        }
                    } else if (estat == Estat.ALTA) {
                        if (taulaUsuaris.getSelectedRow() > -1) {
                            taulaUsuaris.clearSelection();
                        }
                        
                        try {
                            Usuari usu = new Usuari (cp.getLlistaUsuaris().size()+1, textUsuariNom.getText(),
                                                     textUsuariCognom1.getText(), cognom2, 
                                                     data, textUsuariLogin.getText(), textUsuariPassword.getText());
                            cp.addUsuari(usu);
                            cp.commit();
                            netejarTaulaUsuaris();
                            omplirTaulaUsuaris();
                        } catch (GestioProjectesException ex) {
                            System.out.println("Problemes al crear usauri: " + ex);
                        }
                    }

                    netejarFormulari();
                    netejarTaulaProjectesAssignats();
                    canviEstat(Estat.VIEW);
                } else {
                    JOptionPane.showMessageDialog(null, "Les dades del formulari no son correctes", "Error formulari", JOptionPane.ERROR_MESSAGE);
                }
            } else if (botoPremut.equals(buttonCancelarUsuari)) {
                omplirFormulari();
                if (taulaUsuaris.getSelectedRow() > -1) {
                    taulaUsuaris.clearSelection();
                    netejarTaulaProjectesAssignats();
                    canviEstat(Estat.VIEW);
                }
                netejarFormulari();
            } else if (botoPremut.equals(buttonAssignarProjecte)) {
                canviEstat(Estat.MODIFICACIO_PROJECTE);
                novaFinestra();
            } else if (botoPremut.equals(buttonDessasignarProjecte)) {
                if (taulaProjectesAssignats.getSelectedRow() > -1) {
                    try {
                        int idProjecte = (int)taulaProjectesAssignats.getValueAt(taulaProjectesAssignats.getSelectedRow(), 0);
                        cp.desassignarProjecte(cp.getUsuari(idUsuari), cp.getProjecte(idProjecte));
                        cp.commit();
                        netejarTaulaProjectesAssignats();
                        omplirTaulaProjectesAssignats();
                    } catch (GestioProjectesException ex) {
                        System.out.println("Problemes al dessasignar un projecte: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                    canviEstat(Estat.MODIFICACIO_PROJECTE);
                }
            } else if (botoPremut.equals(buttonCancelarProjecte)) {
                if (taulaProjectesAssignats.getSelectedRow() > -1) {
                    taulaProjectesAssignats.clearSelection();
                } 
                canviEstat(Estat.MODIFICACIO_USUARI);
            }
            
        }
    
    }
    
    
    private void canviEstat(Estat estatNou)
    {
        estat = estatNou;
        if (estat == Estat.VIEW)
        {
            buttonNouUsuari.setEnabled(true);
            buttonEsborrarUsuari.setEnabled(false);
            buttonEditarUsuari.setEnabled(false);
            
            buttonGuardarUsuari.setEnabled(false);
            buttonCancelarUsuari.setEnabled(false);
            
            buttonAssignarProjecte.setEnabled(false);
            buttonDessasignarProjecte.setEnabled(false);
            buttonCancelarProjecte.setEnabled(false);
        } 
        else if (estat == Estat.MODIFICACIO_USUARI)
        {
            buttonNouUsuari.setEnabled(true);
            buttonEsborrarUsuari.setEnabled(true);
            buttonEditarUsuari.setEnabled(true);
            
            buttonGuardarUsuari.setEnabled(true);
            buttonCancelarUsuari.setEnabled(true);
            
            buttonAssignarProjecte.setEnabled(true);
            buttonDessasignarProjecte.setEnabled(false);
            buttonCancelarProjecte.setEnabled(false);
        }
        else if (estat == Estat.MODIFICACIO_PROJECTE)
        {
            buttonNouUsuari.setEnabled(false);
            buttonEsborrarUsuari.setEnabled(false);
            buttonEditarUsuari.setEnabled(false);
            
            buttonGuardarUsuari.setEnabled(false);
            buttonCancelarUsuari.setEnabled(false);
            
            buttonAssignarProjecte.setEnabled(true);
            buttonDessasignarProjecte.setEnabled(true);
            buttonCancelarProjecte.setEnabled(true);
        }
        else if (estat == Estat.ALTA)
        {
            netejarFormulari();
            buttonNouUsuari.setEnabled(true);
            buttonEsborrarUsuari.setEnabled(false);
            buttonEditarUsuari.setEnabled(false);
            
            buttonGuardarUsuari.setEnabled(true);
            buttonCancelarUsuari.setEnabled(true);
            
            buttonAssignarProjecte.setEnabled(true);
            buttonDessasignarProjecte.setEnabled(false);
            buttonCancelarProjecte.setEnabled(true);
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
    
    private void tancarCapaPersistencia(){
        try {
            cp.closeCapa();
            System.out.println("Capa tancada");
        } catch (GestioProjectesException ex) {
            System.out.println("Error en tancar la capa de persistència");
        }
    }
}
