/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.app;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.milaifontanals.interficie.CPSingleton;
import org.milaifontanals.interficie.GestioProjectesException;
import org.milaifontanals.interficie.IGestioProjectes;

/**
 *
 * @author anna9
 */
public class Main {

    private static IGestioProjectes cp;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String nomFitxer = null;
        if (args.length == 0) {
            nomFitxer = "infoCapa.properties";
        } else {
            nomFitxer = args[0];
        }
        
        crearCapaPersistencia(nomFitxer);
        
        GestioUsuaris gestio = new GestioUsuaris("Gestió d'usuaris", cp);
        
    }
    
    private static void crearCapaPersistencia(String nomFitxer){
        
        Properties props = new Properties();
        try {
            props.load(new FileReader(nomFitxer));
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "No es troba fitxer de propietats " + nomFitxer, 
                                "Error Capa de Persistencia", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error en carregar fitxer de propietats " + nomFitxer, 
                                "Error Capa de Persistencia", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nomCapa = props.getProperty("nomCapa");
        if (nomCapa == null || nomCapa.equals("")) {
            JOptionPane.showMessageDialog(null, "Fitxer de propietats " + nomFitxer + " no conté propietat nomCapa", 
                                "Error Capa de Persistencia", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cp = null;
        
        try {
            cp = CPSingleton.getGestorProjectes(nomCapa);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en crear capa de persistencia", 
                                "Error Capa de Persistencia", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
}
