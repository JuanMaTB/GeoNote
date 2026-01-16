package com.juanma.geonotes.data;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NoteRepository {

    /*
     * aqui guardo todo en sharedpreferences como un json
     * es simple, rapido y para este proyecto me sobra
     */
    private static final String PREFS = "geonotes_prefs";
    private static final String KEY_NOTES = "notes_json";

    /*****
     * mete una nota nueva y la pone la primera de la lista
     * asi la mas reciente siempre sale arriba (y el widget la pilla facil)
     *****/
    public static void addNote(Context context, Note note) {
        List<Note> notes = getNotes(context);
        notes.add(0, note); // newest first
        saveNotes(context, notes);
    }

    /*****
     * lee el json guardado y lo convierte en una lista de Note
     * si el json esta roto o no existe, devuelve lista vacia
     *****/
    public static List<Note> getNotes(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String raw = sp.getString(KEY_NOTES, "[]");

        List<Note> out = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(raw);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                long id = o.optLong("id");
                String text = o.optString("text");
                double lat = o.optDouble("lat");
                double lng = o.optDouble("lng");
                long createdAt = o.optLong("createdAt");
                out.add(new Note(id, text, lat, lng, createdAt));
            }
        } catch (JSONException e) {
            // si esto falla, prefiero no romper nada: devuelvo vacio y listo
        }
        return out;
    }

    /*****
     * numero total de notas guardadas
     * lo uso sobre todo para el widget
     *****/
    public static int getCount(Context context) {
        return getNotes(context).size();
    }

    /*****
     * texto de la ultima nota (la mas reciente)
     * si no hay notas o no hay texto, devuelvo un mensaje simple
     * y recorto para que en el widget no se vea un tocho
     *****/
    public static String getLastNoteText(Context context) {
        List<Note> notes = getNotes(context);
        if (notes == null || notes.isEmpty()) return "(sin notas)";
        String t = notes.get(0).text == null ? "" : notes.get(0).text.trim();
        if (t.isEmpty()) return "(sin texto)";
        if (t.length() > 22) t = t.substring(0, 22) + "...";
        return t;
    }

    /*****
     * guarda la lista completa convirtiendola a json
     * para mi esto es el "persistir datos" del proyecto
     *****/
    private static void saveNotes(Context context, List<Note> notes) {
        JSONArray arr = new JSONArray();
        for (Note n : notes) {
            JSONObject o = new JSONObject();
            try {
                o.put("id", n.id);
                o.put("text", n.text);
                o.put("lat", n.lat);
                o.put("lng", n.lng);
                o.put("createdAt", n.createdAt);
                arr.put(o);
            } catch (JSONException ignored) {}
        }

        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_NOTES, arr.toString()).apply();
    }
}
