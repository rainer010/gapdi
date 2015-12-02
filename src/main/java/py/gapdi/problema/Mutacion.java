package py.gapdi.problema;

import org.opencv.core.*;
import org.uma.jmetal.operator.MutationOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by rainer on 17/10/2015.
 */
public class Mutacion implements MutationOperator<SolucionEE> {
    Random randomGenerator = new Random();

    @Override
    public SolucionEE execute(SolucionEE solucionEE) {
        if (randomGenerator.nextInt(2) == 0 && solucionEE.getVariableValue(0).getElemento().size() > 2) {
            mutar2(solucionEE);
        }
        return mutar(solucionEE);
    }

    private SolucionEE mutar(SolucionEE mutar) {
        SolucionEE s = mutar.clone();
        List<Mat> lista = s.getVariableValue(0).getElemento();
        lista.set(randomGenerator.nextInt(lista.size()), Problema.crearElementoBasico());
        s.getVariableValue(0).setElemento(lista);
        return s;
    }

    private SolucionEE mutar2(SolucionEE solucion) {
        SolucionEE r = solucion.clone();

        if (randomGenerator.nextInt(2) == 0) {
            int idx =
                    randomGenerator.nextInt(r.getVariableValue(0).getElemento().size());
            r.getVariableValue(0).getElemento().get(idx).release();
            r.getVariableValue(0).getElemento().remove(idx);
        } else {
            int idx = randomGenerator.nextInt(r.getVariableValue(0).getElemento().size() - 1);

            List<Mat> l = new ArrayList<Mat>();
            l.addAll(r.getVariableValue(0).getElemento().subList(0, idx));
            l.add(Problema.crearElementoBasico());
            l.addAll(r.getVariableValue(0).getElemento().subList(idx + 1,
                    r.getVariableValue(0).getElemento().size() - 1));
            r.getVariableValue(0).setElemento(l);
        }
        return r;
    }

}