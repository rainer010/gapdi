package py.gapdi;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rainer on 21/10/2015.
 */
public class OpenCVUtil {
    enum Operacion {
        DILATACION, EROCION
    }

    public static Mat convertToElements(List<Mat> strels) {
        if(strels.size()==1){
            return strels.get(0);
        }
        List<Mat> list = new ArrayList<Mat>();
        list.addAll(strels);
        int tama = 3 + 4 * (list.size() - 1);
        Mat s = Mat.zeros(tama, tama, CvType.CV_8UC1);
        int center = tama / 2 + 1 - 1;
        Mat primero = list.get(0);
        list.remove(0);
//fila 1
        s.put(center - 1, center - 1, primero.get(0, 0));
        s.put(center , center - 1, primero.get(1, 0));
        s.put(center + 1, center - 1, primero.get(2, 0));
//fila 2
        s.put(center - 1, center, primero.get(0, 1));
        s.put(center, center, primero.get(1, 1));
        s.put(center + 1, center, primero.get(2, 1));
//fila 3
        s.put(center - 1, center + 1, primero.get(2, 2));
        s.put(center, center + 1, primero.get(2, 2));
        s.put(center + 1, center + 1, primero.get(2, 2));

        for (Mat strel : list) {
            Imgproc.dilate(s, s, strel);
        }
        return s;
    }

    public static Mat newMetodo(Mat image, Mat strel, int k) {
        Mat result = image.clone();

        for (int i = 0; i < k; i++) {
            Mat topHat = topHat(image, strel);
            org.opencv.core.Core.add(image, topHat, result);
            topHat.release();
            Mat botHat = bottonHat(image, strel);
            org.opencv.core.Core.subtract(result, botHat, result);
            botHat.release();
        }
        strel.release();
        return result;
    }


    private static Mat topHat(Mat image, Mat srtel) {

        Mat clon = image.clone();
        Imgproc.morphologyEx(clon, clon, Imgproc.MORPH_TOPHAT, srtel);
        return clon;
    }

    private static Mat bottonHat(Mat image, Mat srtel) {
        Mat clon = image.clone();
        Imgproc.morphologyEx(clon, clon, Imgproc.MORPH_CLOSE, srtel);
        org.opencv.core.Core.subtract(clon, image, clon);
        return clon;
    }


    private static Mat open(Mat imagen, Mat strel) {
        Mat result = imagen.clone();
        Imgproc.morphologyEx(imagen, result, Imgproc.MORPH_OPEN, strel);
        return result;
    }

    private static Mat close(Mat imagen, Mat strel) {
        Mat result = imagen.clone();
        Imgproc.morphologyEx(imagen, result, Imgproc.MORPH_CLOSE, strel);
        return result;
    }


    public static Mat getMatByArray(int[] array, int rows, int columns) {

        Mat matObject = new Mat(rows, columns, CvType.CV_8UC1);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                matObject.put(row, col, (int) array[row * columns + col]);
            }
        }
        return matObject;
    }

    public static Mat getMatBinaryByArray(int[] array, int rows, int columns) {

        Mat matObject = new Mat(rows, columns, CvType.CV_8UC1);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                matObject.put(row, col, (int) array[row * columns + col]);
            }
        }
        Mat m = new Mat(rows, columns, CvType.CV_8UC1);
        Core.extractChannel(matObject, m, 0);
        return m;
    }


    public static Mat getMatByArray(int[][] matrix) {

        Size s = new Size();
        s.height = matrix.length;
        s.width = matrix[0].length;

        Mat matObject = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, s);

        for (int row = 0; row < s.height; row++) {
            for (int col = 0; col < s.width; col++)
                matObject.put(col, row, matrix[row][col]);
        }
        return matObject;
    }

    public static double entropy(Mat m) {

        Mat hist = new Mat();
        float range[] = {0, 256};
        List<Mat> lll = new ArrayList<Mat>();
        lll.add(m);
        Imgproc.calcHist(lll, new MatOfInt(0), new Mat(), hist, new MatOfInt(256), new MatOfFloat(range), true);

        double total = m.total();
        double sum = 0.0;
        for (int i = 0; i < hist.rows(); i++) {
            double p = (hist.get(i, 0)[0]) / total;
            Double l = (p * (Math.log(p) / Math.log(2)));
            if (l.isNaN())
                l = 0.0;
            sum = sum + l;
        }
        hist.release();
        return -1.0 * sum;
    }


    public static int[] getArrayByMat(Mat m) {
        int[] result = new int[(int) m.total()];
        int idx = 0;
        System.out.print("GABM");
        for (int i = 1; i < m.rows(); i++) {
            for (int j = 1; j < m.cols(); j++) {
//                System.out.print(" "+m.get(i,j)[0]);
                result[idx] = (int) m.get(i, j)[0];
                idx++;
            }
        }
        return result;
    }


    public static void imprimir(Mat m) {


        System.out.print("" + m.rows() + "x" + m.cols() + "=======================================\n");
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) {
                System.out.print(" " + (int) m.get(i, j)[0]);
            }
            System.out.print(";\n");
        }
        System.out.print("=======================================\n");
    }

    public static double contraste(Mat imagen) {
        Mat hist = new Mat();
        float range[] = {0, 256};
        List<Mat> lll = new ArrayList<Mat>();
        lll.add(imagen);
        Imgproc.calcHist(lll, new MatOfInt(0), new Mat(), hist, new MatOfInt(256), new MatOfFloat(range), true);
        double inten_med = Core.mean(imagen).val[0];
        double total = imagen.total();
        double sum = 0;
        for (int i = 0; i < hist.rows(); i++) {
            double p = (hist.get(i, 0)[0]) / total;
            Double l = Math.pow(i - inten_med, 2) * p;
            if (l.isNaN())
                l = 0.0;
            sum = sum + l;
        }
        hist.release();
        return Math.sqrt(sum);
    }

    public static Mat reducirMat(Mat image) {

        if(image.cols()<=3 || image.rows()<=3){
            return image;
        }


        if(Core.countNonZero(image)<3){
            System.out.print("ERROR    \n\n");
            System.out.print(" "+image.rows()
                            +"X"+image.cols()+"\n"
                            );

        }

        Mat anterior;
        int iniC = -1;
        for (int i = 0; i < image.cols(); i++) {
            if (Core.countNonZero(image.col(i)) > 0) {
                iniC = i;
                break;
            }
        }
        anterior=image.submat(0,image.rows()-1, iniC, image.cols()-1);
        image.release();
        image=anterior.clone();
        anterior.release();
        int finC = -1;
        for (int i = image.cols() - 1; i >= 0; i--) {
            if (Core.countNonZero(image.col(i)) > 0) {
                finC = i;
                break;
            }
        }

        anterior=image.submat(0,image.rows()-1, 0, finC);
        image.release();
        image=anterior.clone();
        anterior.release();
        int iniF = -1;
        for (int i = 0; i < image.rows(); i++) {
            if (Core.countNonZero(image.row(i)) > 0) {
                iniF = i;
                break;
            }
        }


        anterior=image.submat(iniF,image.rows()-1, 0, image.cols()-1);
        image.release();
        image=anterior.clone();
        anterior.release();
        int finF = -1;
        for (int i = image.rows() - 1; i >= 0; i--) {
            if (Core.countNonZero(image.row(i)) > 0) {
                finF = i;
                break;
            }
        }
        anterior=image.submat(0,finF, 0, image.cols()-1);
        image.release();
        return anterior.clone();
    }
}
