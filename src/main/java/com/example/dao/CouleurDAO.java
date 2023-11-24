package com.example.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.model.Couleur;
import com.example.util.ResultSetTableDisplay;
import com.example.util.ServletUtils;

import javax.json.JsonObject;

// Définition de la classe CouleurDAO qui implémente l'interface IGenericCRUD
public class CouleurDAO implements IGenericCRUD {
    // Attribut pour la connexion à la base de données
    private DatabaseConnection dbConnection;

    // Constructeur prenant une connexion à la base de données en paramètre
    public CouleurDAO(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // Méthode pour gérer les requêtes GET renvoyant des résultats au format HTML
    public void handleGetHTML(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        // Connexion à la base de données
        dbConnection.connect();

        // Configuration de la réponse HTTP
        response.setContentType("text/html");

        // Récupération du flux de sortie pour écrire la réponse
        PrintWriter out = response.getWriter();

        // Affichage des résultats de la requête sous forme de tableau HTML
        ResultSetTableDisplay.displayHtmlTable(listAllCouleurs(), out);

        // Conversion des résultats de la requête en une chaîne de caractères contenant
        // un tableau HTML
        String couleurHtml = ResultSetTableDisplay.toHtmlTable(listAllCouleurs());
        System.out.println(couleurHtml);

        // Déconnexion de la base de données
        dbConnection.disconnect();
    }

    // Implémentation de la méthode handleGet de l'interface IGenericCRUD pour
    // renvoyer des résultats au format JSON

    @Override
    public void handleGet(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            // Connexion à la base de données et récupération des données
            dbConnection.connect();
            ResultSet resultSet = listAllCouleurs();

            // Convertir le ResultSet en JSON
            String jsonResponse = ServletUtils.convertResultSetToJson(resultSet);

            // Envoyer la réponse JSON
            ServletUtils.sendJsonResponse(response, jsonResponse);

            // Fermer ResultSet et déconnexion de la base de données
            resultSet.close();
            dbConnection.disconnect();
        } catch (SQLException ex) {
            ServletUtils.handleSqlException(response, ex);
        } catch (Exception ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server error: " + ex.getMessage());
        }
    }

