package com.juanma.geonotes.ui.notes;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.juanma.geonotes.R;
import com.juanma.geonotes.data.Note;
import com.juanma.geonotes.data.NoteRepository;
import com.juanma.geonotes.widget.NoteCountWidgetProvider;

public class NoteFragment extends Fragment {

    /*
     * esto lo usa la activity para enterarse de que el usuario
     * ha pulsado una nota y quiere que el mapa se mueva a ese punto
     */
    public interface OnNoteSelectedListener {
        void onNoteSelected(double lat, double lng, String text);
    }

    private Button btnToggle;
    private Button btnSave;
    private EditText etNote;

    private LinearLayout layoutCreate;
    private LinearLayout layoutList;

    private NotesListFragment listFragment;

    // cuando es true estamos viendo la lista, cuando es false estamos creando
    private boolean showList = false;

    // aqui guardo la ubicacion que el usuario elige en el mapa
    private double selectedLat = Double.NaN;
    private double selectedLng = Double.NaN;

    private OnNoteSelectedListener listener;

    public NoteFragment() {
        super(R.layout.fragment_note);
    }

    /*****
     * la activity se registra aqui para que cuando pulses una nota
     * yo le pase lat/lng y asi el mapa se centra donde toca
     *****/
    public void setListener(OnNoteSelectedListener listener) {
        this.listener = listener;

        // por si este fragment ya ha creado el hijo, le paso tambien el listener
        if (listFragment != null) {
            listFragment.setListener((lat, lng, text) -> {
                if (this.listener != null) this.listener.onNoteSelected(lat, lng, text);
            });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnToggle = view.findViewById(R.id.btnToggleView);
        btnSave = view.findViewById(R.id.btnSaveNote);
        etNote = view.findViewById(R.id.etNote);

        layoutCreate = view.findViewById(R.id.layoutCreate);
        layoutList = view.findViewById(R.id.layoutList);

        /*****
         * este fragment lleva dentro otro fragment hijo con la lista
         * asi separo la parte de crear nota de la parte de listar
         *****/
        listFragment = new NotesListFragment();
        listFragment.setListener((lat, lng, text) -> {
            if (listener != null) listener.onNoteSelected(lat, lng, text);
        });

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.notesListContainer, listFragment)
                .commit();

        // boton para alternar entre lista y crear
        btnToggle.setOnClickListener(v -> toggleMode());

        // boton para guardar la nota
        btnSave.setOnClickListener(v -> saveNote());

        // estado inicial: crear nota
        applyMode(false);
    }

    private void toggleMode() {
        applyMode(!showList);
    }

    /*****
     * alterna entre modo crear y modo lista
     * cuando entro en lista refresco para que se vea lo ultimo guardado
     *****/
    private void applyMode(boolean toList) {
        showList = toList;

        layoutCreate.setVisibility(showList ? View.GONE : View.VISIBLE);
        layoutList.setVisibility(showList ? View.VISIBLE : View.GONE);

        // dejo el texto fijo porque solo quiero un boton (como pediste)
        btnToggle.setText("ver / crear notas");

        if (showList && listFragment != null) listFragment.refresh();
    }

    /*****
     * guarda la nota usando la ubicacion seleccionada en el mapa
     * si falta texto o ubicacion, aviso con un toast y no guardo nada
     *****/
    private void saveNote() {
        String text = etNote.getText().toString().trim();

        if (text.isEmpty()) {
            Toast.makeText(requireContext(), "escribe una nota", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Double.isNaN(selectedLat) || Double.isNaN(selectedLng)) {
            Toast.makeText(requireContext(),
                    "haz long press en el mapa para elegir ubicacion",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Note note = Note.create(text, selectedLat, selectedLng);
        NoteRepository.addNote(requireContext(), note);

        // actualizo el widget para que cambie el contador y la ultima nota
        NoteCountWidgetProvider.requestWidgetUpdate(requireContext());

        Toast.makeText(requireContext(),
                "nota guardada: " + selectedLat + ", " + selectedLng,
                Toast.LENGTH_SHORT).show();

        etNote.setText("");

        // al guardar me voy a la lista para que se vea la nota recien creada
        applyMode(true);
    }

    /*****
     * la activity llama a esto cuando el usuario elige un punto en el mapa
     * guardo lat/lng y si estoy en modo crear, muestro un aviso sutil
     *****/
    public void setSelectedLocation(double lat, double lng) {
        selectedLat = lat;
        selectedLng = lng;

        if (isAdded() && !showList) {
            Toast.makeText(requireContext(),
                    "ubicacion lista: " + lat + ", " + lng,
                    Toast.LENGTH_SHORT).show();
        }
    }

    // helpers por si la activity quiere forzar un modo u otro
    public void showListMode() {
        applyMode(true);
    }

    public void showCreateMode() {
        applyMode(false);
    }

    // refresco manual de la lista
    public void refreshList() {
        if (listFragment != null) listFragment.refresh();
    }
}
