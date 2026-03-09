package registration;

import javax.swing.*;
import java.awt.event.*;

public class RegistrationForm extends JFrame implements ActionListener {

    JLabel nameLabel, emailLabel, userLabel, passLabel;
    JTextField nameField, emailField, userField;
    JPasswordField passField;
    JButton registerButton;

    RegistrationForm() {

        setTitle("Registration Form");
        setSize(400,300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50,40,100,25);
        add(nameLabel);

        emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50,80,100,25);
        add(emailLabel);

        userLabel = new JLabel("Username:");
        userLabel.setBounds(50,120,100,25);
        add(userLabel);

        passLabel = new JLabel("Password:");
        passLabel.setBounds(50,160,100,25);
        add(passLabel);

        nameField = new JTextField();
        nameField.setBounds(150,40,150,25);
        add(nameField);

        emailField = new JTextField();
        emailField.setBounds(150,80,150,25);
        add(emailField);

        userField = new JTextField();
        userField.setBounds(150,120,150,25);
        add(userField);

        passField = new JPasswordField();
        passField.setBounds(150,160,150,25);
        add(passField);

        registerButton = new JButton("Register");
        registerButton.setBounds(150,200,100,30);
        registerButton.addActionListener(this);
        add(registerButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        String name = nameField.getText();
        String email = emailField.getText();
        String username = userField.getText();
        String password = new String(passField.getPassword());

        System.out.println("Registration successful!");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Username: " + username);

        JOptionPane.showMessageDialog(this, "Registration Successful!");
    }

    public static void main(String[] args) {
        new RegistrationForm();
    }
}
