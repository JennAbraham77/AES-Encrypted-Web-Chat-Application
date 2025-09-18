import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class CounselorDashboard {
    private String username;
    private JPanel requestPanel;
    private HashMap<String, ChatUI> activeChats = new HashMap<>();

    private static final String DB_URL = "jdbc:mysql://localhost:3306/securechatdb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "theglory095";

    public CounselorDashboard(String username) {
        this.username = username;

        JFrame frame = new JFrame("Counselor Dashboard");
        frame.setSize(1300, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(44, 122, 107));

        frame.add(createTopPanel(username), BorderLayout.NORTH);

        requestPanel = new JPanel();
        requestPanel.setLayout(new BoxLayout(requestPanel, BoxLayout.Y_AXIS));
        requestPanel.setBackground(new Color(200, 232, 215)); // Background color

        JScrollPane scrollPane = new JScrollPane(requestPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                SwingUtilities.invokeLater(() -> loadRequests());
            }
        }, 0, 3000);

        frame.setVisible(true);
    }

    private JPanel createTopPanel(String username) {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(11, 6, 31));

        JLabel headingLabel = new JLabel("<html><span style='color:white;'>Counselor Dashboard</span></html>", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Papyrus", Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        welcomeLabel.setOpaque(true);
        welcomeLabel.setBackground(new Color(11, 6, 31));
        welcomeLabel.setForeground(Color.WHITE);

        topPanel.add(headingLabel);
        topPanel.add(welcomeLabel);
        topPanel.add(Box.createVerticalStrut(15));

        return topPanel;
    }

    private void loadRequests() {
        requestPanel.removeAll();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String pendingSql = "SELECT student_username FROM chat_requests WHERE counselor_username = ? AND status = 'pending'";
            PreparedStatement pendingStmt = conn.prepareStatement(pendingSql);
            pendingStmt.setString(1, username);
            ResultSet pendingRs = pendingStmt.executeQuery();

            boolean hasPending = false;
            while (pendingRs.next()) {
                hasPending = true;
                String studentUsername = pendingRs.getString("student_username");

                JPanel pendingBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
                pendingBox.setBackground(new Color(232, 244, 242));
                pendingBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                pendingBox.add(new JLabel("📩 Request from: " + studentUsername));

                JButton acceptBtn = new JButton("Accept");
                acceptBtn.addActionListener(e -> handleAccept(studentUsername));

                JButton rejectBtn = new JButton("Reject");
                rejectBtn.addActionListener(e -> handleResponse(studentUsername, "rejected"));

                pendingBox.add(acceptBtn);
                pendingBox.add(rejectBtn);
                requestPanel.add(pendingBox);
            }

            if (!hasPending) {
                JLabel noPendingLabel = new JLabel("No pending requests.");
                noPendingLabel.setForeground(Color.BLACK);
                requestPanel.add(noPendingLabel);
            }

            String acceptedSql = "SELECT student_username FROM chat_requests WHERE counselor_username = ? AND status = 'accepted'";
            PreparedStatement acceptedStmt = conn.prepareStatement(acceptedSql);
            acceptedStmt.setString(1, username);
            ResultSet acceptedRs = acceptedStmt.executeQuery();

            while (acceptedRs.next()) {
                String studentUsername = acceptedRs.getString("student_username");

                JPanel activeBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
                activeBox.setBackground(new Color(213, 232, 212));
                activeBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                activeBox.add(new JLabel("✅ Active chat with: " + studentUsername));

                JButton endBtn = new JButton("End Chat");
                endBtn.addActionListener(e -> endChat(studentUsername));

                activeBox.add(endBtn);
                requestPanel.add(activeBox);
            }

            requestPanel.revalidate();
            requestPanel.repaint();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleAccept(String studentUsername) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String checkSql = "SELECT COUNT(*) FROM chat_requests WHERE counselor_username = ? AND status = 'accepted'";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "⚠️ You already have an active chat. End it first.");
                return;
            }

            String updateSql = "UPDATE chat_requests SET status = 'accepted' WHERE student_username = ? AND counselor_username = ?";
            PreparedStatement stmt = conn.prepareStatement(updateSql);
            stmt.setString(1, studentUsername);
            stmt.setString(2, username);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "✅ Chat accepted. Opening chat...");

            SwingUtilities.invokeLater(() -> {
                if (!activeChats.containsKey(studentUsername)) {
                    ChatUI chatWindow = new ChatUI(username, studentUsername, true);
                    activeChats.put(studentUsername, chatWindow);
                }
            });

            loadRequests();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleResponse(String studentUsername, String status) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "UPDATE chat_requests SET status = ? WHERE student_username = ? AND counselor_username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setString(2, studentUsername);
            stmt.setString(3, username);
            stmt.executeUpdate();

            if ("rejected".equals(status)) {
                JOptionPane.showMessageDialog(null, "❌ Chat request rejected.");
            }

            loadRequests();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void endChat(String studentUsername) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "UPDATE chat_requests SET status = 'ended' WHERE student_username = ? AND counselor_username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentUsername);
            stmt.setString(2, username);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "🔚 Chat with " + studentUsername + " has been ended.");

                if (activeChats.containsKey(studentUsername)) {
                    ChatUI chatWindow = activeChats.get(studentUsername);
                    chatWindow.closeWindow();
                    activeChats.remove(studentUsername);
                }

                loadRequests();
            } else {
                JOptionPane.showMessageDialog(null, "⚠️ No active chat found.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
