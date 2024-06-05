package com.example.simpletouchpadclient;

import android.util.Log;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Manages the connection to the server for sending mouse commands.
 * Implements the Singleton design pattern to ensure only one instance of the manager exists.
 */
public class ServerConnectionManager {

    private static final String TAG = "ServerConnectionManager";

    // Socket used for sending mouse commands to the server
    private DatagramSocket mouseSocket;

    // Address of the server
    private InetAddress serverAddress;

    // IP address of the server
    private String serverIP;

    // Port used for mouse commands
    public static final int MOUSE_PORT = 12345;

    // Singleton instance
    private static volatile ServerConnectionManager instance;

    /**
     * Private constructor to enforce Singleton pattern.
     * Initializes the DatagramSocket and server address.
     *
     * @param serverIP The IP address of the server.
     */
    private ServerConnectionManager(String serverIP) {
        this.serverIP = serverIP;
        try {
            // Create a new DatagramSocket for sending mouse commands
            mouseSocket = new DatagramSocket();
            // Resolve the server's IP address
            serverAddress = InetAddress.getByName(serverIP);
        } catch (Exception e) {
            // Log any errors that occur during initialization
            Log.e(TAG, "Error initializing sockets", e);
        }
    }

    /**
     * Returns the Singleton instance of the ServerConnectionManager.
     * If the instance does not exist, it is created.
     *
     * @param serverIP The IP address of the server.
     * @return The Singleton instance of ServerConnectionManager.
     */
    public static ServerConnectionManager getInstance(String serverIP) {
        if (instance == null) {
            // Synchronized block to ensure thread safety
            synchronized (ServerConnectionManager.class) {
                if (instance == null) {
                    instance = new ServerConnectionManager(serverIP);
                }
            }
        }
        return instance;
    }

    /**
     * Sends a command to the server.
     * This method runs in a separate thread to avoid blocking the main thread.
     *
     * @param message The command message to send.
     * @param port    The port on the server to send the command to.
     */
    public void sendCommandToServer(String message, int port) {
        new Thread(() -> {
            try {
                // Convert the message to bytes
                byte[] sendData = message.getBytes();
                // Create a DatagramPacket to send the data to the server
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, port);
                // Send the packet if the port matches the mouse port
                if (port == MOUSE_PORT) {
                    mouseSocket.send(sendPacket);
                } else {
                    // Log an error if the port is invalid
                    Log.e(TAG, "Invalid port specified for sending command");
                }
            } catch (Exception e) {
                // Log any errors that occur during sending
                Log.e(TAG, "Error sending command to server", e);
            }
        }).start();
    }

    /**
     * Closes the DatagramSocket if it is open.
     */
    public void closeConnections() {
        if (mouseSocket != null && !mouseSocket.isClosed()) {
            // Close the socket if it is not already closed
            mouseSocket.close();
        }
    }
}