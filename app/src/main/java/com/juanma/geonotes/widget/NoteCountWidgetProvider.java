package com.juanma.geonotes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.juanma.geonotes.MainActivity;
import com.juanma.geonotes.R;
import com.juanma.geonotes.data.NoteRepository;

public class NoteCountWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context,
                         AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        /*****
         * este metodo se llama cuando el sistema actualiza el widget
         * aqui cargo los datos y actualizo la vista
         *****/
        for (int appWidgetId : appWidgetIds) {

            int count = NoteRepository.getCount(context);
            String last = NoteRepository.getLastNoteText(context);

            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.widget_note_count
            );

            // contador total de notas
            views.setTextViewText(
                    R.id.tvWidgetCount,
                    String.valueOf(count)
            );

            // texto de la ultima nota guardada
            views.setTextViewText(
                    R.id.tvWidgetLast,
                    "ultima GeoNota: " + last
            );

            /*****
             * al pulsar el widget se abre la aplicacion
             *****/
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                            | PendingIntent.FLAG_IMMUTABLE
            );

            views.setOnClickPendingIntent(R.id.widgetRoot, pi);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    /*****
     * llamada manual para forzar la actualizacion del widget
     * se usa justo despues de guardar una nota nueva
     *****/
    public static void requestWidgetUpdate(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName cn =
                new ComponentName(context, NoteCountWidgetProvider.class);

        int[] ids = manager.getAppWidgetIds(cn);

        if (ids != null && ids.length > 0) {
            new NoteCountWidgetProvider()
                    .onUpdate(context, manager, ids);
        }
    }
}
