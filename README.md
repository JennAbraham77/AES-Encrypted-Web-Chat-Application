# AES Encrypted Student-Counselor Web Chat Application

A secure real-time web chat system enabling students to communicate confidentially with counselors. Built with Java and MySQL, the app uses AES-256 encryption to protect messages and includes role-based dashboards for both students and counselors.

**Features:**<br>
- _AES-256 Encryption:_ All messages are encrypted before storage and decrypted on display.

- _Login System:_ User authentication.

- _Student Dashboard:_ View available counselors and initiate chats.

- _Counselor Dashboard:_ Accept or decline chat requests, and manage ongoing sessions.

- _Real-time Messaging:_ Seamless chat experience.

- _Message History:_ Encrypted message history stored in MySQL.

- _Java + Swing Interface:_ Custom-built GUI using Java Swing. <br><br>

🛠️ **Tech Used:** <br>
- _Frontend_ - Java Swing <br><br>
- _Backend_ - Java (Servlets) <br><br>
- _Database_ -	MySQL <br><br>
- _Security_ -	AES Encryption, BCrypt Hashing <br><br>
- _Real-time_ - Chat	Polling <br><br>

📦 **Prerequisites** <br>
- Java JDK 8+

- MySQL Server

- IDE (IntelliJ, Eclipse, NetBeans, VSCode etc)<br>

🔐 **Encryption Overview** <br>
* AES 256-bit encryption secures messages in transit and at rest.

* Java’s javax.crypto library is used for key generation and encryption/decryption.

* Encryption keys are stored securely on the server-side and never exposed to clients.<br><br>

👥 **User Roles** <br>
Student: Can register/login, view counselor profiles, initiate or cancel chat requests, and chat securely.

Counselor: Can login, view incoming requests, accept/reject chats, and communicate securely.

📸 **Screenshots/ Preview:**<br><br>
**A. _Login Window_** <br><br>
![image](https://github.com/user-attachments/assets/eae22996-dc32-4f2d-903e-683f8a8f7231)
![image](https://github.com/user-attachments/assets/6344ddc5-7e1c-46ad-b28c-5e98d7ee50c6) <br><br>

**B. _Student Dashboard**_ <br><br>
![image](https://github.com/user-attachments/assets/d76487f8-a4ad-42ad-9f0f-baaf69699751)
![image](https://github.com/user-attachments/assets/9835aeee-555d-4a74-a9c6-3fed23edbcaf) <br><br>

**C. _Counsellor Dashboard_** <br><br>
![image](https://github.com/user-attachments/assets/50060963-1c49-4a7e-b00f-8df129ffe4a9)
![image](https://github.com/user-attachments/assets/1c646727-134c-4f91-905a-0fcd0fc01259)

**D. _Chat UI Window_** <br><br>
![image](https://github.com/user-attachments/assets/8ba826d8-466e-4688-8a12-0b39411fa98e)
![image](https://github.com/user-attachments/assets/e8dd8fd8-9bed-4002-b82a-fa83cd80013d)
<br><br>
🤝 **Contributing** <br>
Pull requests are welcome! For major changes, please open an issue first to discuss what you'd like to change.

📜 **License** <br>
MIT License







