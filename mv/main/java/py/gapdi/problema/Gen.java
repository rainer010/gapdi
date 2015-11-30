package py.gapdi.problema;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rainer on 17/10/2015.
 * <p/>
 * Representa un gen de un individuo
 * <p/>
 * Contiene el elemento estructurante y la cantidad de iteraciones que se debe correr el algoritmo
 */
public class Gen {

    //elemento estructurante descompuesto
    private List<Mat> elemento;
    private int repeticiones = 1;

    public List<Mat> getElemento() {

        if(elemento==null){
            elemento=new ArrayList<Mat>();
        }
        return elemento;
    }

    public void setElemento(List<Mat> elemento) {
        this.elemento = elemento;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    class Operacion{
        boolean dilatacion;

        int direccionUnion;
        double posicion;
        int direccionGoteo;


    }
}