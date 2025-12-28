package org.example.view;

import org.example.Model.domain.Article;
import org.example.Model.domain.Utilisateur;
import org.example.controller.MainController;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class MainFrame extends JFrame implements ViewManager {

    private MainController controller;
    private JTable tableArticles;
    private ArticleTableModel tableModel;
    private JLabel statusLabel;
    private JButton btnAjouter;
    private JButton btnSupprimer;
    private JButton btnModifier;

    private final Color MARRON_HEPL = new Color(92, 64, 51);

    public MainFrame() {
        setTitle("HEPL E-Commerce - Gestion des Articles");
        setSize(1000, 650); // Légèrement plus grand pour bien voir l'image
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- 1. CONFIGURATION DU BACKGROUND PANEL ---
        BackgroundPanel bgPanel = new BackgroundPanel("vente.jpeg");
        bgPanel.setLayout(new BorderLayout());
        setContentPane(bgPanel);

        // Bordure de la fenêtre principale
        getRootPane().setBorder(BorderFactory.createLineBorder(MARRON_HEPL, 5));

        initMenuBar();
        initTable();
        initControlPanel();

        // Barre d'état avec fond marron opaque
        statusLabel = new JLabel(" Connecté : admin");
        statusLabel.setOpaque(true);
        statusLabel.setBackground(MARRON_HEPL);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setPreferredSize(new Dimension(getWidth(), 30));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem itemQuitter = new JMenuItem("Quitter");
        itemQuitter.addActionListener(e -> System.exit(0));
        menuFichier.add(itemQuitter);
        setJMenuBar(menuBar);
    }

    private void initTable() {
        tableModel = new ArticleTableModel();
        tableArticles = new JTable(tableModel);
        tableArticles.setSelectionForeground(Color.BLACK);
        tableArticles.setSelectionBackground(new Color(184, 207, 229, 150));
        // --- RENDRE LE TABLEAU SEMI-TRANSPARENT POUR LA LISIBILITÉ ---
        tableArticles.setOpaque(false);
        tableArticles.setFillsViewportHeight(true);
        tableArticles.setBackground(new Color(255, 255, 255, 50)); // Voile très léger sur la table

        // Rendre les cellules transparentes
        DefaultTableCellRenderer transparentRenderer = new DefaultTableCellRenderer();
        transparentRenderer.setOpaque(false);
        tableArticles.setDefaultRenderer(Object.class, transparentRenderer);

        // Appliquer les Renderers spécifiques (Stock et Prix)
        ArticleTableModel.StockColorRenderer stockRenderer = tableModel.new StockColorRenderer();
        tableArticles.getColumnModel().getColumn(4).setCellRenderer(stockRenderer);

        DefaultTableCellRenderer prixRenderer = new DefaultTableCellRenderer();
        prixRenderer.setHorizontalAlignment(JLabel.CENTER);
        prixRenderer.setForeground(new Color(0, 102, 204)); // Bleu pour le prix
        prixRenderer.setOpaque(false);
        tableArticles.getColumnModel().getColumn(3).setCellRenderer(prixRenderer);

        JScrollPane scrollPane = new JScrollPane(tableArticles);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Inventaire du Magasin"));

        add(scrollPane, BorderLayout.CENTER);
    }

    private void initControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(MARRON_HEPL);

        // Logo
        try {
            URL imgURL = getClass().getClassLoader().getResource("logo.png");
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                panel.add(new JLabel(new ImageIcon(img)));
            }
        } catch (Exception e) { e.printStackTrace(); }

        // Boutons colorés
        btnAjouter = new JButton("Ajouter");
        btnAjouter.setBackground(new Color(40, 167, 69)); // Vert
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.addActionListener(e -> showArticleEditor(null));

        btnModifier = new JButton("Modifier");
        btnModifier.setBackground(new Color(255, 193, 7)); // Jaune
        btnModifier.addActionListener(e -> {
            int row = tableArticles.getSelectedRow();
            if (row != -1) {
                long id = (long) tableModel.getValueAt(row, 0);
                controller.requestEditArticle(id);
            }
        });

        btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBackground(new Color(220, 53, 69)); // Rouge
        btnSupprimer.setForeground(Color.WHITE);
        btnSupprimer.addActionListener(e -> {
            int row = tableArticles.getSelectedRow();
            if (row != -1) {
                long id = (long) tableModel.getValueAt(row, 0);
                controller.handleDeleteArticle(id);
            }
        });

        panel.add(btnAjouter); panel.add(btnModifier); panel.add(btnSupprimer);
        add(panel, BorderLayout.NORTH);
    }

    @Override public void setController(MainController c) { this.controller = c; }
    @Override public void startApplication() { setVisible(true); }
    @Override public void updateArticleList(List<Article> a) { tableModel.setArticles(a); }
    @Override public void showMessage(String m, String t, int ty) { JOptionPane.showMessageDialog(this, m, t, ty); }

    @Override
    public void showLoginScreen() {
        LoginDialog ld = new LoginDialog(this);
        ld.setVisible(true);
        if (ld.isSucceeded()) controller.handleLogin(ld.getLogin(), ld.getPassword());
        else System.exit(0);
    }

    @Override
    public void showArticleEditor(Article article) {
        ArticleDialog dialog = new ArticleDialog(this, article == null ? "Ajouter" : "Modifier");
        if (article != null) dialog.setArticle(article);
        dialog.setVisible(true);

        if (dialog.isValidated()) {
            if (article == null) controller.handleCreateArticle(dialog.getNom(), dialog.getDesc(), dialog.getPrix(), dialog.getStock());
            else controller.handleUpdateArticle(article.getId(), dialog.getNom(), dialog.getDesc(), dialog.getPrix(), dialog.getStock());
        }
    }

    @Override
    public void loginSuccess(Utilisateur user) {
        statusLabel.setText(" Connecté : " + user.getLogin());
        boolean isAdmin = user.isAdmin();
        btnAjouter.setVisible(isAdmin);
        btnModifier.setVisible(isAdmin);
        btnSupprimer.setVisible(isAdmin);
    }

    // --- CLASSE INTERNE POUR LE FOND AVEC VOILE DE LISIBILITÉ ---
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String fileName) {
            URL imgURL = getClass().getClassLoader().getResource(fileName);
            if (imgURL != null) backgroundImage = new ImageIcon(imgURL).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // 1. Dessiner l'image
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

                // 2. Dessiner un voile blanc semi-transparent (180/255 d'opacité)
                // Cela rend l'image plus "pâle" pour que le texte noir ressorte
                g.setColor(new Color(255, 255, 255, 230));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}