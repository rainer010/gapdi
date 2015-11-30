package py.gapdi;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by rainer on 26/10/2015.
 */
public class ElemtentoEstructuranteHelper {

    public static Mat createCross(int x, int y) {
        Size s = new Size(y, x);
        return Imgproc.getStructuringElement(Imgproc.CV_SHAPE_CROSS, s);
    }

    public static Mat createElipse(int x, int y) {
        Size s = new Size(y, x);
        return Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, s);
    }

    public static Mat createRecta(int x, int y) {
        Size s = new Size(y, x);
        Mat e=Imgproc.getStructuringElement(Imgproc.MORPH_RECT,s);

        for (int i=1;i<y;i++){
            for (int j=1;j<x;j++){
                e.put(i,j,1);
            }
        }
        return e;
//        Imgproc.morphologyEx();
    }


    public static Mat createDiamond(int lado) {
        return fun(lado,2);
    }

    //lado multiplo de 3
    public static Mat createOctagono(int lado) {

        return fun(lado,3);
    }

    private static Mat fun(int lado,int  div) {

        Size size;
        int nuros1s;
        if(div==2) {
            size = new Size(lado * div + 1, lado * div + 1);
            nuros1s=0;
        }else{
            size = new Size(lado * div, lado * div );
            nuros1s=0;
        }
        Mat ele=Imgproc.getStructuringElement(Imgproc.MORPH_RECT,size);

        for (int i = 0; i < ele.rows(); i++) {

            for (int j = 0; j < ele.cols(); j++) {

                if (j>=lado -nuros1s && j<=lado *(div-1)+nuros1s -(div-2)) {
                    ele.put(i,j,1);
                } else {
                    ele.put(i,j,0);
                }
            }

            if(i< lado){
                nuros1s++;
            }else if (i>=lado && i<= lado*(div-1)-(div-1) && div==3){

            }else if (div==2){
                nuros1s--;
            }else {
                nuros1s--;
            }
        }
        return ele;
    }
}
