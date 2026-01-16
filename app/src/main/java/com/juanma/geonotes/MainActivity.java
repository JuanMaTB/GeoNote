package com.juanma.geonotes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.juanma.geonotes.ui.map.MapFragment;
import com.juanma.geonotes.ui.notes.NoteFragment;

public class MainActivity extends AppCompatActivity
        implements MapFragment.OnLocationSelectedListener,
        NoteFragment.OnNoteSelectedListener {

    private MapFragment mapFragment;
    private NoteFragment noteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*****
         * creo los dos fragments principales
         * mapa a la izquierda y notas a la derecha
         *****/
        mapFragment = new MapFragment();
        noteFragment = new NoteFragment();

        // registro la activity como listener de ambos
        mapFragment.setListener(this);
        noteFragment.setListener(this);

        /*****
         * coloco los fragments en sus contenedores
         *****/
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_map, mapFragment)
                .replace(R.id.container_right, noteFragment)
                .commit();
    }

    /*****
     * el mapa llama a esto cuando haces long press
     * guardo la ubicacion en el fragment de notas
     * y te dejo directamente en modo crear
     *****/
    @Override
    public void onLocationSelected(double lat, double lng) {
        if (noteFragment != null) {
            noteFragment.setSelectedLocation(lat, lng);
            noteFragment.showCreateMode();
        }
    }

    /*****
     * la lista de notas llama a esto cuando pulsas una
     * muevo el mapa a esa ubicacion y pongo el marcador
     *****/
    @Override
    public void onNoteSelected(double lat, double lng, String text) {
        if (mapFragment != null) {
            mapFragment.moveTo(lat, lng, true);
        }
    }
}
