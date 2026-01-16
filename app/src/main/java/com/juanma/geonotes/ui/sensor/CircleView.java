package com.juanma.geonotes.ui.sensor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CircleView extends View {

    // paint basico para dibujar el circulo
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // radio actual del circulo en pixeles
    private float radiusPx = 80f;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /*****
     * configuracion inicial del paint
     * color fijo para que se vea claro el dibujo
     *****/
    private void init() {
        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(255, 60, 140, 220);
    }

    /*****
     * cambia el radio del circulo
     * y fuerza que la vista se vuelva a dibujar
     *****/
    public void setRadiusPx(float radiusPx) {
        this.radiusPx = radiusPx;
        invalidate(); // pide a android que llame otra vez a onDraw
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // centro de la vista
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;

        // dibujo del circulo
        canvas.drawCircle(cx, cy, radiusPx, paint);
    }
}
