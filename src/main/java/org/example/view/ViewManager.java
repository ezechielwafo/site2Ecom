package org.example.view;

import org.example.Model.domain.Article;
import org.example.Model.domain.Utilisateur;
import org.example.controller.MainController;

import java.util.List;

public interface ViewManager {

    /** Définit le contrôleur que la vue doit utiliser. */
    void setController(MainController controller);

    /** Affiche l'écran principal ou démarre l'application. */
    void startApplication();

    /** Gère la séquence de connexion. */
    void showLoginScreen();

    /** Met à jour la liste des articles affichée dans la vue Administrateur. */
    void updateArticleList(List<Article> articles);

    /** Affiche une boîte de dialogue pour créer ou modifier un article. */
    void showArticleEditor(Article article);

    /** Informe l'utilisateur d'un changement ou d'une erreur. */
    void showMessage(String message, String title, int type);

    /** Change l'état de l'application après une connexion réussie. */
    void loginSuccess(Utilisateur user);
}