package py.gapdi;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GenerationalGeneticAlgorithm;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.fileoutput.SolutionSetOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import py.gapdi.problema.*;

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * Created by rainer on 15/10/2015.
 */
public class App {
    public static void main(String[] args) throws IOException {

        if (args.length < 3) {
            System.out.print("Error en la cantidad de parametros \n");
            System.out.print(
                    "<path lib openCV> <path dir image> <path dir output> <opcional max iter><opcional tam poblac>\n");
        }
        String pathLib = args[0];
        String pathDb = args[1];
        String pathOut = args[2];

        int numeroMaxIteraciones;
        int poblacionIni;
        if (args.length > 3) {
            numeroMaxIteraciones = Integer.valueOf(args[3]);

            poblacionIni = Integer.valueOf(args[4]);
        } else {
            numeroMaxIteraciones = 400;
            poblacionIni = 100;
        }
        System.load(pathLib);
        System.out.print("COMENZAR EJECUCION\n");
        System.out.print("PARAMETROS:\n");
        System.out.print("Imagenes de entradas:" + pathDb + "\n");
        System.out.print("Directorio de salida:" + pathOut + "\n");
        System.out.print("Nro de iteraciones:" + numeroMaxIteraciones + "\n");
        System.out.print("Tamano de la poblacion:" + poblacionIni + "\n");

        excecuteInDir(pathDb, pathOut, numeroMaxIteraciones, poblacionIni);
    }

    private static void excecuteInDir(final String source, final String out, final int numeroMaxIteraciones, final int poblacionIni) throws IOException {

//        BufferedWriter output = null;
//        File archi = new File(out + "/result.txt");
//        output = new BufferedWriter(new FileWriter(archi));

        final File[] files = new File(source).listFiles();
        int idice = files.length - 1;
        while (idice >= 0) {

            if (count < 4) {
                final File file = files[idice];
                idice--;
                Thread thread = new Thread("New Thread") {
                    public void run() {
                        final ImagePlus imp = IJ.openImage(source + file.getName());
                        System.out.print(file.getName() + "\n");
                        try {
                            App.run(imp, out + "/" + file.getName(), numeroMaxIteraciones, poblacionIni);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            updateCount(-1);
                        }
                    }
                };
                updateCount(+1);
                thread.start();
            }
        }
    }


    private static int count = 0;
    private static final Object countLock = new Object();

    public static void updateCount(int num) {
        synchronized (countLock) {
            count=count+num;
        }
    }


    private static void run(ImagePlus imp, String out, int numeroMaxIteraciones, int poblacionIni) throws IOException {

        ImageConverter c = new ImageConverter(imp);
        c.convertToGray8();
        ImageProcessor p = imp.getProcessor();
        p.resize(0, 5);
        SelectionOperator<List<SolucionEE>, SolucionEE> selectionOperator = new BinaryTournamentSelection<SolucionEE>();
        Problema pro = new Problema(p);
        GenerationalGeneticAlgorithm<SolucionEE> algorithm = new GenerationalGeneticAlgorithm<SolucionEE>(pro,
                numeroMaxIteraciones, poblacionIni,
                new Cruzamiento(), new Mutacion(),
                new BinaryTournamentSelection(), new SequentialSolutionListEvaluator());
        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute();

        List<SolucionEE> population = algorithm.getPopulation();
        Collections.sort(population);
        long computingTime = algorithmRunner.getComputingTime();
        new SolutionSetOutput.Printer(population)
                .setSeparator("\t")
                .setFunFileOutputContext(new DefaultFileOutputContext(out + "_FUN.tsv"))
                .setVarFileOutputContext(new DefaultFileOutputContext(out + "_VAR.tsv"))
                .print();
        Collections.sort(pro.getSolucionesInicialies());
        new SolutionSetOutput.Printer(pro.getSolucionesInicialies())
                .setSeparator("\t")
                .setFunFileOutputContext(new DefaultFileOutputContext(out + "_INI.tsv"))
                .print();
        File fout = new File(out + "_C.txt");
        if (!fout.exists()) {
            fout.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        Gen resultado = algorithm.getResult().getVariableValue(0);
        Mat ele = OpenCVUtil.reducirMat(OpenCVUtil.convertToElements(resultado.getElemento()));

        bw.write(algorithm.getResult().getObjective(0) + "  -> " + OpenCVUtil.contraste(pro.getImageMat()));
        bw.newLine();
        bw.close();
        ele.release();
        JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
        JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
        JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

    }
}
