# Mouse Phone App (Android Client)

<p align="center">
  <img src="https://i.imgur.com/your-app-image.png" alt="App Screenshot" width="300"/>
</p>

<p align="center">
  <strong>Turn your smartphone into a wireless mouse and trackpad for your computer.</strong>
  <br>
  This is the Android client for the <a href="https://github.com/abelpinheiro/mousephoneserver">Mouse Phone Server</a> project.
</p>

---

## 🎯 Overview

The **Mouse Phone App** allows you to control your computer's cursor and mouse clicks using your smartphone as a wireless touchpad. It is ideal for giving presentations, using your computer as a media center, or for anyone looking for a more convenient way to interact with their PC from a distance.

The app is designed for an intuitive user experience, with a fast, low-latency connection established over your local Wi-Fi network.

## ✨ Features

- **Remote Mouse Control:** Move your computer's cursor with gestures on your phone's screen.
- **Left & Right Clicks:** Dedicated buttons to simulate mouse clicks.
- **Simple Connection:** Easily connect to the desktop agent using its IP address and port.
- **Clean Interface:** A modern and minimalist UI built with Jetpack Compose.
- **(Future) QR Code Connection:** Planned functionality for an even faster setup.
- **(Future) Gyroscope Control:** Use your phone's motion sensors to control the cursor.

## 🏗️ Architecture

The system is based on a client-server model that communicates over a local network (Wi-Fi) using the WebSocket protocol.

- **Mobile App (This Repository):** An Android application that captures user gestures on the screen, formats them into JSON, and sends them to the Desktop Agent.
- **Desktop Agent (Server):** A Go server that creates a WebSocket endpoint. It is responsible for receiving messages, interpreting them, and using operating system APIs to control the mouse.

### Communication Diagram
```
+--------------------------+                            +---------------------------+
|   Smartphone (Android)   |                            |   Desktop (PC/Mac/Linux)  |
|                          |                            |                           |
| +----------------------+ |    WebSocket via Wi-Fi     | +-----------------------+ |
| |   App "Remote Mouse"   | |<-----------------------> | |    Desktop Agent      | |
| |                      | |     (Local Network)        | |   (mousephoneserver)  | |
| | +------------------+ | |                            | | +-------------------+ | |
| | |   UI (Touchpad)  | | |                            | | |    WebSocket      | | |
| | +------------------+ | |                            | | |     Server        | | |
| | | Client WebSocket | | |                            | | +-------------------+ | |
| | +------------------+ | |                            | | |  Mouse Controller | | |
| +----------------------+ |                            | | |     (OS API)      | | |
|                          |                            | +-----------------------+ |
+--------------------------+                            +---------------------------+
```

## 🚀 How to Build and Run

To compile and run the project, follow these steps:

1.  **Run the Server:**
    * First, make sure the [Mouse Phone Server](https://github.com/abelpinheiro/mousephoneserver) is running on your computer.
    * Take note of the IP address and port displayed by the server.

2.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/abelpinheiro/MousePhoneApp.git](https://github.com/abelpinheiro/MousePhoneApp.git)
    ```

3.  **Open in Android Studio:**
    * Open the project in Android Studio.
    * Gradle will automatically sync and download all the required dependencies.

4.  **Run the App:**
    * Connect your Android phone (with USB debugging enabled) or start an emulator.
    * Click the 'Run' (▶️) button in Android Studio.

5.  **Connect to the Server:**
    * In the app, tap "Connect Now".
    * Enter your computer's IP address and port, then tap "Connect".
    * Start using your phone as a remote mouse!

## 🤝 Contributing

Contributions are welcome! If you have ideas for new features, improvements, or bug fixes, feel free to open an issue or submit a pull request.

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
