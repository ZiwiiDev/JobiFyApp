package com.example.jobifyapp;
// -----------------------------------------------------------------------------------------------------------------------------
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jobifyapp.basedatos.ControladorBD;
import com.example.jobifyapp.popups.PopReset;
import com.example.jobifyapp.utils.UserUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
// -----------------------------------------------------------------------------------------------------------------------------
public class LoginActivity extends AppCompatActivity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private FirebaseAuth mAuth;

    private Intent intent, reset;
    private ProgressBar progressBar;
    private Button btnLogin, btnVolver;
    private EditText edtEmail, edtPassword;
    private TextView restablecerPswd, tituloRegistar;
    private ImageView showPassword;

    private ControladorBD baseDatosControlador;
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserUtil.init(getApplicationContext());             // Para mantener la persistencia de datos

        baseDatosControlador = new ControladorBD(getApplicationContext());

        // Instancio FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.btnIniciarSesion);
        btnVolver = findViewById(R.id.btnVolver);
        edtEmail = findViewById(R.id.editTextUsername);
        edtPassword = findViewById(R.id.editTextPasswordLogin);
        showPassword = findViewById(R.id.passwordIcono);
        restablecerPswd = findViewById(R.id.tituloOlvidarPassword);
        tituloRegistar = findViewById(R.id.tituloRegistar);
        progressBar = findViewById(R.id.progressBar);

        btnVolver.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        // Botón recuperar la contraseña
        restablecerPswd.setOnClickListener(v -> {
            reset = new Intent(LoginActivity.this, PopReset.class);
            startActivity(reset);
        });

        tituloRegistar.setOnClickListener(v -> {
            reset = new Intent(LoginActivity.this, NewUserType.class);
            startActivity(reset);
            finish();
        });

        // Configuramos el ImageView como clickable
        showPassword.setClickable(true);

        // Agregamos un listener a nuestro ImageView para cambiar la visibilidad del password
        showPassword.setOnClickListener(new View.OnClickListener() {
            private boolean passwordVisible = false;

            @Override
            public void onClick(View v) {
                // Si la contraseña es visible, la ocultamos
                if (passwordVisible) {
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPassword.setImageResource(R.mipmap.iconover_foreground);

                    // Establecemos el estilo EditTextPassword
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        edtPassword.setTextAppearance(R.style.EditTextPassword);
                    } else {
                        edtPassword.setTextAppearance(getApplicationContext(), R.style.EditTextPassword);
                    }

                    passwordVisible = false;
                } else { // Si la contraseña está oculta, la mostramos
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPassword.setImageResource(R.mipmap.icononover_foreground);

                    // Establecemos el estilo EditTextVisible
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        edtPassword.setTextAppearance(R.style.EditTextVisible);
                    } else {
                        edtPassword.setTextAppearance(getApplicationContext(), R.style.EditTextVisible);
                    }

                    passwordVisible = true;
                }

                // Movemos el cursor al final del EditText
                edtPassword.setSelection(edtPassword.length());
            }
        });

        // ################################   LOGIN    #############################################

        btnLogin.setOnClickListener(v -> {
                progressBar.setVisibility(View.GONE);               //Ocultamos la barra de progreso

                // Recupero los valores de los EditText, las variables introducidas
                String email, password;
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), R.string.needEmail, Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), R.string.needPassword, Toast.LENGTH_LONG).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);            // Mostramos la barra de progreso

                // Logueo con el usuario
                logIn(email, password);
        });
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    private void logIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), R.string.errorLogin, Toast.LENGTH_LONG).show();
                    } else {
                        Objects.requireNonNull(mAuth.getCurrentUser()).getIdToken(true).addOnCompleteListener(tokenTask -> {
                            if (tokenTask.isSuccessful()) {
                                String emailUsuario = edtEmail.getText().toString();
                                String tipoUsuario = baseDatosControlador.obtenerEmailPorTipoUsuario(emailUsuario);

                                if (tipoUsuario != null) {
                                    UserUtil.setTipoUsuario(tipoUsuario);
                                    Toast.makeText(getApplicationContext(), R.string.correctoLogin, Toast.LENGTH_SHORT).show();
                                    intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // El usuario no tiene un tipo de usuario asociado
                                    Toast.makeText(LoginActivity.this, R.string.errorLoginDato, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.errorLogin, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
