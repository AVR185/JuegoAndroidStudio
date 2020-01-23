package com.juego.alvaros.bloques;

import com.juego.alvaros.Juego;

/**Rectangulo que aparece en la parte superior de la pantalla en uan posicion aleatora en x
 * y se mueve hacia la parte inferior de la pantalla a velocidad constante
 * desplazandose por el eje x y rebotando al llegar a un borde de la pantalla
 */
public class RectanguloX extends Rectangulo {

    public RectanguloX(Juego j){
        super(j);
        setDireccion_x((Math.random()>=0.5)? 1:-1);
    }

    public void ActualizarCoordenadas(){
        super.ActualizarCoordenadas();
        if (coordenada_x <=0 ||coordenada_x >= anchoPantalla -140){
            direccion_x =-direccion_x;
        }
    }
}