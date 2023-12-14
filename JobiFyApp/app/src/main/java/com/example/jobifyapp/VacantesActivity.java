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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobifyapp.adapters.VacanteTodosAdapter;
import com.example.jobifyapp.basedatos.ControladorBD;
import com.example.jobifyapp.modelo.Vacante;
import com.example.jobifyapp.popups.PopReset;
import com.example.jobifyapp.utils.UserUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Locale;
// -----------------------------------------------------------------------------------------------------------------------------
public class VacantesActivity extends AppCompatActivity implements VacanteTodosAdapter.OnVacanteClickListener {
// -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private Toolbar toolbarVacantes;
    private BottomNavigationView bottomNavigationViewVacantes, bottomNavigationViewVacantesCandidato, bottom_navigation_vacantes_admin;
    private Button btnAgregarVacante;

    private RecyclerView recyclerViewVacantesTodos;
    private ArrayList<Vacante> listaVacantes;
    private VacanteTodosAdapter adaptador;

    private ControladorBD baseDatosControlador;

    private String consultaBusqueda;
    private EditText buscarVacantes;

    /* CONSTANTES */
    private static final int REQUEST_AGREGAR_VACANTE = 1;
    private static final int REQUEST_ACTUALIZAR_VACANTE = 2;
// -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacantes);

        UserUtil.init(getApplicationContext());             // Para mantener la persistencia de datos

        baseDatosControlador = new ControladorBD(getApplicationContext());

        /* Referencia al toolbar */
        toolbarVacantes = findViewById(R.id.toolbarVacantes);
        setSupportActionBar(toolbarVacantes);

        recyclerViewVacantesTodos = findViewById(R.id.recyclerViewVacantesTodos);
        buscarVacantes = findViewById(R.id.buscarVacantes);
        bottomNavigationViewVacantesCandidato = findViewById(R.id.bottom_navigation_vacantes_candidato);
        bottomNavigationViewVacantes = findViewById(R.id.bottom_navigation_vacantes);
        bottom_navigation_vacantes_admin = findViewById(R.id.bottom_navigation_vacantes_admin);
        btnAgregarVacante = findViewById(R.id.btnAgregarVacante);

        consultaBusqueda = buscarVacantes.getText().toString();

