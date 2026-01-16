package com.juanma.geonotes.ui.notes;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.juanma.geonotes.R;
import com.juanma.geonotes.data.Note;
import com.juanma.geonotes.data.NoteRepository;

import java.util.List;

public class NotesListFragment extends Fragment {

    /*
     * callback para avisar cuando el usuario pulsa una nota
     * aqui solo paso la info necesaria para mover el mapa
     */
    public interface OnNoteSelectedListener {
        void onNoteSelected(double lat, double lng, String text);
    }

    private NotesAdapter adapter;
    private TextView tvEmpty;
    private OnNoteSelectedListener listener;

    public NotesListFragment() {
        super(R.layout.fragment_notes_list);
    }

    /*
     * el fragment padre (o la activity) se registra aqui para recibir el click
     */
    public void setListener(OnNoteSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvEmpty = view.findViewById(R.id.tvEmpty);
        RecyclerView rv = view.findViewById(R.id.recyclerNotes);

        /*****
         * recycler normal en vertical
         * y adapter con click para avisar al listener
         *****/
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new NotesAdapter(note -> {
            if (listener != null) {
                listener.onNoteSelected(note.lat, note.lng, note.text);
            }
        });

        rv.setAdapter(adapter);

        // primera carga al entrar
        refresh();
    }

    /*****
     * recarga las notas desde el repositorio y refresca la lista
     * si no hay notas, muestro un texto de "vacio"
     *****/
    public void refresh() {
        if (!isAdded()) return;

        List<Note> notes = NoteRepository.getNotes(requireContext());
        adapter.setData(notes);

        boolean empty = notes == null || notes.isEmpty();
        tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
    }
}
