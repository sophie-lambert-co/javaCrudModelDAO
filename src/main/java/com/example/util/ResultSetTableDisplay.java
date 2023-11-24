package com.example.util;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

public class ResultSetTableDisplay {


// ResultSetTableDisplay fournit des méthodes pour gérer l'affichage des
// résultats d'un ResultSet de différentes manières :

// display : Affiche les résultats dans la console en tant que tableau texte.
// toHtmlTable : Convertit les résultats en une table HTML sous forme de    chaîne
// de caractères.
// displayHtmlTable : Affiche les résultats en tant que table HTML dans un
// PrintWriter fourni (utile pour les applications web, par exemple).
// toJson : Convertit les résultats en format JSON.

    /**
     * Affiche les résultats du ResultSet sous forme de tableau texte dans la console.
     *
     * @param resultSet Le ResultSet à afficher.
     * @throws SQLException En cas d'erreur lors de l'accès aux données du ResultSet.
     */
    public static void display(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Affichage des en-têtes de colonnes
        for (int i = 1; i <= columnCount; i++) {
            // Si c'est la deuxième colonne ou plus, ajouter une virgule et un espace
            if (i > 1) System.out.print(",  ");
            System.out.print(metaData.getColumnName(i));
        }
        System.out.println();

        // Affichage des lignes de données
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                // Si c'est la deuxième colonne ou plus, ajouter une virgule et un espace
                if (i > 1) System.out.print(",  ");
                // Afficher la valeur de la colonne actuelle
                System.out.print(resultSet.getString(i));
            }
            // Passer à la ligne suivante après l'affichage de toutes les colonnes
            System.out.println();
        }
    }

    /**
     * Convertit le ResultSet en une table HTML sous forme de chaîne de caractères.
     *
     * @param resultSet Le ResultSet à convertir.
     * @return Une chaîne de caractères représentant une table HTML.
     * @throws SQLException En cas d'erreur lors de l'accès aux données du ResultSet.
     */
    public static String toHtmlTable(ResultSet resultSet) throws SQLException {
        StringBuilder htmlTable = new StringBuilder();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        htmlTable.append("<table border='1'>");

        // En-têtes de colonnes
        htmlTable.append("<tr>");
        for (int i = 1; i <= columnCount; i++) {
            // Ajouter une cellule d'en-tête avec le nom de la colonne
            htmlTable.append("<th>").append(metaData.getColumnName(i)).append("</th>");
        }
        htmlTable.append("</tr>");

        // Données
        while (resultSet.next()) {
            // Commencer une nouvelle ligne dans la table HTML
            htmlTable.append("<tr>");
            for (int i = 1; i <= columnCount; i++) {
                // Ajouter une cellule avec la valeur de la colonne
                htmlTable.append("<td>").append(resultSet.getString(i)).append("</td>");
            }
            // Finir la ligne dans la table HTML
            htmlTable.append("</tr>");
        }

        htmlTable.append("</table>");

        return htmlTable.toString();
    }

    /**
     * Affiche le ResultSet sous forme de table HTML dans le PrintWriter fourni.
     *
     * @param resultSet Le ResultSet à afficher.
     * @param out Le PrintWriter où afficher le résultat.
     * @throws SQLException En cas d'erreur lors de l'accès aux données du ResultSet.
     */
    public static void displayHtmlTable(ResultSet resultSet, PrintWriter out) throws SQLException {
        // Écrire le début de la page HTML dans le PrintWriter
        out.println("<html><body>");
        // Appeler la méthode toHtmlTable pour obtenir la table HTML et l'écrire dans le PrintWriter
        out.println(toHtmlTable(resultSet));
        // Écrire la fin de la page HTML dans le PrintWriter
        out.println("</body></html>");
    }

    /**
     * Convertit le ResultSet en format JSON à l'aide des classes JsonArrayBuilder
     * et JsonObjectBuilder de l'API JSON.
     *
     * @param resultSet Le ResultSet à convertir.
     * @return Une chaîne de caractères représentant le ResultSet au format JSON.
     * @throws SQLException En cas d'erreur lors de l'accès aux données du ResultSet.
     */
    public static String toJson(ResultSet resultSet) throws SQLException {
        // Créer un constructeur de tableau JSON
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Itérer sur chaque ligne du ResultSet
        while (resultSet.next()) {
            // Créer un constructeur d'objet JSON pour chaque ligne
            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
            // Itérer sur chaque colonne de la ligne actuelle
            for (int i = 1; i <= columnCount; i++) {
                // Ajouter la paire clé-valeur (nom de la colonne, valeur) à l'objet JSON
                jsonObjectBuilder.add(metaData.getColumnName(i), resultSet.getString(i));
            }
            // Ajouter l'objet JSON de la ligne à la construction du tableau JSON
            jsonArrayBuilder.add(jsonObjectBuilder);
        }

        // Construire le tableau JSON final et le convertir en chaîne de caractères
        return jsonArrayBuilder.build().toString();
    }
}

