package py.gapdi.problema;

import org.opencv.core.Mat;
import org.uma.jmetal.operator.CrossoverOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by rainer on 17/10/2015.
 */
public class Cruzamiento implements CrossoverOperator<SolucionEE> {
    Random randomGenerator = new Random();

    @Override
    public List<SolucionEE> execute(List<SolucionEE> solucionEEs) {
        return simplePunto(solucionEEs.get(0), solucionEEs.get(1));
    }

    private List<SolucionEE> simplePunto(SolucionEE s1, SolucionEE s2) {
        List<SolucionEE> solusiones = new ArrayList<>();
        SolucionEE clon1 = s1.clone();
        SolucionEE clon2 = s2.clone();
        int tama1 = clon1.getVariableValue(0).getElemento().size();
        int tama2 = clon2.getVariableValue(0).getElemento().size();


        if (tama1 > tama2) {
            int puntoDeCruce = randomGenerator.nextInt(tama2);
            cruce(clon1, clon2, puntoDeCruce);
        }else if(tama1!=1 && tama2!=1){

            int puntoDeCruce = randomGenerator.nextInt(tama1);
            cruce(clon2,clon1,puntoDeCruce);
        }
        solusiones.add(clon1);
        solusiones.add(clon2);
        return solusiones;
    }
    private void cruce(SolucionEE mayor,SolucionEE menor,int puntoCruce) {

        List<Mat> indMa=new ArrayList<>();
        List<Mat> indMe=new ArrayList<>();
        for (int i=0;i<mayor.getVariableValue(0).getElemento().size();i++){
            if(puntoCruce<=i){
                indMe.add(mayor.getVariableValue(0).getElemento().get(i));
            }else{
                indMa.add(mayor.getVariableValue(0).getElemento().get(i));
            }
        }
        for (int i=0;i<menor.getVariableValue(0).getElemento().size();i++){
            if(puntoCruce<i){
                indMe.add(menor.getVariableValue(0).getElemento().get(i));
            }else{
                indMa.add(menor.getVariableValue(0).getElemento().get(i));
            }
        }
        if(indMa.isEmpty() || indMe.isEmpty()){
            System.out.print(" CRUCE: UNO DE LOS DOS INDIVIDUOS VACIO");
        }
        mayor.getVariableValue(0).setElemento(indMa);
        menor.getVariableValue(0).setElemento(indMe);
        return;
    }
}
