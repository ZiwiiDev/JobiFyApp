package com.example.jobifyapp.popups;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;

import com.example.jobifyapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
// -----------------------------------------------------------------------------------------------------------------------------
public class PopDeletePerfil extends AppCompatActivity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private Button btnNo, btnSi;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_delete_perfil);

        btnNo = findViewById(R.id.borrarNoPerfil);
        btnSi = findViewById(R.id.borrarSiPerfil);

        btnNo.setOnClickListener(v -> {
            finish();
        });

        // Diseño de la ventana emergente
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -50;

        getWindow().setAttributes(params);

        Objects.requireNonNull(getSupportActionBar()).hide();
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
