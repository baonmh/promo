package promo_creation.utils;

import com.opencsv.CSVWriter;
import promo_creation.objects.Credential;
import promo_creation.objects.DirectPromo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvUtil {


    public static List<DirectPromo> readDirectPromoListFromCsv(String filePath) throws IOException {
        try (Stream<String> linesStream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            System.out.println("readDirectPromoListFromCsv BEGIN");
            return linesStream.skip(1)
                    .map(DirectPromo::new)
                    .collect(Collectors.toList());
        }
    }

    public static List<Credential> readCredentialsFromCsv(String filePath) throws IOException {
        try (Stream<String> linesStream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            System.out.println("readCredentialsFromCsv BEGIN");
            return linesStream.skip(1)
                    .map(Credential::new)
                    .collect(Collectors.toList());
        }
    }

    public static void writeDirectPromoToCsv(String filepath, DirectPromo directPromo) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(filepath, true), ',');
        csvWriter.writeNext(directPromo.toArray());
        System.out.println("writeDirectPromoToCsv END");
        csvWriter.close();
    }


}
