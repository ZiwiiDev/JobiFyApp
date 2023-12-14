package com.example.jobifyapp;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobifyapp.adapters.SolicitudAdapter;
import com.example.jobifyapp.basedatos.ControladorBD;
import com.example.jobifyapp.modelo.Solicitud;
import com.example.jobifyapp.utils.UserUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
// -----------------------------------------------------------------------------------------------------------------------------
public class SolicitudesActivity extends AppCompatActivity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private BottomNavigationView bottomNavigationViewSolicitudes;
    private Toolbar toolbarSolicitudes;

    private RecyclerView recyclerViewSolicitudes;
    private ArrayList<Solicitud> listaSolicitudes;
    private SolicitudAdapter adaptador;

    private ControladorBD baseDatosControlador;

    private String consultaBusqueda;
    private EditText buscarSolicitudes;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);

        UserUtil.init(getApplicationContext());             // Para mantener la persistencia de datos

        baseDatosControlador = new ControladorBD(getApplicationContext());

        /* Referencia al toolbar */
        toolbarSolicitudes = findViewById(R.id.toolbarSolicitudes);
        setSupportActionBar(toolbarSolicitudes);

        buscarSolicitudes = findViewById(R.id.buscarSolicitud);
        recyclerViewSolicitudes = findViewById(R.id.recyclerViewSolicitudes);
        bottomNavigationViewSolicitudes = findViewById(R.id.bottom_navigation_solicitudes);

        consultaBusqueda = buscarSolicitudes.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Log.e("numSolicitudes", String.valueOf(baseDatosControlador.numSolicitudes()));

        // Escuchar los eventos de selección de elemento en la navegación inferior
        bottomNavigationViewSolicitudes.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.inicio:
                    // Acción al hacer clic en el elemento de inicio
                    Intent inicio = new Intent(SolicitudesActivity.this, MainActivity.class);
                    startActivity(inicio);
                    return true;
                case R.id.vacantes:
                    // Acción al hacer clic en el elemento de vacantes
                    Intent solicitudes = new Intent(SolicitudesActivity.this, VacantesActivity.class);
                    startActivity(solicitudes);
                    return true;
            }
            return false;
        });

        buscarSolicitudes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita implementar en este caso
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cuando se realiza un cambio en el texto de búsqueda
                consultaBusqueda = s.toString();
                adaptador.filtrarSolicitud(consultaBusqueda);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No se necesita implementar en este caso
            }
        });

        listaSolicitudes = baseDatosControlador.obtenerSolicitudes();
        if (user != null && user.getEmail() != null) {
            mostrarSolicitudes();
        }
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
                Intent perfil = new Intent(SolicitudesActivity.this, PerfilActivity.class);
                startActivity(perfil);
                break;
            case R.id.ayuda:
                Intent i = new Intent(SolicitudesActivity.this, AyudaActivity.class);
                startActivity(i);
                break;
            case R.id.acercaDe:
                Intent i1 = new Intent(SolicitudesActivity.this, ContactoActivity.class);
                startActivity(i1);
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
                    Intent e = new Intent(SolicitudesActivity.this, InicioActivity.class);
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
    public void mostrarSolicitudes() {
        adaptador = new SolicitudAdapter(listaSolicitudes);

        adaptador.setOnDownloadClickListener(new SolicitudAdapter.OnDownloadClickListener() {
            @Override
            public void onDownloadClick(Solicitud solicitud) {
                byte[] curriculumBytes = solicitud.getArchivo();  // Obtén los bytes del currículum

                if (ContextCompat.checkSelfPermission(SolicitudesActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Si el permiso no está concedido, pedir permisos
                    ActivityCompat.requestPermissions(SolicitudesActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    // Acceso al almacenamiento externo
                    // Comprueba que el currículum no sea nulo y tenga contenido
                    if (curriculumBytes != null && curriculumBytes.length > 0) {
                        // Nombre del archivo
                        String nombreArchivo = solicitud.getIdVacante().getNombre().trim().toLowerCase(Locale.ROOT) + "-" +
                                solicitud.getId()+
                                ".pdf";

                        // Directorio de descargas
                        File directorioDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                        // Crea un archivo en el directorio de descargas con el nombre del archivo
                        File archivo = new File(directorioDescargas, nombreArchivo);

                        if (archivo.exists()) {
                            // El currículum ya ha sido descargado
                            Toast.makeText(getApplicationContext(), getString(R.string.yaDescargadoCV), Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                // Crea un flujo de salida para escribir los bytes en el archivo
                                FileOutputStream outputStream = new FileOutputStream(archivo);
                                outputStream.write(curriculumBytes);
                                outputStream.close();

                                // Muestra un mensaje o realiza alguna acción para indicar que el archivo se ha descargado correctamente
                                Toast.makeText(getApplicationContext(), getString(R.string.descargarCVcorrecto), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), getString(R.string.noCorrectoCV), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        recyclerViewSolicitudes.setAdapter(adaptador);
        recyclerViewSolicitudes.setLayoutManager(new GridLayoutManager(this, 1));
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            // Verifica si el código de solicitud es para el permiso WRITE_EXTERNAL_STORAGE
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Verifica si se otorgó el permiso WRITE_EXTERNAL_STORAGE
                // Permiso concedido, intenta descargar el CV nuevamente
                mostrarSolicitudes();
            } else {
                // Permiso denegado, muestra un mensaje o realiza alguna acción para indicar al usuario que la descarga no se puede realizar sin el permiso
                Toast.makeText(getApplicationContext(), getString(R.string.permisoDenegado), Toast.LENGTH_SHORT).show();
            }
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
