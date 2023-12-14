package com.example.jobifyapp;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.jobifyapp.utils.UserUtil;
// -----------------------------------------------------------------------------------------------------------------------------
public class NewUserType extends AppCompatActivity {
    /* VARIABLES */
    private ImageView candidatoUser, empresaUser;

    // Variable para almacenar el tipo de usuario seleccionado
    String tipoUsuario;
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_type);

        UserUtil.init(getApplicationContext());             // Para mantener la persistencia de datos

        candidatoUser = findViewById(R.id.candidatouser);
        empresaUser = findViewById(R.id.empresafondo);

        candidatoUser.setOnClickListener(v -> {
            tipoUsuario = "candidato";              // Asignar el tipo de usuario como "candidato"
            UserUtil.setTipoUsuario(tipoUsuario);
            launchRegisterActivity();
        });

        empresaUser.setOnClickListener(v -> {
            tipoUsuario = "empresa";                 // Asignar el tipo de usuario como "empresa"
            UserUtil.setTipoUsuario(tipoUsuario);
            launchRegisterActivity();
        });
        // -----------------------------------------------------------------------------------------------------------------------------
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    private void launchRegisterActivity() {
        Intent registerIntent = new Intent(NewUserType.this, RegisterActivity.class);
        startActivity(registerIntent);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
