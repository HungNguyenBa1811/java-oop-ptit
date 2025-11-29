package main.java.utils;

import javafx.stage.FileChooser;
import java.io.File;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {
    public static File chooseOpenCsv(Object source, String title) {
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle(title == null ? "Open CSV" : title);
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            return fc.showOpenDialog(GenericUtils.getStageFromSource(source));
        } catch (Exception e) {
            return null;
        }
    }

    public static File chooseSaveCsv(Object source, String initialFileName, String title) {
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle(title == null ? "Save CSV" : title);
            if (initialFileName != null) fc.setInitialFileName(initialFileName);
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            return fc.showSaveDialog(GenericUtils.getStageFromSource(source));
        } catch (Exception e) {
            return null;
        }
    }

    public static List<String> readLines(File f) throws Exception {
        if (f == null) return new ArrayList<>();
        return Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
    }

    public static void writeCsv(File target, String[] header, List<String[]> rows) throws Exception {
        if (target == null) return;
        try (BufferedWriter bw = Files.newBufferedWriter(target.toPath(), StandardCharsets.UTF_8)) {
            if (header != null) {
                bw.write(String.join(";", header));
                bw.newLine();
            }
            if (rows != null) {
                for (String[] r : rows) {
                    String[] esc = new String[r.length];
                    for (int i = 0; i < r.length; i++) esc[i] = escape(r[i]);
                    bw.write(String.join(";", esc));
                    bw.newLine();
                }
            }
        }
    }

    public static String escape(String v) {
        if (v == null) return "";
        return v.replace(";", ",");
    }

    public static String[] parseLine(String line) {
        if (line == null) return new String[0];
        return line.split(";", -1);
    }
}
