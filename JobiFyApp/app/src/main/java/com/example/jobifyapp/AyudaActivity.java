package com.example.jobifyapp;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
// -----------------------------------------------------------------------------------------------------------------------------
public class AyudaActivity extends AppCompatActivity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private Button btnVolver;
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);

        btnVolver = findViewById(R.id.botonVolverAyuda);

        // Si pulso el botón "Volver" regreso a la pantalla anterior
        btnVolver.setOnClickListener(view -> {
            Intent intent = new Intent(AyudaActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
