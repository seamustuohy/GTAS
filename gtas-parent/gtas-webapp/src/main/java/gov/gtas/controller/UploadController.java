package gov.gtas.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {
    /** root dir by default is catalina_home */
    private static String INDIR = "gtas_in/";

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/upload")
    public void upload(@RequestParam("file") MultipartFile file, @RequestParam("username") String username ) throws IOException {
        byte[] bytes;
        FileOutputStream output = null;
        try {
            if (!file.isEmpty()) {
                 bytes = file.getBytes();
                 String filename = INDIR + file.getOriginalFilename();
                 output = new FileOutputStream(new File(filename));
                 IOUtils.write(bytes, output);
            }
        } finally {
            if (output != null) {
                output.close();
            }
        }

        System.out.println(String.format("receive %s from %s", file.getOriginalFilename(), username));
    }

}