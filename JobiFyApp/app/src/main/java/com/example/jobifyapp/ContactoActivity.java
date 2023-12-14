package com.example.jobifyapp;
// -----------------------------------------------------------------------------------------------------------------------------
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
// -----------------------------------------------------------------------------------------------------------------------------
public class ContactoActivity extends AppCompatActivity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private Button btnVolver;
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        btnVolver = findViewById(R.id.botonVolverAcercaDe);

        // Si pulso el botón "Volver" regreso a la pantalla anterior
        btnVolver.setOnClickListener(view -> {
            Intent intent = new Intent(ContactoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
