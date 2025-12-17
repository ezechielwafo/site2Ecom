package org.example.controller;

import org.example.Model.domain.Article;
import org.example.Model.domain.Utilisateur;
import org.example.Model.persistance.ArticleDAO;
import org.example.view.ViewManager;

import javax.swing.JOptionPane;

public class MainController {

    private final ArticleDAO articleDAO;
    private final ViewManager view;

    private Utilisateur currentUser; // Gère l'état de la session

    public MainController(ArticleDAO articleDAO, ViewManager view) {
        this.articleDAO = articleDAO;
        this.view = view;

        // Initialisation croisée
        this.view.setController(this);

        // Démarrer la séquence de l'application
        this.view.startApplication();
    }

    // --- AUTHENTIFICATION ---

    public void handleLogin(String login, String password) {
        // TODO: Appeler l'Authenticator du Modèle pour vérifier les credentials

        // Simulation TEMPORAIRE (à remplacer par le Template Method)
        if ("admin".equals(login) && "pass".equals(password)) {
            currentUser = new Utilisateur(1, login, password, true); // Admin
            view.loginSuccess(currentUser);
            view.showMessage("Bienvenue, Administrateur!", "Connexion réussie", JOptionPane.INFORMATION_MESSAGE);
            refreshArticleList(); // Charger les données pour l'admin
        } else {
            view.showMessage("Login ou mot de passe incorrect.", "Erreur d'authentification", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- GESTION DES ARTICLES (CRUD) ---

    public void refreshArticleList() {
        view.updateArticleList(articleDAO.findAll());
    }

    public void handleCreateArticle(String nom, String desc, double prix, int stock) {
        if (currentUser == null || !currentUser.isAdmin()) {
            view.showMessage("Accès refusé. Vous devez être administrateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Article newArticle = new Article(nom, desc, prix, stock);
        articleDAO.create(newArticle);
        articleDAO.saveAll(); // Sauvegarde immédiate (simple)
        refreshArticleList();
    }

    public void handleDeleteArticle(long id) {
        if (currentUser == null || !currentUser.isAdmin()) {
            view.showMessage("Accès refusé. Vous devez être administrateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (articleDAO.delete(id)) {
            articleDAO.saveAll();
            refreshArticleList();
            view.showMessage("Article " + id + " supprimé.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } else {
            view.showMessage("Article introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // TODO: Ajouter handleUpdateArticle

    // --- GESTION DE LA PERSISTANCE ---

    public void handleSaveAll() {
        articleDAO.saveAll();
        view.showMessage("Données sauvegardées.", "Persistance", JOptionPane.INFORMATION_MESSAGE);
    }
}