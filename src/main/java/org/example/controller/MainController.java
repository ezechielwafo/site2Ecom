package org.example.controller;

import org.example.Model.auth.AuthenticatorTemplate;
import org.example.Model.auth.MemoireAuthenticator;
import org.example.Model.domain.Article;
import org.example.Model.domain.Utilisateur;
import org.example.Model.persistance.ArticleDAO;
import org.example.view.ViewManager;

import javax.swing.JOptionPane;

public class MainController {

    private final ArticleDAO articleDAO;
    private final ViewManager view;
    private AuthenticatorTemplate authenticator;
    private Utilisateur currentUser;

    public MainController(ArticleDAO articleDAO, ViewManager view) {
        this.articleDAO = articleDAO;
        this.view = view;

        // Initialisation croisée
        this.view.setController(this);

        // On définit l'authentificateur par défaut (Mémoire pour l'instant)
        this.setAuthenticator(new MemoireAuthenticator());

        // On lance l'IHM et on demande le login
        this.view.startApplication();
        this.view.showLoginScreen();
    }

    public void setAuthenticator(AuthenticatorTemplate auth) {
        this.authenticator = auth;
    }

    // --- AUTHENTIFICATION ---
    public void handleLogin(String login, String password) {
        // On utilise UNIQUEMENT le Template Method ici
        Utilisateur user = authenticator.login(login, password);

        if (user != null) {
            this.currentUser = user;
            view.loginSuccess(user);
            view.showMessage("Bienvenue, " + user.getLogin() + " !", "Connexion réussie", JOptionPane.INFORMATION_MESSAGE);
            refreshArticleList();
        } else {
            view.showMessage("Login ou mot de passe incorrect.", "Erreur d'authentification", JOptionPane.ERROR_MESSAGE);
            // On redemande le login si échec
            view.showLoginScreen();
        }
    }

    // --- GESTION DES ARTICLES (CRUD) ---
    public void refreshArticleList() {
        view.updateArticleList(articleDAO.findAll());
    }

    public void handleCreateArticle(String nom, String desc, double prix, int stock) {
        if (currentUser == null || !currentUser.isAdmin()) {
            view.showMessage("Accès refusé. Réservé aux administrateurs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Article newArticle = new Article(nom, desc, prix, stock);
        articleDAO.create(newArticle);
        articleDAO.saveAll(); // Persistance binaire
        refreshArticleList();
    }

    public void handleDeleteArticle(long id) {
        if (currentUser == null || !currentUser.isAdmin()) {
            view.showMessage("Accès refusé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (articleDAO.delete(id)) {
            articleDAO.saveAll();
            refreshArticleList();
            view.showMessage("Article supprimé.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } else {
            view.showMessage("Article introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleSaveAll() {
        articleDAO.saveAll();
        view.showMessage("Données sauvegardées avec succès.", "Persistance", JOptionPane.INFORMATION_MESSAGE);
    }
}