package org.example.Model.persistance;

import org.example.Model.domain.Article;
import java.util.List;

public interface ArticleDAO {
    // CRUD
    Article create(Article article);
    List<Article> findAll();
    Article findById(long id);
    boolean update(Article article);
    boolean delete(long id);

    // PERSISTANCE
    void saveAll();
    void loadAll();
}