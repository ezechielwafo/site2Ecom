package org.example.Model.persistance;

import org.example.Model.domain.Article;

import java.util.List;

public interface ArticleDAO {

    // CRUD
    Article create(Article article); // Ajoute un article et lui attribue un ID
    List<Article> findAll();
    Article findById(long id);
    boolean update(Article article);
    boolean delete(long id);

    // PERSISTANCE (Sérialisation des conteneurs)
    void saveAll();
    void loadAll();
}