package org.example.Model.persistance;

import org.example.Model.domain.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryArticleDAO implements ArticleDAO {

    private List<Article> articles;
    private long nextId;

    public InMemoryArticleDAO() {
        this.articles = new ArrayList<>();
        this.nextId = 1;
        loadAll(); // Tente de charger au démarrage
    }

    @Override
    public Article create(Article article) {
        // Crée une nouvelle instance avec l'ID géré par le DAO
        Article newArticle = new Article(nextId++, article.getNom(), article.getDescription(), article.getPrix(), article.getQuantiteStock());
        articles.add(newArticle);
        return newArticle;
    }

    @Override
    public List<Article> findAll() {
        return new ArrayList<>(articles);
    }

    @Override
    public Article findById(long id) {
        return articles.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
    }

    @Override
    public boolean update(Article updatedArticle) {
        Optional<Article> existing = articles.stream()
                .filter(a -> a.getId() == updatedArticle.getId())
                .findFirst();

        if (existing.isPresent()) {
            Article oldArticle = existing.get();
            // Mise à jour de toutes les propriétés modifiables
            oldArticle.setNom(updatedArticle.getNom());
            oldArticle.setDescription(updatedArticle.getDescription());
            oldArticle.setPrix(updatedArticle.getPrix());
            oldArticle.setQuantiteStock(updatedArticle.getQuantiteStock());
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        return articles.removeIf(a -> a.getId() == id);
    }

    @Override
    public void saveAll() {
        // TODO: Utiliser ObjectOutputStream pour sérialiser 'articles' dans un fichier binaire
        System.out.println("DAO: Sauvegarde des articles (à implémenter)...");
    }

    @Override
    public void loadAll() {
        // TODO: Utiliser ObjectInputStream pour désérialiser 'articles' du fichier binaire
        // Si le fichier n'existe pas, ou en cas d'erreur, initialiser avec des données de test:
        if (articles.isEmpty()) {
            System.out.println("DAO: Initialisation des données de test.");
            articles.add(new Article(nextId++, "Téléphone X42", "Écran OLED, 5G", 899.99, 5));
            articles.add(new Article(nextId++, "Écouteurs sans fil", "Annulation de bruit", 129.00, 20));
        }
        // Assurez-vous que nextId est mis à jour après le chargement pour éviter les doublons d'ID
        this.nextId = articles.stream().mapToLong(Article::getId).max().orElse(0) + 1;
    }
}