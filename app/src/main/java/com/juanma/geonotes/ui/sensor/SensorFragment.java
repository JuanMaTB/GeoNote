package com.juanma.geonotes.ui.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juanma.geonotes.R;

public class SensorFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private CircleView circleView;
    private TextView tvValues;

    // limites del radio para que el circulo no desaparezca ni se salga
    private static final float MIN_RADIUS = 30f;
    private static final float MAX_RADIUS = 250f;

    public SensorFragment() {}

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_sensor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        circleView = view.findViewById(R.id.circleView);
        tvValues = view.findViewById(R.id.tvValues);

        /*****
         * preparo el sensor manager y el acelerometro
         * no empiezo a escuchar aun, eso lo hago en onStart
         *****/
        sensorManager = (SensorManager)
                requireContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onStart() {
        super.onStart();

        /*****
         * empiezo a escuchar el sensor cuando el fragment esta visible
         * asi ahorro recursos y sigo el ciclo de vida correcto
         *****/
        if (sensorManager != null && accelerometer != null) {
            sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_UI
            );
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        /*****
         * dejo de escuchar el sensor cuando salgo del fragment
         * muy importante para no consumir bateria
         *****/
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) return;
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // muestro los valores actuales del acelerometro
        tvValues.setText(String.format("x=%.2f y=%.2f z=%.2f", x, y, z));

        /*****
         * calculo una inclinacion sencilla usando x e y
         * no busco precision fisica, solo un efecto visual claro
         *****/
        float tilt = (float) Math.sqrt((x * x) + (y * y));

        // normalizo el valor a un rango 0..1
        float t = tilt / 10f;
        if (t < 0f) t = 0f;
        if (t > 1f) t = 1f;

        // convierto esa inclinacion en un radio visible
        float radius = MIN_RADIUS + (MAX_RADIUS - MIN_RADIUS) * t;

        if (circleView != null) {
            circleView.setRadiusPx(radius);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // no lo necesito para este ejercicio
    }
}
