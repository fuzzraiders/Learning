package registration

import javax.swing.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class RegistrationForm : JFrame(), ActionListener {

    var nameLabel = JLabel("Name:")
    var emailLabel = JLabel("Email:")
    var userLabel = JLabel("Username:")
    var passLabel = JLabel("Password:")

    var nameField = JTextField()
    var emailField = JTextField()
    var userField = JTextField()
    var passField = JPasswordField()

    var registerButton = JButton("Register")

    init {

        title = "Registration Form"
        setSize(400,300)
        layout = null
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        nameLabel.setBounds(50,40,100,25)
        add(nameLabel)

        emailLabel.setBounds(50,80,100,25)
        add(emailLabel)

        userLabel.setBounds(50,120,100,25)
        add(userLabel)

        passLabel.setBounds(50,160,100,25)
        add(passLabel)

        nameField.setBounds(150,40,150,25)
        add(nameField)

        emailField.setBounds(150,80,150,25)
        add(emailField)

        userField.setBounds(150,120,150,25)
        add(userField)

        passField.setBounds(150,160,150,25)
        add(passField)

        registerButton.setBounds(150,200,100,30)
        registerButton.addActionListener(this)
        add(registerButton)

        isVisible = true
    }

    override fun actionPerformed(e: ActionEvent) {

        val name = nameField.text
        val email = emailField.text
        val username = userField.text

        println("Registration successful!")
        println("Name: $name")
        println("Email: $email")
        println("Username: $username")

        JOptionPane.showMessageDialog(this,"Registration Successful!")
    }
}

fun main() {
    RegistrationForm()
}
