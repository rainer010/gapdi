package multi;

import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

/**
 * Created by rainer on 10/05/2016.
 */
public class SolucionMO implements BinarySolution {

    private BinarySet solucion;
    private int numeroBits;
    private double[] evaluacion;

    public SolucionMO(int numeroBits) {
        this.numeroBits = numeroBits;
        solucion = new BinarySet(numeroBits);
    }

    @Override
    public int getNumberOfBits(int i) {
        return 1;
    }

    @Override
    public int getTotalNumberOfBits() {
        return numeroBits;
    }

    @Override
    public void setObjective(int i, double v) {
        this.evaluacion[i] = v;
    }

    @Override
    public double getObjective(int i) {
        return this.evaluacion[i];
    }

    @Override
    public BinarySet getVariableValue(int i) {
        return solucion;
    }

    @Override
    public void setVariableValue(int i, BinarySet binarySet) {
        this.solucion = binarySet;
    }

    @Override
    public String getVariableValueString(int i) {
        return null;
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
    public Solution<BinarySet> copy() {
        SolucionMO copia = new SolucionMO(this.numeroBits);
        copia.setSolucion((BinarySet) this.solucion.clone());
        return copia;
    }

    @Override
    public void setAttribute(Object o, Object o1) {

    }

    @Override
    public Object getAttribute(Object o) {
        return null;
    }

    public BinarySet getSolucion() {
        return solucion;
    }

    public void setSolucion(BinarySet solucion) {
        this.solucion = solucion;
    }

    public double[] getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(double[] evaluacion) {
        this.evaluacion = evaluacion;
    }


}
