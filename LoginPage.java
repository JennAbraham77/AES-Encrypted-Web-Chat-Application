import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage {
    
    static class GradientPanel extends JPanel {
        private Color startColor;
        private Color endColor;

        public GradientPanel(Color startColor, Color endColor) {
            this.startColor = startColor;
            this.endColor = endColor;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            

            GradientPaint gradient = new GradientPaint(
                0, 0, startColor, 
                0, getHeight(), endColor 
            );
            
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight()); 
        }
    }

    public static void main(String[] args) {

        JFrame fr = new JFrame("Login Page");
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setSize(1300, 750);
        fr.setResizable(true);
        fr.setLocationRelativeTo(null);
        fr.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(11, 6, 31));
        topPanel.setPreferredSize(new Dimension(850, fr.getHeight()));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        ImageIcon originalIcon = new ImageIcon("logo.png"); 
        Image scaledImage = originalIcon.getImage().getScaledInstance(550, 350, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel aboutLabel = new JLabel("<html><div style='text-align:center;'> <br>&nbsp;&nbsp;&nbsp;This AES-encrypted web chat application offers a secure platform for confidential communication between students and counselors. Students can browse and select from a variety of counselors based on their individual needs and preferences, fostering a more personalized and comfortable support experience. The system goes beyond traditional student-counselor interactions, creating a modern, accessible space for meaningful connection.<br></div></html>");
        aboutLabel.setFont(new Font("Times new roman", Font.ITALIC, 18));
        aboutLabel.setForeground(new Color(137, 130, 161));
        aboutLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutLabel.setVisible(false); 

        JButton aboutButton = new JButton("About");
        aboutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutButton.setFocusPainted(false);
        aboutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        aboutButton.setBackground(new Color(92, 184, 92)); 
        aboutButton.setForeground(Color.WHITE);
        aboutButton.setMaximumSize(new Dimension(160, 40));
        aboutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        aboutButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        aboutButton.setContentAreaFilled(false);
        aboutButton.setOpaque(true);
        aboutButton.setBorder(BorderFactory.createLineBorder(new Color(92, 184, 92), 2));
        
        aboutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                aboutButton.setBackground(new Color(25, 77, 25)); 
            }
        
            public void mouseExited(java.awt.event.MouseEvent evt) {
                aboutButton.setBackground(new Color(92, 184, 92)); 
            }
        });
        
        aboutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aboutLabel.setVisible(!aboutLabel.isVisible()); 
                topPanel.revalidate();
                topPanel.repaint();
            }
        });

        topPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        topPanel.add(imageLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        topPanel.add(aboutButton);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(aboutLabel);

        fr.add(topPanel, BorderLayout.WEST);

        GradientPanel mainPanel = new GradientPanel(new Color(0, 128, 128), new Color(144, 238, 144)); // Teal to Light Green

        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0;
        gbcMain.gridy = GridBagConstraints.RELATIVE;
        gbcMain.insets = new Insets(10, 10, 10, 10);
        gbcMain.fill = GridBagConstraints.HORIZONTAL;

        JLabel welcomeLabel = new JLabel("Please Login to access the chat facility!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.BLACK);

        JPanel boxPanel = new JPanel(new GridBagLayout());
        boxPanel.setBackground(Color.WHITE);
        boxPanel.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 2));
        boxPanel.setPreferredSize(new Dimension(330, 260)); 
        boxPanel.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 3, true));

        GridBagConstraints gbcBox = new GridBagConstraints();
        gbcBox.insets = new Insets(5, 10, 5, 10);
        gbcBox.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(50, 50, 50));

        gbcBox.gridx = 0;
        gbcBox.gridy=0;
        gbcBox.gridwidth = 2;
        gbcBox.anchor = GridBagConstraints.CENTER;
        boxPanel.add(titleLabel, gbcBox);

        gbcBox.gridy++;
        gbcBox.gridwidth = 1;
        gbcBox.anchor = GridBagConstraints.WEST;
        JLabel roleLabel = new JLabel("Login as:");
        boxPanel.add(roleLabel, gbcBox);

        gbcBox.gridx = 1;
        String[] roles = {"Student", "Counselor"};
        JComboBox<String> roleDropdown = new JComboBox<>(roles);
        roleDropdown.setPreferredSize(new Dimension(190, 25));
        boxPanel.add(roleDropdown, gbcBox);

        gbcBox.gridx = 0;
        gbcBox.gridy++;
        JLabel userLabel = new JLabel("Username:");
        boxPanel.add(userLabel, gbcBox);

        gbcBox.gridx = 1;
        JTextField userField = new JTextField(17);
        userField.setFont(new Font("Arial", Font.PLAIN, 14));
        boxPanel.add(userField, gbcBox);

        gbcBox.gridx = 0;
        gbcBox.gridy++;
        JLabel passLabel = new JLabel("Password:");
        boxPanel.add(passLabel, gbcBox);

        gbcBox.gridx = 1;
        JPasswordField passField = new JPasswordField(17);
        passField.setFont(new Font("Arial", Font.PLAIN, 14));
        boxPanel.add(passField, gbcBox);

        gbcBox.gridx = 0;
        gbcBox.gridy++;
        gbcBox.gridwidth = 2;
        gbcBox.anchor = GridBagConstraints.CENTER;
        
        
        JButton loginButton = new JButton("Submit");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(120, 30));
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(25, 12, 39)); 
            }
        
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(70, 130, 180)); 
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                String selectedRole = (String) roleDropdown.getSelectedItem();

                if (DatabaseConnection.validateLogin(username, password, selectedRole)) {
                    if (selectedRole.equalsIgnoreCase("Student")) {
                        new StudentDashboard(username);
                        fr.dispose();
                    } else if (selectedRole.equalsIgnoreCase("Counselor")) {
                        new CounselorDashboard(username);
                        fr.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(fr, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        boxPanel.add(loginButton, gbcBox);

        mainPanel.add(welcomeLabel, gbcMain);
        mainPanel.add(boxPanel, gbcMain);

        fr.add(mainPanel, BorderLayout.CENTER);

        fr.setVisible(true);
    }
}
