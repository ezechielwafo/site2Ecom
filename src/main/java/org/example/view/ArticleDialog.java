package org.example.view;

import org.example.Model.domain.Article;
import javax.swing.*;
import java.awt.*;

public class ArticleDialog extends JDialog {
    private JTextField txtNom = new JTextField(20);
    private JTextField txtDesc = new JTextField(20);
    private JTextField txtPrix = new JTextField(10);
    private JTextField txtStock = new JTextField(10);
    private boolean validated = false;

    public ArticleDialog(Frame parent, String title) {
        super(parent, title, true);

        // Couleur de fond de la fenêtre (VERT)
        Color vertFenetre = new Color(46, 139, 87);
        getContentPane().setBackground(vertFenetre);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Les champs sont maintenant BLANCS avec texte NOIR (par défaut)
        // On s'assure juste qu'ils ont une bordure propre
        styleField(txtNom); styleField(txtDesc); styleField(txtPrix); styleField(txtStock);

        formPanel.add(createWhiteLabel("Nom :")); formPanel.add(txtNom);
        formPanel.add(createWhiteLabel("Description :")); formPanel.add(txtDesc);
        formPanel.add(createWhiteLabel("Prix (€) :")); formPanel.add(txtPrix);
        formPanel.add(createWhiteLabel("Stock :")); formPanel.add(txtStock);

        add(formPanel, BorderLayout.CENTER);

        // Bouton de validation NOIR avec texte BLANC
        JButton btnValider = new JButton("Valider");
        btnValider.setBackground(Color.BLACK);
        btnValider.setForeground(Color.WHITE);
        btnValider.addActionListener(e -> { if(validateInput()) { validated = true; dispose(); } });

        JPanel pnlBtn = new JPanel();
        pnlBtn.setOpaque(false);
        pnlBtn.add(btnValider);
        add(pnlBtn, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    private void styleField(JTextField field) {
        field.setBackground(Color.WHITE); // REPASSE EN BLANC
        field.setForeground(Color.BLACK); // TEXTE NOIR
        field.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
    }

    private JLabel createWhiteLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE); // Libellés toujours blancs pour trancher sur le vert
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        return lbl;
    }

    public void setArticle(Article a) {
        if (a != null) {
            txtNom.setText(a.getNom());
            txtDesc.setText(a.getDescription());
            txtPrix.setText(String.valueOf(a.getPrix()));
            txtStock.setText(String.valueOf(a.getQuantiteStock()));
        }
    }

    private boolean validateInput() {
        try {
            Double.parseDouble(txtPrix.getText());
            Integer.parseInt(txtStock.getText());
            return !txtNom.getText().isEmpty();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Données invalides !");
            return false;
        }
    }

    public boolean isValidated() { return validated; }
    public String getNom() { return txtNom.getText(); }
    public String getDesc() { return txtDesc.getText(); }
    public double getPrix() { return Double.parseDouble(txtPrix.getText()); }
    public int getStock() { return Integer.parseInt(txtStock.getText()); }
}