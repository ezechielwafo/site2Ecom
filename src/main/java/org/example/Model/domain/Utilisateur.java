package org.example.Model.domain;

import java.io.Serializable;

public class Utilisateur implements EstIdentifiable, Serializable {
    private static final long serialVersionUID = 1L;

    private final long id;
    private String login;
    private String motDePasseHash; // On utilise un Hash en pratique
    private boolean isAdmin;

    public Utilisateur(long id, String login, String motDePasseHash, boolean isAdmin) {
        this.id = id;
        this.login = login;
        this.motDePasseHash = motDePasseHash;
        this.isAdmin = isAdmin;
    }

    @Override
    public long getId() { return id; }
    public String getLogin() { return login; }
    public String getMotDePasseHash() { return motDePasseHash; }
    public boolean isAdmin() { return isAdmin; }

    // Simplification : Dans ce TP, on peut comparer directement le mot de passe
    public boolean checkPassword(String password) {
        // TODO: En vrai, hacher 'password' et comparer au 'motDePasseHash'
        return motDePasseHash.equals(password);
    }
}