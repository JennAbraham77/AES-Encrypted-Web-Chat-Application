# AES Encrypted Student-Counselor Web Chat Application

A secure real-time web chat system enabling students to communicate confidentially with counselors. Built with Java and MySQL, the app uses AES-256 encryption to protect messages and includes role-based dashboards for both students and counselors.

**Features:**<br>
🛡️_AES-256 Encryption:_ All messages are encrypted before storage and decrypted on display.

🔐 _Login System:_ User authentication.

🧑‍🎓 _Student Dashboard:_ View available counselors and initiate chats.

🧑‍💼 _Counselor Dashboard:_ Accept or decline chat requests, and manage ongoing sessions.

💬 _Real-time Messaging: _Seamless chat experience.

🗃️ _Message History: _Encrypted message history stored in MySQL.

🌐_ Java + Swing Interface: _Custom-built GUI using Java Swing.

🛠️** Technologies Used:**
_Frontend _- Java Swing 
_Backend	 _ - Java (Servlets)
_Database _ -	MySQL
_Security  _-	AES Encryption, BCrypt Hashing
_Real-time_ - Chat	Polling 

📦 **Prerequisites**
Java JDK 8+

MySQL Server

IDE (IntelliJ, Eclipse, NetBeans, VSCode etc)

🔐** Encryption Overview**
AES 256-bit encryption secures messages in transit and at rest.

Java’s javax.crypto library is used for key generation and encryption/decryption.

Encryption keys are stored securely on the server-side and never exposed to clients.

👥** User Roles**
Student: Can register/login, view counselor profiles, initiate or cancel chat requests, and chat securely.

Counselor: Can login, view incoming requests, accept/reject chats, and communicate securely.

📸 **Screenshots/ Preview:**<br><br>
A._ Login Window_<br>
![image](https://github.com/user-attachments/assets/eae22996-dc32-4f2d-903e-683f8a8f7231)
![image](https://github.com/user-attachments/assets/6344ddc5-7e1c-46ad-b28c-5e98d7ee50c6)

B._Student Dashboard_<br>
![image](https://github.com/user-attachments/assets/d76487f8-a4ad-42ad-9f0f-baaf69699751)
![image](https://github.com/user-attachments/assets/9835aeee-555d-4a74-a9c6-3fed23edbcaf)

C._Counsellor Dashboard_<br>
![image](https://github.com/user-attachments/assets/50060963-1c49-4a7e-b00f-8df129ffe4a9)
![image](https://github.com/user-attachments/assets/1c646727-134c-4f91-905a-0fcd0fc01259)

D._Chat UI Window_<br>
![image](https://github.com/user-attachments/assets/8ba826d8-466e-4688-8a12-0b39411fa98e)
![image](https://github.com/user-attachments/assets/e8dd8fd8-9bed-4002-b82a-fa83cd80013d)

🤝 **Contributing**
Pull requests are welcome! For major changes, please open an issue first to discuss what you'd like to change.

📜 **License**
MIT License







