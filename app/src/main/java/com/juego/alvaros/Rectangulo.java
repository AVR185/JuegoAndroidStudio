package com.juego.alvaros;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Clase base de la que heredan todos los tipos de rectangulos
 */


public abstract class Rectangulo {
    public int coordenada_x,coordenada_y;

    /*Convencion de la direccion
        Eje x Derecha   1 Izquerda  1
        Eje y Abajo     1 Arriba    1
        En ambos ejes 0 indica estatico
     */
    public int direccion_x;
    public int direccion_y = 1;

    public int velocidad =10;

    //Ancho de pantalla de prueba
    public int anchoPantalla =500;
    public int altoPantalla =1200;

    public Rectangulo(){
        CalcularCoordenadas();
    }
    //Determina la posicion x en la que aparece
    public void CalcularCoordenadas(){
        double x;
        x = Math.random();
        coordenada_x =(int) Math.floor(anchoPantalla*x);
    }


    public void ActualizarCoordenadas(){
        coordenada_x+= velocidad*direccion_x;
        coordenada_y+= velocidad*direccion_y;
    }
    //Metodo necesita el primer argumento
   /* public void Dibujar(Canvas c, Paint p){
        c.drawBitmap(,coordenada_x,coordenada_y,p);
    }*/

    public int getCoordenada_x() {
        return coordenada_x;
    }

    public void setCoordenada_x(int coordenada_x) {
        this.coordenada_x = coordenada_x;
    }

    public int getCoordenada_y() {
        return coordenada_y;
    }

    public void setCoordenada_y(int coordenada_y) {
        this.coordenada_y = coordenada_y;
    }

    public int getDireccion_x() {
        return direccion_x;
    }

    public void setDireccion_x(int direccion_x) {
        this.direccion_x = direccion_x;
    }

    public int getDireccion_y() {
        return direccion_y;
    }

    public void setDireccion_y(int direccion_y) {
        this.direccion_y = direccion_y;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

}
