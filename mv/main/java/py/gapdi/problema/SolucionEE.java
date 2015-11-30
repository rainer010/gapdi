package py.gapdi.problema;

import org.opencv.core.Mat;
import org.uma.jmetal.solution.Solution;

/**
 * Created by rainer on 17/10/2015.
 */
public class SolucionEE implements Solution<Gen>, Comparable<SolucionEE> {

    public int id;
    private Gen solucion;
    private double[] fitness = new double[2];


    @Override
    public void setObjective(int i, double v) {
        this.fitness[i] = v;
    }

    @Override
    public double getObjective(int i) {
        if (i > 0) {
            return id;
        }
        return fitness[i];
    }

    @Override
    public Gen getVariableValue(int i) {
        return solucion;
    }

    @Override
    public void setVariableValue(int i, Gen gen) {
        this.solucion = gen;
    }

    @Override
    public String getVariableValueString(int i) {
        String r = ""+solucion.getElemento().size();
//        for (int k = 0; k < solucion.geteEstructurante().rows(); k++) {
//            for (int j = 0; j < solucion.geteEstructurante().cols(); j++) {
//                r = r + ((int) solucion.geteEstructurante().get(k, j)[0]) + " ";
//            }
//            r = r + ";";
//        }
//        r = r + "\n==================================";

        return r;
    }

    @Override
    public int getNumberOfVariables() {
        return 1;
    }

    @Override
    public int getNumberOfObjectives() {
        return 2;
    }

    @Override
    public Solution<Gen> copy() {

        System.out.print("COPY \n");
        return this.clone();

    }

    @Override
    public void setAttribute(Object o, Object o1) {

    }

    @Override
    public Object getAttribute(Object o) {
        return 1.0;
    }


    protected SolucionEE clone() {
        SolucionEE s = new SolucionEE();
        s.id=ids+1;
        ids++;
        s.setObjective(0, this.fitness[0]);
        s.setObjective(1, this.fitness[1]);
        Gen g = new Gen();
        for (Mat m : solucion.getElemento()) {
            g.getElemento().add(m.clone());
        }
        g.setRepeticiones(this.solucion.getRepeticiones());
        s.setVariableValue(0, g);
        return s;
    }

    public static int ids=0;
    @Override
    public int compareTo(SolucionEE o) {
        double l=this.getObjective(0) - o.getObjective(0);
        if(l>0){
            return 1;
        }else if(l<0){
            return -1;
        }

        return 0;
    }
}