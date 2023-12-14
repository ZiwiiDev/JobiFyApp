package com.example.jobifyapp.adapters;
// -----------------------------------------------------------------------------------------------------------------------------
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobifyapp.R;
import com.example.jobifyapp.modelo.Vacante;
import com.example.jobifyapp.utils.UserUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
// -----------------------------------------------------------------------------------------------------------------------------
public class VacanteDestacadoAdapter extends RecyclerView.Adapter<VacanteDestacadoAdapter.VacanteDestacadoViewHolder>{
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private ArrayList<Vacante> listaVacantesDestacados;
    private ArrayList<Vacante> listaVacantesOriginal;       // Nueva lista para guardar la lista original sin filtrar
    private Context context;
    private VacanteDestacadoAdapter.OnVacanteDestacadoClickListener onVacanteDestacadoClickListener;
    // -----------------------------------------------------------------------------------------------------------------------------
    public VacanteDestacadoAdapter(ArrayList<Vacante> listaVacantesDestacados, Context context) {
        this.listaVacantesDestacados = listaVacantesDestacados;
        this.listaVacantesOriginal = new ArrayList<>(listaVacantesDestacados);        // Guardar una copia de la lista original

        this.context = context;
        UserUtil.init(context.getApplicationContext());
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public VacanteDestacadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_vacantes, parent, false);
        return new VacanteDestacadoViewHolder(view);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public interface OnVacanteDestacadoClickListener {
        void onVacanteDestacadoClick(Vacante vacante);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void setOnVacanteDestacadoClickListener(VacanteDestacadoAdapter.OnVacanteDestacadoClickListener listener) {
        this.onVacanteDestacadoClickListener = listener;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull VacanteDestacadoViewHolder holder, int position) {
        Vacante vacante = listaVacantesDestacados.get(position);

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

        if (tipoUsuario != null && (tipoUsuario.equals("empresa") || tipoUsuario.equals("admin") || tipoUsuario.equals("candidato"))) {
            holder.imageViewEditar.setVisibility(View.GONE);
            holder.imageViewEliminar.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onVacanteDestacadoClickListener != null) {
                    onVacanteDestacadoClickListener.onVacanteDestacadoClick(vacante);
                }
            }
        });
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public int getItemCount() {
        if (listaVacantesDestacados != null) {
            // Filtra la lista de vacantes por aquellas que estén destacadas
            ArrayList<Vacante> vacantesDestacadas = new ArrayList<>();
            for (Vacante vacante : listaVacantesDestacados) {
                if (vacante.getDestacado() == 1) { // Suponiendo que 1 significa que está destacada
                    vacantesDestacadas.add(vacante);
                }
            }
            return vacantesDestacadas.size();
        } else {
            return 0;
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NotifyDataSetChanged")
    public void filtrarVacantes(String consultaBusqueda) {
        listaVacantesDestacados.clear(); // Limpiar la lista actual

        if (consultaBusqueda.isEmpty()) {
            listaVacantesDestacados.addAll(listaVacantesOriginal); // Si la consulta de búsqueda está vacía, mostrar todas las vacantes
        } else {
            for (Vacante vacante : listaVacantesOriginal) {
                if (vacante.getNombre().toLowerCase().contains(consultaBusqueda.toLowerCase())) {
                    listaVacantesDestacados.add(vacante); // Agregar vacantes que coincidan con la búsqueda
                }
            }
        }

        notifyDataSetChanged(); // Notificar cambios en los datos
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public static class VacanteDestacadoViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreVacanteCardView;
        private TextView categoriaVacanteCardView;
        private TextView fechaVacanteCardView;
        private TextView salarioVacanteCardView;
        private ImageView imageViewEditar;
        private ImageView imageViewEliminar;

        public VacanteDestacadoViewHolder(View itemView) {
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
