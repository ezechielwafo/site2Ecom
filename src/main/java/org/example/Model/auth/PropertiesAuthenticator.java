package org.example.Model.auth;

import org.example.Model.domain.Utilisateur;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesAuthenticator extends AuthenticatorTemplate {
    private static final String FILE_PATH = "users.properties";

    @Override
    protected Utilisateur findUserByLogin(String login) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(FILE_PATH)) {
            props.load(fis);

            String value = props.getProperty(login);
            if (value != null) {
                // On découpe la ligne "password,isAdmin"
                String[] parts = value.split(",");
                String password = parts[0];
                boolean isAdmin = Boolean.parseBoolean(parts[1]);

                // On retourne l'utilisateur trouvé
                return new Utilisateur(System.currentTimeMillis(), login, password, isAdmin);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier properties : " + e.getMessage());
        }
        return null; // Utilisateur non trouvé ou erreur de fichier
    }
}