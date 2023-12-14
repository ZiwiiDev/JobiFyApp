package com.example.jobifyapp.popups;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
public class PopEditCategoria extends AppCompatActivity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private Button btnEditarCategoria, btnVolverCategoriaEdit;                  // Se definen los botones
    private EditText nombreCategoriaDatoEdit;                                   // Se define el campo de texto para interactuar con la interfaz de usuario

    private ControladorBD baseDatosControlador;                                 // Se declara una instancia del controlador de base de datos

    private Categoria categoria;                                                // Para almacenar la categoría que se va a agregar
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_edit_categoria);

        // Se obtienen las referencias de los elementos visuales en el diseño de la actividad
        btnEditarCategoria = findViewById(R.id.btnEditarCategoria);
        btnVolverCategoriaEdit = findViewById(R.id.btnVolverCategoriaEdit);
        nombreCategoriaDatoEdit = findViewById(R.id.nombreCategoriaDatoEdit);

        baseDatosControlador = new ControladorBD(getApplicationContext());          // Se inicializa la instancia del controlador de base de datos

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

        // Para no tener problemas con la toolbar la ocultamos
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Finalizar la activity
        btnVolverCategoriaEdit.setOnClickListener(v -> {
            finish();
        });

        // Editar la categoría de la base de datos
        btnEditarCategoria.setOnClickListener(v -> {
            categoria = (Categoria) getIntent().getSerializableExtra("categoria");
            if (categoria != null) {
                String nombreCategoria = nombreCategoriaDatoEdit.getText().toString();                      // Se obtiene el nombre de la categoría del campo de texto

                // VERIFICAR SI EL NOMBRE EXISTE EN LA BASE DE DATOS TAMBIÉN

                // Si el nombre no está vacío y no es el mismo que ya estaba para evitar actualizaciones innecesarias
                if (!nombreCategoria.isEmpty() && !nombreCategoria.equals(categoria.getNombre())) {
                    categoria.setNombre(nombreCategoria);               // Establecer el nombre de la categoría

                    // Actualizar la categoría en la base de datos
                    baseDatosControlador.editarCategoria(categoria);

                    Toast.makeText(getApplicationContext(), getString(R.string.editarCorrectamenteCategoria), Toast.LENGTH_SHORT).show();           // Mensaje de éxito
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.errorAgregarCategoria), Toast.LENGTH_SHORT).show();        // Mensaje de error
                }
            }

            Intent intent = new Intent();
            intent.putExtra("categoriaEditada", categoria);
            setResult(RESULT_OK, intent);
            finish();
        });
        // -----------------------------------------------------------------------------------------------------------------------------
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
