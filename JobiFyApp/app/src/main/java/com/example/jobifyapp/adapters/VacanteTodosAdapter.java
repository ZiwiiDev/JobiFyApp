package com.example.jobifyapp.adapters;
// -----------------------------------------------------------------------------------------------------------------------------
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobifyapp.ActualizarVacante;
import com.example.jobifyapp.R;
import com.example.jobifyapp.basedatos.ControladorBD;
import com.example.jobifyapp.modelo.Vacante;
import com.example.jobifyapp.utils.UserUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
// -----------------------------------------------------------------------------------------------------------------------------
public class VacanteTodosAdapter extends RecyclerView.Adapter<VacanteTodosAdapter.VacanteViewHolder>{
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private ArrayList<Vacante> listaVacantes;
    private ArrayList<Vacante> listaOriginal;       // Nueva lista para guardar la lista original sin filtrar
    private Context context;
    private ControladorBD baseDatosControlador;     // Variable para el controlador de base de datos
    private OnVacanteClickListener onVacanteClickListener;
    // -----------------------------------------------------------------------------------------------------------------------------
    public VacanteTodosAdapter(ArrayList<Vacante> listaVacantes, Context context, ControladorBD baseDatosControlador) {
        this.listaVacantes = listaVacantes;
        this.listaOriginal = new ArrayList<>(listaVacantes);        // Guardar una copia de la lista original

        this.context = context;
        this.baseDatosControlador = baseDatosControlador; // Asignar el controlador de base de datos
        UserUtil.init(context.getApplicationContext());
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public VacanteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_vacantes, parent, false);
        return new VacanteTodosAdapter.VacanteViewHolder(view);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public interface OnVacanteClickListener {
        void onVacanteClick(Vacante vacante);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void setOnVacanteClickListener(OnVacanteClickListener listener) {
        this.onVacanteClickListener = listener;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull VacanteViewHolder holder, int position) {
        Vacante vacante = listaVacantes.get(position);

        Date fechaVacante = vacante.getFecha();
        if (fechaVacante == null) {
            fechaVacante = Calendar.getInstance().getTime();
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = dateFormat.format(fechaVacante);

        holder.fechaVacanteCardView.setText(fecha);

        holder.nombreVacanteCardView.setText(vacante.getNombre());

        if (vacante.getIdCategoria() != null) {
            holder.categoriaVacanteCardView.setText(vacante.getIdCategoria().getNombre());
        } else {
            holder.categoriaVacanteCardView.setText(R.string.categoriaDesconocida);
        }

        holder.salarioVacanteCardView.setText(String.valueOf(vacante.getSalario()));

        String tipoUsuario = UserUtil.getTipoUsuario();

        if (tipoUsuario != null && (tipoUsuario.equals("empresa") || tipoUsuario.equals("admin"))) {
            holder.imageViewEditar.setVisibility(View.VISIBLE);
            holder.imageViewEliminar.setVisibility(View.VISIBLE);

            holder.imageViewEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActualizarVacante.class);
                    intent.putExtra("nombreVacante", vacante.getNombre());
                    context.startActivity(intent);
                }
            });

            holder.imageViewEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eliminarVacante(vacante);
                }
            });
        } else {
            holder.imageViewEditar.setVisibility(View.GONE);
            holder.imageViewEliminar.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onVacanteClickListener != null) {
                    onVacanteClickListener.onVacanteClick(vacante);
                }
            }
        });
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    private void eliminarVacante(Vacante vacante) {
        int position = listaVacantes.indexOf(vacante);
        if (position != -1) {
            listaVacantes.remove(position);
            listaOriginal.remove(vacante);
            notifyItemRemoved(position);

            baseDatosControlador.eliminarVacanteBD(vacante.getNombre());

            Toast.makeText(context, R.string.eliminadaVacante, Toast.LENGTH_SHORT).show();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public int getItemCount() {
        if (listaVacantes != null) {
            return listaVacantes.size();
        } else {
            return 0;
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void agregarVacante(Vacante vacante) {
        listaVacantes.add(vacante); // Agregar la vacante a la lista actual
        listaOriginal.add(vacante); // Agregar la vacante a la lista original sin filtrar
        notifyItemInserted(listaVacantes.size() - 1); // Notificar la inserción
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void actualizarVacante(Vacante vacanteActualizada) {
        int posicion = listaVacantes.indexOf(vacanteActualizada);
        if (posicion != -1) {
            listaVacantes.set(posicion, vacanteActualizada);
            notifyItemChanged(posicion);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NotifyDataSetChanged")
    public void filtrarVacantes(String consultaBusqueda) {
        listaVacantes.clear(); // Limpiar la lista actual

        if (consultaBusqueda.isEmpty()) {
            listaVacantes.addAll(listaOriginal); // Si la consulta de búsqueda está vacía, mostrar todas las vacantes
        } else {
            for (Vacante vacante : listaOriginal) {
                if (vacante.getNombre().toLowerCase().contains(consultaBusqueda.toLowerCase())) {
                    listaVacantes.add(vacante); // Agregar vacantes que coincidan con la búsqueda
                }
            }
        }

        notifyDataSetChanged(); // Notificar cambios en los datos
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public static class VacanteViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreVacanteCardView;
        private TextView categoriaVacanteCardView;
        private TextView fechaVacanteCardView;
        private TextView salarioVacanteCardView;
        private ImageView imageViewEditar;
        private ImageView imageViewEliminar;

        public VacanteViewHolder(View itemView) {
            super(itemView);
            nombreVacanteCardView = itemView.findViewById(R.id.nombreVacanteCardView);
            categoriaVacanteCardView = itemView.findViewById(R.id.categoriaVacanteCardView);
            fechaVacanteCardView = itemView.findViewById(R.id.fechaVacanteCardView);
            salarioVacanteCardView = itemView.findViewById(R.id.salarioVacanteCardView);
            imageViewEditar = itemView.findViewById(R.id.imageViewEditar);
            imageViewEliminar = itemView.findViewById(R.id.imageViewEliminar);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
