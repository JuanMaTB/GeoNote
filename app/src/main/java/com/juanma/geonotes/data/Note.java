package com.juanma.geonotes.data;

/*
 * esta clase representa una nota con ubicacion en el mapa
 * guarda el texto, las coordenadas y el momento en el que se creo
 */
public class Note {

    public long id;
    public String text;
    public double lat;
    public double lng;
    public long createdAt;

    /*****
     * constructor principal
     * se usa cuando ya se conocen todos los datos de la nota
     *****/
    public Note(long id, String text, double lat, double lng, long createdAt) {
        this.id = id;
        this.text = text;
        this.lat = lat;
        this.lng = lng;
        this.createdAt = createdAt;
    }

    /*****
     * metodo de ayuda para crear una nota rapidamente
     * genera el id y la fecha usando el tiempo actual
     *****/
    public static Note create(String text, double lat, double lng) {
        long now = System.currentTimeMillis();
        return new Note(now, text, lat, lng, now);
    }
}
