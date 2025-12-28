package org.example.view;

import org.example.Model.domain.Article;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ArticleTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Nom", "Description", "Prix", "Stock", "Dernière MÀJ"};
    private List<Article> articles = new ArrayList<>();

    // Contrainte n°4 : Utilisation de DateTimeFormatter
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void setArticles(List<Article> articles) {
        this.articles = articles;
        fireTableDataChanged(); // Notifie la JTable qu'il faut se rafraîchir
    }

    @Override
    public int getRowCount() { return articles.size(); }

    @Override
    public int getColumnCount() { return columnNames.length; }

    @Override
    public String getColumnName(int col) { return columnNames[col]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Article article = articles.get(rowIndex);
        switch (columnIndex) {
            case 0: return article.getId();
            case 1: return article.getNom();
            case 2: return article.getDescription();
            case 3: return String.format("%.2f €", article.getPrix());
            case 4: return article.getQuantiteStock();
            case 5:
                // Application du formateur de date
                return article.getDateMiseAJour().format(formatter);
            default: return null;
        }
    }
    public class StockColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // On récupère la valeur de la colonne "Stock" (indice 4)
            int stock = (int) table.getModel().getValueAt(row, 4);

            if (stock < 5) {
                c.setForeground(Color.RED); // Alerte stock bas
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            } else {
                c.setForeground(table.getForeground()); // Couleur normale
            }
            return c;
        }
    }
}