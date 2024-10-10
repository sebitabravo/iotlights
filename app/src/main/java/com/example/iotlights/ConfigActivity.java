package com.example.iotlights;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        // Obtener la habitación seleccionada
        String room = getIntent().getStringExtra("room");
        TextView roomText = findViewById(R.id.roomTitle);
        roomText.setText(room);

        // Configuración de los botones Encender y Apagar
        Button onButton = findViewById(R.id.btnTurnOn);
        Button offButton = findViewById(R.id.btnTurnOff);
        Button backButton = findViewById(R.id.btnBack);

        onButton.setOnClickListener(v -> {
            // Lógica para encender la luz
        });

        offButton.setOnClickListener(v -> {
            // Lógica para apagar la luz
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ConfigActivity.this, ControlActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Limpiar las actividades anteriores
            startActivity(intent);
            finish(); // Finalizar la actividad actual
        });

    }
}