        buscarVacantes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita implementar en este caso
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cuando se realiza un cambio en el texto de búsqueda
                consultaBusqueda = s.toString().trim().toLowerCase(Locale.getDefault());
                adaptador.filtrarVacantes(consultaBusqueda);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No se necesita implementar en este caso
            }
        });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Log.e("numVacantes", String.valueOf(baseDatosControlador.numVacantes()));

        if (user != null && user.getEmail() != null) {
            String tipoUsuario = UserUtil.getTipoUsuario();

            if (tipoUsuario != null && tipoUsuario.equals("candidato")) {
                bottomNavigationViewVacantesCandidato.setVisibility(View.VISIBLE);
                bottomNavigationViewVacantes.setVisibility(View.GONE);
                bottom_navigation_vacantes_admin.setVisibility(View.GONE);
                btnAgregarVacante.setVisibility(View.GONE);

                // Escuchar los eventos de selección de elemento en la navegación inferior
                bottomNavigationViewVacantesCandidato.setOnNavigationItemSelectedListener(item -> {
                    if (item.getItemId() == R.id.inicio) {// Acción al hacer clic en el elemento de inicio
                        Intent inicio = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(inicio);
                        return true;
                    }
                    return false;
                });
            } else if (tipoUsuario != null && tipoUsuario.equals("empresa")) {
                bottomNavigationViewVacantesCandidato.setVisibility(View.GONE);
                bottomNavigationViewVacantes.setVisibility(View.VISIBLE);
                bottom_navigation_vacantes_admin.setVisibility(View.GONE);
                btnAgregarVacante.setVisibility(View.VISIBLE);

                // Escuchar los eventos de selección de elemento en la navegación inferior
                bottomNavigationViewVacantes.setOnNavigationItemSelectedListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.inicio:
                            // Acción al hacer clic en el elemento de inicio
                            Intent inicio = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(inicio);
                            return true;
                        case R.id.solicitudes:
                            // Acción al hacer clic en el elemento de solicitudes
                            Intent solicitudes = new Intent(getApplicationContext(), SolicitudesActivity.class);
                            startActivity(solicitudes);
                            return true;
                    }
                    return false;
                });

                btnAgregarVacante.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), AgregarOfertaActivity.class);
                    startActivityForResult(intent, REQUEST_AGREGAR_VACANTE);
                });
            } else if (tipoUsuario != null && tipoUsuario.equals("admin")) {
                bottomNavigationViewVacantesCandidato.setVisibility(View.GONE);
                bottomNavigationViewVacantes.setVisibility(View.GONE);
                bottom_navigation_vacantes_admin.setVisibility(View.VISIBLE);
                btnAgregarVacante.setVisibility(View.GONE);

                bottom_navigation_vacantes_admin.setOnNavigationItemSelectedListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.inicio:
                            // Acción al hacer clic en el elemento de inicio
                            Intent inicio = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(inicio);
                            return true;
                        case R.id.categorias:
                            // Acción al hacer clic en el elemento de categorias
                            Intent categorias = new Intent(getApplicationContext(), CategoriasActivity.class);
                            startActivity(categorias);
                            return true;
                        case R.id.usuarios:
                            // Acción al hacer clic en el elemento de categorias
                            Intent usuarios = new Intent(getApplicationContext(), UsuariosActivity.class);
                            startActivity(usuarios);
                            return true;
                    }
                    return false;
                });
            } else {
                Toast.makeText(VacantesActivity.this, getString(R.string.errorUsuario), Toast.LENGTH_SHORT).show();     // El tipo de usuario es desconocido o no está definido
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);       // El usuario no está autenticado, redirigir a la pantalla de inicio de sesión
            startActivity(intent);
            finish();
        }

        listaVacantes = baseDatosControlador.obtenerVacantes();
        mostrarVacantes();
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflar el menú correspondiente según el tipo de usuario
        String tipoUsuario = UserUtil.getTipoUsuario();

        // Verificar el tipo de usuario y gestionar las selecciones de opciones de menú correspondientes
        if (tipoUsuario != null && (tipoUsuario.equals("candidato") || tipoUsuario.equals("admin") || tipoUsuario.equals("empresa"))) {
            getMenuInflater().inflate(R.menu.menu_usuario, menu);
        } else {
            Toast.makeText(VacantesActivity.this, getString(R.string.errorUsuario), Toast.LENGTH_SHORT).show();     // El tipo de usuario es desconocido o no está definido
        }
        return true;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint({"NonConstantResourceId", "InflateParams"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Verificar el tipo de usuario actual
        String tipoUsuario = UserUtil.getTipoUsuario();

        // Verificar el tipo de usuario y gestionar las selecciones de opciones de menú correspondientes
        if (tipoUsuario != null && (tipoUsuario.equals("candidato") || tipoUsuario.equals("admin") || tipoUsuario.equals("empresa"))) {
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
        } else {
            Toast.makeText(VacantesActivity.this, getString(R.string.errorUsuario), Toast.LENGTH_SHORT).show();     // El tipo de usuario es desconocido o no está definido
        }
        return super.onOptionsItemSelected(item);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void mostrarVacantes() {
        adaptador = new VacanteTodosAdapter(listaVacantes, this, baseDatosControlador);
        adaptador.setOnVacanteClickListener(this); // Establecer el listener en el adaptador
        recyclerViewVacantesTodos.setAdapter(adaptador);
        recyclerViewVacantesTodos.setLayoutManager(new GridLayoutManager(this, 1));
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_AGREGAR_VACANTE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("vacanteAgregada")) {
                Vacante vacanteAgregada = (Vacante) data.getSerializableExtra("vacanteAgregada");

                if (vacanteAgregada != null) {
                    int nuevoId = baseDatosControlador.obtenerUltimoIdCategoria() + 1;
                    vacanteAgregada.setId(nuevoId);

                    adaptador.agregarVacante(vacanteAgregada);
                    adaptador.filtrarVacantes(consultaBusqueda); // Filtrar las categorías con la consulta actual
                }
            }
        } else if (requestCode == REQUEST_ACTUALIZAR_VACANTE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("vacanteActualizada")) {
                Vacante vacanteActualizada = (Vacante) data.getSerializableExtra("vacanteActualizada");

                if (vacanteActualizada != null) {
                    adaptador.actualizarVacante(vacanteActualizada);
                    adaptador.filtrarVacantes(consultaBusqueda); // Filtrar las categorías con la consulta actual
                }
            }
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onVacanteClick(Vacante vacante) {
        Intent detallesIntent = new Intent(this, DetallesActivity.class);
        detallesIntent.putExtra("detallesVacante", vacante);
        startActivity(detallesIntent);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
