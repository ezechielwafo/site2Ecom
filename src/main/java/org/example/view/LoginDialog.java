package org.example.view;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private JTextField txtLogin = new JTextField(15);
    private JPasswordField txtPass = new JPasswordField(15);
    private boolean succeeded = false;

    public LoginDialog(Frame parent) {
        super(parent, "Connexion", true);
        setLayout(new GridLayout(3, 2));

        add(new JLabel(" Login:")); add(txtLogin);
        add(new JLabel(" Pass:")); add(txtPass);

        JButton btnLogin = new JButton("Se connecter");
        btnLogin.addActionListener(e -> {
            succeeded = true;
            dispose();
        });
        add(btnLogin);

        pack();
        setLocationRelativeTo(parent);
    }

    public String getLogin() { return txtLogin.getText(); }
    public String getPassword() { return new String(txtPass.getPassword()); }
    public boolean isSucceeded() { return succeeded; }
}