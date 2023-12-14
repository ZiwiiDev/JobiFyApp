package com.example.jobifyapp.adapters;
// -----------------------------------------------------------------------------------------------------------------------------
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobifyapp.R;
import com.example.jobifyapp.modelo.Usuario;

import java.util.ArrayList;
import java.util.Objects;
// -----------------------------------------------------------------------------------------------------------------------------
public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>{
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private ArrayList<Usuario> listaUsuario;
    private ArrayList<Usuario> listaOriginal; // Nueva lista para guardar la lista original sin filtrar
    // -----------------------------------------------------------------------------------------------------------------------------
    public UsuarioAdapter(ArrayList<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
        this.listaOriginal = new ArrayList<>(listaUsuario); // Guardar una copia de la lista original
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_usuarios, parent, false);
        return new UsuarioAdapter.UsuarioViewHolder(view);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = listaUsuario.get(position);

        holder.emailUsuarioCardView.setText(usuario.getEmail());

        // Si no está establecido algún dato sale el texto de "No establecido"
        if (!Objects.equals(usuario.getNombre(), "")) {
            holder.nombreUsuarioCardViewTitulo.setText(usuario.getNombre());
        } else {
            holder.nombreUsuarioCardViewTitulo.setText(R.string.noEstablecido);
        }

        if (!Objects.equals(usuario.getUsername(), "")) {
            holder.usernameUsuarioCardView.setText(usuario.getUsername());
        } else {
            holder.usernameUsuarioCardView.setText(R.string.noEstablecido);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public int getItemCount() {
        if (listaUsuario != null) {
            return listaUsuario.size();
        } else {
            return 0;
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NotifyDataSetChanged")
    public void filtrarUsuario(String consultaBusqueda) {
        listaUsuario.clear(); // Limpiar la lista actual

        if (consultaBusqueda.isEmpty()) {
            listaUsuario.addAll(listaOriginal); // Si la consulta de búsqueda está vacía, mostrar todos los usuarios
        } else {
            // Verificar si el nombre, email o username contiene la consulta
            for (Usuario usuario : listaOriginal) {
                if (usuario.getNombre().toLowerCase().contains(consultaBusqueda.toLowerCase()) ||
                        usuario.getEmail().toLowerCase().contains(consultaBusqueda.toLowerCase()) ||
                        usuario.getUsername().toLowerCase().contains(consultaBusqueda.toLowerCase())) {
                    listaUsuario.add(usuario); // Agregar usuarios que coincidan con la búsqueda
                }
            }
        }

        notifyDataSetChanged(); // Notificar cambios en los datos
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreUsuarioCardViewTitulo;
        private TextView usernameUsuarioCardView;
        private TextView emailUsuarioCardView;

        public UsuarioViewHolder(View itemView) {
            super(itemView);
            nombreUsuarioCardViewTitulo = itemView.findViewById(R.id.nombreUsuarioCardViewTitulo);
            usernameUsuarioCardView = itemView.findViewById(R.id.usernameUsuarioCardView);
            emailUsuarioCardView = itemView.findViewById(R.id.emailUsuarioCardView);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
