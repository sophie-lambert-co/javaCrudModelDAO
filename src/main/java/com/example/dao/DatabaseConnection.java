package com.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gère la connexion à la base de données MySQL.
 */
public class DatabaseConnection {

    // Informations de connexion
    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    
    // Objet représentant la connexion à la base de données
    private Connection jdbcConnection;

    /**
     * Constructeur de la classe DatabaseConnection.
     * @param jdbcURL L'URL JDBC de la base de données.
     * @param jdbcUsername Le nom d'utilisateur de la base de données.
     * @param jdbcPassword Le mot de passe de la base de données.
     */
    public DatabaseConnection(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    /**
     * Établit la connexion à la base de données.
     * @throws SQLException En cas d'échec de la connexion.
     */
    public void connect() throws SQLException {
        try {
            // Chargement du driver JDBC MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Gestion de l'exception si le driver n'est pas trouvé
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
        
        // Établissement de la connexion à la base de données
        jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);        
    }

    /**
     * Ferme la connexion à la base de données.
     * @throws SQLException En cas d'échec de la fermeture de la connexion.
     */
    public void disconnect() throws SQLException {
        // Vérification et fermeture de la connexion si elle est ouverte
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

    /**
     * Renvoie l'objet Connection actuel.
     * @return L'objet Connection.
     */
    public Connection getJdbcConnection() {
        return jdbcConnection;
    }
}


