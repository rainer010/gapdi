package py.gapdi.problema;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.uma.jmetal.problem.Problem;
import py.gapdi.OpenCVUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by rainer on 15/10/2015.
 */
public class Problema implements Problem<SolucionEE> {

    private Mat imageMat;
    private List<SolucionEE> solucionesInicialies = new ArrayList<SolucionEE>();

    public List<SolucionEE> getSolucionesInicialies() {
        return solucionesInicialies;
    }

    public void setSolucionesInicialies(List<SolucionEE> solucionesInicialies) {
        this.solucionesInicialies = solucionesInicialies;
    }

    public Problema(Mat imagen) {
        imageMat = imagen;
    }

    @Override
    public int getNumberOfVariables() {
        return 1;
    }

    @Override
    public int getNumberOfObjectives() {
        return 1;
    }

    @Override
    public int getNumberOfConstraints() {
        return 0;
    }

    @Override
    public String getName() {
        return "CONTRASTE";
    }

    @Override
    public void evaluate(SolucionEE solucionEE) {
//        long inico = System.currentTimeMillis();
        obtenerMejorSolucion(solucionEE, 15);
//        System.out.print(
//                " Val: " + solucionEE.getObjective(0) + "  tiempo:" + (System.currentTimeMillis() - inico) + " \n");

    }

    private void obtenerMejorSolucion(SolucionEE s, int maxIteraciones) {
        Gen e = s.getVariableValue(0);
        double max = 99999999;
        Mat ele = OpenCVUtil.reducirMat(OpenCVUtil.convertToElements(e.getElemento()));
        for (int i = 1; i < maxIteraciones; i++) {
            Mat result = OpenCVUtil.newMetodo(imageMat, ele, i);
            double l = 255 - OpenCVUtil.contraste(result);
            if (l < max) {
                s.setObjective(0, l);
                e.setRepeticiones(i);
                max = l;
            }
            result.release();
        }
        ele.release();
    }

    static Random randomGenerator = new Random();

    @Override
    public SolucionEE createSolution() {
        int tamano = randomGenerator.nextInt(100) + 1;
        SolucionEE s = new SolucionEE();
        s.id = SolucionEE.ids + 1;
        SolucionEE.ids++;
        s.setVariableValue(0, new Gen());
        for (int i = 0; i < tamano; i++) {
            s.getVariableValue(0).getElemento().add(crearElementoBasico());
        }
        solucionesInicialies.add(s);
        return s;
    }

    public static Mat crearElementoBasico() {
        Mat m = Mat.zeros(3, 3, CvType.CV_8UC1);

        //centro
        m.put(1, 1, 1);
        //demas
        //f1
        m.put(0, 0, randomGenerator.nextInt(2));
        m.put(0, 1, randomGenerator.nextInt(2));
        m.put(0, 2, randomGenerator.nextInt(2));
        //f2
        m.put(1, 0, randomGenerator.nextInt(2));
        m.put(1, 2, randomGenerator.nextInt(2));
        //f3
        m.put(2, 0, randomGenerator.nextInt(2));
        m.put(2, 1, randomGenerator.nextInt(2));
        m.put(2, 2, randomGenerator.nextInt(2));

//        System.out.print(" "+m.get(0,0)[0]+" "+m.get(0,1)[0]+" "+m.get(0,2)[0]+"\n");
//        System.out.print(" "+m.get(1,0)[0]+" "+m.get(1,1)[0]+" "+m.get(1,2)[0]+"\n");
//        System.out.print(" "+m.get(2,0)[0]+" "+m.get(2,1)[0]+" "+m.get(2,2)[0]+"\n");
//        System.out.print("-----------------------\n");
        return m;
    }

    public Mat getImageMat() {
        return imageMat;
    }

    public void setImageMat(Mat imageMat) {
        this.imageMat = imageMat;
    }
}
