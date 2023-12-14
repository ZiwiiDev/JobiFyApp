package com.example.jobifyapp.adapters;
// -----------------------------------------------------------------------------------------------------------------------------
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobifyapp.R;
import com.example.jobifyapp.modelo.Solicitud;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
// -----------------------------------------------------------------------------------------------------------------------------
public class SolicitudAdapter extends RecyclerView.Adapter<SolicitudAdapter.SolicitudViewHolder>{
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private ArrayList<Solicitud> listaSolicitudes;
    private ArrayList<Solicitud> listaOriginal;     // Nueva lista para guardar la lista original sin filtrar
    private OnDownloadClickListener mOnDownloadClickListener;
    // -----------------------------------------------------------------------------------------------------------------------------
    public SolicitudAdapter(ArrayList<Solicitud> listaSolicitudes) {
        this.listaSolicitudes = listaSolicitudes;
        this.listaOriginal = new ArrayList<>(listaSolicitudes);   // Guardar una copia de la lista original
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public interface OnDownloadClickListener {
        void onDownloadClick(Solicitud solicitud);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void setOnDownloadClickListener(OnDownloadClickListener listener) {
        mOnDownloadClickListener = listener;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public SolicitudViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_solicitudes, parent, false);
        return new SolicitudAdapter.SolicitudViewHolder(view);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull SolicitudAdapter.SolicitudViewHolder holder, int position) {
        Solicitud solicitud = listaSolicitudes.get(position);

        // Verificar si el objeto Vacante es nulo
        if (solicitud.getIdVacante() != null) {
            Date fechaSolicitud = solicitud.getFecha();
            if (fechaSolicitud == null) {
                fechaSolicitud = Calendar.getInstance().getTime();
            }

            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = dateFormat.format(fechaSolicitud);

            // Si no está establecido algún dato sale el texto de "No establecido"
            if (!Objects.equals(solicitud.getIdVacante().getNombre(), "")) {
                holder.nombreVacanteSolicitudCardView.setText(solicitud.getIdVacante().getNombre());
            } else {
                holder.nombreVacanteSolicitudCardView.setText(R.string.noEstablecido);
            }

            holder.numSolicitudCardView.setText(String.valueOf(solicitud.getId()));

            if (!Objects.equals(fecha, "")) {
                holder.fechaSolicitudCardView.setText(fecha);
            } else {
                holder.fechaSolicitudCardView.setText(R.string.noEstablecido);
            }

            holder.btnDescargarCVCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Llamar al método de la interfaz para manejar el evento de clic
                    if (mOnDownloadClickListener != null) {
                        mOnDownloadClickListener.onDownloadClick(solicitud);
                    }
                }
            });
        } else {
            // Si se ha borrado la vacante
            holder.nombreVacanteSolicitudCardView.setText(R.string.vacanteNoExiste);
            holder.numSolicitudCardView.setText(R.string.cero);
            holder.fechaSolicitudCardView.setText(R.string.noHayFecha);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public int getItemCount() {
        if (listaSolicitudes != null) {
            return listaSolicitudes.size();
        } else {
            return 0;
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NotifyDataSetChanged")
    public void filtrarSolicitud(String consultaBusqueda) {
        listaSolicitudes.clear();     // Limpiar la lista actual

        if (consultaBusqueda.isEmpty()) {
            listaSolicitudes.addAll(listaOriginal); // Si la consulta de búsqueda está vacía, mostrar todas las solicitudes
        } else {
            // Verificar si los datos contienen la consulta
            for (Solicitud solicitud : listaOriginal) {
                if (solicitud.getIdVacante().getNombre().toLowerCase().contains(consultaBusqueda.toLowerCase())) {
                    listaSolicitudes.add(solicitud);
                }
            }
        }

        notifyDataSetChanged(); // Notificar cambios en los datos
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public static class SolicitudViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreVacanteSolicitudCardView;
        private TextView numSolicitudCardView;
        private TextView fechaSolicitudCardView;
        private ImageView btnDescargarCVCardView;

        public SolicitudViewHolder(@NonNull View itemView) {
            super(itemView);

            btnDescargarCVCardView = itemView.findViewById(R.id.btnDescargarCVCardView);
            nombreVacanteSolicitudCardView = itemView.findViewById(R.id.nombreVacanteSolicitudCardView);
            numSolicitudCardView = itemView.findViewById(R.id.numSolicitudCardView);
            fechaSolicitudCardView = itemView.findViewById(R.id.fechaSolicitudCardView);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
