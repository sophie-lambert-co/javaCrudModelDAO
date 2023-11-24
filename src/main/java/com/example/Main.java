package com.example;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

import com.example.dao.DatabaseConnection;
import com.example.servlet.AlimentServlet;
import com.example.servlet.CouleurServlet;
import com.example.servlet.TypeAlimentServlet;
import com.example.servlet.DataAccessServletTest;

//http://localhost:8080/webapps/jsp/CouleurListForm.jsp
//http://localhost:8080/aliment/*  pour recupere le Json de la table aliment 

/**
 * Classe principale représentant le point d'entrée de l'application.
 * Elle configure et lance un serveur Tomcat embarqué avec des servlets associés.
 */
public class Main {
    public static void main(String[] args) throws LifecycleException {
      
        // Création d'une instance Tomcat pour le serveur embarqué
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        
        // Initialisation de la connexion à la base de données
        String jdbcURL = "jdbc:mysql://localhost:3306/alimentations";
        String jdbcUsername = "root";
        String jdbcPassword = "root";
        DatabaseConnection dbConnection = new DatabaseConnection(jdbcURL, jdbcUsername, jdbcPassword);

        // Configuration du contexte de l'application dans le serveur Tomcat
        String contextPath = "/";
        String docBase = new File(".").getAbsolutePath();
        Context context = tomcat.addWebapp(contextPath, docBase);

        // Stocker la connexion dans le contexte de l'application
        context.getServletContext().setAttribute("DB_CONNECTION", dbConnection);

        // Ajout d'un servlet (DataAccessServlet)
        Wrapper servletWrapper = Tomcat.addServlet(context, "DataAccessServlet", new DataAccessServletTest());
        servletWrapper.setLoadOnStartup(1);
        servletWrapper.addMapping("/dataaccess");

        // Ajout d'un autre servlet (CouleurServlet)
        Wrapper couleurServletWrapper = Tomcat.addServlet(context, "CouleurServlet", new CouleurServlet());
        couleurServletWrapper.setLoadOnStartup(1);
        couleurServletWrapper.addMapping("/couleur/*");     
        
        // Ajout d'un autre servlet (AlimentServlet)
        Wrapper alimentServletWrapper = Tomcat.addServlet(context, "alimentServlet", new AlimentServlet());
        alimentServletWrapper.setLoadOnStartup(1);
        alimentServletWrapper.addMapping("/aliment/*");     
        
         // Ajout d'un autre servlet (TypeAlimentServlet)
        Wrapper typeAlimentServletWrapper = Tomcat.addServlet(context, "TypeAlimentServlet", new TypeAlimentServlet());
        typeAlimentServletWrapper.setLoadOnStartup(1);
        typeAlimentServletWrapper.addMapping("/type_aliment/*");  
        
        // Récupération du connecteur Tomcat
        tomcat.getConnector();

        // Démarrage du serveur
        tomcat.start();
        
        // Attente indéfinie du serveur (le programme ne se termine pas ici)
        tomcat.getServer().await();
    }
}

