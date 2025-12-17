package org.example.view;

import org.example.Model.domain.Article;
import org.example.Model.domain.Utilisateur;
import org.example.controller.MainController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame implements ViewManager {

    private MainController controller;
    private JTable tableArticles;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public MainFrame() {
        // Configuration de base (Etape 1 du TP)
        setTitle("HEPL E-Commerce - Gestion des Articles");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Barre de Menu (Etape 2)
        initMenuBar();

        // 2. Tableau des articles (Centre - JTable)
        initTable();

        // 3. Panneau de contrôle (Sud - Boutons)
        initControlPanel();

        // 4. Barre d'état
        statusLabel = new JLabel(" Bienvenue. Veuillez vous connecter.");
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem itemQuitter = new JMenuItem("Quitter");
        itemQuitter.addActionListener(e -> System.exit(0));
        menuFichier.add(itemQuitter);

        JMenu menuProduits = new JMenu("Produits");
        JMenuItem itemRafraichir = new JMenuItem("Rafraîchir la liste");
        itemRafraichir.addActionListener(e -> controller.refreshArticleList());
        menuProduits.add(itemRafraichir);

        menuBar.add(menuFichier);
        menuBar.add(menuProduits);
        setJMenuBar(menuBar);
    }

    private void initTable() {
        String[] columnNames = {"ID", "Nom", "Description", "Prix (€)", "Stock", "Dernière MÀJ"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Lecture seule
        };

        tableArticles = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableArticles);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des Articles"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAjouter = new JButton("Ajouter Article");
        btnAjouter.addActionListener(e -> showArticleEditor(null)); // null = création

        JButton btnSupprimer = new JButton("Supprimer Sélection");
        btnSupprimer.addActionListener(e -> {
            int row = tableArticles.getSelectedRow();
            if (row != -1) {
                long id = (long) tableModel.getValueAt(row, 0);
                controller.handleDeleteArticle(id);
            } else {
                showMessage("Sélectionnez un article à supprimer.", "Attention", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(btnAjouter);
        panel.add(btnSupprimer);
        add(panel, BorderLayout.NORTH);
    }

    // --- Implémentation de l'interface ViewManager ---

    @Override
    public void setController(MainController controller) {
        this.controller = controller;
    }

    @Override
    public void startApplication() {
        setVisible(true);
        // Au démarrage, on force souvent le login (on verra cela à l'étape suivante)
    }

    @Override
    public void updateArticleList(List<Article> articles) {
        tableModel.setRowCount(0); // On vide le tableau
        for (Article a : articles) {
            tableModel.addRow(new Object[]{
                    a.getId(),
                    a.getNom(),
                    a.getDescription(),
                    a.getPrix(),
                    a.getQuantiteStock(),
                    a.getDateMiseAJour()
            });
        }
    }

    @Override
    public void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    @Override
    public void showLoginScreen() {
        // On créera une JDialog pour le login plus tard
        System.out.println("Affichage écran login...");
    }

    @Override
    public void showArticleEditor(Article article) {
        // On créera une JDialog pour l'ajout/édition
        String nom = JOptionPane.showInputDialog(this, "Nom de l'article :");
        if (nom != null && !nom.isEmpty()) {
            controller.handleCreateArticle(nom, "Description", 10.0, 5);
        }
    }

    @Override
    public void loginSuccess(Utilisateur user) {
        statusLabel.setText(" Connecté en tant que : " + user.getLogin() + (user.isAdmin() ? " (Admin)" : ""));
    }
}