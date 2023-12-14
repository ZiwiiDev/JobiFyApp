package com.example.jobifyapp;
// -----------------------------------------------------------------------------------------------------------------------------
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jobifyapp.basedatos.ControladorBD;
import com.example.jobifyapp.modelo.Categoria;
import com.example.jobifyapp.modelo.Empresa;
import com.example.jobifyapp.modelo.Vacante;

import java.util.ArrayList;
// -----------------------------------------------------------------------------------------------------------------------------
public class AgregarOfertaActivity extends AppCompatActivity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private Button btnVolverOferta, btnAgregarOferta;
    private EditText nombreOfertaDato, salarioOfertaDato, detallesOfertaDato;
    private Switch destacadoOfertaDato;
    private Spinner spinnerCategoria;

    private ControladorBD baseDatosControlador;

    private Vacante nuevaVacante;
    private Categoria categoria;
    private Empresa empresa;
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_oferta);

        baseDatosControlador = new ControladorBD(getApplicationContext());          // Se inicializa la instancia del controlador de base de datos

        // Inicializar variables de interfaz de usuario
        nombreOfertaDato = findViewById(R.id.nombreOfertaDato);
        salarioOfertaDato = findViewById(R.id.salarioOfertaDato);
        destacadoOfertaDato = findViewById(R.id.destacadoOfertaDatoAgregar);
        detallesOfertaDato = findViewById(R.id.detallesOfertaDato);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);

        btnVolverOferta = findViewById(R.id.btnVolverOferta);
        btnAgregarOferta = findViewById(R.id.btnAgregarOferta);

        // Obtener todas las categorías de la base de datos
        ArrayList<Categoria> listaCategorias = baseDatosControlador.obtenerTodasLasCategorias();

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
        spinnerCategoria.setAdapter(adapter);

        // Configurar el listener para el botón Agregar Oferta
        btnAgregarOferta.setOnClickListener(view -> agregarOferta());

        // Si pulso el botón "Volver" regreso a la pantalla anterior
        btnVolverOferta.setOnClickListener(view -> {
            Intent intent = new Intent(AgregarOfertaActivity.this, VacantesActivity.class);
            startActivity(intent);
            finish();
        });
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    private void agregarOferta() {
        // Obtener los valores de los campos de entrada
        String nombre = nombreOfertaDato.getText().toString();
        String salarioStr = salarioOfertaDato.getText().toString();
        int destacado = destacadoOfertaDato.isChecked() ? 1 : 0;        // Si es 1 está destacado, 0 no está destacado
        String detalles = detallesOfertaDato.getText().toString();
        String categoriaSeleccionada = spinnerCategoria.getSelectedItem().toString();

        // Verificar si el nombre de la vacante ya existe en la base de datos
        boolean vacanteExiste = baseDatosControlador.verificarVacanteExistente(nombre);

        // Validar que los campos requeridos estén completos
        if (nombre.isEmpty() || salarioStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.completarCampos), Toast.LENGTH_SHORT).show();
        } else {
            if (!vacanteExiste) {
                double salario = Double.parseDouble(salarioStr);        // Convertir el salario a un valor numérico

                // Obtener el ID del usuario actual
                int idUsuarioActual = baseDatosControlador.obtenerIdEmpresa();

                // Crear una nueva instancia de la clase Vacante con los valores obtenidos
                nuevaVacante = new Vacante();
                nuevaVacante.setNombre(nombre);
                nuevaVacante.setSalario(salario);
                nuevaVacante.setDestacado(destacado);
                nuevaVacante.setDetalles(detalles);

                // Crear una instancia de la clase Categoria con la categoría seleccionada
                if (TextUtils.isEmpty(categoriaSeleccionada)) {
                    Toast.makeText(this, R.string.categoriaVacia, Toast.LENGTH_SHORT).show();
                } else {
                    categoria = baseDatosControlador.obtenerCategoriaPorNombre(categoriaSeleccionada);
                    if (categoria != null) {
                        nuevaVacante.setIdCategoria(categoria);
                    } else {
                        Toast.makeText(this, R.string.errorCategoria, Toast.LENGTH_SHORT).show();
                    }
                }

                // Crear una instancia de la clase Empresa con el usuario actual
                empresa = new Empresa();
                empresa.setId(idUsuarioActual);
                nuevaVacante.setIdUsuario(empresa);

                // Agregar la nueva vacante a la base de datos
                baseDatosControlador.agregarVacante(nuevaVacante);

                Toast.makeText(this, getString(R.string.datosExitoVacante), Toast.LENGTH_SHORT).show();

                // Configurar el resultado y finalizar la actividad
                Intent intent = new Intent();
                intent.putExtra("vacanteAgregada", nuevaVacante);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.vacanteExiste), Toast.LENGTH_SHORT).show(); // Mensaje de categoría existente
            }
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
