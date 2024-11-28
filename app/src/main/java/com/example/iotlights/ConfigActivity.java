package com.example.iotlights;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import modelo.Light;

public class ConfigActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String userId;
    private ImageView lightImage;
    private String zoneId;
    private String zoneName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

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

        // Obtener el ID y nombre de la zona
        zoneId = getIntent().getStringExtra("zoneId");
        zoneName = getIntent().getStringExtra("zoneName");

        if (zoneId == null || zoneName == null) {
            Toast.makeText(this, "Error al obtener los datos de la zona.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView roomText = findViewById(R.id.roomTitle);
        roomText.setText(zoneName);

        lightImage = findViewById(R.id.lightStateImage);

        // Botón para agregar luces
        Button addLightButton = findViewById(R.id.btnAddLight);
        addLightButton.setOnClickListener(v -> abrirFormularioAgregarLuz());

        // Botón para eliminar la zona
        Button deleteZoneButton = findViewById(R.id.btnDeleteZone);
        deleteZoneButton.setOnClickListener(v -> eliminarZona());

        // Configuración del botón de regreso
        Button backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> finish());
    }

    private void abrirFormularioAgregarLuz() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Luz");

        // Formulario para ingresar datos
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText inputId = new EditText(this);
        inputId.setHint("ID de la luz");
        layout.addView(inputId);

        EditText inputState = new EditText(this);
        inputState.setHint("Estado (true o false)");
        layout.addView(inputState);

        builder.setView(layout);

        builder.setPositiveButton("Agregar", (dialog, which) -> {
            String lightId = inputId.getText().toString().trim();
            boolean isOn = Boolean.parseBoolean(inputState.getText().toString().trim());
            agregarLuz(lightId, isOn);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void agregarLuz(String lightId, boolean isOn) {
        Light newLight = new Light(lightId, isOn);

        db.collection("users").document(userId)
                .collection("zones").document(zoneId)
                .collection("lights").document(lightId)
                .set(newLight)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Luz agregada correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al agregar la luz.", Toast.LENGTH_SHORT).show();
                });
    }

    private void eliminarZona() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Zona");
        builder.setMessage("¿Estás seguro de que deseas eliminar esta zona? Esta acción no se puede deshacer.");

        builder.setPositiveButton("Eliminar", (dialog, which) -> {
            db.collection("users").document(userId)
                    .collection("zones").document(zoneId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Zona eliminada correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al eliminar la zona.", Toast.LENGTH_SHORT).show();
                    });
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
