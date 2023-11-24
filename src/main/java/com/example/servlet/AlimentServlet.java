package com.example.servlet;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import com.example.dao.AlimentDAO;
import com.example.dao.DatabaseConnection;
import com.example.util.Routeur;

import java.io.IOException;

/**
 * Servlet pour la gestion des opérations sur les entités Aliment.
 */
@WebServlet("/aliment/*")
public class AlimentServlet extends HttpServlet {

    private AlimentDAO alimentDao;  // DAO pour les opérations sur les entités Aliment
    private Routeur routeur;        // Utilitaire pour acheminer les requêtes vers les méthodes appropriées du DAO

    /**
     * Méthode d'initialisation appelée lors du démarrage de la servlet.
     * Elle initialise la connexion à la base de données et le routeur.
     */
    public void init() {
        // Récupère le contexte de la servlet pour accéder aux attributs partagés
        ServletContext context = getServletContext();
        
        // Récupère l'objet DatabaseConnection du contexte pour établir la connexion à la base de données
        DatabaseConnection dbConnection = (DatabaseConnection) context.getAttribute("DB_CONNECTION");
        
        // Initialise l'objet AlimentDAO avec la connexion à la base de données
        alimentDao = new AlimentDAO(dbConnection);
        
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
        routeur.routeRequest(request, response, alimentDao);
    }

    // Vous pouvez ajouter des méthodes supplémentaires si nécessaire
}



// Une servlet est une classe Java qui étend les capacités d'un serveur, généralement un serveur web, pour gérer les requêtes HTTP et générer des réponses dynamiques. Les servlets font partie de la technologie Java Enterprise Edition (Java EE) et sont principalement utilisées pour développer des applications web robustes et extensibles.

// Voici quelques caractéristiques clés des servlets :

// Gestion des Requêtes HTTP : Les servlets sont conçues pour gérer les requêtes HTTP, telles que les requêtes GET et POST, émises par les navigateurs web ou d'autres clients.

// Génération de Réponses Dynamiques : Les servlets peuvent générer des réponses dynamiques basées sur les données en cours d'exécution, souvent en utilisant des modèles ou des mécanismes de génération de contenu dynamique.

// Plateforme Indépendante : Les servlets sont des composants Java et sont donc indépendantes de la plateforme. Elles peuvent être déployées sur n'importe quel serveur d'applications compatible Java.

// Gestion de Sessions : Les servlets peuvent gérer des sessions utilisateur, permettant ainsi de suivre l'état des utilisateurs entre différentes requêtes.

// Interaction avec d'autres Technologies Java EE : Les servlets peuvent interagir avec d'autres composants Java EE tels que les EJB (Enterprise JavaBeans), JSP (JavaServer Pages), JDBC (Java Database Connectivity), etc.

// Utilisation d'Annotations : Depuis Java EE 5, l'utilisation d'annotations (comme @WebServlet) simplifie la configuration des servlets.

// Les servlets jouent un rôle essentiel dans le développement web en Java, offrant une approche robuste et orientée objet pour la gestion des requêtes HTTP et la création d'applications web dynamiques.






