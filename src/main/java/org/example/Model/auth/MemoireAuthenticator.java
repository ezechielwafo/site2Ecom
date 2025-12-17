package org.example.Model.auth;

import org.example.Model.domain.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class MemoireAuthenticator extends AuthenticatorTemplate {
    private List<Utilisateur> users = new ArrayList<>();

    public MemoireAuthenticator() {
        // Données en dur pour le test
        users.add(new Utilisateur(1, "admin", "pass", true));
        users.add(new Utilisateur(2, "user", "1234", false));
    }

    @Override
    protected Utilisateur findUserByLogin(String login) {
        return users.stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst()
                .orElse(null);
    }
}