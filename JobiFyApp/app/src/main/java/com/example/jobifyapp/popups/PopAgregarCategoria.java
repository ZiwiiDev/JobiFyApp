package com.example.jobifyapp.popups;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobifyapp.R;
import com.example.jobifyapp.basedatos.ControladorBD;
import com.example.jobifyapp.modelo.Categoria;

import java.util.Objects;
// -----------------------------------------------------------------------------------------------------------------------------
public class PopAgregarCategoria extends AppCompatActivity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private Button btnAgregarCategoria, btnVolverCategoria;         // Se definen los botones
    private EditText nombreCategoriaDato;                           // Se define el campo de texto para interactuar con la interfaz de usuario

    private ControladorBD baseDatosControlador;                     // Se declara una instancia del controlador de base de datos

    private Categoria categoria;                                    // Para almacenar la categoría que se va a agregar
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_agregar_categoria);

        baseDatosControlador = new ControladorBD(getApplicationContext());          // Se inicializa la instancia del controlador de base de datos

        // Se obtienen las referencias de los elementos visuales en el diseño de la actividad
        btnAgregarCategoria = findViewById(R.id.btnAgregarCategoria);
        btnVolverCategoria = findViewById(R.id.btnVolverCategoria);
        nombreCategoriaDato = findViewById(R.id.nombreCategoriaDato);

        // Se configura el diseño de la ventana emergente
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

        // Para no tener problemas con la toolbar la ocultamos
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Finalizar la activity
        btnVolverCategoria.setOnClickListener(v -> {
            finish();
        });

        // Agregar la categoría a la base de datos
        btnAgregarCategoria.setOnClickListener(v -> {
            String nombreCategoria = nombreCategoriaDato.getText().toString();                  // Se obtiene el nombre de la categoría del campo de texto

            // Verificar si el nombre de la categoría ya existe en la base de datos
            boolean categoriaExistente = baseDatosControlador.verificarCategoriaExistente(nombreCategoria);

            // Si el nombre no está vacío
            if (!nombreCategoria.isEmpty()) {
                if (!categoriaExistente) {
                    categoria = new Categoria(); // Crear una nueva instancia de Categoria
                    categoria.setNombre(nombreCategoria); // Establecer el nombre de la categoría

                    long idCategoria = baseDatosControlador.agregarCategoria(categoria);
                    if (idCategoria != -1) {
                        // La categoría se insertó correctamente y se asignó el ID correcto
                        categoria.setId((int) idCategoria);

                        Toast.makeText(getApplicationContext(), getString(R.string.agregarCorrectamenteCategoria), Toast.LENGTH_SHORT).show(); // Mensaje de éxito
                        Log.e("nuevaCategoria", categoria.toString());

                        Intent intent = new Intent();
                        intent.putExtra("categoriaAgregada", categoria);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.errorCategoriaInsertar), Toast.LENGTH_SHORT).show(); // Mensaje de categoría existente
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.categoriaExiste), Toast.LENGTH_SHORT).show(); // Mensaje de categoría existente
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.errorAgregarCategoria), Toast.LENGTH_SHORT).show(); // Mensaje de error
            }
        });
        // -----------------------------------------------------------------------------------------------------------------------------
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
