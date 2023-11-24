package com.example.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.example.dao.AlimentDAO;
import com.example.dao.CouleurDAO;
import com.example.dao.DatabaseConnection;
import com.example.util.ResultSetTableDisplay;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

// Annotation indiquant que cette classe est une servlet accessible à l'URL "/dataaccess"
@WebServlet("/dataaccess")
public class DataAccessServletTest extends HttpServlet  {

    private DatabaseConnection dbConnection; // Objet pour gérer la connexion à la base de données
    private CouleurDAO couleurDao;           // DAO pour les opérations sur les entités Couleur
    private AlimentDAO alimentDao;           // DAO pour les opérations sur les entités Aliment

    /**
     * Méthode d'initialisation appelée lors du démarrage de la servlet.
     * Elle initialise la connexion à la base de données et les DAO nécessaires.
     */
    public void init() {
        // Récupérer la connexion à partir du contexte de l'application
        ServletContext contextdb = getServletContext();
        dbConnection = (DatabaseConnection) contextdb.getAttribute("DB_CONNECTION");
        
        // Initialiser les DAO avec la connexion à la base de données
        couleurDao = new CouleurDAO(dbConnection);
        alimentDao = new AlimentDAO(dbConnection);
    }

    /**
     * Méthode de gestion des requêtes HTTP GET.
     * Elle est appelée lorsque la servlet reçoit une requête HTTP GET.
     * @param request La requête HTTP.
     * @param response La réponse HTTP.
     * @throws ServletException En cas d'erreur de la servlet.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Établir la connexion à la base de données
            dbConnection.connect();
            
            // Définir le type de contenu de la réponse comme du HTML
            response.setContentType("text/html");
            PrintWriter out = response.getWriter(); // Obtient un objet PrintWriter pour écrire la réponse
            
            // Afficher les données des entités Couleur et Aliment dans des tableaux HTML
            ResultSetTableDisplay.displayHtmlTable(couleurDao.listAllCouleurs(), out);
            ResultSetTableDisplay.displayHtmlTable(alimentDao.listAllAliments(), out);

            // Déconnecter la base de données
            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
