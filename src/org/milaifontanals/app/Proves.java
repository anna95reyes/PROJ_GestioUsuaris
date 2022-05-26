/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.app;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.milaifontanals.interficie.CPSingleton;
import org.milaifontanals.interficie.GestioProjectesException;
import org.milaifontanals.interficie.IGestioProjectes;
import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.Usuari;

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
            System.out.println("APLICACIÓ VIA FACTORIA");
            // Aquesta aplicació està preparada per obtenir un objecte de la capa invocant el constructor
            // sense paràmetres
            cp = CPSingleton.getGestorProjectes(nomCapa);
            System.out.println("Capa de persistència creada");
        } catch (Exception ex) {
            System.out.println("");
            infoError(ex);
            return;
        }
        
        
        try {
            List<Usuari> usuaris = new ArrayList();
            usuaris = cp.getLlistaUsuaris();
            for (Usuari usu: usuaris) {
                System.out.println("USUARI: "+usu.getNom());
            }
        } catch (Exception ex){
            
        }
        
        try {
            System.out.println("ExisteixUsuari: " + cp.existeixUsuari(1));
        } catch (Exception ex){
            
        }
        
        try {
            System.out.println("ExisteixProjecte: " + cp.existeixProjecte(1));
        } catch (Exception ex){
            
        }
        
        
        try {
            System.out.println("Usuari: " + cp.getUsuari(1).getNom());
        } catch (Exception ex){
            
        }
        
        
        
//        Usuari nouUsuari = new Usuari(14, "Prova", "Prova", "Prova", new Date(2000-1900, 5-1, 14), "login prova", "password prova");
//        try {
//            System.out.println("ADD Usuari");
//            cp.addUsuari(nouUsuari);
//            cp.commit();
//        } catch (Exception ex){
//            
//        }
        
//        try {
//            System.out.println("UPDATE Usuari");
//            cp.modificarUsuari(new Usuari(14, "Prova 2", "Prova 2", "Prova 2", new Date(2000-1900, 5-1, 20), "login prova 2", "password prova 2"));
//            cp.commit();
//        } catch (Exception ex){
//            
//        }
        
//        try {
//            System.out.println("DELETE Usuari");
//            cp.deleteUsuari(14);
//            cp.commit();
//        } catch (Exception ex){
//            
//        }
        
        
        try {
            List<Projecte> projectes = new ArrayList();
            projectes = cp.getLlistaProjectes();
            for (Projecte proj: projectes) {
                System.out.println("PROJECTE: "+proj.getNom());
            }
        } catch (Exception ex){
            
        }
        
        try {
            System.out.println("Projecte: " + cp.getProjecte(1).getNom());
        } catch (Exception ex){
            
        }
        
        try {
            System.out.println("Rol: " + cp.getRol(1).getNom());
        } catch (Exception ex){
            
        }
        
        try {
            System.out.println("Projectes Assignats: ");
            List<Projecte> projectes = new ArrayList();
            projectes = cp.getLlistaProjectesAssignats(cp.getUsuari(1));
            for (Projecte proj: projectes) {
                System.out.println("PROJECTE ASSIGNAT: " + proj.getId() + " " + proj.getNom());
            }
        } catch (Exception ex){
            System.out.println("ERROR LLISTA ASSIGNATS: " + ex.getMessage());
        }
        
//        try {
//            Usuari usuari = cp.getUsuari(2);
//            Projecte projecte = cp.getProjecte(3);
//            System.out.println("Assignar projecte");
//            cp.assignarProjecte(usuari, projecte);
//            cp.commit();
//        } catch (Exception ex){
//            System.out.println("ERROR ASSIGNAR PROJECTE: " + ex.getMessage());
//        }
//        
//        try {
//            Usuari usuari = cp.getUsuari(1);
//            Projecte projecte = cp.getProjecte(1);
//            System.out.println("Dessasignar projecte");
//            cp.desassignarProjecte(usuari, projecte);
//            cp.commit();
//        } catch (Exception ex){
//            System.out.println("ERROR DESASSIGNAR PROJECTE: " + ex.getMessage());
//        }
        
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
