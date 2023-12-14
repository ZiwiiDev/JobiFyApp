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
import com.example.jobifyapp.modelo.Categoria;

import java.util.ArrayList;
// -----------------------------------------------------------------------------------------------------------------------------
public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private ArrayList<Categoria> listaCategorias;
    private ArrayList<Categoria> listaOriginal; // Nueva lista para guardar la lista original sin filtrar
    // -----------------------------------------------------------------------------------------------------------------------------
    public CategoriaAdapter(ArrayList<Categoria> listaCategorias) {
        this.listaCategorias = listaCategorias;
        this.listaOriginal = new ArrayList<>(listaCategorias); // Guardar una copia de la lista original
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_categorias, parent, false);
        return new CategoriaViewHolder(view);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Categoria categoria = listaCategorias.get(position);
        holder.nombreCategoriaCardView.setText(categoria.getNombre());
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public int getItemCount() {
        if (listaCategorias != null) {
            return listaCategorias.size();
        } else {
            return 0;
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void agregarCategoria(Categoria categoria) {
        listaCategorias.add(categoria); // Agregar la categoría a la lista actual
        listaOriginal.add(categoria); // Agregar la categoría a la lista original sin filtrar
        notifyItemInserted(listaCategorias.size() - 1); // Notificar la inserción
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NotifyDataSetChanged")
    public void filtrarCategorias(String consultaBusqueda) {
        listaCategorias.clear(); // Limpiar la lista actual

        if (consultaBusqueda.isEmpty()) {
            listaCategorias.addAll(listaOriginal); // Si la consulta de búsqueda está vacía, mostrar todas las categorías
        } else {
            for (Categoria categoria : listaOriginal) {
                if (categoria.getNombre().toLowerCase().contains(consultaBusqueda.toLowerCase())) {
                    listaCategorias.add(categoria); // Agregar categorías que coincidan con la búsqueda
                }
            }
        }

        notifyDataSetChanged(); // Notificar cambios en los datos
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreCategoriaCardView;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreCategoriaCardView = itemView.findViewById(R.id.nombreCategoriaCardView);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
