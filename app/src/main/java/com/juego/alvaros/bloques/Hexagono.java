package com.juego.alvaros.bloques;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.juego.alvaros.Juego;

public class Hexagono extends Rectangulo {

    public Hexagono(Juego j) {
        super(j);
        this.setVelocidad(20);
        this.setDireccion_y((Math.random()>=0.5)? 1:-1);
    }

    //Metodo necesita el primer argumento
    @Override
    public void Dibujar(Canvas c, Paint p){
        c.drawBitmap(Juego.getHexagono(),coordenada_x,coordenada_y,p);
    }

    public void ActualizarCoordenadas(){
        super.ActualizarCoordenadas();
        if (coordenada_y <=0 ||coordenada_y >= altoPantalla -30){
            direccion_y =-direccion_y;
        }
    }

    public void CalcularCoordenadas(){
        super.ActualizarCoordenadas();
        double y;
        y = Math.random();
        if(y>=0.5){ //sale por la izquierda
            this.coordenada_x = 0;
            this.coordenada_y = (int) Math.floor((altoPantalla-30)*y);

        } else { //sale por la derecha
            this.coordenada_x = anchoPantalla-30;
            this.direccion_x = -1;
            this.coordenada_y = (int) Math.floor((altoPantalla-30)*y);
        }

    }
}
