package org.example;

import com.formdev.flatlaf.FlatLightLaf;
import org.example.Model.persistance.ArticleDAO;
import org.example.Model.persistance.InMemoryArticleDAO;
import org.example.controller.MainController;
import org.example.view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // 1. D'ABORD, on configure le Look and Feel (AVANT toute création de fenêtre)
        try {
            // FlatLightLaf.setup() est plus stable que setLookAndFeel
            FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("Échec de l'initialisation de FlatLaf, utilisation du thème par défaut.");
        }

        // 2. ENSUITE, on démarre l'application dans le thread Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            // Initialisation du Modèle
            ArticleDAO articleDAO = new InMemoryArticleDAO();

            // Initialisation de la Vue
            MainFrame view = new MainFrame();

            // Initialisation du Contrôleur (qui lie les deux et gère le login)
            new MainController(articleDAO, view);

            System.out.println("Application démarrée avec succès.");
        });
    }
}