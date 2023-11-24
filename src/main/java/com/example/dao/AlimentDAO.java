package com.example.dao;

import com.example.util.UpdateResult;

import java.io.IOException;
import java.sql.*;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.model.Aliment;
import com.example.util.ServletUtils;

// La classe AlimentDAO a un constructeur qui prend une connexion à la base de données
//  (DatabaseConnection) en paramètre et l'attribut dbConnection est initialisé avec cette valeur.
// Il est toujours recommandé de passer des dépendances nécessaires par le biais du constructeur pour 
// garantir une meilleure gestion des dépendances.
public class AlimentDAO implements IGenericCRUD {

    private DatabaseConnection dbConnection;

    // Constructeur qui prend la connexion à la base de données en paramètre
    public AlimentDAO(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // La méthode createAlimentFromJson prend un objet JSON (JsonObject) en
    // paramètre et crée un objet Aliment à partir de ses propriétés.
    // Elle utilise la classe Aliment pour créer une instance avec les valeurs du
    // JSON.
    // La gestion des valeurs par défaut (au cas où elles ne sont pas présentes dans
    // le JSON) est effectuée avec les méthodes getString et getJsonNumber avec une
    // valeur par défaut.

    // Création d'Aliment depuis JSON :
    private Aliment createAlimentFromJson(JsonObject jsonObject) {
        return new Aliment(
                jsonObject.getString("nom", ""),
                (float) jsonObject.getJsonNumber("poids_moyen").doubleValue(),
                jsonObject.getInt("calories"),
                (float) jsonObject.getJsonNumber("vitamines_C").doubleValue(),
                jsonObject.getInt("type_id"),
                jsonObject.getInt("couleur_id"));
    }

    // La méthode handleGet gère les requêtes HTTP GET.
    // Elle commence par se connecter à la base de données.
    // Exécute une requête SELECT pour récupérer tous les aliments.
    // Convertit le résultat en format JSON à l'aide de la méthode utilitaire
    // convertResultSetToJson.
    // Envoie la réponse JSON à la servlet.
    // Enfin, elle ferme le ResultSet et déconnecte la base de données dans le bloc
    // finally pour garantir la déconnexion même en cas d'exception.

    // GET - Récupération de tous les aliments :
    @Override
    public void handleGet(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {

        try {
            dbConnection.connect();
            ResultSet resultSet = listAllAliments();
            String jsonResponse = ServletUtils.convertResultSetToJson(resultSet);
            ServletUtils.sendJsonResponse(response, jsonResponse);
            dbConnection.disconnect();
        } catch (SQLException ex) {
            ServletUtils.handleSqlException(response, ex);
        } catch (Exception ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server error: " + ex.getMessage());

        } finally {
            dbConnection.disconnect(); // Déconnexion même en cas d'exception

        }
    }

    // La méthode handlePost gère les requêtes HTTP POST.
    // Elle commence par lire et parser le corps de la requête JSON.
    // Utilise la méthode createAlimentFromJson pour créer un objet Aliment à partir
    // du JSON.
    // La validation des valeurs requises et l'insertion dans la base de données
    // sont effectuées dans la méthode insertAlimentAndGet.
    // La réponse JSON est ensuite envoyée à la servlet.
    // Le bloc finally assure la déconnexion de la base de données même en cas
    // d'exception.

    // POST - Insertion d'un nouvel aliment :
    @Override
    public void handlePost(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            // Lire et parser le corps de la requête JSON
            JsonObject jsonObject = ServletUtils.parseJsonRequest(request);

            // Utilisation de la méthode
            Aliment aliment = createAlimentFromJson(jsonObject);

            // Récupérer les valeurs requises

            // Validation

            // Insérer le nouvel aliment et récupérer un ResultSet
            String jsonResponse = insertAlimentAndGet(aliment);

            // Utiliser toJson pour convertir le ResultSet en JSON
            ServletUtils.sendJsonResponse(response, jsonResponse);

            // Envoyer la réponse JSON
            ServletUtils.sendJsonResponse(response, jsonResponse);
        } catch (SQLException ex) {
            ServletUtils.handleSqlException(response, ex);
        } catch (IOException ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error inserting aliment: " + ex.getMessage());
        } finally {
            dbConnection.disconnect(); // Déconnexion même en cas d'exception
        }

    }

    // La méthode handlePut gère les requêtes HTTP PUT pour mettre à jour un
    // aliment.

    // PUT - Mise à jour d'un aliment :
    @Override
    public void handlePut(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        try {
            // Elle commence par parser le corps de la requête JSON pour obtenir les données
            // de l'aliment à mettre à jour.
            JsonObject jsonObject = ServletUtils.parseJsonRequest(request);

            // Utilise la méthode createAlimentFromJson pour créer un objet Aliment à partir
            // du JSON.
            Aliment aliment = createAlimentFromJson(jsonObject);

            // Valide si le champ "nom" est présent et non vide.
            // Si la validation réussit, elle utilise la méthode updateAliment pour
            // effectuer la mise à jour dans la base de données.
            if (aliment.getNom() == null || aliment.getNom().isEmpty()) {
                ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                        "Missing required field: nom");
                return;
            }

            // Cette ligne appelle la méthode updateAliment pour mettre à jour les
            // informations de l'aliment dans la base de données. La variable updated
            // contient le résultat de cette opération.
            UpdateResult updated = updateAliment(aliment);

            // La réponse est ensuite envoyée en fonction du succès de la mise à jour.
            if (updated.isSuccess()) {
                ServletUtils.sendJsonResponse(response, "{\"message\": \"Aliment updated successfully.\"}");
                // Si la mise à jour est réussie (updated.isSuccess() est vrai), la méthode
                // sendJsonResponse de la classe utilitaire ServletUtils est appelée pour
                // envoyer une réponse JSON indiquant que la mise à jour a été effectuée avec
                // succès.
            } else {
                ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND,
                        "Aliment not found or not updated");

                // Sinon, si la mise à jour n'a pas réussi (par exemple, l'aliment n'a pas été
                // trouvé dans la base de données), la méthode sendErrorResponse est utilisée
                // pour renvoyer une réponse avec le code d'erreur
                // HttpServletResponse.SC_NOT_FOUND et un message indiquant que l'aliment n'a
                // pas été trouvé ou n'a pas été mis à jour.
            }
            // Dans cette partie, les exceptions qui pourraient être levées pendant la mise
            // à jour de l'aliment sont gérées.
        } catch (NumberFormatException ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
            // Si une NumberFormatException est levée, cela signifie que le format d'un
            // nombre est invalide (par exemple, lors de la conversion d'une chaîne en un
            // nombre). Une réponse d'erreur Bad Request (code 400) est renvoyée avec un
            // message indiquant le problème.
        } catch (Exception ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server error: " + ex.getMessage());
            // Si une autre exception non spécifique est levée, une réponse d'erreur interne
            // du serveur (code 500) est renvoyée avec un message indiquant qu'une erreur du
            // serveur s'est produite.
        } finally {
            dbConnection.disconnect(); // Déconnexion même en cas d'exception
            // Le bloc finally assure que la déconnexion de la base de données est
            // effectuée, même en cas d'exception, pour éviter les fuites de ressources.
        }
    }

    @Override
    public void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        try {
            // Lire et parser le corps de la requête JSON
            JsonObject jsonObject = ServletUtils.parseJsonRequest(request);
            // Récupérer l'identifiant de la couleur à supprimer
            int id = jsonObject.getInt("id");
            boolean deleted = deleteAliment(id);
            if (deleted) {
                ServletUtils.sendJsonResponse(response, "{\"message\": \"Aliment deleted successfully.\"}");
            } else {
                ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Error deleting aliment");
            }
        } catch (NumberFormatException ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid format for ID");
        } catch (SQLException ex) {
            ServletUtils.handleSqlException(response, ex);
        } catch (Exception ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server error: " + ex.getMessage());
        } finally {
            dbConnection.disconnect(); // Déconnexion même en cas d'exception
        }
    }

    // Recherche par ID :La méthode handleFindById gère les requêtes HTTP GET pour
    // rechercher un aliment par son ID.
    @Override
    public void handleFindById(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        try {
            // Extraire l'ID de l'aliment à partir des paramètres de la requête
            int id = Integer.parseInt(request.getParameter("id"));

            // Récupérer l'aliment par son ID
            ResultSet resultSet = findById(id);

            // Convertir le ResultSet en JSON
            String jsonResponse = ServletUtils.convertResultSetToJson(resultSet);

            // Envoyer la réponse JSON
            ServletUtils.sendJsonResponse(response, jsonResponse);

        } catch (NumberFormatException ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        } catch (SQLException ex) {
            ServletUtils.handleSqlException(response, ex);
        } catch (Exception ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server error: " + ex.getMessage());
        } finally {
            dbConnection.disconnect(); // Déconnexion même en cas d'exception
        }
    }

    // GET : recherche d'un aliment par son nom
    // On extrait le nom de la requête HTTP, crée un objet Aliment avec ce nom, puis
    // utilise la méthode findByName pour récupérer le résultat depuis la base de
    // données.
    @Override
    public void handleFindByName(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        try {
            String nom = request.getParameter("nom");

            Aliment aliment = new Aliment();
            aliment.setNom(nom);
            ResultSet resultSet = findByName(aliment);

            // Le résultat est converti en JSON et renvoyé en réponse.
            String jsonResponse = ServletUtils.convertResultSetToJson(resultSet);
            ServletUtils.sendJsonResponse(response, jsonResponse);

           
            // Les exceptions sont gérées de manière similaire aux autres méthodes.
        } catch (SQLException ex) {
            ServletUtils.handleSqlException(response, ex);
        } catch (Exception ex) {
            ServletUtils.sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server error: " + ex.getMessage());
            // Le bloc finally assure la déconnexion de la base de données même en cas
            // d'exception.
        } finally {
            dbConnection.disconnect(); // Déconnexion même en cas d'exception
        }
    }

    /**
     * Insère un nouvel aliment dans la base de données.
     *
     * @param aliment L'aliment à insérer.
     * @return Vrai si l'insertion a réussi, sinon faux.
     * @throws SQLException En cas d'erreur SQL.
     */

    public boolean insertAliment(Aliment aliment) throws SQLException {
        // Cette méthode insère un nouvel aliment dans la base de données en utilisant
        // une requête SQL préparée.
        // Les valeurs de l'aliment sont définies comme paramètres dans la requête.

        String sql = "INSERT INTO aliments (nom, poids_moyen, calories, vitamines_C, type_id, couleur_id) VALUES (?, ?, ?, ?, ?, ?)";
        dbConnection.connect();

        try (PreparedStatement statement = dbConnection.getJdbcConnection().prepareStatement(sql)) {
            // Définir les valeurs des paramètres de la requête
            statement.setString(1, aliment.getNom());
            statement.setFloat(2, aliment.getPoidsMoyen());
            statement.setInt(3, aliment.getCalories());
            statement.setFloat(4, aliment.getVitaminesC());
            // Vérifier et définir les valeurs des clés étrangères, en tenant compte de la
            // possibilité de null
            // Les clés étrangères (type_id et couleur_id) sont gérées en tenant compte de
            // la possibilité de valeurs null.
            if (aliment.getTypeId() != null) {
                statement.setInt(5, aliment.getTypeId());
            } else {
                statement.setNull(5, Types.INTEGER);
            }

            if (aliment.getCouleurId() != null) {
                statement.setInt(6, aliment.getCouleurId());
            } else {
                statement.setNull(6, Types.INTEGER);
            }
            // Exécuter la requête et retourner le résultat
            // Le résultat de l'insertion est renvoyé.
            boolean rowInserted = statement.executeUpdate() > 0;
            return rowInserted;
        } finally {
            // Déconnexion dans le bloc finally pour garantir qu'elle soit effectuée
            dbConnection.disconnect();
        }
    }

    /**
     * Récupère tous les aliments de la base de données.
     *
     * @return Un ResultSet contenant tous les aliments.
     * @throws SQLException En cas d'erreur SQL.
     */
    // Read (Select)
    public ResultSet listAllAliments() throws SQLException {
        String sql = "SELECT * FROM aliments";
        dbConnection.connect();

        try (Statement statement = dbConnection.getJdbcConnection().createStatement()) {
            // Exécuter la requête et retourner le ResultSet
            ResultSet resultSet = statement.executeQuery(sql);

            // Note: Le gestionnaire de l'appelant doit fermer le resultSet et déconnecter
            return resultSet;
        } finally {
            // Déconnexion dans le bloc finally pour garantir qu'elle soit effectuée
            dbConnection.disconnect();
        }
    }

    /**
     * Insère un nouvel aliment dans la base de données et retourne le résultat sous
     * forme de JSON.
     *
     * @param aliment L'aliment à insérer.
     * @return Une représentation JSON du nouvel aliment inséré.
     * @throws SQLException En cas d'erreur SQL.
     */
    public String insertAlimentAndGet(Aliment aliment) throws SQLException {
        // Méthode similaire à insertAliment, mais avec un traitement supplémentaire
        // pour récupérer l'aliment inséré.
        // La représentation JSON de l'aliment inséré est renvoyée.
        // Cette méthode est similaire à insertAliment, mais elle va au-delà en
        // récupérant l'aliment inséré à l'aide d'une requête supplémentaire.
        String insertSql = "INSERT INTO aliments (nom, poids_moyen, calories, vitamines_C, type_id, couleur_id) VALUES (?, ?, ?, ?, ?, ?)";
        String selectSql = "SELECT * FROM aliments WHERE id = ?";

        dbConnection.connect();

        // Insertion du nouvel aliment
        try (PreparedStatement insertStatement = dbConnection.getJdbcConnection().prepareStatement(insertSql,
                Statement.RETURN_GENERATED_KEYS)) {
            insertStatement.setString(1, aliment.getNom());
            insertStatement.setFloat(2, aliment.getPoidsMoyen());
            insertStatement.setInt(3, aliment.getCalories());
            insertStatement.setFloat(4, aliment.getVitaminesC());

            if (aliment.getTypeId() != null) {
                insertStatement.setInt(5, aliment.getTypeId());
            } else {
                insertStatement.setNull(5, Types.INTEGER);
            }

            if (aliment.getCouleurId() != null) {
                insertStatement.setInt(6, aliment.getCouleurId());
            } else {
                insertStatement.setNull(6, Types.INTEGER);
            }

            insertStatement.executeUpdate();

            // Récupération de l'identifiant généré
            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("Creating aliment failed, no ID obtained.");
                }
                int newAlimentId = generatedKeys.getInt(1);

                // Récupération du nouvel aliment inséré
                try (PreparedStatement selectStatement = dbConnection.getJdbcConnection().prepareStatement(selectSql)) {
                    selectStatement.setInt(1, newAlimentId);
                    try (ResultSet resultSet = selectStatement.executeQuery()) {
                        // Convert the resultSet to JSON here, before closing the connection
                        String jsonResponse = ServletUtils.convertResultSetToJson(resultSet);
                        // Le résultat final est retourné sous forme de chaîne JSON.
                        return jsonResponse; // Return the JSON string
                    }
                }
            }

                    } finally {
                        // Déconnexion dans le bloc finally pour garantir qu'elle soit effectuée
                        dbConnection.disconnect();
                    }
                }
            
        
    

    // updateResult
    /**
     * Met à jour les informations d'un aliment dans la base de données.
     *
     * @param aliment L'aliment avec les nouvelles informations.
     * @return Un objet UpdateResult indiquant le succès de la mise à jour.
     */

    // Cette méthode met à jour les informations d'un aliment dans la base de
    // données en utilisant une requête SQL de mise à jour.
    public UpdateResult updateAliment(Aliment aliment) {
        try {
            // Requête SQL pour mettre à jour un aliment
            String sql = "UPDATE aliments SET nom = ?, poids_moyen = ?, calories = ?, vitamines_C = ?, type_id = ?, couleur_id = ? WHERE id = ?";

            // Connexion à la base de données
            dbConnection.connect();

            try (PreparedStatement updateStatement = dbConnection.getJdbcConnection().prepareStatement(sql)) {
                // Les paramètres de la mise à jour sont définis en utilisant les propriétés de
                // l'aliment.
                // Paramètres pour la mise à jour
                updateStatement.setString(1, aliment.getNom());
                updateStatement.setFloat(2, aliment.getPoidsMoyen());
                updateStatement.setInt(3, aliment.getCalories());
                updateStatement.setFloat(4, aliment.getVitaminesC());
                updateStatement.setInt(5, aliment.getTypeId());
                updateStatement.setInt(6, aliment.getCouleurId());
                updateStatement.setInt(7, aliment.getId());

                // Exécution de la mise à jour
                boolean updated = updateStatement.executeUpdate() > 0;

                // Retourner le résultat de la mise à jour
                // Le résultat de la mise à jour est retourné sous la forme d'un objet
                // UpdateResult.
                if (updated) {
                    return new UpdateResult(true, "Aliment updated successfully");
                } else {
                    return new UpdateResult(false, "Aliment not found or not updated");
                }
            } catch (SQLException ex) {
                // Gérer les exceptions liées à la base de données
                ex.printStackTrace(); // Log l'exception pour le débogage
                return new UpdateResult(false, "Error updating aliment: " + ex.getMessage());
            } finally {
                // Déconnexion dans le bloc finally pour garantir qu'elle soit effectuée, même
                // en cas d'exception
                dbConnection.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // Log l'exception pour le débogage
            return new UpdateResult(false, "Error updating aliment: " + ex.getMessage());
        }
    }

    // Delete
    /**
     * Supprime un aliment de la base de données en fonction de son ID.
     *
     * @param id L'ID de l'aliment à supprimer.
     * @return Vrai si la suppression a réussi, sinon faux.
     * @throws SQLException En cas d'erreur SQL.
     */
    // Cette méthode supprime un aliment de la base de données en fonction de son ID
    // en utilisant une requête SQL de suppression.
    // Le résultat de la suppression est renvoyé sous la forme d'un booléen.
    public boolean deleteAliment(int id) throws SQLException {
        // Requête SQL pour supprimer un aliment en fonction de son ID
        String sql = "DELETE FROM aliments WHERE id = ?";
        // Connexion à la base de données
        dbConnection.connect();

        try (PreparedStatement statement = dbConnection.getJdbcConnection().prepareStatement(sql)) {
            // Paramètre pour la suppression
            statement.setInt(1, id);

            boolean rowDeleted = statement.executeUpdate() > 0;
            statement.close();
            // Exécution de la suppression et retour du résultat

            return rowDeleted;
        } finally {
            // Déconnexion dans le bloc finally pour garantir qu'elle soit effectuée
            dbConnection.disconnect();
        }
    }

    // Méthode pour trouver un aliment par son ID
    /**
     * Récupère un aliment de la base de données en fonction de son ID.
     *
     * @param id L'ID de l'aliment à récupérer.
     * @return Un ResultSet contenant les informations de l'aliment trouvé.
     * @throws SQLException En cas d'erreur SQL.
     */
    public ResultSet findById(int id) throws SQLException { // Requête SQL pour récupérer un aliment en fonction de son
                                                            // ID

        String sql = "SELECT * FROM aliments WHERE id = ?";
        // Connexion à la base de données
        dbConnection.connect();
        try (PreparedStatement statement = dbConnection.getJdbcConnection().prepareStatement(sql)) {
            // Paramètre pour la requête
            // L'ID est passé en paramètre pour permettre une recherche spécifique.
            statement.setInt(1, id);
            // Exécution de la requête et retour du ResultSet
            //
            ResultSet resultSet = statement.executeQuery();
            // Note: La gestion de la fermeture du ResultSet et de la déconnexion devrait
            // être effectuée par l'appelant
            return resultSet;
        } finally {
            // Déconnexion dans le bloc finally pour garantir qu'elle soit effectuée
            dbConnection.disconnect();
        }
    }

    /**
     * Récupère des aliments de la base de données en fonction de leur nom.
     *
     * @param aliment L'objet Aliment contenant le nom de l'aliment à rechercher.
     * @return Un ResultSet contenant les informations des aliments trouvés.
     * @throws SQLException En cas d'erreur SQL.
     */

    public ResultSet findByName(Aliment aliment) throws SQLException {
        // Requête SQL pour récupérer des aliments en fonction de leur nom
        String sql = "SELECT * FROM aliments WHERE nom = ?";
        // Connexion à la base de données
        dbConnection.connect();
        try (PreparedStatement statement = dbConnection.getJdbcConnection().prepareStatement(sql)) {
            // Paramètre pour la requête
            // Le nom de l'aliment est passé en paramètre à partir de l'objet Aliment.
            // Le résultat est renvoyé sous la forme d'un ResultSet.
            statement.setString(1, aliment.getNom());
            // Exécution de la requête et retour du ResultSet
            return statement.executeQuery();
        } finally {
            // Déconnexion dans le bloc finally pour garantir qu'elle soit effectuée
            dbConnection.disconnect();

        }
    }
}
