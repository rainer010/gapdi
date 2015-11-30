package py.gapdi;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import py.gapdi.problema.Problema;
import sun.applet.Main;

/**
 * Created by rainer on 12/11/2015.
 */
public class MainTest {
    public static void main(String[] args){
        System.load("C:\\Users\\rainer\\Opencv\\opencv\\build\\java\\x86\\" + Core.NATIVE_LIBRARY_NAME + ".dll");
        Mat a= Problema.crearElementoBasico();

        OpenCVUtil.imprimir(OpenCVUtil.reducirMat(a));

    }
}
