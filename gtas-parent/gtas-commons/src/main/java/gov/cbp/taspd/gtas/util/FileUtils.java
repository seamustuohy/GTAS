package gov.cbp.taspd.gtas.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static String readSmallTextFile(String filePath, Charset charset) {
        Path path = Paths.get(filePath);
        try {
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes, charset);
        } catch (IOException e) {
          System.out.println(e);
          return null;
        }       
    }
}
