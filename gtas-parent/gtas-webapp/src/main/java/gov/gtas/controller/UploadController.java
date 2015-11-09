package gov.gtas.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import gov.gtas.repository.ApisMessageRepository;
import gov.gtas.repository.PnrRepository;

@Controller
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    /** root dir by default is catalina_home */
    private static String INDIR = "gtas_in/";

    @Autowired
    private PnrRepository pnrMessageDao;

    @Autowired
    private ApisMessageRepository apisMessageDao;

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

        logger.info(String.format("received %s from %s", file.getOriginalFilename(), username));
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/deleteall")
    public void wipeAllMessages() {
        logger.info("DELETE ALL MESSAGES");
        pnrMessageDao.deleteAll();
        apisMessageDao.deleteAll();
    }
}