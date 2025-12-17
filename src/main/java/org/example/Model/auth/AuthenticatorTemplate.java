package org.example.Model.auth;

import org.example.Model.domain.Utilisateur;

public abstract class AuthenticatorTemplate {

    // Voici la "Template Method" : l'algorithme est fixé ici
    public final Utilisateur login(String login, String password) {
        // 1. Charger les données (dépend de l'implémentation : mémoire ou fichier)
        Utilisateur user = findUserByLogin(login);

        // 2. Vérifier si l'utilisateur existe
        if (user == null) return null;

        // 3. Vérifier le mot de passe
        if (user.getMotDePasseHash().equals(password)) {
            return user; // Succès
        }

        return null; // Échec
    }

    // Ces méthodes sont "abstraites" : les enfants vont les remplir
    protected abstract Utilisateur findUserByLogin(String login);
}