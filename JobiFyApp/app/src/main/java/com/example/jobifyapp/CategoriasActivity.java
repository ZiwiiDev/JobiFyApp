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
import android.widget.Button;
import android.widget.EditText;

import com.example.jobifyapp.adapters.CategoriaAdapter;
import com.example.jobifyapp.basedatos.ControladorBD;
import com.example.jobifyapp.modelo.Categoria;
import com.example.jobifyapp.popups.PopAgregarCategoria;
import com.example.jobifyapp.utils.UserUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;
// -----------------------------------------------------------------------------------------------------------------------------
public class CategoriasActivity extends AppCompatActivity{
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private Toolbar toolbarCategorias;
    private BottomNavigationView bottomNavigationViewCategorias;
    private Button btnAgregarCategoria;

    private RecyclerView recyclerViewCategorias;
    private ArrayList<Categoria> listaCategorias;
    private CategoriaAdapter adaptador;

    private ControladorBD baseDatosControlador;

    private String consultaBusqueda;
    private EditText buscarCategorias;

    /* CONSTANTES */
    private static final int REQUEST_AGREGAR_CATEGORIA = 1;
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        UserUtil.init(getApplicationContext());             // Para mantener la persistencia de datos

        baseDatosControlador = new ControladorBD(getApplicationContext());

        /* Referencia al toolbar */
        toolbarCategorias = findViewById(R.id.toolbarCategorias);
        setSupportActionBar(toolbarCategorias);

        btnAgregarCategoria = findViewById(R.id.btnAgregarCategorias);
        buscarCategorias = findViewById(R.id.buscarCategorias);
        bottomNavigationViewCategorias = findViewById(R.id.bottom_navigation_categorias);
        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias);

        consultaBusqueda = buscarCategorias.getText().toString();

        // Escuchar los eventos de selección de elemento en la navegación inferior
        bottomNavigationViewCategorias.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.inicio:
                    // Acción al hacer clic en el elemento de solicitudes
                    Intent inicio = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(inicio);
                    return true;
                case R.id.vacantes:
                    // Acción al hacer clic en el elemento de vacantes
                    Intent vacantes = new Intent(getApplicationContext(), VacantesActivity.class);
                    startActivity(vacantes);
                    return true;
                case R.id.usuarios:
                    // Acción al hacer clic en el elemento de usuarios
                    Intent usuarios = new Intent(getApplicationContext(), UsuariosActivity.class);
                    startActivity(usuarios);
                    return true;
            }
            return false;
        });

        btnAgregarCategoria.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PopAgregarCategoria.class);
            startActivityForResult(intent, REQUEST_AGREGAR_CATEGORIA);
        });

        buscarCategorias.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita implementar en este caso
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cuando se realiza un cambio en el texto de búsqueda
                consultaBusqueda = s.toString().trim().toLowerCase(Locale.getDefault());
                adaptador.filtrarCategorias(consultaBusqueda);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No se necesita implementar en este caso
            }
        });

        listaCategorias = baseDatosControlador.obtenerCategorias();
        mostrarCategorias();
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_usuario, menu);
        return true;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint({"NonConstantResourceId", "InflateParams"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setView(getLayoutInflater().inflate(R.layout.dialog_confirm, null));
                builder1.setCancelable(false);
                builder1.setPositiveButton("No", (dialog, id) -> {
                    // Si le da a que "No" no hace nada
                });

                builder1.setNegativeButton("Sí", (dialog, id) -> {
                    // Si le da a que "Sí" cierra sesión
                    FirebaseAuth.getInstance().signOut(); // Cierro la sesión y vuelvo a la pantalla de login
                    UserUtil.setTipoUsuario(null);
                    Intent e = new Intent(getApplicationContext(), InicioActivity.class);
                    startActivity(e);
                    finish();
                });

                AlertDialog alert2 = builder1.create();
                alert2.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void mostrarCategorias() {
        adaptador = new CategoriaAdapter(listaCategorias);
        recyclerViewCategorias.setAdapter(adaptador);
        recyclerViewCategorias.setLayoutManager(new GridLayoutManager(this, 1));
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_AGREGAR_CATEGORIA && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("categoriaAgregada")) {
                Categoria categoriaAgregada = (Categoria) data.getSerializableExtra("categoriaAgregada");

                if (categoriaAgregada != null) {
                    int nuevoId = baseDatosControlador.obtenerUltimoIdCategoria() + 1;
                    categoriaAgregada.setId(nuevoId);

                    adaptador.agregarCategoria(categoriaAgregada);
                    listaCategorias.add(categoriaAgregada);
                    adaptador.filtrarCategorias(consultaBusqueda); // Filtrar las categorías con la consulta actual
                }
            }
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
