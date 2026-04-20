import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame implements ActionListener {
    private JTextField userText;
    private JPasswordField passwordText;
    private JLabel success;
    private JButton button;

    public LoginFrame() {
        setTitle("Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        JLabel userLabel = new JLabel("User Name");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        button = new JButton("Login");
        button.setBounds(10, 80, 80, 25);
        button.addActionListener(this);
        panel.add(button);

        success = new JLabel("");
        success.setBounds(10, 110, 300, 25);
        panel.add(success);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String user = userText.getText();
        String password = new String(passwordText.getPassword());

        if (user.equals("Ville") && password.equals("12345")) {
            showSuccessDialog();
            passwordText.setText("");
            button.setEnabled(false);
        } else {
            success.setText("Invalid Username or Password");
        }
    }

    private void showSuccessDialog() {
        JFrame successFrame = new JFrame("Login Successful");
        successFrame.setSize(350, 200);
        successFrame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(null);

        JLabel successMessage = new JLabel("Login Successful!");
        successMessage.setBounds(100, 25, 150, 25);
        panel.add(successMessage);

        JButton okButton = new JButton("OK");
        okButton.setBounds(100, 70, 80, 25);
        okButton.addActionListener(e -> {
            successFrame.dispose();
            new TaskTrackerFrame(userText.getText());
        });

        panel.add(okButton);
        successFrame.add(panel);
        successFrame.setVisible(true);
    }
}