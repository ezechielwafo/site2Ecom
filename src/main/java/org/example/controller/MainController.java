package org.example.controller;

import org.example.Model.auth.AuthenticatorTemplate;
import org.example.Model.auth.PropertiesAuthenticator;
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

        // Authentification par fichier Properties (Contrainte du TP)
        this.setAuthenticator(new PropertiesAuthenticator());

        // On lance l'IHM et on demande le login
        this.view.startApplication();
        this.view.showLoginScreen();
    }

    public void setAuthenticator(AuthenticatorTemplate auth) {
        this.authenticator = auth;
    }

    // --- AUTHENTIFICATION ---
    public void handleLogin(String login, String password) {
        Utilisateur user = authenticator.login(login, password);

        if (user != null) {
            this.currentUser = user;
            view.loginSuccess(user);
            view.showMessage("Bienvenue, " + user.getLogin() + " !", "Connexion réussie", JOptionPane.INFORMATION_MESSAGE);
            refreshArticleList();
        } else {
            view.showMessage("Login ou mot de passe incorrect.", "Erreur d'authentification", JOptionPane.ERROR_MESSAGE);
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
        refreshArticleList();
    }

    public void handleDeleteArticle(long id) {
        if (currentUser == null || !currentUser.isAdmin()) {
            view.showMessage("Accès refusé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (articleDAO.delete(id)) {
            refreshArticleList();
            view.showMessage("Article supprimé.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } else {
            view.showMessage("Article introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- MODIFICATION (UPDATE du CRUD) ---

    // Étape A : On récupère l'objet et on demande à la vue d'ouvrir l'éditeur pré-rempli
    public void requestEditArticle(long id) {
        Article a = articleDAO.findById(id);
        if (a != null) {
            view.showArticleEditor(a);
        }
    }

    // Étape B : On enregistre les modifications envoyées par la JDialog
    public void handleUpdateArticle(long id, String nom, String desc, double prix, int stock) {
        if (currentUser == null || !currentUser.isAdmin()) {
            view.showMessage("Action non autorisée.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Article a = articleDAO.findById(id);
        if (a != null) {
            a.setNom(nom);
            a.setDescription(desc);
            a.setPrix(prix);
            a.setQuantiteStock(stock); // Correction du nom de la méthode

            articleDAO.update(a); // Sauvegarde et sérialisation
            refreshArticleList(); // Mise à jour du JTable
            view.showMessage("Article " + id + " mis à jour !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void handleSaveAll() {
        articleDAO.saveAll();
        view.showMessage("Données sauvegardées avec succès.", "Persistance", JOptionPane.INFORMATION_MESSAGE);
    }
}