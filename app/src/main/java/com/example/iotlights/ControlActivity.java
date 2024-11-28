package com.example.iotlights;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class ControlActivity extends AppCompatActivity {

    private LinearLayout zonesContainer;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            userId = currentUser.getUid();
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        zonesContainer = findViewById(R.id.zonesContainer);

        // Cargar zonas desde Firestore
        cargarZonasDesdeFirestore();

        // Botón para agregar nuevas zonas
        Button addZoneButton = findViewById(R.id.btnAddZone);
        addZoneButton.setOnClickListener(v -> agregarNuevaZona());

        // Botón para regresar
        Button backButton = findViewById(R.id.btnBack2);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ControlActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void agregarNuevaZona() {
        String nuevaZona = "Nueva Zona"; // Nombre genérico
        Map<String, Object> zoneData = new HashMap<>();
        zoneData.put("name", nuevaZona);

        CollectionReference zonesRef = db.collection("users").document(userId).collection("zones");
        zonesRef.add(zoneData)
                .addOnSuccessListener(documentReference -> {
                    String zoneId = documentReference.getId();
                    agregarBotonZona(zoneId, nuevaZona);
                    Toast.makeText(this, "Zona creada", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al crear la zona", Toast.LENGTH_SHORT).show());
    }

    private void cargarZonasDesdeFirestore() {
        CollectionReference zonesRef = db.collection("users").document(userId).collection("zones");

        zonesRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String zoneId = document.getId();
                        String zoneName = document.getString("name");
                        if (zoneName != null) {
                            agregarBotonZona(zoneId, zoneName);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar las zonas", Toast.LENGTH_SHORT).show());
    }

    private void agregarBotonZona(String zoneId, String zoneName) {
        Button zoneButton = new Button(this);
        zoneButton.setText("Configurar " + zoneName);
        zoneButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Ir a ConfigActivity
        zoneButton.setOnClickListener(v -> {
            Intent intent = new Intent(ControlActivity.this, ConfigActivity.class);
            intent.putExtra("zoneId", zoneId);
            intent.putExtra("zoneName", zoneName);
            startActivity(intent);
        });

        // Renombrar zona al mantener presionado
        zoneButton.setOnLongClickListener(v -> {
            mostrarDialogoEditarZona(zoneId, zoneName, zoneButton);
            return true;
        });

        zonesContainer.addView(zoneButton);
    }

    private void mostrarDialogoEditarZona(String zoneId, String oldZoneName, Button zoneButton) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Editar nombre de zona");

        final android.widget.EditText input = new android.widget.EditText(this);
        input.setText(oldZoneName);
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String newZoneName = input.getText().toString().trim();
            if (!newZoneName.isEmpty()) {
                actualizarNombreEnFirestore(zoneId, newZoneName, zoneButton);
            } else {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void actualizarNombreEnFirestore(String zoneId, String newZoneName, Button zoneButton) {
        DocumentReference zoneRef = db.collection("users").document(userId).collection("zones").document(zoneId);

        zoneRef.update("name", newZoneName)
                .addOnSuccessListener(aVoid -> {
                    zoneButton.setText("Configurar " + newZoneName);
                    Toast.makeText(this, "Zona actualizada", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar la zona", Toast.LENGTH_SHORT).show());
    }
}