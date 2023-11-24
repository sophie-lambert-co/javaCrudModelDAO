package com.example.util;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletResponse;

import com.example.model.TypeAliment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServletUtils {

    // Méthode pour parser une requête JSON
    // La méthode parseJsonRequest utilise un JsonReader pour lire et parser le
    // corps d'une requête HTTP en format JSON.
    public static JsonObject parseJsonRequest(javax.servlet.http.HttpServletRequest request) throws IOException {
        try (BufferedReader reader = request.getReader();
                JsonReader jsonReader = Json.createReader(reader)) {
            return jsonReader.readObject();
        }
    }

    // Méthode pour valider les données de la requête JSON en fonction des clés
    // fournies
    // La méthode validateRequestData permet de vérifier si des champs spécifiques
    // sont présents dans une requête JSON.
    public static void validateRequestData(JsonObject data, String... keys) throws IOException {
        for (String key : keys) {
            if (data.getString(key, "").isEmpty()) {
                throw new IOException("Missing required field: " + key);
            }
        }
    }

    // Méthode pour convertir un ResultSet en format JSON
    // La méthode convertResultSetToJson utilise ResultSetTableDisplay pour
    // convertir un ResultSet en format JSON.
    public static String convertResultSetToJson(ResultSet resultSet) throws SQLException {
        return ResultSetTableDisplay.toJson(resultSet);
    }

    // Méthode pour valider les données de la requête JSON pour un type d'aliment
    // La deuxième méthode validateRequestData est spécifique à la validation des
    // données pour un type d'aliment.
    public static void validateRequestData(TypeAliment typeAliment) throws IOException {
        if (typeAliment.getNom() == null || typeAliment.getNom().isEmpty()) {
            throw new IOException("Missing nom");
        }
    }

    // Méthode pour envoyer une réponse JSON
    // La méthode sendJsonResponse configure la réponse HTTP pour renvoyer du JSON.
    public static void sendJsonResponse(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(json);
        }
    }

    // Méthode pour gérer une exception SQL
    // La méthode handleSqlException est utilisée pour gérer les exceptions SQL en
    // envoyant une réponse d'erreur.
    public static void handleSqlException(HttpServletResponse response, SQLException ex) throws IOException {
        // Log SQL Exception ici
        sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "SQL Error: " + ex.getMessage());
    }

    // Méthode pour envoyer une réponse d'erreur
    // La méthode sendErrorResponse est une méthode utilitaire pour envoyer des
    // réponses d'erreur avec un code de statut spécifié et un message associé.
    public static void sendErrorResponse(HttpServletResponse response, int statusCode, String message)
            throws IOException {
        response.sendError(statusCode, message);
    }
}
