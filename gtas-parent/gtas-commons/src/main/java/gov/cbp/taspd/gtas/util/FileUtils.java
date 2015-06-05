package gov.cbp.taspd.gtas.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static byte[] readSmallFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
          System.out.println(e);
          return null;
        }       
    }
}
