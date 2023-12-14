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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobifyapp.adapters.VacanteDestacadoAdapter;
import com.example.jobifyapp.adapters.VacanteTodosAdapter;
import com.example.jobifyapp.basedatos.ControladorBD;
import com.example.jobifyapp.modelo.Categoria;
import com.example.jobifyapp.modelo.Empresa;
import com.example.jobifyapp.modelo.Vacante;
import com.example.jobifyapp.utils.UserUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
// -----------------------------------------------------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity implements VacanteDestacadoAdapter.OnVacanteDestacadoClickListener {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private TextView nombreUsuario;

    private BottomNavigationView bottomNavigationView, bottom_navigationCandidato, bottom_navigationAdmin;
    private Toolbar toolbar;

    private ArrayList<Vacante> listaVacantesDestacadas;
    private VacanteDestacadoAdapter adaptador;
    private RecyclerView recyclerViewVacantesDestacadas;

    private ArrayList<Categoria> listaCategorias;

    private ControladorBD baseDatosControlador;

    private String consultaBusqueda;
    private EditText buscarVacanteDestacada;
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserUtil.init(getApplicationContext());             // Para mantener la persistencia de datos

        baseDatosControlador = new ControladorBD(getApplicationContext());

        /* Referencia al toolbar */
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerViewVacantesDestacadas = findViewById(R.id.recyclerViewVacantesDestacadas);
        buscarVacanteDestacada = findViewById(R.id.buscarVacanteDestacada);
        nombreUsuario = findViewById(R.id.usuarioNombre);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottom_navigationCandidato = findViewById(R.id.bottom_navigationCandidato);
        bottom_navigationAdmin = findViewById(R.id.bottom_navigationAdmin);

        consultaBusqueda = buscarVacanteDestacada.getText().toString();

        buscarVacanteDestacada.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita implementar en este caso
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cuando se realiza un cambio en el texto de búsqueda
                consultaBusqueda = s.toString();
                adaptador.filtrarVacantes(consultaBusqueda);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No se necesita implementar en este caso
            }
        });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Log.e("numCategorias", String.valueOf(baseDatosControlador.numCategorias()));

        if (user != null  && user.getEmail() != null) {
            String email = user.getEmail();
            String tipoUsuario = UserUtil.getTipoUsuario();

            nombreUsuario.setText(email);

            if (tipoUsuario != null && tipoUsuario.equals("candidato")) {
                bottom_navigationCandidato.setVisibility(View.VISIBLE);
                bottomNavigationView.setVisibility(View.GONE);
                bottom_navigationAdmin.setVisibility(View.GONE);

                // Escuchar los eventos de selección de elemento en la navegación inferior
                bottom_navigationCandidato.setOnNavigationItemSelectedListener(item -> {
                    if (item.getItemId() == R.id.vacantes) {// Acción al hacer clic en el elemento de inicio
                        Intent vacantes = new Intent(getApplicationContext(), VacantesActivity.class);
                        startActivity(vacantes);
                        return true;
                    }
                    return false;
                });
            } else if (tipoUsuario != null && tipoUsuario.equals("empresa")) {
                bottom_navigationCandidato.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottom_navigationAdmin.setVisibility(View.GONE);

                // Escuchar los eventos de selección de elemento en la navegación inferior
                bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.vacantes:
                            // Acción al hacer clic en el elemento de inicio
                            Intent vacantes = new Intent(getApplicationContext(), VacantesActivity.class);
                            startActivity(vacantes);
                            return true;
                        case R.id.solicitudes:
                            // Acción al hacer clic en el elemento de solicitudes
                            Intent solicitudes = new Intent(getApplicationContext(), SolicitudesActivity.class);
                            startActivity(solicitudes);
                            return true;
                    }
                    return false;
                });
            } else if (tipoUsuario != null && tipoUsuario.equals("admin")) {
                bottom_navigationCandidato.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.GONE);
                bottom_navigationAdmin.setVisibility(View.VISIBLE);

                // Escuchar los eventos de selección de elemento en la navegación inferior
                bottom_navigationAdmin.setOnNavigationItemSelectedListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.vacantes:
                            // Acción al hacer clic en el elemento de inicio
                            Intent vacantes = new Intent(getApplicationContext(), VacantesActivity.class);
                            startActivity(vacantes);
                            return true;
                        case R.id.categorias:
                            // Acción al hacer clic en el elemento de categorias
                            Intent solicitudes = new Intent(getApplicationContext(), CategoriasActivity.class);
                            startActivity(solicitudes);
                            return true;
                        case R.id.usuarios:
                            // Acción al hacer clic en el elemento de usuarios
                            Intent usuarios = new Intent(getApplicationContext(), UsuariosActivity.class);
                            startActivity(usuarios);
                            return true;
                    }
                    return false;
                });
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.errorUsuario), Toast.LENGTH_SHORT).show();     // El tipo de usuario es desconocido o no está definido
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);       // El usuario no está autenticado, redirigir a la pantalla de inicio de sesión
            startActivity(intent);
            finish();
        }

        listaCategorias = baseDatosControlador.obtenerCategorias();
        if (listaCategorias.isEmpty()) {
            agregarCategoriasPredeterminadas();
        }

        listaVacantesDestacadas = baseDatosControlador.obtenerVacantesDestacadas();
        mostrarVacantesDestacadas();
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
    /* Método para obtener las vacantes destacadas de la base de datos */
    public void mostrarVacantesDestacadas() {
        adaptador = new VacanteDestacadoAdapter(listaVacantesDestacadas, this);
        adaptador.setOnVacanteDestacadoClickListener(this); // Establecer el listener en el adaptador
        recyclerViewVacantesDestacadas.setAdapter(adaptador);
        recyclerViewVacantesDestacadas.setLayoutManager(new GridLayoutManager(this,1)); // Primero contexto y luego el número de columnas
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onVacanteDestacadoClick(Vacante vacante) {
        Intent detallesIntent = new Intent(this, DetallesActivity.class);
        detallesIntent.putExtra("detallesVacante", vacante);
        startActivity(detallesIntent);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    private void agregarCategoriasPredeterminadas () {
        // No hay categorías almacenadas, creo categorías predefinidas
        Categoria catArquitectura = new Categoria();
        Categoria catRRHH = new Categoria();
        Categoria catSoftware = new Categoria();

        catArquitectura.setNombre("Arquitectura");
        catRRHH.setNombre("Recursos Humanos");
        catSoftware.setNombre("Desarrollo de software");

        long idCategoriaArq = baseDatosControlador.agregarCategoria(catArquitectura);
        long idCategoriaRRHH = baseDatosControlador.agregarCategoria(catRRHH);
        long idCategoriaSoftware = baseDatosControlador.agregarCategoria(catSoftware);

        // La categoría se insertó correctamente y se asignó el ID correcto
        catArquitectura.setId((int) idCategoriaArq);
        catRRHH.setId((int) idCategoriaRRHH);
        catSoftware.setId((int) idCategoriaSoftware);

        // Agrega las categorías a la lista
        listaCategorias.add(catArquitectura);
        listaCategorias.add(catRRHH);
        listaCategorias.add(catSoftware);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
