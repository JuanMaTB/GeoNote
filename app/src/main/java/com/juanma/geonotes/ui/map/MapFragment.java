package com.juanma.geonotes.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.juanma.geonotes.R;

public class MapFragment extends Fragment {

    /*
     * interfaz para avisar a la activity cuando el usuario
     * selecciona un punto en el mapa
     */
    public interface OnLocationSelectedListener {
        void onLocationSelected(double lat, double lng);
    }

    private GoogleMap map;
    private Marker selectedMarker;
    private OnLocationSelectedListener listener;

    public MapFragment() {
        super(R.layout.fragment_map);
    }

    /*
     * la activity se registra aqui para recibir la ubicacion seleccionada
     */
    public void setListener(OnLocationSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment smf = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (smf != null) {
            smf.getMapAsync(googleMap -> {
                map = googleMap;

                /*****
                 * activo los controles normales del mapa
                 * zoom, brujula, gestos y boton de mi ubicacion
                 *****/
                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setCompassEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.getUiSettings().setMapToolbarEnabled(true);
                map.getUiSettings().setZoomGesturesEnabled(true);
                map.getUiSettings().setScrollGesturesEnabled(true);
                map.getUiSettings().setRotateGesturesEnabled(true);
                map.getUiSettings().setTiltGesturesEnabled(true);

                enableMyLocationIfPermitted();

                /*****
                 * pulsacion larga sobre el mapa
                 * coloca un marcador y avisa a la activity
                 *****/
                map.setOnMapLongClickListener(latLng -> {
                    setSelectedMarker(latLng);
                    if (listener != null) {
                        listener.onLocationSelected(latLng.latitude, latLng.longitude);
                    }

                    Toast.makeText(requireContext(),
                            "seleccion: " + latLng.latitude + ", " + latLng.longitude,
                            Toast.LENGTH_SHORT).show();
                });

                /*****
                 * posicion inicial del mapa
                 * arranca centrado en madrid
                 *****/
                LatLng madrid = new LatLng(40.4168, -3.7038);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(madrid, 12f));
            });
        }
    }

    /*****
     * activa la capa de mi ubicacion solo si hay permisos
     * asi evitamos crasheos
     *****/
    private void enableMyLocationIfPermitted() {
        if (map == null) return;

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            map.setMyLocationEnabled(true);
        }
    }

    /*****
     * coloca o mueve el marcador seleccionado
     * si ya habia uno, lo quita antes
     *****/
    private void setSelectedMarker(LatLng latLng) {
        if (map == null) return;

        if (selectedMarker != null) selectedMarker.remove();
        selectedMarker = map.addMarker(
                new MarkerOptions().position(latLng).title("nota")
        );
    }

    /*****
     * mueve la camara a unas coordenadas concretas
     * se usa al pulsar una nota desde la lista
     *****/
    public void moveTo(double lat, double lng, boolean withMarker) {
        if (map == null) return;

        LatLng ll = new LatLng(lat, lng);
        if (withMarker) setSelectedMarker(ll);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 16f));
    }
}
