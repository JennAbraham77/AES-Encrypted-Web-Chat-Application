import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatUI {
    private JFrame frame;
    private JPanel chatPanel;
    private JTextField messageField;
    private JButton sendButton;
    private JButton endChatButton;
    private String currentUser;
    private String recipient;
    private boolean isCounselor;

    private boolean chatEnded = false;
    private Timer messageTimer;
    private Timer statusTimer;

    private List<String> displayedMessages = new ArrayList<>();

    public ChatUI(String currentUser, String recipient, boolean isCounselor) {
        this.currentUser = currentUser;
        this.recipient = recipient;
        this.isCounselor = isCounselor;
        initializeUI();
        startMessageReceiver();
        startStatusWatcher();
    }

    private void initializeUI() {
        frame = new JFrame("Chat: " + currentUser + " ↔ " + recipient);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(11, 6, 31));

        JScrollPane scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));
        messageField.setBackground(Color.WHITE);
        messageField.setForeground(Color.BLACK);
        messageField.setCaretColor(Color.BLACK);
        messageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        sendButton = new JButton("Send");
        sendButton.setFocusPainted(false);
        sendButton.setFont(new Font("Arial", Font.BOLD, 13));
        sendButton.setBackground(new Color(183, 132, 56));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.addActionListener(e -> sendMessage());

        endChatButton = new JButton("End Chat");
        endChatButton.setFocusPainted(false);
        endChatButton.setFont(new Font("Arial", Font.BOLD, 13));
        endChatButton.setBackground(new Color(102, 51, 51));
        endChatButton.setForeground(Color.WHITE);
        endChatButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        endChatButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        endChatButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to end the chat?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                endChatInDatabase();
                addMessageBubble("\uD83D\uDD1A You ended the chat.", true, false);
                disableChat();
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        closeWindow();
                    }
                }, 5000);
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(new Color(240, 240, 240));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        inputPanel.add(messageField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(sendButton);
        buttonPanel.add(endChatButton);

        inputPanel.add(buttonPanel, BorderLayout.EAST);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        frame.toFront();
    }

    private void addMessageBubble(String text, boolean isSentByCurrentUser, boolean fromLocalSend) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JTextArea messageLabel = new JTextArea(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public boolean isOpaque() {
                return false;
            }
        };

        messageLabel.setWrapStyleWord(true);
        messageLabel.setLineWrap(true);
        messageLabel.setEditable(false);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setBackground(isSentByCurrentUser ? new Color(0, 123, 255) : new Color(108, 117, 125));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        JPanel alignPanel = new JPanel();
        alignPanel.setLayout(new BoxLayout(alignPanel, BoxLayout.X_AXIS));
        alignPanel.setBackground(new Color(11, 6, 31));

        if (isSentByCurrentUser) {
            alignPanel.add(Box.createHorizontalGlue());
            alignPanel.add(messageLabel);
        } else {
            alignPanel.add(messageLabel);
            alignPanel.add(Box.createHorizontalGlue());
        }

        chatPanel.add(alignPanel);
        chatPanel.add(Box.createVerticalStrut(8));
        chatPanel.revalidate();
        scrollToBottom();

        if (fromLocalSend) {
            try {
                String encrypted = ChatEncryption.encryptMessage(text);
                displayedMessages.add(currentUser + ":" + encrypted);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage() {
        if (chatEnded) return;

        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            ChatEncryption.sendMessage(currentUser, recipient, message);
            addMessageBubble(message, true, true);
            messageField.setText("");
        }
    }

    private void startMessageReceiver() {
        messageTimer = new Timer(true);
        messageTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                receiveMessages();
            }
        }, 0, 1000);
    }

    private void receiveMessages() {
        if (chatEnded) return;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/securechatdb", "root", "theglory095");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT sender, message FROM messages WHERE " +
                             "((sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?)) ORDER BY sent_at ASC")) {

            stmt.setString(1, currentUser);
            stmt.setString(2, recipient);
            stmt.setString(3, recipient);
            stmt.setString(4, currentUser);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String sender = rs.getString("sender");
                String encryptedMessage = rs.getString("message");
                String decryptedMessage = ChatEncryption.decryptMessage(encryptedMessage);

                String uniqueKey = sender + ":" + encryptedMessage;
                if (!displayedMessages.contains(uniqueKey)) {
                    displayedMessages.add(uniqueKey);
                    boolean isSentByCurrentUser = sender.equals(currentUser);
                    SwingUtilities.invokeLater(() ->
                            addMessageBubble(decryptedMessage, isSentByCurrentUser, false)
                    );
                }
            }

        } catch (Exception e) {
            System.err.println("Error retrieving messages: " + e.getMessage());
        }
    }

    private void startStatusWatcher() {
        statusTimer = new Timer(true);
        statusTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/securechatdb", "root", "theglory095");
                     PreparedStatement stmt = conn.prepareStatement(
                             "SELECT status FROM chat_requests WHERE " +
                                     "(student_username = ? AND counselor_username = ?)")) {

                    stmt.setString(1, isCounselor ? recipient : currentUser);
                    stmt.setString(2, isCounselor ? currentUser : recipient);

                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        String status = rs.getString("status");
                        if ("ended".equalsIgnoreCase(status) && !chatEnded) {
                            SwingUtilities.invokeLater(() -> {
                                addMessageBubble("\uD83D\uDD10 This chat has been ended by the other user.", false, false);
                                disableChat();
                                new Timer().schedule(new TimerTask() {
                                    public void run() {
                                        closeWindow();
                                    }
                                }, 5000);
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 3000);
    }

    private void disableChat() {
        chatEnded = true;
        sendButton.setEnabled(false);
        endChatButton.setEnabled(false);
        messageField.setEditable(false);
    }

    private void endChatInDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/securechatdb", "root", "theglory095")) {
            String sql = "UPDATE chat_requests SET status = 'ended' WHERE " +
                    "(student_username = ? AND counselor_username = ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            String studentUsername = isCounselor ? recipient : currentUser;
            String counselorUsername = isCounselor ? currentUser : recipient;
            System.out.println("Trying to end chat: student=" + studentUsername + ", counselor=" + counselorUsername);
            
            stmt.setString(1, studentUsername);
            stmt.setString(2, counselorUsername);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            Container parent = chatPanel.getParent();
            if (parent instanceof JViewport) {
                JViewport viewport = (JViewport) parent;
                Rectangle bounds = chatPanel.getBounds();
                viewport.setViewPosition(new Point(0, bounds.height));
            }
        });
    }

    public void closeWindow() {
        if (frame != null) {
            frame.dispose();
        }
        if (messageTimer != null) messageTimer.cancel();
        if (statusTimer != null) statusTimer.cancel();
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java ChatUI <currentUser> <recipient> <isCounselor>");
            return;
        }
        new ChatUI(args[0], args[1], Boolean.parseBoolean(args[2]));
    }
}
