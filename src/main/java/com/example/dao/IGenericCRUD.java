package com.example.dao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.io.IOException;

/**
 * Interface définissant les opérations CRUD génériques.
 */
public interface IGenericCRUD {

    /**
     * Gère les requêtes HTTP GET pour l'entité.
     * @param request La requête HTTP.
     * @param response La réponse HTTP.
     * @throws SQLException En cas d'échec d'interaction avec la base de données.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
    void handleGet(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException;

    /**
     * Gère les requêtes HTTP POST pour l'entité.
     * @param request La requête HTTP.
     * @param response La réponse HTTP.
     * @throws SQLException En cas d'échec d'interaction avec la base de données.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
    void handlePost(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException;

    /**
     * Gère les requêtes HTTP PUT pour l'entité.
     * @param request La requête HTTP.
     * @param response La réponse HTTP.
     * @throws SQLException En cas d'échec d'interaction avec la base de données.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
    void handlePut(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException;

    /**
     * Gère les requêtes HTTP DELETE pour l'entité.
     * @param request La requête HTTP.
     * @param response La réponse HTTP.
     * @throws SQLException En cas d'échec d'interaction avec la base de données.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
    void handleDelete(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException;

    /**
     * Gère les requêtes pour trouver une entité par son identifiant.
     * @param request La requête HTTP.
     * @param response La réponse HTTP.
     * @throws SQLException En cas d'échec d'interaction avec la base de données.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
    void handleFindById(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException;

    /**
     * Gère les requêtes pour trouver une entité par son nom.
     * @param request La requête HTTP.
     * @param response La réponse HTTP.
     * @throws SQLException En cas d'échec d'interaction avec la base de données.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
    void handleFindByName(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException;
}

