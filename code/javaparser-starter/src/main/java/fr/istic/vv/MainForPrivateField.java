package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MainForPrivateField {

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
        PrivateFieldWithoutGetterPrinter printer = new PrivateFieldWithoutGetterPrinter();

        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));

            saveResultsToCSV(printer.getResultsList(), "PrivateFieldWithoutGetterResult.csv");

            return SourceRoot.Callback.Result.DONT_SAVE;
        });
    }


    private static void saveResultsToCSV(List<String> resultsList, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // En-tête CSV si nécessaire
            writer.write("Elements\n");

            // Écrire chaque résultat dans le fichier CSV
            for (String result : resultsList) {
                writer.write(result + "\n");
            }

            System.out.println("Results saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }


}
