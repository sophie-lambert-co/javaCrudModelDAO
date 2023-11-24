package com.example.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.dao.IGenericCRUD;

import java.io.IOException; 

public class Routeur {

    // Méthode principale pour router les requêtes vers les méthodes appropriées du DAO
    public void routeRequest(HttpServletRequest request, HttpServletResponse response, IGenericCRUD dao) throws IOException {
        // Récupérer la méthode de la requête (GET, POST, PUT, DELETE)
        String method = request.getMethod();

        // Récupérer les paramètres d'identification de l'entité (ID et/ou nom)
        String idParam = request.getParameter("id");
        String nameParam = request.getParameter("name");

        try {
            // Vérifier la méthode et les paramètres pour déterminer l'action à effectuer
            if ("GET".equals(method) && nameParam != null) {
                // Si la méthode est GET et le paramètre de nom est présent, appeler handleFindByName
                //"Si la méthode de la requête est égale à 'GET' et que le paramètre 'nameParam' n'est pas nul, alors exécutez le bloc de code à l'intérieur des accolades."
                dao.handleFindByName(request, response);
            } else if ("GET".equals(method) && idParam != null) {
                // Si la méthode est GET et le paramètre d'ID est présent, appeler handleFindById
                dao.handleFindById(request, response);
            } else {
                // Utiliser une instruction switch pour traiter les autres méthodes (POST, PUT, DELETE)
                // 
                // L'instruction switch en Java est utilisée pour effectuer différentes actions en fonction de la valeur d'une expression. C'est une manière plus concise d'écrire une séquence de plusieurs instructions if-else
                
                switch (method) {
                    case "GET":
                        dao.handleGet(request, response); 
                        //Si la méthode HTTP de la requête est "GET", la méthode handleGet de l'objet dao sera appelée.
                        break;
                    case "POST":
                        dao.handlePost(request, response);
                        break;
                    case "PUT":
                        dao.handlePut(request, response);
                        break;
                    case "DELETE":
                        dao.handleDelete(request, response);
                        break;
                    default:
                        // Si la méthode n'est pas supportée, renvoyer une erreur
                        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Méthode non supportée");
                        break;
                }
            }
        } catch (Exception e) {
            // En cas d'exception, imprimer la trace de la pile et renvoyer une erreur interne du serveur
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur interne du serveur");
        }
    }
}
