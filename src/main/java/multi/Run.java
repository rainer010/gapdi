package multi;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

/**
 * Created by rainer on 10/05/2016.
 */
public class Run {
    public static final double P_CRUZAMIENTO = 0.75;
    public static final double P_MUTACION = 0.025;
    public static SinglePointCrossover crozz = new SinglePointCrossover(P_CRUZAMIENTO);
    public static BitFlipMutation mutation = new BitFlipMutation(P_MUTACION);

    public static PrintWriter reporteTotal;

    public static void main(String[] args) throws IOException {
        Mat imagen = Imgcodecs.imread("db2/000016.jpg", Imgcodecs.IMREAD_GRAYSCALE);
        // definir el problema, recibe como parametro una imagen new Mat()
        ProblemaSEBinarioMO pro = new ProblemaSEBinarioMO(imagen);
        int numeroMaxIteraciones=100;
        int tamanhoPoblacion=40;
        NSGAII<BinarySolution> multi = new NSGAII<BinarySolution>(pro,
                numeroMaxIteraciones, tamanhoPoblacion, crozz, mutation,
                new org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection<BinarySolution>(),
                new SequentialSolutionListEvaluator());

        AlgorithmRunner algorithmRunner = (new AlgorithmRunner.Executor(multi)).execute();
        List population = (List) multi.getResult();


    }


}

