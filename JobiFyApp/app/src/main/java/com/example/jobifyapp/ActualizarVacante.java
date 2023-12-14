package com.example.jobifyapp;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobifyapp.basedatos.ControladorBD;
import com.example.jobifyapp.modelo.Categoria;
import com.example.jobifyapp.modelo.Vacante;

import java.util.ArrayList;
// -----------------------------------------------------------------------------------------------------------------------------
public class ActualizarVacante extends AppCompatActivity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private Button btnActualizarOferta, btnVolverOfertaActualizar;
    private TextView nombreOfertaDatoActualizar;
    private EditText salarioOfertaDatoActualizar, detallesOfertaDatoActualizar;
    private Switch destacadoOfertaDatoActualizar;
    private Spinner spinnerCategoriaActualizar;

    private ControladorBD baseDatosControlador;

    private ArrayList<Categoria> listaCategorias;

    private Vacante actualizarVacante;
    private Categoria actualizarCategoria;
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_vacante);

        baseDatosControlador = new ControladorBD(getApplicationContext());          // Se inicializa la instancia del controlador de base de datos

        // Inicializar variables de interfaz de usuario
        nombreOfertaDatoActualizar = findViewById(R.id.nombreOfertaDatoActualizar);
        salarioOfertaDatoActualizar = findViewById(R.id.salarioOfertaDatoActualizar);
        detallesOfertaDatoActualizar = findViewById(R.id.detallesOfertaDatoActualizar);
        destacadoOfertaDatoActualizar = findViewById(R.id.destacadoOfertaDatoActualizar);
        spinnerCategoriaActualizar = findViewById(R.id.spinnerCategoriaActualizar);

        btnActualizarOferta = findViewById(R.id.btnActualizarOferta);
        btnVolverOfertaActualizar = findViewById(R.id.btnVolverOfertaActualizar);

        // Obtener la vacante seleccionada de la actividad anterior
        if (getIntent().hasExtra("nombreVacante")) {
            String nombreVacante = getIntent().getStringExtra("nombreVacante");
            actualizarVacante = baseDatosControlador.obtenerVacantePorNombre(nombreVacante);
            Log.e("Antes de actualizar", actualizarVacante.toString());
        }

        if (actualizarVacante != null) {
            // Establecer los valores de la vacante en los campos correspondientes
            nombreOfertaDatoActualizar.setText(actualizarVacante.getNombre());
            salarioOfertaDatoActualizar.setText(String.valueOf(actualizarVacante.getSalario()));
            detallesOfertaDatoActualizar.setText(actualizarVacante.getDetalles());

            // Establecer el estado del Switch según el valor de destacado
            destacadoOfertaDatoActualizar.setChecked(actualizarVacante.getDestacado() == 1);

            // Obtener todas las categorías de la base de datos
            listaCategorias = baseDatosControlador.obtenerTodasLasCategorias();

            // Crear una lista de nombres de categorías
            ArrayList<String> nombresCategorias = new ArrayList<>();
            for (Categoria categoria : listaCategorias) {
                nombresCategorias.add(categoria.getNombre());
            }

            // Crear un adaptador con la lista de nombres de categorías
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresCategorias);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Verificar si el adaptador está vacío
            if (adapter.getCount() == 0) {
                // No hay categorías seleccionadas, mostrar Toast
                Toast.makeText(getApplicationContext(), R.string.spinnerVacio, Toast.LENGTH_SHORT).show();
            }

            // Establecer el adaptador en el Spinner
            spinnerCategoriaActualizar.setAdapter(adapter);

            // Obtener la categoría de la vacante seleccionada
            actualizarCategoria = baseDatosControlador.obtenerCategoriaPorNombre(actualizarVacante.getIdCategoria().getNombre());

            if (actualizarCategoria != null) {
                // Establecer la categoría seleccionada en el Spinner
                int index = adapter.getPosition(actualizarCategoria.getNombre());
                spinnerCategoriaActualizar.setSelection(index);
            }
        }

        // Configurar el listener para el botón Agregar Oferta
        btnActualizarOferta.setOnClickListener(view -> actualizarOferta());

        // Si pulso el botón "Volver" regreso a la pantalla anterior
        btnVolverOfertaActualizar.setOnClickListener(view -> {
            finish();
        });
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    private void actualizarOferta() {
        // Obtener los valores de los campos de entrada
        String nombreActualizar = nombreOfertaDatoActualizar.getText().toString();
        String salarioStrActualizar = salarioOfertaDatoActualizar.getText().toString();
        int destacadoActualizar = destacadoOfertaDatoActualizar.isChecked() ? 1 : 0;        // Si es 1 está destacado, 0 no está destacado
        String detallesActualizar = detallesOfertaDatoActualizar.getText().toString();
        String nombreCategoria = spinnerCategoriaActualizar.getSelectedItem().toString();

        double salario = Double.parseDouble(salarioStrActualizar);        // Convertir el salario a un valor numérico

        if (actualizarCategoria != null) {
            actualizarVacante.setNombre(nombreActualizar);
            actualizarVacante.setSalario(salario);
            actualizarVacante.setDestacado(destacadoActualizar);
            actualizarVacante.setDetalles(detallesActualizar);

            // Crear una instancia de la clase Categoria con la categoría seleccionada
            if (TextUtils.isEmpty(nombreCategoria)) {
                Toast.makeText(this, R.string.categoriaVacia, Toast.LENGTH_SHORT).show();
            } else {
                // Obtener la categoría seleccionada
                actualizarCategoria = baseDatosControlador.obtenerCategoriaPorNombre(nombreCategoria);
                if (actualizarCategoria != null) {
                    actualizarVacante.setIdCategoria(actualizarCategoria);
                } else {
                    Toast.makeText(this, R.string.errorCategoria, Toast.LENGTH_SHORT).show();
                }
            }

            // Llamar al método para actualizar la vacante en la base de datos
            baseDatosControlador.actualizarVacante(actualizarVacante);

            Toast.makeText(this, getString(R.string.datosActualizadosVacante), Toast.LENGTH_SHORT).show();
            Log.e("actualizadaVacante", actualizarVacante.toString());

            // Configurar el resultado y finalizar la actividad
            Intent intent = new Intent();
            intent.putExtra("vacanteActualizada", actualizarVacante);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, R.string.errorCategoria, Toast.LENGTH_SHORT).show();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
