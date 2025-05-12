import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class ChatEncryption {
    private static final String AES = "AES";
    private static final String SECRET_KEY = "thisis16byteskey"; // 16-character key

    private static SecretKeySpec generateKey(String key) throws Exception {
        return new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES);
    }

    public static String encryptMessage(String message) throws Exception {
        SecretKeySpec keySpec = generateKey(SECRET_KEY);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptMessage(String encryptedMessage) {
        try {
            SecretKeySpec keySpec = generateKey(SECRET_KEY);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedMessage);
            return new String(cipher.doFinal(decodedBytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Decryption failed: " + e.getMessage());
            return "[ERROR: Could not decrypt message]";
        }
    }

    public static void sendMessage(String sender, String receiver, String message) {
        try {
            String encryptedMessage = encryptMessage(message);
            
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/securechatdb", "root", "theglory095");
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO messages (sender, receiver, message, sent_at) VALUES (?, ?, ?, NOW())")) {
    
                stmt.setString(1, sender);
                stmt.setString(2, receiver);
                stmt.setString(3, encryptedMessage);
                
                System.out.println("Executing SQL: INSERT INTO messages (sender, receiver, message, sent_at) VALUES ('" 
                             + sender + "', '" + receiver + "', '" + encryptedMessage + "', NOW())");
    
                int rowsInserted = stmt.executeUpdate();
                System.out.println("Rows inserted: " + rowsInserted);
            }
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static List<String> receiveMessages(String user, Timestamp lastReceivedTimestamp) {
        List<String> messages = new ArrayList<>();
        String query = "SELECT sender, message, sent_at FROM messages WHERE receiver = ? AND sent_at > ? ORDER BY sent_at ASC";
        Timestamp latestTimestamp = lastReceivedTimestamp;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/securechatdb", "root", "theglory095");
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, user);
            stmt.setTimestamp(2, lastReceivedTimestamp != null ? lastReceivedTimestamp : Timestamp.valueOf("2001-07-05 00:00:00"));

            System.out.println("Executing SQL Query: " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
    
            int count = 0;
            while (rs.next()) {
                count++;
                String sender = rs.getString("sender");
                String encryptedMessage = rs.getString("message");
                Timestamp sentAt = rs.getTimestamp("sent_at");
    
                System.out.println("Retrieved: Sender: " + sender + " | Encrypted: " + encryptedMessage + " | Sent At: " + sentAt);    
                String decryptedMessage = decryptMessage(encryptedMessage);
                messages.add(sender + ": " + decryptedMessage);
    
                latestTimestamp = sentAt;
            }
    
            System.out.println("Total messages retrieved: " + count);
    
        } catch (Exception e) {
            System.err.println("Error retrieving messages: " + e.getMessage());
            e.printStackTrace();
        }
        return messages;
    }
}
