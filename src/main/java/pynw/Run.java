package pynw;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GenerationalGeneticAlgorithm;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import py.gapdi.helper.HOpencv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 * Created by rainer on 10/05/2016.
 */
public class Run {
    public static final double P_CRUZAMIENTO = 0.75;
    public static final double P_MUTACION = 0.025;
    public static SinglePointCrossover crozz = new SinglePointCrossover(P_CRUZAMIENTO);
    public static BitFlipMutation mutation = new BitFlipMutation(P_MUTACION);


    public static void main(String[] args) throws IOException {
        System.loadLibrary("opencv_java300");
        for (int i = 2; i <= 2; i++) {
            excecuteInDir("db2/", "result/" + "test/" + i, 500*40, 40, 11, 11);
        }
    }

    private static void excecuteInDir(final String source, final String out,
                                      final int numeroMaxIteraciones, final int poblacionTamanho,
                                      final int fila, final int columna) throws IOException {

        System.out.print("Nro de iteraciones:" + numeroMaxIteraciones + "\n");
        System.out.print("Tamano de la poblacion:" + poblacionTamanho + "\n");

        final File[] files = new File(source).listFiles();
        int idice = files.length - 1;

        while (idice >= 0) {
            final File file = files[idice];
            idice--;
            System.out.print(file.getName() + "\n");
            Mat imagen = Imgcodecs.imread(source + file.getName(), Imgcodecs.IMREAD_GRAYSCALE);
            imagen.reshape(256,256);
            Mat rz=new Mat();
            Imgproc.resize(imagen,rz,new Size(256,256));
            rz.convertTo(rz,CvType.CV_8UC1);
//            Imgcodecs.imwrite(out + "/src/"+file.getName(),rz);
            run(rz, out,
                    numeroMaxIteraciones, poblacionTamanho, fila, columna, file.getName());
        }

    }

    private static void run(Mat imp, final String out,
                            int numeroMaxIteraciones,
                            final int tamanhoPoblacion,
                            int fila, int columna, final String filename) throws IOException {

        ProblemaSEBinario pro = new ProblemaSEBinario(imp);
        File directori = new File(out + "/");
        directori.mkdirs();

        final Calendar tiempo_ini = Calendar.getInstance();
        final long tiempoEnMinutos = 2;
        File salidaF = new File(out + "/" + filename + ".txt");
        if (!salidaF.exists()) {
            salidaF.createNewFile();
        }
        final FileWriter fw = new FileWriter(salidaF, true);
        final PrintWriter pw = new PrintWriter(fw);
        pw.print("Fitness\tSSIM\tCONSTRASTE\n");
        GenerationalGeneticAlgorithm<BinarySolution> algorithm = new GenerationalGeneticAlgorithm<BinarySolution>(pro,
                numeroMaxIteraciones, tamanhoPoblacion, crozz, mutation,
                new org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection<BinarySolution>(),
                new SequentialSolutionListEvaluator()) {

            @Override
            public void updateProgress() {
                super.updateProgress();
                Solucion s = (Solucion) this.getResult();
                String rS = "" + s.getObjective(0) + "\t" + s.getEvaluacion()[1] +
                        "\t" + s.getEvaluacion()[2] + "\n";
                rS = rS.replace(".", ",");
                pw.print(rS);
            }


        };


        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute();

        pw.close();
        Solucion s = (Solucion) algorithm.getResult();

        Mat se = pro.convertToMat(s.getSolucion());
        Imgcodecs.imwrite(out + "/" + filename, HOpencv.newMetodo_Binario_OCV(imp, se));

        se.convertTo(se, 0, 255.0);
        new File(out + "/se/").mkdirs();
        Imgcodecs.imwrite(out + "/se/" + filename + "F.png", se);
        imp.release();

    }


}

