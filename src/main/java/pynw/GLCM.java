package pynw;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import py.gapdi.OpenCVUtil;

/**
 * Created by rainer on 17/05/2016.
 */
public class GLCM {

    public static void main(String[] args) {
        System.loadLibrary("opencv_java300");
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir using System:" + currentDir);
        Mat s = Imgcodecs.imread("db2/mamo.jpg", Imgcodecs.IMREAD_GRAYSCALE);

//        int[] diff = {0, 2};
//
//        Mat r=calcularGLCM(s, diff, 8);
//        Mat des=r.clone();
//        Core.normalize(r,des);
//        OpenCVUtil.imprimir(des);
        System.out.print("  "+calcularContraste(s));
    }

    public static Mat calcularGLCM(Mat m, int[] dxdy, int level) {

        Mat reducida = m.clone();
        Mat cooccurrence = Mat.zeros(level, level, CvType.CV_32FC1);
        for (int r = 0; r < m.rows(); r++) {
            for (int c = 0; c < m.cols(); c++) {
                if ((r + dxdy[0]) >= 0 && (r + dxdy[0]) < m.rows() && (c + dxdy[1]) >= 0 && (c + dxdy[1]) < m.cols()) {

                    double pactual=reducida.get(r, c)[0];
                    double pvecino=reducida.get(r + dxdy[0], c + dxdy[1])[0];

                    int value1 = (int) ((pactual * level) / 255);
                    int value2 = (int) ((pvecino * level) / 255);
                    double []d=cooccurrence.get(value1, value2);
                    if(d!=null) {
                        cooccurrence.put(value1, value2, d[0] + 1);
                    }
                }
            }
        }
        return cooccurrence;
    }

    public static double calcularContraste(Mat imagen){
        int[] diff = {0, 1};
        Mat cco=calcularGLCM(imagen,diff,8);
        Mat con=cco.clone();
        double total=Core.sumElems(cco).val[0];
        Core.divide(cco,Core.sumElems(cco),con);
//        OpenCVUtil.imprimir(con);
        double contraste=0.0;
        for (int r = 0; r < con.rows(); r++) {
            for (int c = 0; c < con.cols(); c++) {
//                System.out.print("  "+con.get(r,c)[0]+" \n");
                contraste=contraste+((con.get(r,c)[0])*((r-c)*(r-c)));
//                System.out.print("  "+(con.get(r,c)[0]*((r-c)*(r-c)))+"\n");
            }
        }
        return contraste/((8-1)^2);
    }
}
