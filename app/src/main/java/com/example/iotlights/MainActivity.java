package com.example.iotlights;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editEmail, editPassword;

    // Reemplaza este ID con el de tu proyecto en OneSignal
    private static final String ONESIGNAL_APP_ID = "ae719e6b-362f-46fd-835f-b0767dfe6078";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Inicializar OneSignal
        initOneSignal();

        // Referenciar los campos y botones
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        Button loginButton = findViewById(R.id.btnLogin);
        Button registerButton = findViewById(R.id.btnRegister);

        // Acción para iniciar sesión
        loginButton.setOnClickListener(v -> loginUser());

        // Acción para ir a la pantalla de registro
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void initOneSignal() {
        // Configuración de depuración (opcional, puedes quitarlo en producción)
        OneSignal.getDebug().setLogLevel(com.onesignal.debug.LogLevel.VERBOSE);

        // Inicializar OneSignal
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);

        // Solicitar permiso para notificaciones
        OneSignal.getNotifications().requestPermission(false, com.onesignal.Continue.none());
    }

    private void loginUser() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // Validar campos
        if (email.isEmpty()) {
            editEmail.setError("El email es obligatorio");
            editEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Introduce un email válido");
            editEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editPassword.setError("La contraseña es obligatoria");
            editPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editPassword.setError("La contraseña debe tener al menos 6 caracteres");
            editPassword.requestFocus();
            return;
        }

        // Autenticar al usuario
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Login exitoso
                Intent intent = new Intent(MainActivity.this, ControlActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                // Error en la autenticación
                Toast.makeText(MainActivity.this, "Fallo al iniciar sesión: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
