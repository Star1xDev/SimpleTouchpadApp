package com.example.simpletouchpadclient;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * The main activity of the application.
 * Handles the setup and display of the user interface.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display
        EdgeToEdge.enable(this);
        // Set the content view to the layout defined in activity_main.xml
        setContentView(R.layout.activity_main);

        // Set up insets listener to handle system bar padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Get the insets for the system bars
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply padding to the view based on the system bars' insets
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            // Return the insets to propagate down the view hierarchy
            return insets;
        });

        // Get the instance of ServerConnectionManager with the server IP
        ServerConnectionManager connectionManager = ServerConnectionManager.getInstance("192.168.1.5");
        // Get the touchpad view from the layout
        ConstraintLayout touchpad_view = findViewById(R.id.Touchpad);
        // Create a MouseTouchpadHandler with the connection manager and touchpad view
        MouseTouchpadHandler m = new MouseTouchpadHandler(connectionManager, touchpad_view);
        // Set up the touch listener for the touchpad
        m.setupTouchListener();
    }
}