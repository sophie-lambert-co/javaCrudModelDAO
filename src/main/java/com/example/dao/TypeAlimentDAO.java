package com.example.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.model.TypeAliment;
import com.example.util.ResultSetTableDisplay;
import com.example.util.ServletUtils;

import javax.json.JsonObject;

// Définition de la classe CouleurDAO qui implémente l'interface IGenericCRUD
public class TypeAlimentDAO implements IGenericCRUD {
    // Attribut pour la connexion à la base de données
    private DatabaseConnection dbConnection;

    // Constructeur prenant une connexion à la base de données en paramètre
    public TypeAlimentDAO(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // Méthode pour gérer les requêtes GET renvoyant des résultats au format HTML
    public void handleGetHTML(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        // Connexion à la base de données
        dbConnection.connect();

        // Configuration de la réponse HTTP
        response.setContentType("text/html");

        // Utilisation de try-with-resources pour garantir la fermeture du PrintWriter

        // Récupération du flux de sortie pour écrire la réponse
       try( PrintWriter out = response.getWriter()){

        // Affichage des résultats de la requête sous forme de tableau HTML
        ResultSetTableDisplay.displayHtmlTable(listAllTypeAliment(), out);

        // Conversion des résultats de la requête en une chaîne de caractères contenant
        // un tableau HTML
        String couleurHtml = ResultSetTableDisplay.toHtmlTable(listAllTypeAliment());
        System.out.println(couleurHtml);
       }
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
            ResultSet resultSet = listAllTypeAliment();

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

            // Créer un objet typeAliment à partir des données JSON
            TypeAliment typeAliment = new TypeAliment();
            typeAliment.setNom(jsonObject.getString("nom", ""));
            typeAliment.setId(jsonObject.getInt("id"));

            // Validation
            ServletUtils.validateRequestData(typeAliment);

            // Insérer la nouveau type d'aliment et récupérer un ResultSet
            ResultSet resultSet = insertTypeAlimentAndGet(typeAliment);

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

            // Récupérer les données 'id', 'nom'
            int id = jsonObject.getInt("id");
            String nom = jsonObject.getString("nom", "");

            // Validation
            if (id <= 0 || nom.isEmpty()) {
                ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID or missing required fields");
                return;
            }

             // Créer un objet typeAliment à partir de l'id et du nom
            TypeAliment typeAliment = new TypeAliment();
            typeAliment.setId(id);
            typeAliment.setNom(nom);

            // Mettre à jour le type d'aliment
            boolean updated = updateTypeAliment(typeAliment);

            // Envoyer une réponse en fonction du succès de la mise à jour
            if (updated) {
                ServletUtils.sendJsonResponse(response, "{\"message\": \"Type aliment updated successfully.\"}");
            } else {
                ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND,
                        "Type aliment not found or not updated");
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

            // Récupérer l'identifiant du type d'aliment à supprimer
            int id = jsonObject.getInt("id");

            // Créer un objet typeAliment à partir de l'id
            TypeAliment typeAliment = new TypeAliment();
            typeAliment.setId(id);

            // Supprimer le type d'aliment
            boolean deleted = deleteTypeAliment(id);

            // Envoyer une réponse en fonction du succès de la suppression
            if (deleted) {
                ServletUtils.sendJsonResponse(response, "{\"message\": \"Type aliment deleted successfully.\"}");
            } else {
                ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND,
                        "Type aliment not found or not deleted");
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
            // Extraire l'ID du type d'aliment à partir des paramètres de la requête
            int id = Integer.parseInt(request.getParameter("id"));

            // Récupérer le type d'aliment par son ID
            ResultSet resultSet = findById(id);

            // Créer un objet typeAliment à partir de l'id
            TypeAliment typeAliment = new TypeAliment();
            typeAliment.setId(id);

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
            // Récupérer le nom du type d'aliment à partir des paramètres de la requête
            String nom = request.getParameter("name");

            // Créer un objet typeAliment à partir du nom
            TypeAliment typeAliment = new TypeAliment();
            typeAliment.setNom(nom);

            // Récupérer du type d'aliment par son nom
            ResultSet resultSet = findByName(typeAliment);

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

    // Méthode pour insérer une nouveau type d'aliment dans la base de données
    public boolean insertTypeAliment(TypeAliment typeAliment) throws SQLException {
        String sql = "INSERT INTO typeAliment (nom) VALUES (?)";
        dbConnection.connect();
        PreparedStatement statement = dbConnection.getJdbcConnection().prepareStatement(sql);

        statement.setString(1, typeAliment.getNom());

        boolean result = statement.executeUpdate() > 0;

        statement.close();
        dbConnection.disconnect();

        return result;
    }

    // Méthode pour insérer un type d'aliment et récupérer le résultat sous forme
    // de ResultSet
    public ResultSet insertTypeAlimentAndGet(TypeAliment typeAliment) throws SQLException {
        String insertSql = "INSERT INTO typeAliment (nom) VALUES ( ?)";
        String selectSql = "SELECT * FROM typeAliment WHERE id = ?";

        dbConnection.connect();

        // Insertion du nouveau type d'aliment
        PreparedStatement insertStatement = dbConnection.getJdbcConnection().prepareStatement(insertSql,
                Statement.RETURN_GENERATED_KEYS);
        insertStatement.setString(1, typeAliment.getNom());
        insertStatement.executeUpdate();

        // Récupération de l'identifiant généré
        ResultSet generatedKeys = insertStatement.getGeneratedKeys();
        if (!generatedKeys.next()) {
            throw new SQLException("Creating color failed, no ID obtained.");
        }
        int newTypeAlimentId = generatedKeys.getInt(1);
        insertStatement.close();

        // Récupération du nouveau type d'aliment
        PreparedStatement selectStatement = dbConnection.getJdbcConnection().prepareStatement(selectSql);
        selectStatement.setInt(1, newTypeAlimentId);
        ResultSet resultSet = selectStatement.executeQuery();

        // Note: La gestion de la fermeture du ResultSet et de la déconnexion de la base
        // de données devrait être effectuée par l'appelant.

        return resultSet;
    }

    // Méthode pour récupérer tous les enregistrements de la table typeAliment
    public ResultSet listAllTypeAliment() throws SQLException {
        String sql = "SELECT * FROM typeAliment";
        dbConnection.connect();

        Statement statement = dbConnection.getJdbcConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // Note: L'appelant devrait gérer la fermeture du resultSet et la déconnexion
        return resultSet;
    }

    // Méthode pour mettre à jour un type d'aliment dans la base de données
    public boolean updateTypeAliment(TypeAliment typeAliment) throws SQLException {
        String sql = "UPDATE typeAliment SET nom = ? WHERE id = ?";
        dbConnection.connect();

        PreparedStatement statement = dbConnection.getJdbcConnection().prepareStatement(sql);
        statement.setInt(1, typeAliment.getId());
        statement.setString(2, typeAliment.getNom());

        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        dbConnection.disconnect();
        return rowUpdated;
    }

    // Méthode pour supprimer un type d'aliment de la base de données
    public boolean deleteTypeAliment(int id) throws SQLException {
        String sql = "DELETE FROM typeAliment WHERE id = ?";

        dbConnection.connect();

        PreparedStatement statement = dbConnection.getJdbcConnection().prepareStatement(sql);
        statement.setInt(1, id);

        boolean rowDeleted = statement.executeUpdate() > 0;
        statement.close();
        dbConnection.disconnect();
        return rowDeleted;
    }

    // Méthode pour récupérer le dernier type d'aliment insérée
    public ResultSet getLastInsertedTypeAliment() throws SQLException {
        String sql = "SELECT * FROM typeAliment ORDER BY id DESC LIMIT 1";

        // dbConnection.connect();

        Statement statement = dbConnection.getJdbcConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // Note: L'appelant devrait gérer la fermeture du resultSet et la déconnexion
        return resultSet;
    }

    // Méthode pour trouver un type d'aliment par son ID
    public ResultSet findById(int id) throws SQLException {
        String sql = "SELECT * FROM typeAliment WHERE id = ?";
        dbConnection.connect();
        PreparedStatement statement = dbConnection.getJdbcConnection().prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        // Note: La gestion de la fermeture du ResultSet et de la déconnexion de la base
        // de données devrait être effectuée par l'appelant.
        return resultSet;
    }

    // Méthode pour trouver un type d'aliment par son nom
    public ResultSet findByName(TypeAliment typeAliment) throws SQLException {
        String sql = "SELECT * FROM typeAliment WHERE nom = ?";
        dbConnection.connect();
        PreparedStatement statement = dbConnection.getJdbcConnection().prepareStatement(sql);
        statement.setString(1, typeAliment.getNom());
        return statement.executeQuery();
    }
}
