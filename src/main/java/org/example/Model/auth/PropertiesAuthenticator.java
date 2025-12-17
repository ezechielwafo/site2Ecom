package org.example.Model.auth;

import org.example.Model.domain.Utilisateur;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesAuthenticator extends AuthenticatorTemplate {

    @Override
    protected Utilisateur findUserByLogin(String login) {
        Properties prop = new Properties();
        try (FileInputStream in = new FileInputStream("src/main/resources/users.properties")) {
            prop.load(in);
            String data = prop.getProperty(login); // Format dans le fichier: admin=pass,true
            if (data != null) {
                String[] parts = data.split(",");
                return new Utilisateur(10, login, parts[0], Boolean.parseBoolean(parts[1]));
            }
        } catch (Exception e) {
            System.err.println("Erreur lecture fichier properties: " + e.getMessage());
        }
        return null;
    }
}