    // Implémentation de la méthode handlePost de l'interface IGenericCRUD pour
    // gérer les requêtes POST
    @Override
    public void handlePost(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            // Lire et parser le corps de la requête JSON
            JsonObject jsonObject = ServletUtils.parseJsonRequest(request);

            // // Récupérer les valeurs 'nom' et 'hexadecimal_rvb'
            // String nom = jsonObject.getString("nom", "");
            // String hexadecimal_rvb = jsonObject.getString("hexadecimal_rvb", "");

            // Créer un objet Couleur à partir des données JSON
            Couleur nouvelleCouleur = new Couleur(
                    jsonObject.getString("nom", ""),
                    jsonObject.getString("hexadecimal_rvb", ""));

            // Validation
            ServletUtils.validateRequestData(jsonObject, "nom", "hexadecimal_rvb");

            // Insérer la nouvelle couleur et récupérer un ResultSet
            ResultSet resultSet = insertCouleurAndGet(nouvelleCouleur);

            // Utiliser toJson pour convertir le ResultSet en JSON
            String jsonResponse = ServletUtils.convertResultSetToJson(resultSet);

            // Envoyer la réponse JSON
            ServletUtils.sendJsonResponse(response, jsonResponse);
        } catch (SQLException ex) {
            ServletUtils.handleSqlException(response, ex);
        } catch (IOException ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error inserting color: " + ex.getMessage());
        }
    }

    // Implémentation de la méthode handlePut de l'interface IGenericCRUD pour gérer
    // les requêtes PUT
    @Override
    public void handlePut(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            // Lire et parser le corps de la requête JSON
            JsonObject jsonObject = ServletUtils.parseJsonRequest(request);

            // // Récupérer les données 'id', 'nom', et 'hexadecimal_rvb'
            // int id = jsonObject.getInt("id");
            // String nomCouleur = jsonObject.getString("nom", "");
            // String hexadecimal_rvb = jsonObject.getString("hexadecimal_rvb", "");

            // Créer un objet Couleur à partir des données JSON
            Couleur couleur = new Couleur(
                    jsonObject.getString("nom", ""),
                    jsonObject.getString("hexadecimal_rvb", ""));

            // Validation
            if (couleur.getNom().isEmpty() || couleur.getHexadecimal_rvb().isEmpty()) {
                ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Missing required fields");
                return;
            }

            // Mettre à jour la couleur
            boolean updated = updateCouleur(couleur);

            // Envoyer une réponse en fonction du succès de la mise à jour
            if (updated) {
                ServletUtils.sendJsonResponse(response, "{\"message\": \"Color updated successfully.\"}");
            } else {
                ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND,
                        "Color not found or not updated");
            }
        } catch (NumberFormatException ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid format for ID");
        } catch (SQLException ex) {
            ServletUtils.handleSqlException(response, ex);
        } catch (Exception ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server error: " + ex.getMessage());
        }
    }

    // Implémentation de la méthode handleDelete de l'interface IGenericCRUD pour
    // gérer les requêtes DELETE
    @Override
    public void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        try {
            // Lire et parser le corps de la requête JSON
            JsonObject jsonObject = ServletUtils.parseJsonRequest(request);

            // Récupérer l'identifiant de la couleur à supprimer
            int id = jsonObject.getInt("id");

            // Supprimer la couleur
            boolean deleted = deleteCouleur(id);

            // Envoyer une réponse en fonction du succès de la suppression
            if (deleted) {
                ServletUtils.sendJsonResponse(response, "{\"message\": \"Color deleted successfully.\"}");
            } else {
                ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND,
                        "Color not found or not deleted");
            }
        } catch (NumberFormatException ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid format for ID");
        } catch (SQLException ex) {
            ServletUtils.handleSqlException(response, ex);
        } catch (Exception ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server error: " + ex.getMessage());
        }
    }

    // Implémentation de la méthode handleFindById de l'interface IGenericCRUD pour
    // rechercher une couleur par son ID
    @Override
    public void handleFindById(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        try {
            // Extraire l'ID de la couleur à partir des paramètres de la requête
            int id = Integer.parseInt(request.getParameter("id"));

            // Récupérer la couleur par son ID
            ResultSet resultSet = findById(id);

            // Convertir le ResultSet en JSON
            String jsonResponse = ServletUtils.convertResultSetToJson(resultSet);

            // Envoyer la réponse JSON
            ServletUtils.sendJsonResponse(response, jsonResponse);

            // Fermer ResultSet et déconnexion de la base de données
            resultSet.close();
            dbConnection.disconnect();
        } catch (NumberFormatException ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        } catch (SQLException ex) {
            ServletUtils.handleSqlException(response, ex);
        } catch (Exception ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server error: " + ex.getMessage());
        }
    }

    // Implémentation de la méthode handleFindByName de l'interface IGenericCRUD
    // pour rechercher une couleur par son nom
    @Override
    public void handleFindByName(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        try {
            // Récupérer le nom de la couleur à partir des paramètres de la requête
            String nomCouleur = request.getParameter("name");

            // Créer un objet Couleur à partir du nom
            Couleur couleur = new Couleur();
            couleur.setNom(nomCouleur);

            // Récupérer la couleur par son nom
            ResultSet resultSet = findByName(couleur);

            // Convertir le ResultSet en JSON
            String jsonResponse = ServletUtils.convertResultSetToJson(resultSet);
            // Envoyer la réponse JSON
            ServletUtils.sendJsonResponse(response, jsonResponse);

            // Fermer ResultSet et déconnexion de la base de données
            resultSet.close();
            dbConnection.disconnect();
        } catch (SQLException ex) {
            ServletUtils.handleSqlException(response, ex);
        } catch (Exception ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server error: " + ex.getMessage());
        }
    }

    // Méthode pour insérer une nouvelle couleur dans la base de données
    public boolean insertCouleur(Couleur couleur) throws SQLException {
        String sql = "INSERT INTO couleur (nom, hexadecimal_rvb) VALUES (?, ?)";
        dbConnection.connect();
        PreparedStatement statement = dbConnection.getJdbcConnection().prepareStatement(sql);

        statement.setString(1, couleur.getNom());
        statement.setString(2, couleur.getHexadecimal_rvb());

        boolean result = statement.executeUpdate() > 0;

        statement.close();
        dbConnection.disconnect();

        return result;
    }

    // Méthode pour insérer une nouvelle couleur et récupérer le résultat sous forme
    // de ResultSet
    public ResultSet insertCouleurAndGet(Couleur couleur) throws SQLException {
        String insertSql = "INSERT INTO couleur (nom, hexadecimal_rvb) VALUES (?, ?)";
        String selectSql = "SELECT * FROM couleur WHERE id = ?";

        dbConnection.connect();

        // Insertion de la nouvelle couleur
        PreparedStatement insertStatement = dbConnection.getJdbcConnection().prepareStatement(insertSql,
                Statement.RETURN_GENERATED_KEYS);
        insertStatement.setString(1, couleur.getNom());
        insertStatement.setString(2, couleur.getHexadecimal_rvb());
        insertStatement.executeUpdate();

        // Récupération de l'identifiant généré
        ResultSet generatedKeys = insertStatement.getGeneratedKeys();
        if (!generatedKeys.next()) {
            throw new SQLException("Creating color failed, no ID obtained.");
        }
        int newColorId = generatedKeys.getInt(1);
        insertStatement.close();

        // Récupération de la nouvelle couleur insérée
        PreparedStatement selectStatement = dbConnection.getJdbcConnection().prepareStatement(selectSql);
        selectStatement.setInt(1, newColorId);
        ResultSet resultSet = selectStatement.executeQuery();

        // Note: La gestion de la fermeture du ResultSet et de la déconnexion de la base
        // de données devrait être effectuée par l'appelant.

        return resultSet;
    }

    // Méthode pour récupérer tous les enregistrements de la table couleur
    public ResultSet listAllCouleurs() throws SQLException {
        String sql = "SELECT * FROM couleur";
        dbConnection.connect();

        Statement statement = dbConnection.getJdbcConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // Note: L'appelant devrait gérer la fermeture du resultSet et la déconnexion
        return resultSet;
    }

    // Méthode pour mettre à jour une couleur dans la base de données
    public boolean updateCouleur(Couleur couleur) throws SQLException {
        String sql = "UPDATE couleur SET nom = ?, hexadecimal_rvb = ? WHERE id = ?";
        dbConnection.connect();

        PreparedStatement statement = dbConnection.getJdbcConnection().prepareStatement(sql);
        statement.setInt(3, couleur.getId());
        statement.setString(1, couleur.getNom());
        statement.setString(2, couleur.getHexadecimal_rvb()); // Mise à jour de la valeur hexadecimal_rvb

        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        dbConnection.disconnect();
        return rowUpdated;
    }

    // Méthode pour supprimer une couleur de la base de données
    public boolean deleteCouleur(int couleurId) throws SQLException {
        String sql = "DELETE FROM couleur WHERE id = ?";

        dbConnection.connect();

        PreparedStatement statement = dbConnection.getJdbcConnection().prepareStatement(sql);
        statement.setInt(1, couleurId);

        boolean rowDeleted = statement.executeUpdate() > 0;
        statement.close();
        dbConnection.disconnect();
        return rowDeleted;
    }

    // Méthode pour récupérer la dernière couleur insérée
    public ResultSet getLastInsertedColor() throws SQLException {
        String sql = "SELECT * FROM couleur ORDER BY id DESC LIMIT 1";

        // dbConnection.connect();

        Statement statement = dbConnection.getJdbcConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // Note: L'appelant devrait gérer la fermeture du resultSet et la déconnexion
        return resultSet;
    }

    // Méthode pour trouver une couleur par son ID
    public ResultSet findById(int couleurId) throws SQLException {
        String sql = "SELECT * FROM couleur WHERE id = ?";
        dbConnection.connect();
        PreparedStatement statement = dbConnection.getJdbcConnection().prepareStatement(sql);
        statement.setInt(1, couleurId);
        ResultSet resultSet = statement.executeQuery();
        // Note: La gestion de la fermeture du ResultSet et de la déconnexion de la base
        // de données devrait être effectuée par l'appelant.
        return resultSet;
    }

    // Méthode pour trouver une couleur par son nom
    public ResultSet findByName(Couleur couleur) throws SQLException {
        String sql = "SELECT * FROM couleur WHERE nom = ?";
        dbConnection.connect();
        PreparedStatement statement = dbConnection.getJdbcConnection().prepareStatement(sql);
        statement.setString(1, couleur.getNom());
        return statement.executeQuery();
    }
}
