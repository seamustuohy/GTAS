package gov.gtas.services;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.gtas.error.ErrorUtils;
import gov.gtas.model.Message;
import gov.gtas.parsers.util.FileUtils;
import gov.gtas.parsers.util.ParseUtils;
import gov.gtas.repository.MessageRepository;
import gov.gtas.vo.MessageVo;

@Service
public class Loader {    
    @Autowired
    private MessageRepository<Message> msgDao;

    @Autowired
    private ApisMessageService apisLoader;
    
    @Autowired
    private PnrMessageService pnrLoader;

    public void processMessage(File f) {
        String filePath = f.getAbsolutePath();

        if (exceedsMaxSize(f)) {
            amitError("exceeds max file size");
            return;
        }

        String text = null;
        MessageService svc = null;
        try {
            byte[] raw = FileUtils.readSmallFile(filePath);  
            String tmp = new String(raw, StandardCharsets.US_ASCII);        
            text = ParseUtils.stripStxEtxHeaderAndFooter(tmp);
            
            if (text.contains("PAXLST")) {
                svc = apisLoader;
            } else if (text.contains("PNRGOV")) {
                svc = pnrLoader;
            } else {
                amitError("unrecognized file type");
                return;
            }
        } catch (Exception e) {
            String stacktrace = ErrorUtils.getStacktrace(e);
            Message m = new Message();
            m.setError(stacktrace);
            m.setFilePath(filePath);
            m.setCreateDate(new Date());
            msgDao.save(m);
        }
        
        svc.setFilePath(filePath);
        for (String rawMessage : svc.preprocess(text)) {
            MessageVo parsedMessage = svc.parse(rawMessage);
            if (parsedMessage != null) {
                svc.load(parsedMessage);
            }
        }
    }
    
    private boolean exceedsMaxSize(File f) {
        final int MAX_SIZE = 64000;  // bytes
        double numBytes = f.length();
        return numBytes > MAX_SIZE;
    }
    
    private void amitError(String err) {
        System.err.println("************************************************************");
        System.err.println(String.format("GTAS LOADER ERROR: %s", err));
        System.err.println("************************************************************");
    }
}
