package com.example.jobifyapp.popups;
// -----------------------------------------------------------------------------------------------------------------------------
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobifyapp.R;
import com.example.jobifyapp.basedatos.ControladorBD;
import com.google.firebase.auth.FirebaseAuth;
// -----------------------------------------------------------------------------------------------------------------------------
public class PopReset extends Activity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private Button enviar, btnVolverLogin;                      // Variable para el botón de enviar
    private EditText email, confirmEmail;                       // Variables para los campos de correo electrónico y confirmación

    private FirebaseAuth mAuth;                                 // Variable para la instancia de FirebaseAuth
    private ControladorBD baseDatosControlador;
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_reset);

        baseDatosControlador = new ControladorBD(getApplicationContext());

        email = findViewById(R.id.correoReset);
        confirmEmail = findViewById(R.id.correoResetConfirm);
        enviar = findViewById(R.id.restP);
        btnVolverLogin = findViewById(R.id.btnVolverLogin);

        // Obtener la instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

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

        btnVolverLogin.setOnClickListener(v -> {
            finish();
        });

        enviar.setOnClickListener(v -> {
            String mail = email.getText().toString();                   // Obtener el texto del campo de correo electrónico
            String mailConfirm = confirmEmail.getText().toString();     // Obtener el texto del campo de confirmación de correo electrónico

            if (mail.isEmpty() || mailConfirm.isEmpty()) {              // Comprobar si alguno de los campos está vacío
                Toast.makeText(PopReset.this, R.string.needEmail, Toast.LENGTH_LONG).show();           // Mostrar un mensaje de que se necesitan datos válidos
            } else {
                if (!mail.equals(mailConfirm)) {                        // Comprobar si los correos electrónicos coinciden
                    Toast.makeText(PopReset.this, R.string.coincidirEmail, Toast.LENGTH_LONG).show();  // Mostrar un mensaje de que los correos electrónicos no coinciden
                } else {
                    if (isEmailInDatabase(mail)) {                      // Comprobar si el correo electrónico está registrado en la lista
                        mAuth.sendPasswordResetEmail(mail)              // Enviar el correo electrónico de restablecimiento de contraseña
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {          // Comprobar si el envío del correo electrónico fue exitoso
                                        Toast.makeText(PopReset.this, R.string.mailsend, Toast.LENGTH_LONG).show();        // Mostrar un mensaje de que se envió el correo electrónico exitosamente
                                        finish();                       // Cerrar la actividad
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(PopReset.this, R.string.invalidEmail, Toast.LENGTH_LONG).show();  // Mostrar un mensaje de que el correo electrónico no está registrado en la BD
                                });
                    } else {
                        Toast.makeText(PopReset.this, R.string.invalidEmail, Toast.LENGTH_LONG).show();  // Mostrar un mensaje de que el correo electrónico no está registrado en la BD
                    }
                }
            }
        });
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    private boolean isEmailInDatabase(String email) {
        String emailBD = baseDatosControlador.obtenerEmailPorTipoUsuario(email);
        return emailBD != null;             // Si sale null es porque no está en la base de datos
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------

