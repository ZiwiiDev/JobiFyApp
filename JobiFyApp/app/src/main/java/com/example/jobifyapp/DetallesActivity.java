package com.example.jobifyapp;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobifyapp.basedatos.ControladorBD;
import com.example.jobifyapp.modelo.Solicitud;
import com.example.jobifyapp.modelo.Usuario;
import com.example.jobifyapp.modelo.Vacante;
import com.example.jobifyapp.utils.UserUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
// -----------------------------------------------------------------------------------------------------------------------------
public class DetallesActivity extends AppCompatActivity {
    /* VARIABLES */
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private Intent volver;
    private TextView categoriaVacante, fechaVacante, salarioVacante, detallesVacante, nombreVacante;
    private Button btnVolverDetalles, btnEnviarCV;

    private ControladorBD baseDatosControlador;
    private Solicitud solicitud;

    /* CONSTANTES */
    private static final int REQUEST_CODE_ADJUNTAR_ARCHIVO = 1;
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        UserUtil.init(getApplicationContext());             // Para mantener la persistencia de datos

        baseDatosControlador = new ControladorBD(getApplicationContext());

        btnVolverDetalles = findViewById(R.id.btnVolverDetalles);
        btnEnviarCV = findViewById(R.id.btnEnviarCV);

        nombreVacante = findViewById(R.id.nombreVacante);
        categoriaVacante = findViewById(R.id.categoriaVacante);
        fechaVacante = findViewById(R.id.fechaVacante);
        salarioVacante = findViewById(R.id.salarioVacante);
        detallesVacante = findViewById(R.id.detallesVacante);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null && user.getEmail() != null) {
            String tipoUsuario = UserUtil.getTipoUsuario();

            if (tipoUsuario != null && tipoUsuario.equals("candidato")) {
                btnEnviarCV.setVisibility(View.VISIBLE);
            } else if (tipoUsuario != null && (tipoUsuario.equals("empresa") || tipoUsuario.equals("admin"))) {
                btnEnviarCV.setVisibility(View.GONE);
            } else {
                Toast.makeText(DetallesActivity.this, getString(R.string.errorUsuario), Toast.LENGTH_SHORT).show();     // El tipo de usuario es desconocido o no está definido
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);       // El usuario no está autenticado, redirigir a la pantalla de inicio de sesión
            startActivity(intent);
            finish();
        }

        // Obtener la vacante seleccionada del Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("detallesVacante")) {
            Vacante vacante = (Vacante) intent.getSerializableExtra("detallesVacante");
            if (vacante != null) {
                // Establecer los datos en los TextView
                nombreVacante.setText(vacante.getNombre().toUpperCase());

                if (vacante.getIdCategoria() != null) {
                    categoriaVacante.setText(vacante.getIdCategoria().getNombre());
                } else {
                    categoriaVacante.setText(R.string.categoriaDesconocida);
                }

                Date fechaVacanteActualizar = vacante.getFecha();
                if (fechaVacanteActualizar == null) {
                    fechaVacanteActualizar = Calendar.getInstance().getTime();
                }

                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String fecha = dateFormat.format(fechaVacanteActualizar);

                fechaVacante.setText(fecha);
                salarioVacante.setText(String.valueOf(vacante.getSalario()));
                detallesVacante.setText(vacante.getDetalles());
            }
        }

        // Si pulso el botón "Volver" regreso a la pantalla anterior
        // HACER QUE VUELVA A DESTACADOS O VACANTES, DEPENDE DE DONDE ESTUVIESE ANTES
        btnVolverDetalles.setOnClickListener(v -> {
            volver = new Intent(DetallesActivity.this, MainActivity.class);
            startActivity(volver);
        });

        btnEnviarCV.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(DetallesActivity.this);
            builder.setTitle("JobiFy · Adjuntar Currículum (.pdf)");

            // Crear una vista personalizada para el cuadro de diálogo
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_adjuntar_curriculum, null);
            builder.setView(dialogView);

            // Obtener la referencia del botón "Adjuntar"
            Button btnAdjuntar = dialogView.findViewById(R.id.btnAdjuntar);

            // Crear el cuadro de diálogo
            AlertDialog dialog = builder.create();

            // Mostrar el cuadro de diálogo
            dialog.show();

            // Escuchar el clic del botón "Adjuntar"
            btnAdjuntar.setOnClickListener(adjuntarView -> {
                // Intent para abrir la galería de archivos del dispositivo
                Intent galeriaArchivos = new Intent(Intent.ACTION_GET_CONTENT);
                galeriaArchivos.setType("application/pdf"); // Filtrar por archivos PDF
                startActivityForResult(galeriaArchivos, REQUEST_CODE_ADJUNTAR_ARCHIVO);
            });

            // Agregar este código para cerrar el diálogo al finalizar la actividad
            dialog.setOnDismissListener(dialogInterface -> {
                // Finalizar la actividad
                finish();
            });
        });
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADJUNTAR_ARCHIVO && resultCode == RESULT_OK && data != null) {
            // Obtiene la URI del archivo seleccionado
            Uri uriArchivo = data.getData();

            // Convierte la URI a una ruta de archivo
            String rutaArchivo = obtenerRutaArchivo(uriArchivo);

            // Verificar que se haya obtenido la ruta del archivo
            if (rutaArchivo != null) {
                // Obtener la vacante actual
                Vacante vacanteActual = obtenerVacanteActual();

                String email = user.getEmail();

                int usuarioId = baseDatosControlador.obtenerIdUsuario(email);
                Usuario usuario = baseDatosControlador.obtenerDatosUsuario(usuarioId);
                usuario.setEmail(email);

                if (vacanteActual != null) {
                    // Obtener el archivo en formato byte[]
                    byte[] archivoAdjunto = obtenerArchivoByte(rutaArchivo);

                    // Crear una nueva solicitud con los datos necesarios
                    Random random = new Random();
                    int idSolicitud = random.nextInt(Integer.MAX_VALUE); // Obtener un valor positivo

                    solicitud = new Solicitud(idSolicitud, new Date(), archivoAdjunto, vacanteActual, usuario);

                    Log.e("intentandoEnviar", solicitud.toString());

                    // Guardar la solicitud en la base de datos
                    boolean exito = baseDatosControlador.guardarSolicitud(solicitud);

                    if (exito) {
                        // La solicitud se guardó exitosamente
                        Log.e("solicitudEnviadaExito", solicitud.toString());
                        Toast.makeText(DetallesActivity.this, getString(R.string.solicitudCorrecto), Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        // Hubo un error al guardar la solicitud
                        Toast.makeText(DetallesActivity.this, getString(R.string.errorSolicitud), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                // No se pudo obtener la ruta del archivo
                Toast.makeText(DetallesActivity.this, getString(R.string.solicitudError), Toast.LENGTH_SHORT).show();
            }
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    // Método para obtener la ruta de archivo a partir de una URI
    private String obtenerRutaArchivo(Uri uri) {
        String ruta = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            // URI de contenido (por ejemplo, seleccionado desde la galería)
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                File tempFile = crearArchivoTemporal();
                if (tempFile != null) {
                    OutputStream outputStream = new FileOutputStream(tempFile);
                    copiarStream(inputStream, outputStream);
                    ruta = tempFile.getAbsolutePath();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            // URI de archivo
            ruta = uri.getPath();
        }
        return ruta;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    // Método para crear un archivo temporal en el directorio de caché
    private File crearArchivoTemporal() {
        try {
            File tempDir = getCacheDir();
            String tempFileName = "temp_file.pdf";
            return File.createTempFile(tempFileName, null, tempDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    // Método para copiar el contenido de un flujo de entrada (InputStream) a un flujo de salida (OutputStream)
    private void copiarStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outputStream.close();
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    // Método para convertir un archivo a un array de bytes (byte[])
    private byte[] obtenerArchivoByte(String rutaArchivo) {
        try {
            File file = new File(rutaArchivo);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            fis.close();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    // Método para obtener la vacante actual desde el intent
    private Vacante obtenerVacanteActual() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("detallesVacante")) {
            Vacante vacante = (Vacante) intent.getSerializableExtra("detallesVacante");
            if (vacante != null) {
                String nombreVacante = vacante.getNombre(); // Obtener el nombre de la vacante
                return baseDatosControlador.obtenerVacantePorNombre(nombreVacante); // Devolver la vacante actual
            }
        }
        return null; // Valor predeterminado si no se encuentra la vacante actual
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
