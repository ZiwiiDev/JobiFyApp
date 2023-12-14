package com.example.jobifyapp;
// -----------------------------------------------------------------------------------------------------------------------------
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
// -----------------------------------------------------------------------------------------------------------------------------
public class InicioActivity extends AppCompatActivity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private Button btnInicio;
    private Intent intent;

    private FirebaseAuth mAuth;
    private FirebaseUser userInicio;
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        // Instancio FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Splash (Al iniciar por primera vez la aplicación aparece el logo)
        try {
            Thread.sleep(1000);
            setTheme(R.style.NoStatusBar);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        btnInicio = findViewById(R.id.btnInicio);

        btnInicio.setOnClickListener(view -> {
            intent = new Intent(InicioActivity.this, EleccionActivity.class);
            startActivity(intent);
        });
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onStart() {
        super.onStart();
        userInicio = mAuth.getCurrentUser();

        if (userInicio != null) {
            Intent intent2 = new Intent(InicioActivity.this, MainActivity.class);
            startActivity(intent2);
            finish();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
