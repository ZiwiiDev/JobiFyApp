package com.example.jobifyapp;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.jobifyapp.adapters.UsuarioAdapter;
import com.example.jobifyapp.basedatos.ControladorBD;
import com.example.jobifyapp.modelo.Usuario;
import com.example.jobifyapp.utils.UserUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
// -----------------------------------------------------------------------------------------------------------------------------
public class UsuariosActivity extends AppCompatActivity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private BottomNavigationView bottomNavigationViewUsuarios;
    private Toolbar toolbarUsuarios;

    private RecyclerView recyclerViewUsuarios;
    private ArrayList<Usuario> listaUsuarios;
    private UsuarioAdapter adaptador;

    private ControladorBD baseDatosControlador;

    private String consultaBusqueda;
    private EditText buscarUsuarios;
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        UserUtil.init(getApplicationContext());             // Para mantener la persistencia de datos

        baseDatosControlador = new ControladorBD(getApplicationContext());

        /* Referencia al toolbar */
        toolbarUsuarios = findViewById(R.id.toolbarUsuarios);
        setSupportActionBar(toolbarUsuarios);

        buscarUsuarios = findViewById(R.id.buscarUsuarios);
        recyclerViewUsuarios = findViewById(R.id.recyclerViewUsuarios);
        bottomNavigationViewUsuarios = findViewById(R.id.bottom_navigation_usuarios);

        consultaBusqueda = buscarUsuarios.getText().toString();

        // Escuchar los eventos de selección de elemento en la navegación inferior
        bottomNavigationViewUsuarios.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.inicio:
                    // Acción al hacer clic en el elemento de inicio
                    Intent inicio = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(inicio);
                    return true;
                case R.id.vacantes:
                    // Acción al hacer clic en el elemento de vacantes
                    Intent solicitudes = new Intent(getApplicationContext(), VacantesActivity.class);
                    startActivity(solicitudes);
                    return true;
                case R.id.categorias:
                    // Acción al hacer clic en el elemento de vacantes
                    Intent categorias = new Intent(getApplicationContext(), CategoriasActivity.class);
                    startActivity(categorias);
                    return true;
            }
            return false;
        });

        buscarUsuarios.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita implementar en este caso
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cuando se realiza un cambio en el texto de búsqueda
                consultaBusqueda = s.toString();
                adaptador.filtrarUsuario(consultaBusqueda);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No se necesita implementar en este caso
            }
        });

        listaUsuarios = baseDatosControlador.obtenerUsuarios();
        mostrarUsuarios();
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint({"NonConstantResourceId", "InflateParams"})
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.perfil:
                Intent perfil = new Intent(getApplicationContext(), PerfilActivity.class);
                startActivity(perfil);
                break;
            case R.id.ayuda:
                Intent ayuda = new Intent(getApplicationContext(), AyudaActivity.class);
                startActivity(ayuda);
                break;
            case R.id.acercaDe:
                Intent acercaDe = new Intent(getApplicationContext(), ContactoActivity.class);
                startActivity(acercaDe);
                break;
            case R.id.btnLogout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(getLayoutInflater().inflate(R.layout.dialog_confirm, null));
                builder.setCancelable(false);
                builder.setPositiveButton("No", (dialog, id) -> {
                    // Si le da a que "No" no hace nada
                });

                builder.setNegativeButton("Sí", (dialog, id) -> {
                    // Si le da a que "Sí" cierra sesión
                    FirebaseAuth.getInstance().signOut(); // Cierro la sesión y vuelvo a la pantalla de login
                    UserUtil.setTipoUsuario(null);
                    Intent e = new Intent(getApplicationContext(), InicioActivity.class);
                    startActivity(e);
                    finish();
                });

                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void mostrarUsuarios(){
        adaptador = new UsuarioAdapter(listaUsuarios);
        recyclerViewUsuarios.setAdapter(adaptador);
        recyclerViewUsuarios.setLayoutManager(new GridLayoutManager(this, 1));
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
