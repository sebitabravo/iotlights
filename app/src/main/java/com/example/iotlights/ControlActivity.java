package com.example.iotlights;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        // Botón para configurar la habitación
        Button roomButton = findViewById(R.id.btnConfigRoom);
        roomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la actividad de configuración
                Intent intent = new Intent(ControlActivity.this, ConfigActivity.class);
                intent.putExtra("room", "Habitacion");
                startActivity(intent);
            }
        });

        // Botón para configurar la cocina
        Button kitchenButton = findViewById(R.id.btnConfigKitchen);
        kitchenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la actividad de configuración
                Intent intent = new Intent(ControlActivity.this, ConfigActivity.class);
                intent.putExtra("room", "Cocina");
                startActivity(intent);
            }
        });
    }
}
