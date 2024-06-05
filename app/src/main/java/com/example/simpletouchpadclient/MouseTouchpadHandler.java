package com.example.simpletouchpadclient;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Handles touch events on a touchpad view and translates them into mouse commands to be sent to a server.
 */
public class MouseTouchpadHandler {

    // The view representing the touchpad
    private View touchPad;

    // Sensitivity of the touchpad
    private float Mouse_Sens = 1;

    // Initial coordinates of the touch event
    private int initX, initY;

    // Connection manager to send commands to the server
    private ServerConnectionManager connectionManager;

    /**
     * Constructor to initialize the MouseTouchpadHandler with the connection manager and the touchpad view.
     *
     * @param connectionManager The connection manager to use for sending commands.
     * @param touchPad          The view representing the touchpad.
     */
    public MouseTouchpadHandler(ServerConnectionManager connectionManager, View touchPad) {
        this.connectionManager = connectionManager;
        this.touchPad = touchPad;
    }

    /**
     * Sets the sensitivity of the touchpad.
     *
     * @param mouse_Sens The sensitivity value.
     */
    public void setMouse_Sens(float mouse_Sens) {
        Mouse_Sens = mouse_Sens;
    }

    /**
     * Sets up the touch listener for the touchpad view.
     */
    @SuppressLint("ClickableViewAccessibility")
    public void setupTouchListener() {
        touchPad.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handleTouchDown(event); // Handle the touch down event
                    break;
                case MotionEvent.ACTION_MOVE:
                    handleTouchMove(event); // Handle the touch move event
                    break;
            }
            return true;
        });
    }

    /**
     * Handles the touch down event by recording the initial touch coordinates.
     *
     * @param event The MotionEvent representing the touch down.
     */
    private void handleTouchDown(MotionEvent event) {
        initX = (int) event.getX(); // Record the initial X coordinate
        initY = (int) event.getY(); // Record the initial Y coordinate
    }

    /**
     * Handles the touch move event by calculating the displacement and sending it to the server.
     *
     * @param event The MotionEvent representing the touch move.
     */
    private void handleTouchMove(MotionEvent event) {
        int currentDisX = (int) ((event.getX() - initX) * Mouse_Sens); // Calculate X displacement
        int currentDisY = (int) ((event.getY() - initY) * Mouse_Sens); // Calculate Y displacement
        initX = (int) event.getX(); // Update the initial X coordinate
        initY = (int) event.getY(); // Update the initial Y coordinate
        if (currentDisX != 0 || currentDisY != 0) {
            String currentMessage = "M," + currentDisX + "," + currentDisY; // Construct message for server
            sendMouseCommand(currentMessage);
        }
    }

    /**
     * Sends a mouse command to the server.
     *
     * @param message The command message to send.
     */
    public void sendMouseCommand(String message) {
        connectionManager.sendCommandToServer(message, ServerConnectionManager.MOUSE_PORT);
    }
}