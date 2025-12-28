package org.example.Model.auth;

import org.example.Model.domain.Utilisateur;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesAuthenticator extends AuthenticatorTemplate {
    // Le nom du fichier suffit car il est à la racine du dossier resources
    private static final String FILE_NAME = "users.properties";

    @Override
    protected Utilisateur findUserByLogin(String login) {
        Properties props = new Properties();

        // Utilisation du ClassLoader pour lire le fichier dans src/main/resources
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                System.err.println("Erreur : Impossible de trouver " + FILE_NAME + " dans les ressources.");
                return null;
            }

            props.load(is);

            String value = props.getProperty(login);
            if (value != null) {
                // On découpe la ligne "password,isAdmin"
                String[] parts = value.split(",");
                if (parts.length >= 2) {
                    String password = parts[0];
                    boolean isAdmin = Boolean.parseBoolean(parts[1]);

                    // On retourne l'utilisateur trouvé avec un ID unique (timestamp)
                    return new Utilisateur(System.currentTimeMillis(), login, password, isAdmin);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier properties : " + e.getMessage());
        }
        return null; // Utilisateur non trouvé ou erreur
    }
}