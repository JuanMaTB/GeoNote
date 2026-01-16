package com.juanma.geonotes.ui.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.juanma.geonotes.R;
import com.juanma.geonotes.data.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.VH> {

    /*
     * callback simple para avisar cuando el usuario pulsa una nota
     */
    public interface OnItemClick {
        void onClick(Note note);
    }

    // lista interna de notas que se muestran
    private final List<Note> data = new ArrayList<>();

    // listener que se ejecuta al pulsar una nota
    private final OnItemClick onItemClick;

    public NotesAdapter(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    /*****
     * reemplaza los datos actuales y refresca la lista
     * se llama cada vez que se entra en el modo lista
     *****/
    public void setData(List<Note> notes) {
        data.clear();
        if (notes != null) data.addAll(notes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Note n = data.get(position);

        // texto de la nota
        holder.tvText.setText(n.text);

        // coordenadas asociadas a la nota
        holder.tvCoords.setText(n.lat + ", " + n.lng);

        /*****
         * al pulsar una nota aviso al fragment
         * para que el mapa se mueva a esa ubicacion
         *****/
        holder.itemView.setOnClickListener(v -> {
            if (onItemClick != null) onItemClick.onClick(n);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /*
     * viewholder simple con el texto y las coordenadas
     */
    static class VH extends RecyclerView.ViewHolder {

        TextView tvText;
        TextView tvCoords;

        VH(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvNoteText);
            tvCoords = itemView.findViewById(R.id.tvNoteCoords);
        }
    }
}
