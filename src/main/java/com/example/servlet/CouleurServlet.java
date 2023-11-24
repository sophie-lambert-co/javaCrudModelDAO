package com.example.servlet;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import com.example.dao.CouleurDAO;
import com.example.dao.DatabaseConnection;
import com.example.util.Routeur;

import java.io.IOException;

/**
 * Servlet pour la gestion des opérations sur les entités Couleur.
 */
@WebServlet("/couleur/*")
public class CouleurServlet extends HttpServlet {

    //private DatabaseConnection dbConnection; // Cette ligne a été commentée car l'objet n'est pas utilisé dans cette classe spécifique
    private CouleurDAO couleurDao;           // DAO pour les opérations sur les entités Couleur
    private Routeur routeur;                  // Utilitaire pour acheminer les requêtes vers les méthodes appropriées du DAO

    /**
     * Méthode d'initialisation appelée lors du démarrage de la servlet.
     * Elle initialise la connexion à la base de données, le DAO et le routeur.
     */
    public void init() {
        // Initialisation avec vos paramètres de connexion
        ServletContext context = getServletContext();
        
        // Récupère l'objet DatabaseConnection du contexte pour établir la connexion à la base de données
        DatabaseConnection dbConnection = (DatabaseConnection) context.getAttribute("DB_CONNECTION");
        
        // Initialise l'objet CouleurDAO avec la connexion à la base de données
        couleurDao = new CouleurDAO(dbConnection);
        
        // Initialise l'objet Routeur
        routeur = new Routeur();
    }

    /**
     * Méthode de service qui gère les requêtes HTTP pour la servlet.
     * Elle délègue le traitement au routeur pour acheminer la requête au DAO approprié.
     * @param request La requête HTTP.
     * @param response La réponse HTTP.
     * @throws ServletException En cas d'erreur de la servlet.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Délègue le traitement au routeur pour acheminer la requête au DAO approprié
        routeur.routeRequest(request, response, couleurDao);
    }
    
}

