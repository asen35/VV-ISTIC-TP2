package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MainForCyclomaticComplexities {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if (!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());
        CyclomaticComplexities printer = new CyclomaticComplexities();

        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));

            saveResultsToCSV(printer.getResultsList(), printer.getCcNames(), printer.getCcValues(), "CyclomaticComplexities.csv");

            return SourceRoot.Callback.Result.DONT_SAVE;
        });
    }

    private static void saveResultsToCSV(List<String> resultsList, List<String> ccNames, List<Integer> ccValues, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // En-tête CSV si nécessaire
            writer.write("Elements\n");

            // Écrire chaque résultat dans le fichier CSV
            for (String result : resultsList) {
                writer.write(result + "\n");
            }

            // Écrire l'histogramme
            writer.write('\n' + generateHistogram(ccNames, ccValues));
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    private static String generateHistogram(List<String> ccNames, List<Integer> ccValues) {
        String histogram = "";

        histogram += "Cyclomatic Complexity Histogram:\n";

        // Generate histogram
        for (int i = 0; i < ccNames.size(); i++) {
            int count;
            if ((count = ccValues.get(i)) != -1) {
                histogram += "CC " + ccNames.get(i) + ": ;" + "*".repeat(count) + '\n';
            } else {
                histogram += ccNames.get(i) + '\n';
            }
        }

        return histogram;
    }
}