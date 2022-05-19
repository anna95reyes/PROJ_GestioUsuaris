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
import org.milaifontanals.interficie.GestioProjectesException;
import org.milaifontanals.interficie.IGestioProjectes;

/**
 *
 * @author anna9
 */
public class Proves {

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

        Properties props = new Properties();
        try {
            props.load(new FileReader(nomFitxer));
        } catch (FileNotFoundException ex) {
            System.out.println("No es troba fitxer de propietats " + nomFitxer);
            infoError(ex);
            System.out.println("Avortem aplicació");
            return;
        } catch (IOException ex) {
            System.out.println("Error en carregar fitxer de propietats " + nomFitxer);
            infoError(ex);
            System.out.println("Avortem aplicació");
            return;
        }
        String nomCapa = props.getProperty("nomCapa");
        if (nomCapa == null || nomCapa.equals("")) {
            System.out.println("Fitxer de propietats " + nomFitxer + " no conté propietat nomCapa");
            System.out.println("Avortem aplicació");
            return;
        }

        IGestioProjectes cp = null;
        try {
            System.out.println("Intentem crear capa de persistència");
            Class compo = Class.forName(nomCapa);
            cp = (IGestioProjectes) compo.newInstance();
            
            
            
            System.out.println("Capa de persistència creada");
        } catch (Exception ex) {
            System.out.println("Problema en crear capa de persistència");
            infoError(ex);
            return;
        }
        
        try {
            cp.closeCapa();
            System.out.println("Capa tancada");
        } catch (GestioProjectesException ex) {
            System.out.println("Error en tancar la capa de persistència");
            infoError(ex);
        }
    }
    
    private static void infoError(Throwable aux) {
        do {
            if (aux.getMessage() != null) {
                System.out.println("\t" + aux.getMessage());
            }
            aux = aux.getCause();
        } while (aux != null);

    }
}
