package com.example.iotlights;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ControlActivity extends AppCompatActivity {

    private ArrayList<String> zones; // Lista de zonas dinámicas
    private LinearLayout zonesContainer; // Contenedor para los botones dinámicos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        // Inicializar lista de zonas
        zones = new ArrayList<>();
        zones.add("Habitación");
        zones.add("Cocina");

        zonesContainer = findViewById(R.id.zonesContainer);

        // Agregar botones para las zonas iniciales
        for (String zone : zones) {
            agregarBotonZona(zone);
        }

        // Botón para agregar nuevas zonas
        Button addZoneButton = findViewById(R.id.btnAddZone);
        addZoneButton.setOnClickListener(v -> {
            // Aquí puedes abrir un diálogo para que el usuario ingrese el nombre de la nueva zona
            String nuevaZona = "Nueva Zona " + (zones.size() + 1); // Esto es solo un ejemplo
            zones.add(nuevaZona);
            agregarBotonZona(nuevaZona);
            Toast.makeText(ControlActivity.this, "Zona agregada: " + nuevaZona, Toast.LENGTH_SHORT).show();
        });
    }

    // Método para agregar un botón para cada zona
    private void agregarBotonZona(String zone) {
        Button zoneButton = new Button(this);
        zoneButton.setText("Configurar " + zone);
        zoneButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        zoneButton.setOnClickListener(v -> {
            // Redirigir a la actividad de configuración con el nombre de la zona
            Intent intent = new Intent(ControlActivity.this, ConfigActivity.class);
            intent.putExtra("room", zone);
            startActivity(intent);
        });

        zonesContainer.addView(zoneButton);
    }
}