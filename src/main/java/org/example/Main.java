package org.example;

import org.example.Model.persistance.ArticleDAO;
import org.example.Model.persistance.InMemoryArticleDAO;
import org.example.controller.MainController;
import org.example.view.MainFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        // IMPORTANT : Démarrer l'IHM Swing dans l'EDT
        SwingUtilities.invokeLater(() -> {

            // 1. Créer le MODÈLE (DAO)
            ArticleDAO articleDAO = new InMemoryArticleDAO();
            // TODO: Créer et assembler les autres DAO (UtilisateurDAO, etc.)

            // 2. Créer la VUE (MainFrame ou LoginFrame)
            MainFrame view = new MainFrame();

            // 3. Contrôleur (fait le lien entre les deux)
            MainController controller = new MainController(articleDAO, view);
            // L'implémentation de la Vue Swing est la prochaine grande étape

            // Utilisez ici votre classe Swing réelle : ViewManager view = new MainFrame();

            // Temporairement, vous aurez besoin de créer la classe MainFrame implémentant ViewManager.

            // Pour l'instant, on utilise une version simplifiée pour que l'IDE n'affiche pas d'erreur.

            // ViewManager view = new MainFrame(); // <-- Ceci sera votre vraie classe

            // 3. Créer le CONTRÔLEUR et démarrer l'application

            // MainController controller = new MainController(articleDAO, view);

            System.out.println("Structure MVC de base créée. Prochaine étape : Implémenter la Vue Swing (MainFrame) !");
        });
    }
}