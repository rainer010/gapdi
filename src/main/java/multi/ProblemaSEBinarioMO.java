package multi;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.uma.jmetal.problem.impl.AbstractBinaryProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.binarySet.BinarySet;
import py.gapdi.OpenCVUtil;
import py.gapdi.helper.HOpencv;
import pynw.Solucion;

import java.util.Random;


public class ProblemaSEBinarioMO extends AbstractBinaryProblem {

    static Random randomGenerator = new Random();

    private Mat imagen;
    //filas numero de filas en el cuadrante nro impar
    private int dimF = 11;
    //columnas numero de columnas del cuadrante nro impar
    private int dimC = 11;
    private double CS;



    public ProblemaSEBinarioMO(Mat imagen) {
        this.imagen = imagen;
        CS=OpenCVUtil.contraste(imagen);


    }

    int sol = 0;

    @Override
    public Solucion createSolution() {
        Solucion s = new Solucion(dimC * dimF - 1);
        for (int i = 0; i < dimC * dimF - 1; i++) {
            if (sol == 0) {
                s.getSolucion().set(i, false);
            }else if(sol==1){
                s.getSolucion().set(i,true);
            }else{
                s.getSolucion().set(i, randomGenerator.nextBoolean());
            }
        }
        sol++;
        return s;
    }

    //
    public Mat convertToMat(BinarySet m) {
        Mat SE = new Mat(dimF * 2 - 1, dimC * 2 - 1, CvType.CV_8UC1);
        SE.put(dimF - 1, dimC - 1, 1);
        String s = "";
        int total=m.getBinarySetLength()-1;
        for (int i = 0; i < m.getBinarySetLength(); i++) {
            asignarValorEnPosicion(m.get(i) ? 1 : 0, i, SE);
            s = s + (m.get(total-i) ? "1" : "0");
        }
        return SE;
    }

    //
    public void asignarValorEnPosicion(int valor, long posicion, Mat SE) {

        int c = SE.cols() / 2 + 1;
        int fil = (int) (posicion / c);
        int col = (int) (posicion % c);
        SE.put(fil, col, valor);
        SE.put(SE.rows() - 1 - fil, col, valor);
        SE.put(fil, SE.cols() - 1 - col, valor);
        SE.put(SE.rows() - 1 - fil, SE.cols() - 1 - col, valor);
    }


    @Override
    protected int getBitsPerVariable(int i) {
        return 0;
    }

    @Override
    public void evaluate(BinarySolution solucion) {
        evaluate((Solucion) solucion);
    }


    public void evaluate(Solucion solucion) {

        Mat se = convertToMat(solucion.getSolucion());
        Mat act = null;
        try {
            act = HOpencv.newMetodo_Binario_OCV(imagen, se);
        } catch (Exception e) {
            System.out.print("ERROR " + e + "\n");
        }
        double[] e = OpenCVUtil.calcularMetrica(imagen, act,CS);
        act.release();

        solucion.setObjective(0, e[1]);
        solucion.setObjective(1, e[2]);
        solucion.setEvaluacion(e);
    }
}
